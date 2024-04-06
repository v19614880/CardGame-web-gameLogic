package structures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import structures.card.*;
import structures.contracts.Ability;
import structures.contracts.OnHitListener;
import structures.contracts.ProvokeListener;
import structures.contracts.OnSummonListener;
import structures.game.Board;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Class to implement game logic
 */

public class GameLogic {


    /**
     * Processes a click event on a tile on the game board by updating the game state.
     *
     * @param clickedPosition The {@link Position} on the board that was clicked by the player.
     * @param gameState       The current state of the game represented by {@link GameState}.
     */
    public static void processClickedTile(Position clickedPosition, GameState gameState) {
        gameState.setClickedPosition(clickedPosition);
        if (!gameState.isHighlighted()) {
            gameState.setClickedUnit(gameState.findUnitByPosition(clickedPosition));
        }
    }

    /**
     * Selects a unit based on a clicked position on the game board.
     * <p>
     * This method is deprecated and currently not in use. It was originally designed
     * to handle unit selection when a player clicked a tile on the board.
     *
     * @param out             The ActorRef instance for communication.
     * @param gameState       The current state of the game.
     * @param clickedPosition The position on the board that was clicked.
     */
    @Deprecated
    public static void selectUnit(ActorRef out, GameState gameState, Position clickedPosition) {

        if (gameState.getClickedPosition() != null) {
            gameState.getBoard().clearHighlight(out, gameState);
            gameState.setClickedPosition(null);
        }

        for (GameUnit gameUnit : gameState.getOnBoardGameUnit()) {
            if (gameUnit.getUnit() != null && gameUnit.getUnit().getPosition().equals(clickedPosition)) {
                if (gameUnit.isFriendlyUnit()) {
                    if (gameUnit.isMoved()) {
                        BasicCommands.addPlayer1Notification(out, "Unit has already moved", 2);
                    } else {
                        gameState.getBoard().highlightMovable(out, clickedPosition, gameState);
                        gameState.setClickedPosition(clickedPosition);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Moves a game unit to a new position on the board.
     * <p>
     * This method is deprecated and currently not in use. It was originally designed
     * for moving a unit to a new position after it was selected by the player.
     *
     * @param out         The ActorRef instance for communication.
     * @param gameState   The current state of the game.
     * @param newPosition The new position to which the unit is to be moved.
     */
    @Deprecated
    public static void moveUnit(ActorRef out, GameState gameState, Position newPosition) {

        Position selectedPosition = gameState.getClickedPosition();

        // Iterate through on-board units to find and move the selected unit
        for (GameUnit gameUnit : gameState.getOnBoardGameUnit()) {
            if (gameUnit.getUnit() != null && gameUnit.getUnit().getPosition().equals(selectedPosition)) {
                if (!gameUnit.isMoved()) {
                    gameUnit.getUnit().move(out, gameState.getBoard(), newPosition, gameState);
                    gameState.getBoard().updateUnitPosition(gameUnit.getUnit(), newPosition, gameState);
                    gameUnit.setMoved(true);
                    break; // Exit loop after moving the unit
                } else {
                    BasicCommands.addPlayer1Notification(out, "Unit cannot be moved", 2);
                    return;
                }
            }
        }

        // Reset game state and highlight target tile
        gameState.clear(out);
    }

    /**
     * Moves a specified game unit to a target position on the game board.
     * <p>
     * This method identifies a game unit based on a highlighted position within the game state
     * and attempts to move it to a specified target position. If the identified unit has already
     * moved during the current round, a notification is displayed to the player and no action is
     * taken. Otherwise, the unit is moved to the new position, its state is updated to reflect
     * the move, and any game state highlights cleared.
     *
     * @param out            An {@link ActorRef} to send commands and notifications through.
     * @param gameState      The current state of the game.
     * @param targetPosition The {@link Position} to which the unit should be moved.
     */
    public static void move(ActorRef out, GameState gameState, Position targetPosition) {

        gameState.getBoard().clearHighlight(out, gameState);
        GameUnit toBeMoved = gameState.getClickedUnit();

        if (!toBeMoved.isProvoked()) {
            if (toBeMoved.isMoved()) {
                BasicCommands.addPlayer1Notification(out, "Moved, attacked or beam shocked.", 2);
            } else {
                BasicCommands.moveUnitToTile(out, toBeMoved.getUnit(), gameState.getBoard().getTileByPosition(targetPosition));
                GameLogic.playAnimation(out, toBeMoved.getUnit(), UnitAnimationType.move);
                toBeMoved.getUnit().setPosition(targetPosition);
                toBeMoved.setMoved(true);

                // Update provoke status after moving
                provokedStatus(out, gameState);
                System.out.println("set provoke state ##aa");
            }
        } else {
            BasicCommands.addPlayer1Notification(out, "Unit affected by Provoke!", 2);
            System.out.println("test provoke message");
        }
        gameState.clear(out);

        System.out.println("#1After move - isProvoked: " + toBeMoved.isProvoked());
    }

    /**
     * Moves a specified game unit to a target position on the game board and then execute a subsequent runnable.
     *
     * @param out            The {@link ActorRef} to send commands and notifications through.
     * @param gameState      The current state of the game.
     * @param targetPosition The {@link Position} to which the unit should be moved.
     * @param afterMove      The {@link Runnable} task to execute after the move operation.
     */
    public static void move(ActorRef out, GameState gameState, Position targetPosition, Runnable afterMove) {

        gameState.getBoard().clearHighlight(out, gameState);
        GameUnit toBeMoved = gameState.getClickedUnit();

        if (!toBeMoved.isProvoked()) {
            if (toBeMoved.isMoved()) {
                BasicCommands.addPlayer1Notification(out, "Already moved or attacked.", 2);
            } else {
                BasicCommands.moveUnitToTile(out, toBeMoved.getUnit(), gameState.getBoard().getTileByPosition(targetPosition));
                toBeMoved.getUnit().setPosition(targetPosition);
                toBeMoved.setMoved(true);
                System.out.println("test 10");

                // Wait for the move animation to complete before executing the next action
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(afterMove, BasicCommands.playUnitAnimation(out, toBeMoved.getUnit(), UnitAnimationType.move), TimeUnit.MILLISECONDS);

                // Update provoke status after moving
                provokedStatus(out, gameState);
                System.out.println("set provoke state ##af");


            }
            gameState.clear(out);
        } else {
            BasicCommands.addPlayer1Notification(out, "Unit affected by Provoke!", 2);
            System.out.println("test5");
        }
        System.out.println("#1After move - isProvoked: " + toBeMoved.isProvoked());

    }

    /**
     * Draws the specified game unit on the specified tile and updates its health and attack values after a short delay. An animation effect is also played upon summoning.
     *
     * @param out      The ActorRef instance representing the output channel to which commands are sent.
     * @param gameUnit The GameUnit instance to be drawn on the board.
     * @param tile     The Tile instance where the game unit is to be placed.
     */
    public static void drawUnitOnBoard(ActorRef out, GameUnit gameUnit, Tile tile) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        BasicCommands.drawUnit(out, gameUnit.getUnit(), tile);
        executorService.schedule(() -> {
            GameLogic.playEffect(out, StaticConfFiles.F_1_SUMMON, tile);
            BasicCommands.setUnitHealth(out, gameUnit.getUnit(), gameUnit.getHealth());
            BasicCommands.setUnitAttack(out, gameUnit.getUnit(), gameUnit.getAttack());
        }, 300, TimeUnit.MILLISECONDS);
    }

    /**
     * Plays a visual effect animation on a specified tile.
     *
     * @param out  The ActorRef object allowing communication with the client-side to send commands.
     * @param conf A string representing the path to the effect configuration file.
     * @param tile The Tile object where the effect animation will be displayed.
     */
    public static void playEffect(ActorRef out, String conf, Tile tile) {
        BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(conf), tile);
    }


    /**
     * Executes the playing of a card from the player's hand onto the game board.
     * This function is called when a player selects a card from their hand with the intention
     * to play it and then clicks on a target position on the board. The function determines
     * whether the selected card is a creature or a spell card and performs actions accordingly.
     *
     * @param out             An {@link ActorRef} used to send commands for updating the game's UI.
     * @param clickedPosition The {@link Position} on the board where the player intends to play the selected card.
     * @param gameState       The current state of the game, represented by {@link GameState}.
     */
    public static void play(ActorRef out, Position clickedPosition, GameState gameState) {
        Tile tile = gameState.getBoard().getTileByPosition(clickedPosition);
        if (gameState.isPlayerTurn()) {
            int cardPos = gameState.getClickedCard().getCardPos();
            List<GameUnit> cardsInHand = gameState.getHand().getCards();
            if (cardPos > 0 && cardPos <= cardsInHand.size()) {
                // get the card at the specified position
                GameUnit playedCard = cardsInHand.get(cardPos - 1);
                if (playedCard.getCard().isCreature()) {
                    // remove the card from the hand
                    cardsInHand.remove(cardPos - 1);

                    // set position of the card
                    playedCard.getUnit().setPositionByTile(tile);

                    // Creature has a provoke skill，then active it.
                    if (playedCard instanceof ProvokeListener) {
                        ((ProvokeListener) playedCard).onProvoke(out, gameState);
                    }

                    // delete the card and re-order remaining cards
                    BasicCommands.deleteCard(out, cardPos);
                    updateCardPositions(out, cardsInHand);


                    // draw the card on the board and set its initial values.
                    gameState.getBoard().clearHighlight(out, gameState);
                    drawUnitOnBoard(out, playedCard, tile);

                    // Notify all units that implement OnSummonListener
                    GameLogic.notifyOpeningGambitUnits(out, gameState);

                    // add the card to gameState
                    gameState.getOnBoardGameUnit().add(playedCard);
                } else {
                    // perform the spell card's ability
                    GameLogic.performCardAbility(out, playedCard, gameState);
                    // delete the card and re-order remaining cards
                    cardsInHand.remove(cardPos - 1);
                    BasicCommands.deleteCard(out, cardPos);
                    updateCardPositions(out, cardsInHand);
                }
                System.out.println("Played card: " + playedCard.getCard().getCardname());
            }
            gameState.clear(out);
        } else {
            // AI will set the gameState provided a card is playable
            GameUnit playedCard = gameState.getClickedCard();
            if (playedCard.getCard().isCreature()) {
                // remove the card from AI hand
                gameState.getAIHand().remove(playedCard);

                // set the position of the played card
                playedCard.getUnit().setPositionByTile(tile);

                // draw the card on the board and set its initial values.
                gameState.getBoard().clearHighlight(out, gameState);
                drawUnitOnBoard(out, playedCard, tile);

                // Notify all units that implement OnSummonListener
                GameLogic.notifyOpeningGambitUnits(out, gameState);

                // add the card to gameState
                gameState.getOnBoardGameUnit().add(playedCard);
            } else {
                // perform the spell card's ability
                GameLogic.performCardAbility(out, playedCard, gameState);
                gameState.getAIHand().remove(playedCard);
            }
            System.out.println("AI Played card: " + playedCard.getCard().getCardname());
            gameState.clear(out);
        }
    }

    /**
     * This method summons a Wraithling at the clicked position on the game board until counter reaches 3.
     *
     * @param out             Output communication channel
     * @param clickedPosition The position where the Wraithling is intended to be summoned.
     * @param gameState       Current state of the game.
     */
    public static void playWraithlingSwarm(ActorRef out, Position clickedPosition, GameState gameState) {
        gameState.setGameLock(true);

        if (gameState.getBoard().getTileByPosition(clickedPosition).getMode() != 1) {
            BasicCommands.addPlayer1Notification(out, "Invalid position", 2);
            return;
        }

        GameUnit wraithlingSwarm = gameState.getClickedCard();
        GameLogic.summonWraithling(out, clickedPosition, gameState);
        gameState.getBoard().clearHighlight(out, gameState);
        ((WraithlingSwarm) wraithlingSwarm).counter++;
        GameLogic.forceWait(500);
        if (((WraithlingSwarm) wraithlingSwarm).counter < 3) {
            gameState.getBoard().highlightPlayable(out, wraithlingSwarm, gameState);
        }
        if (((WraithlingSwarm) wraithlingSwarm).counter == 3) {
            int cardPos = wraithlingSwarm.getCardPos();
            List<GameUnit> cardsInHand = gameState.getHand().getCards();
            if (cardPos > 0 && cardPos <= cardsInHand.size()) {
                // remove the card from the hand
                cardsInHand.remove(cardPos - 1);

                // delete the card and re-order remaining cards
                BasicCommands.deleteCard(out, cardPos);
                updateCardPositions(out, cardsInHand);

                // Notify all units that implement OnSummonListener
                GameLogic.notifyOpeningGambitUnits(out, gameState);

                GamePlayer gamePlayer = (GamePlayer) gameState.getHumanPlayer();
                GameLogic.updatePlayerMana(out, gamePlayer, gamePlayer.getPlayer().getMana() - 3);
                gameState.clear(out);
                gameState.setGameLock(false);
            }
        }
    }

    private static void updateCardPositions(ActorRef out, List<GameUnit> cardsInHand) {
        for (int i = 0; i < cardsInHand.size(); i++) {
            GameUnit card = cardsInHand.get(i);
            if (!isAligned(card, i)) {
                BasicCommands.deleteCard(out, card.getCardPos());
                card.setCardPos(i + 1);
                card.draw(out, 0);
            }
        }
    }

    private static boolean isAligned(GameUnit card, int correctIndex) {
        return card.getCardPos() - 1 == correctIndex;
    }

    /**
     * Attempts to trigger the ability of a given {@link GameUnit} if it implements the {@link Ability} interface.
     *
     * @param out       The {@link ActorRef} instance used for sending messages to the actor system.
     * @param spell     The {@link GameUnit} which is being checked for an ability to perform. s the {@link Ability} interface.
     * @param gameState The current state of the game.
     * @see Ability
     */
    public static void performCardAbility(ActorRef out, GameUnit spell, GameState gameState) {
        if (spell instanceof Ability) {
            ((Ability) spell).perform(out, gameState);
        }
    }

    /**
     * Summons a Wraithling unit onto the specified tile of the board.
     *
     * @param out       The ActorRef instance representing the output channel to which commands are sent for UI updates.
     * @param position  The Position object representing the board tile coordinates where the Wraithling is to be summoned.
     * @param gameState The current GameState object containing information about the game state.
     */
    public static void summonWraithling(ActorRef out, Position position, GameState gameState) {
        GameUnit wraithling = new Wraithling();
        Tile tile = gameState.getBoard().getTileByPosition(position);
        wraithling.getUnit().setPositionByTile(tile);
        drawUnitOnBoard(out, wraithling, tile);
        GameLogic.playEffect(out, StaticConfFiles.F_1_WRAITHLING_SUMMON, tile);
        gameState.getOnBoardGameUnit().add(wraithling);
    }

    private static Position calculateRange(Position targetPosition, GameState gameState) {

        Board board = gameState.getBoard();
        Set<Position> targetAttackables = new HashSet<>();
        int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}};
        for (int[] dir : dirs) {
            Position newPos = new Position(targetPosition.getTilex() + dir[0], targetPosition.getTiley() + dir[1]);
            if (board.isValidPosition(newPos) && board.isNotOccupied(newPos, gameState)) {
                targetAttackables.add(newPos);
            }
        }
        if (!targetAttackables.isEmpty()) {
            List<Position> positionList = new ArrayList<>(targetAttackables);
            return positionList.get(new Random().nextInt(positionList.size()));
        }
        return null;
    }

    private static Position calculateRange(Position sourcePosition, Position targetPosition, GameState gameState) {
        Position calculatedPosition = null;

        Board board = gameState.getBoard();
        int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}};
        Set<Position> targetAttackables = new HashSet<>();
        for (int[] dir : dirs) {
            Position newPos = new Position(targetPosition.getTilex() + dir[0], targetPosition.getTiley() + dir[1]);
            if (board.isValidPosition(newPos) && board.isNotOccupied(newPos, gameState)) {
                targetAttackables.add(newPos);
            }
        }

