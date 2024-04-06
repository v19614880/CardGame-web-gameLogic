package structures.game;

import akka.actor.ActorRef;
import structures.Constants;
import structures.GameState;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.card.*;
import structures.contracts.PopulateTilesCompletionCallback;
import utils.BasicObjectBuilders;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@code Board} class represents the physical space where game actions occur in the card game.
 * It encapsulates the logic and structure of the game board, including its dimensions, the positioning of units,
 * and the valid areas where units can move, attack, or perform other actions. The class provides methods for
 * manipulating the game state based on player actions or game rules, such as moving units, engaging in combat,
 * or applying game effects. It also includes utility methods for determining valid positions for actions, checking
 * for the presence of units, and highlighting potential moves or actions for the player.
 */
public class Board {
    private final int board_X;
    private final int board_Y;
    private final Tile[][] tiles;

    public Board() {
        this.board_X = getBoard_X();
        this.board_Y = getBoard_Y();
        this.tiles = new Tile[board_X][board_Y];
        init();
    }

    public int getBoard_X() {
        return Constants.BOARD_SIZE_X;
    }

    public int getBoard_Y() {
        return Constants.BOARD_SIZE_Y;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    private void init() {
        for (int i = 0; i < board_X; i++) {
            for (int j = 0; j < board_Y; j++) {
                Tile tile = BasicObjectBuilders.loadTile(i, j);
                tiles[i][j] = tile;
            }
        }
    }

    /**
     * Populates the game board tiles on the client side and triggers a callback upon completion.
     * This method asynchronously schedules the drawing of each tile on the board through an {@link ScheduledExecutorService}.
     * Once all tiles are drawn, it shuts down the executor and invokes the provided {@link PopulateTilesCompletionCallback} to signify completion.
     *
     * @param out      The {@link ActorRef} object used for sending draw commands to the client.
     * @param callback An instance of {@link PopulateTilesCompletionCallback} to be called upon the completion of tile population.
     */
    public void populateTiles(ActorRef out, PopulateTilesCompletionCallback callback) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            private int i = 0;
            private int j = 0;

            @Override
            public void run() {
                tiles[i][j].draw(out, 0);

                if (++j >= board_Y) {
                    i++;
                    j = 0;
                }

                if (i >= board_X) {
                    executorService.shutdown();
                    callback.onCompletion();
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * Retrieve a tile at the given coordinates.
     *
     * @param x The x-coordinate of the tile
     * @param y The y-coordinate of the tile
     * @return The tile at the specified position on the board
     * @throws IndexOutOfBoundsException If the x or y coordinate
     *                                   is out of the board's range
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= board_X || y < 0 || y >= board_Y) {
            throw new IndexOutOfBoundsException("Invalid coordinates");
        }
        return tiles[x][y];
    }

    /**
     * Finds and returns a tile based on a given position.
     *
     * @param p The {@link Position} object representing the coordinates of the tile to be found.
     * @return The {@link Tile} at the given position if it exists within the board's boundaries.
     * @throws IndexOutOfBoundsException if the position's coordinates are outside the board's range.
     */
    public Tile getTileByPosition(Position p) {
        return this.getTile(p.getTilex(), p.getTiley());
    }

    /**
     * Highlights potential playable or target positions for a selected card in the player's hand.
     * This method determines which positions on the game board should be highlighted based on the type and
     * specific mechanics of the card that has been selected. Creatures may highlight positions where they can
     * be summoned, while spells and artifacts highlight either friendly or enemy units depending on the spell's
     * effect.
     *
     * @param out       An {@link ActorRef} to send commands and notifications for updating UI highlights on the board.
     * @param gameState The current state of the game represented by {@link GameState}.
     */
    public void highlightPlayable(ActorRef out, GameUnit cardToBePlayed, GameState gameState) {
        if (cardToBePlayed.getCard().isCreature()) {
            // if creature, highlight playable positions
            findPlayablePositions(gameState);
            highlightPositions(out, gameState.getPositionsToHighlight(), 1);
        } else {
            // if spell, highlight target
            if (cardToBePlayed instanceof HornOfTheForsaken) {// Horn of the Forsaken
                gameState.getPositionsToHighlight().add(gameState.getHumanPlayer().getUnit().getPosition());
                highlightPositions(out, gameState.getPositionsToHighlight(), 1);
            } else if (cardToBePlayed instanceof WraithlingSwarm) {// Wraithling Swarm
                findPlayablePositions(gameState);
                highlightPositions(out, gameState.getPositionsToHighlight(), 1);
            } else if (cardToBePlayed instanceof DarkTerminus) {// Dark Terminus
                for (GameUnit gameCard : gameState.getOnBoardGameUnit()) {
                    if (!gameCard.isFriendlyUnit() && !gameCard.equals(gameState.getAIPlayer())) {
                        gameState.getPositionsToHighlight().add(gameCard.getUnit().getPosition());
                    }
                }
                highlightPositions(out, gameState.getPositionsToHighlight(), 2);
            } else if (cardToBePlayed instanceof BeamShock) {// Beam Shock
                for (GameUnit gameCard : gameState.getOnBoardGameUnit()) {
                    if (gameCard.isFriendlyUnit() && !gameCard.equals(gameState.getHumanPlayer())) {
                        gameState.getPositionsToHighlight().add(gameCard.getUnit().getPosition());
                    }
                }
                highlightPositions(out, gameState.getPositionsToHighlight(), 2);
            } else if (cardToBePlayed instanceof SundropElixir) {// Sundrop Elixir
                for (GameUnit gameCard : gameState.getOnBoardGameUnit()) {
                    if (!gameCard.isFriendlyUnit()) {
                        gameState.getPositionsToHighlight().add(gameCard.getUnit().getPosition());
                    }
                }
                highlightPositions(out, gameState.getPositionsToHighlight(), 1);
            } else if (cardToBePlayed instanceof TrueStrike) {// True Strike
                for (GameUnit gameCard : gameState.getOnBoardGameUnit())
                    if (gameCard.isFriendlyUnit()) {
                        gameState.getPositionsToHighlight().add(gameCard.getUnit().getPosition());
                    }
                highlightPositions(out, gameState.getPositionsToHighlight(), 2);
            } else {
                System.out.println("No specific logic for this ID: " + cardToBePlayed.getCard().getId());
            }
        }
        gameState.setHighlighted(true);
    }

    /**
     * Checks if a specific position on the board is playable, meaning it is highlighted and
     * available to be played onto the board.
     *
     * @param p The position to be checked for playability.
     * @return true if the position is highlighted and thus playable, false otherwise.
     */
    public boolean isPlayable(Position p, GameState gameState) {
        return gameState.getPositionsToHighlight().contains(p);
    }

    /**
     * Determines if a specified position on the game board is occupied by a friendly unit.
     *
     * @param p         The {@link Position} to check if it is occupied by a friendly unit.
     * @param gameState The current state of the game represented by {@link GameState}.
     * @return Returns {@code true} if the position is occupied by a friendly unit, otherwise returns {@code false}.
     */
    public boolean isFriendlyUnitPosition(Position p, GameState gameState) {
        for (GameUnit gameUnit : gameState.getOnBoardGameUnit()) {
            if (gameUnit.isFriendlyUnit() == gameState.isPlayerTurn() && gameUnit.getUnit().getPosition().equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Highlights potential movement and attack positions for a selected unit on the game board.
     * This method calculates and displays both the positions where a selected unit can move and the positions
     * from which it can attack.
     *
     * @param out             An {@link ActorRef} used to send commands for drawing highlights on the UI.
     * @param clickedPosition The {@link Position} of the unit that was clicked, initiating the highlight operation.
     * @param gameState       The current state of the game represented by {@link GameState}, used to identify current
     *                        unit positions, and to update which positions are to be highlighted.
     */
    public void highlightMovable(ActorRef out, Position clickedPosition, GameState gameState) {
        gameState.setHighlighted(clickedPosition);

        GameUnit gameUnit = gameState.findUnitByPosition(clickedPosition);

        if (gameUnit instanceof YoungFlamewing) {
            List<Position> positionList = new ArrayList<>();
            Set<Position> uniquePositions = new HashSet<>();
            Tile[][] tiles = gameState.getBoard().getTiles();

            for (Tile[] tile1 : tiles) {
                for (Tile tile : tile1) {
                    positionList.add(new Position(tile.getTilex(), tile.getTiley()));
                }
            }

            for (Position p : positionList) {
                if (gameState.getBoard().isNotOccupied(p, gameState)) {
                    uniquePositions.add(p);
                } else if (gameState.getBoard().containsEnemy(p, gameState)) {
                    uniquePositions.add(p);
                }
            }

            gameState.getPositionsToHighlight().addAll(uniquePositions);
            List<Position> positionsToHighlightList = gameState.getPositionsToHighlight();
            highlightPositions(out, positionsToHighlightList, gameState);
        } else {
            //movable position
            int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0},
                    {-2, 0}, {2, 0}, {0, -2}, {0, 2}};

            Set<Position> uniquePositions = new HashSet<>();

            for (int[] dir : dirs) {
                Position newPos = new Position(clickedPosition.getTilex() + dir[0], clickedPosition.getTiley() + dir[1]);
                if (isValidPosition(newPos) && !isFriendlyUnitPosition(newPos, gameState)) {
                    uniquePositions.add(newPos);
                }
            }

            // attack position
            int[][] more = {{1, -2}, {2, -2}, {2, -1}, {3, -1}, {3, 0}, {3, 1}, {2, 1}, {2, 2}, {1, 2}, {-1, 2}, {-2, 2}, {-2, 1}, {-3, 1}, {-3, 0}, {-3, -1}, {-2, -1}, {-2, -2}, {-1, -2}};

            for (int[] dir : more) {
                Position newPos = new Position(clickedPosition.getTilex() + dir[0], clickedPosition.getTiley() + dir[1]);
                if (containsEnemy(newPos, gameState) && isValidPosition(newPos)) {
                    uniquePositions.add(newPos);
                }
            }

            gameState.getPositionsToHighlight().addAll(uniquePositions);

            List<Position> positionsToHighlightList = gameState.getPositionsToHighlight();
            for (Position p : positionsToHighlightList) {
                if (containsEnemy(p, gameState)) {
                    getTile(p.getTilex(), p.getTiley()).draw(out, 2);
                } else {
                    getTile(p.getTilex(), p.getTiley()).draw(out, 1);
                }
            }
        }
        gameState.setHighlighted(true);
    }

    public boolean containsEnemy(Position p, GameState gameState) {
        List<GameUnit> list = gameState.getOnBoardGameUnit();
        for (GameUnit gameUnit : list) {
            boolean isEnemy = gameState.isPlayerTurn() != gameUnit.isFriendlyUnit();
            if (isEnemy && gameUnit.getUnit().getPosition().equals(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the current highlighting from all previously highlighted tiles.
     *
     * @param out ActorRef object responsible for sending un-highlight commands to the front-end to update the UI.
     */
    public void clearHighlight(ActorRef out, GameState gameState) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try {
                for (Tile[] tilesRow : gameState.getBoard().getTiles()) {
                    for (Tile tile : tilesRow) {
                        if (tile.getMode() != 0) {
                            tile.draw(out, 0);
                            Thread.sleep(10);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Preserve interrupt status.
            } finally {
                gameState.getPositionsToHighlight().clear();
                executorService.shutdown();
            }
        });
    }

    /**
     * Checks if a specified position on the game board is not occupied by any unit.
     *
     * @param p         The {@link Position} to check for the absence of any game unit.
     * @param gameState The current state of the game as represented by {@link GameState}.
     * @return Returns {@code true} if no unit occupies the specified position; otherwise, returns {@code false}.
     */
    public boolean isNotOccupied(Position p, GameState gameState) {
        for (GameUnit gameCard : gameState.getOnBoardGameUnit()) {
            if (gameCard.getUnit().getPosition().equals(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if a specified position is within the boundaries of the game board.
     *
     * @param p The {@link Position} to be validated against the board's boundaries.
     * @return Returns {@code true} if the position is within the game board's boundaries; otherwise, returns {@code false}.
     */
    public boolean isValidPosition(Position p) {
        return p.getTilex() >= 0 && p.getTilex() < getBoard_X() && p.getTiley() >= 0 && p.getTiley() < getBoard_Y();
    }

    private void highlightPositions(ActorRef out, List<Position> positionsToHighlight, int mode) {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final long period = 10L; // Interval in milliseconds
        final Runnable drawTask = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < positionsToHighlight.size()) {
                    Position position = positionsToHighlight.get(currentIndex++);
                    getTile(position.getTilex(), position.getTiley()).draw(out, mode);
                } else {
                    executorService.shutdown();
                }
            }
        };
        executorService.scheduleAtFixedRate(drawTask, 0, period, TimeUnit.MILLISECONDS);
    }

    private void highlightPositions(ActorRef out, List<Position> positionsToHighlight, GameState gameState) {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final long period = 10L; // Interval in milliseconds
        final Runnable drawTask = new Runnable() {
            private int currentIndex = 0;

            @Override
            public void run() {
                if (currentIndex < positionsToHighlight.size()) {
                    Position position = positionsToHighlight.get(currentIndex++);
                    if (containsEnemy(position, gameState)) {
                        getTile(position.getTilex(), position.getTiley()).draw(out, 2);
                    } else {
                        getTile(position.getTilex(), position.getTiley()).draw(out, 1);
                    }
                } else {
                    executorService.shutdown();
                }
            }
        };
        executorService.scheduleAtFixedRate(drawTask, 0, period, TimeUnit.MILLISECONDS);
    }

    private void findPlayablePositions(GameState gameState) {
        int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}};
        Set<Position> uniquePositions = new HashSet<>();
        boolean isPlayerTurn = gameState.isPlayerTurn();

        for (GameUnit gameCard : gameState.getOnBoardGameUnit()) {
            boolean isMatchingTurn = isPlayerTurn == gameCard.isFriendlyUnit();
            if (isMatchingTurn) {
                Position position = gameCard.getUnit().getPosition();
                for (int[] dir : dirs) {
                    Position newPos = new Position(position.getTilex() + dir[0], position.getTiley() + dir[1]);
                    if (isValidPosition(newPos) && isNotOccupied(newPos, gameState)) {
                        uniquePositions.add(newPos);
                    }
                }
            }
        }
        gameState.getPositionsToHighlight().addAll(uniquePositions);
    }

    @Deprecated
    public boolean checkMove(GameState g, Position currentPosition, Position targetPosition) {
        // 檢查目標位置是否在遊戲板的有效範圍內
        if (!isValidPosition(targetPosition)) {
            return false;
        }

        // 獲取所有高亮的位置
        List<Position> highlightedPositions = g.getPositionsToHighlight();

        // 檢查目標位置是否被高亮且未被敵方單位佔據
        if (highlightedPositions.contains(targetPosition)) {
            // 需要確保目標位置不是由敵方單位佔據
            GameUnit aiPlayer = g.getAIPlayer();
            if (aiPlayer != null && aiPlayer.getUnit().getPosition().equals(targetPosition)) {
                return false; // 目標位置被敵方單位佔據
            }
            return true; // 目標位置可移動
        }

        return false; // 目標位置不在高亮的位置列表中

    }

    @Deprecated
    public void updateUnitPosition(Unit unit, Position newPosition, GameState gameState) {
        unit.setPosition(newPosition);

        // Update position in GameCard if it exists
        for (GameUnit card : gameState.getOnBoardGameUnit()) {
            if (card.getUnit() != null && card.getUnit().getId() == unit.getId()) {
                card.setUnit(unit);
                break;
            }
        }
    }

    // Finds a detour tile to move to if the direct path is blocked.
    public Tile findDetourTile(Position current, Position target, GameState gameState) {
        // Check in a wider radius for a detour tile
        int[][] directions = {
                // Check immediate adjacent tiles first
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                // Then check diagonal and two-tile-away spots
                {-1, -1}, {1, -1}, {1, 1}, {-1, 1},
                {-2, 0}, {2, 0}, {0, -2}, {0, 2},
                {-2, -1}, {2, -1}, {2, 1}, {-2, 1},
                {-1, -2}, {1, -2}, {1, 2}, {-1, 2}
        };

        for (int[] dir : directions) {
            Position newPos = new Position(current.getTilex() + dir[0], current.getTiley() + dir[1]);
            if (isValidPosition(newPos) && isNotOccupied(newPos, gameState)) {
                // Check if the detour is not also blocked by an enemy
                if (!containsEnemy(newPos, gameState)) {
                    return getTileByPosition(newPos);
                }
            }
        }
        return null; // No detour found

    }


    // Helper method to determine if vertical movement should be prioritized
    public boolean determineYFirst(Position start, Position end) {
        // Simple heuristic: if the difference in Y is greater than X, prioritize Y movement first
        return Math.abs(start.getTiley() - end.getTiley()) > Math.abs(start.getTilex() - end.getTilex());
    }

}