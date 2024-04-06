package structures;

import akka.actor.ActorRef;
import structures.basic.Position;
import structures.card.GameUnit;
import structures.contracts.DeathWatchListener;
import structures.contracts.OnDeathListener;
import structures.contracts.OnSummonListener;
import structures.contracts.ZealListener;
import structures.game.Board;
import structures.game.Deck;
import structures.game.Hand;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class can be used to hold information about the ongoing game.
 * It's created with the GameActor.
 *
 * @author Dr. Richard McCreadie
 */
public class GameState {
    private boolean gameInitialised = false;
    private boolean isPlayerTurn = false;
    private boolean isCardClicked = false;
    private boolean isHighlighted = false;
    private boolean gameLock = false;
    private int turn = 0;
    private Deck deck;
    private Board board;
    private Hand hand;
    private GameUnit clickedUnit;
    private GameUnit clickedCard;
    private final List<GameUnit> onBoardGameUnit = new LinkedList<>();
    private final List<GameUnit> AIHand = new LinkedList<>();
    private final List<Position> positionsToHighlight = new LinkedList<>();
    private Position clickedPosition; //for tracking current unit's position
    private Position highlighted;

    public List<GameUnit> getOnBoardGameUnit() {
        return onBoardGameUnit;
    }

    public List<Position> getPositionsToHighlight() {
        return positionsToHighlight;
    }

    /**
     * Retrieves the game unit representing the human avatar on the game board.
     *
     * @return The game unit representing the human avatar, or null if not found.
     */
    public GameUnit getHumanPlayer() {
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (gameUnit.getUnit().getId() == Constants.PLAYER_ID) {
                return gameUnit;
            }
        }
        return null;
    }

    /**
     * Retrieves the game unit representing the AI avatar on the game board.
     *
     * @return The game unit representing the AI avatar, or null if not found.
     */
    public GameUnit getAIPlayer() {
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (gameUnit.getUnit().getId() == Constants.AI_ID) {
                return gameUnit;
            }
        }
        return null;
    }

    public List<GameUnit> getAIHand() {
        return AIHand;
    }

    public boolean gameNotLocked() {
        return !gameLock;
    }

    public void setGameLock(boolean gameLock) {
        this.gameLock = gameLock;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public GameUnit getClickedCard() {
        return clickedCard;
    }

    public void setClickedCard(GameUnit clickedCard) {
        this.clickedCard = clickedCard;
    }

    public GameUnit getClickedUnit() {
        return clickedUnit;
    }

    public void setClickedUnit(GameUnit clickedUnit) {
        this.clickedUnit = clickedUnit;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public boolean isGameInitialised() {
        return gameInitialised;
    }

    public void setGameInitialised(boolean gameInitialised) {
        this.gameInitialised = gameInitialised;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public boolean isCardClicked() {
        return isCardClicked;
    }

    public void setCardClicked(boolean cardClicked) {
        isCardClicked = cardClicked;
    }

    public Position getClickedPosition() {
        return clickedPosition;
    }

    public void setClickedPosition(Position clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    public Position getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(Position highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Finds a game unit on the board by its position.
     *
     * @param p The position to search for.
     * @return The game unit found at the specified position, or null if no unit is found at that position.
     */
    public GameUnit findUnitByPosition(Position p) {
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (gameUnit.getUnit().getPosition().equals(p)) {
                return gameUnit;
            }
        }
        return null;
    }


    /**
     * Retrieves a list of all AI units currently on the game board.
     *
     * @return A list containing all AI units currently on the game board.
     */
    public List<GameUnit> getAllOnboardAIUnits() {
        List<GameUnit> list = new ArrayList<>();
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (!gameUnit.isFriendlyUnit()) {
                list.add(gameUnit);
            }
        }
        return list;
    }

    /**
     * Retrieves a list of open gambit units currently on the game board.
     * An open gambit unit is defined as any game unit that implements the OnSummonListener interface.
     *
     * @return A list of open gambit units that are currently on the board.
     */
    public List<GameUnit> getOpeningGambitUnits() {
        List<GameUnit> list = new ArrayList<>();
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (gameUnit instanceof OnSummonListener) {
                list.add(gameUnit);
            }
        }
        return list;
    }

    /**
     * Retrieves a list of death watch units currently on the game board.
     * A death watch unit is defined as any game unit that implements the DeathWatchListener interface.
     *
     * @return A list of death watch units that are currently on the board.
     */
    public List<GameUnit> getDeathWatchUnits() {
        List<GameUnit> list = new ArrayList<>();
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (gameUnit instanceof DeathWatchListener) {
                list.add(gameUnit);
            }
        }
        return list;
    }

    public List<GameUnit> getZealUnits() {
        List<GameUnit> list = new ArrayList<>();
        for (GameUnit gameUnit : this.onBoardGameUnit) {
            if (gameUnit instanceof ZealListener) {
                list.add(gameUnit);
            }
        }
        return list;
    }

    /**
     * Resets the gameState by clearing highlighted tiles on the board, resetting the flag for a clicked card, resetting the flag for a clicked tile,
     * and nullifying the reference to a clicked card.
     *
     * @param out An ActorRef object that may be used for sending messages back to the client or handling game output.
     */
    public void clear(ActorRef out) {
        getBoard().clearHighlight(out, this);
        getHand().clearClicked(out);
        setCardClicked(false);
        setHighlighted(false);
        setClickedUnit(null);
        setClickedCard(null);
        setClickedPosition(null);
        setHighlighted(null);
    }
}