        int[][] dirs2 = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0},
                {-2, 0}, {2, 0}, {0, -2}, {0, 2}};

        Set<Position> sourceMovables = new HashSet<>();
        for (int[] dir : dirs2) {
            Position newPos = new Position(sourcePosition.getTilex() + dir[0], sourcePosition.getTiley() + dir[1]);
            if (board.isValidPosition(newPos) && board.isNotOccupied(newPos, gameState)) {
                sourceMovables.add(newPos);
            }
        }

        // Find the common positions that exist in both targetAttackables and sourceMovables
        Set<Position> commonPositions = new HashSet<>(targetAttackables);
        commonPositions.retainAll(sourceMovables);

        // calculate the closest position to the source
        if (!commonPositions.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            for (Position position : commonPositions) {
                double distance = calculateDistance(sourcePosition, position);
                if (distance < minDistance) {
                    minDistance = distance;
                    calculatedPosition = position;
                }
            }
        }

        return calculatedPosition;
    }

    private static double calculateDistance(Position pos1, Position pos2) {
        // Calculate Euclidean distance between two positions
        int deltaX = pos1.getTilex() - pos2.getTilex();
        int deltaY = pos1.getTiley() - pos2.getTiley();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Processes an attack, handling both direct attacks and movement plus attack actions.
     * This method determines if an attacking unit is within range to attack a target unit directly. If the target is out of
     * direct attack range, find a closer position for the attacker to move to before attacking,
     * provided the attacking unit has not already moved this turn.
     *
     * @param out       An {@link ActorRef} to send commands and notifications through.
     * @param source    The attacking {@link GameUnit}.
     * @param target    The target {@link GameUnit} of the attack.
     * @param gameState The current state of the game represented by {@link GameState}.
     */
    public static void processAttack(ActorRef out, GameUnit source, GameUnit target, GameState gameState) {
        gameState.getBoard().clearHighlight(out, gameState);
        if (source.hasNotAttacked()) {
            if (source.isProvoked()) {
                boolean targetIsProvoker = target instanceof ProvokeListener && isInProvokeRange(source.getUnit().getPosition(), target.getUnit().getPosition());

                if (!targetIsProvoker) {
                    BasicCommands.addPlayer1Notification(out, "Can only attack the provoking unit!", 2);
                    gameState.clear(out);
                    return; // Exit the method if the source is provoked and the target is not the Provoker
                }
            }

            Position sourcePosition = source.getUnit().getPosition();
            Position targetPosition = target.getUnit().getPosition();
            int deltaX = Math.abs(targetPosition.getTilex() - sourcePosition.getTilex());
            int deltaY = Math.abs(targetPosition.getTiley() - sourcePosition.getTiley());
            if (deltaX > 1 || deltaY > 1) {
                Position calculatedPosition = null;
                if (source instanceof YoungFlamewing) {
                    calculatedPosition = GameLogic.calculateRange(targetPosition, gameState);
                } else {
                    calculatedPosition = GameLogic.calculateRange(sourcePosition, targetPosition, gameState);
                }

                if (calculatedPosition == null) {
                    BasicCommands.addPlayer1Notification(out, "Out of range, there's no position to move to.", 2);
                } else {
                    if (!source.isMoved()) {
                        GameLogic.move(out, gameState, calculatedPosition, () -> {
                            GameLogic.executeAttackSequence(out, source, target, gameState);
                        });
                    } else {
                        BasicCommands.addPlayer1Notification(out, "Already moved and out of range.", 2);
                    }
                }
                gameState.clear(out);
                return;
            }
            GameLogic.executeAttackSequence(out, source, target, gameState);
        } else {
            BasicCommands.addPlayer1Notification(out, "Attacked, beam shocked or cannot attack on the first round.", 2);
        }
        gameState.clear(out);
    }

    private static void executeAttackSequence(ActorRef out, GameUnit source, GameUnit target, GameState gameState) {
        // attack
        GameLogic.playAnimation(out, source.getUnit(), UnitAnimationType.attack);
        target.updateHealth(out, target.getHealth() - source.getAttack(), gameState);
        BasicCommands.playUnitAnimation(out, source.getUnit(), UnitAnimationType.idle);
        source.setAttacked(true);
        source.setMoved(true); // if attacked, forfeit the ability to move within the current round.

        // counterattack
        GameLogic.playAnimation(out, target.getUnit(), UnitAnimationType.attack);
        source.updateHealth(out, source.getHealth() - target.getAttack(), gameState);
        BasicCommands.playUnitAnimation(out, target.getUnit(), UnitAnimationType.idle);

        if (source.getID() == Constants.PLAYER_ID) {
            ((OnHitListener) source).onHit(out, gameState);
        } else if (target.getID() == Constants.PLAYER_ID) {
            ((OnHitListener) target).onHit(out, gameState);
        }

        source.onDeath(out, gameState);
        target.onDeath(out, gameState);
    }

    /**
     * Resets the movement status of all game units on the board to false.
     *
     * @param gameState The current GameState object containing information about the game state.
     */
    public static void clearIsMovedStatus(GameState gameState) {
        for (GameUnit gameUnit : gameState.getOnBoardGameUnit()) {
            gameUnit.setMoved(false);
        }
    }

    /**
     * Resets the mana of a specified player to zero.
     *
     * @param out    An ActorRef object used for communication with the client.
     * @param player The GamePlayer instance whose mana will be reset.
     */
    public static void resetMana(ActorRef out, GamePlayer player) {
        player.updateMana(out, 0);
    }

    /**
     * Updates the mana of a specified player to a new value.
     *
     * @param out    An ActorRef object used for communication with the client.
     * @param player The GamePlayer instance whose mana is being updated.
     * @param mana   The new mana value to be assigned to the player.
     */
    public static void updatePlayerMana(ActorRef out, GamePlayer player, int mana) {
        player.updateMana(out, mana);
    }

    /**
     * Executes an animation on a unit and waits for the estimated duration of the animation.
     *
     * @param out           The ActorRef to send messages to.
     * @param unit          The Unit on which to perform the animation.
     * @param animationType The type of animation to play.
     */
    public static void playAnimation(ActorRef out, Unit unit, UnitAnimationType animationType) {
        try {
            Thread.sleep(BasicCommands.playUnitAnimation(out, unit, animationType));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("The wait for the animation completion was interrupted.");
        } catch (Exception e) {
            System.err.println("An error occurred while trying to play the animation and wait:");
        }
    }

    /**
     * Resets the 'has attacked' status for all game units currently on the game board.
     *
     * @param gameState The current state of the game.
     */
    public static void clearHasAttackedStatus(GameState gameState) {
        for (GameUnit gameUnit : gameState.getOnBoardGameUnit()) {
            gameUnit.setAttacked(false);
        }
    }

    /**
     * Pauses the execution of the current thread for a specified duration.
     *
     * @param milliseconds The duration, in milliseconds.
     * @throws IllegalArgumentException if the value of {@code milliseconds} is negative.
     * @throws RuntimeException         Wraps and rethrows an InterruptedException as an unchecked exception
     *                                  if the thread's sleep is interrupted.
     */
    public static void forceWait(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if two positions are adjacent.
     *
     * @param provokerPos provoker's position
     * @param effectPos   the position where provoker should affect
     * @return true if positions are adjacent
     */
    public static boolean isInProvokeRange(Position provokerPos, Position effectPos) {
        // Check if positions are within one tile of each other, including diagonally
        int dx = Math.abs(provokerPos.getTilex() - effectPos.getTilex());
        int dy = Math.abs(provokerPos.getTiley() - effectPos.getTiley());
        // Units must be within one tile in x and/or y direction to be in range
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0); // Exclude the case where both are 0, which is the unit itself
    }

    /**
     * Checks if units are affected by the Provoke skill and updates their status accordingly.
     * This method iterates through all units on the board. If an enemy unit is found adjacent
     * to the unit with Provoke, that enemy unit is marked as provoked. This status means that
     * the enemy unit cannot move and can only attack units with Provoke.
     *
     * @param provokeUnit The unit that has the Provoke ability.
     * @param gameState   The current state of the game, which includes all units on the board.
     */
    public static void unitAffectedByProvoke(GameUnit provokeUnit, GameState gameState) {

        // Only enemy units adjacent to the provoke unit should be marked as provoked
        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            if (!unit.equals(provokeUnit) && !unit.isFriendlyUnit() == provokeUnit.isFriendlyUnit() && isInProvokeRange(provokeUnit.getUnit().getPosition(), unit.getUnit().getPosition())) {
                unit.setProvoked(true);
                System.out.println("Unit ID: " + unit.getUnit().getId() + ", Type: " + unit.getClass().getSimpleName() + ", Position: " + unit.getUnit().getPosition() + " is now provoked");
            }
        }
    }


    /**
     * Updates the provoked status of all units on the board based on their proximity to units with the Provoke ability.
     * This method first resets the provoked status of all units to false. Then, it iterates through all units on the board.
     * If a unit has the Provoke ability (i.e., it implements the {@link ProvokeListener} interface), the method
     * triggers its provoke effect, potentially setting adjacent enemy units' provoked status to true.
     * <p>
     * The provoked status restricts a unit's ability to move and may impose other game-specific rules.
     *
     * @param out       The {@link ActorRef} used for sending commands and notifications. This is necessary for
     *                  invoking UI updates or other actions that depend on the Actor model. Can be null in a testing
     *                  environment where Actor interactions are not required.
     * @param gameState The current state of the game, represented by a {@link GameState} object. This object contains
     *                  information about all the units currently on the board and other relevant game details.
     */
    public static void provokedStatus(ActorRef out, GameState gameState) {
        // Reset provoke status of all units first
        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            unit.setProvoked(false);
        }
        System.out.println("clean provoke state");

        // Loop through all units and apply Provoke effects
        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            if (unit instanceof ProvokeListener) {
                ((ProvokeListener) unit).onProvoke(out, gameState);
            }
        }
        System.out.println("reset provoke state");
    }

    /**
     * Processes the beam shock effect on all game units on the board.
     *
     * @param gameState The {@link GameState} object representing the current state of the game.
     */
    public static void processBeamShock(GameState gameState) {
        List<GameUnit> gameUnitList = gameState.getOnBoardGameUnit();

        for (GameUnit gameUnit : gameUnitList) {
            if (gameUnit.isBeamShocked()) {
                gameUnit.setBeamShocked(false);
                gameUnit.setAttacked(true);
                gameUnit.setMoved(true);
            }
        }
    }

    public static void notifyOpeningGambitUnits(ActorRef out, GameState gameState) {
        List<GameUnit> openingGambitUnits = gameState.getOpeningGambitUnits();
        for (GameUnit gameUnit : openingGambitUnits) {
            ((OnSummonListener) gameUnit).onSummon(out, gameState);
        }
    }
}