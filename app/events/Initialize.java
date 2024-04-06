package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.Constants;
import structures.GameState;
import structures.basic.Tile;
import structures.card.GamePlayer;
import structures.card.GameUnit;
import structures.game.AILogic;
import structures.game.Board;
import structures.game.Deck;
import structures.game.Hand;
import structures.GameLogic;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to receive commands from the back-end.
 * <p>
 * {
 * messageType = “initialize"
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class Initialize implements EventProcessor {

    /**
     * Processes game events during gameplay.
     * If the game is not initialized, initializes game elements, draws initial cards,
     * populates empty tiles on the board, and initializes players.
     *
     * @param out       The actor reference responsible for sending commands to the front-end.
     * @param gameState The current state of the game.
     * @param message   The JSON message containing the event data.
     */
    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (!gameState.isGameInitialised()) {
            // initialize game elements
            buildGameEssentials(gameState);

            // draw 3 cards from the deck
            for (int i = 0; i < Constants.INITIAL_CARD_SIZE; i++) {
                gameState.getHand().drawCard(gameState.getDeck(), out);
                AILogic.drawAICard(gameState);
            }

            // populate empty tiles on the board
            // initialize players only after tiles are rendered complete
            gameState.getBoard().populateTiles(out, () -> {
                initializePlayers(out, gameState);
            });

        }
    }

    /**
     * Initializes players, adds them to the game state, sets up UI elements,
     * places player units on the board, and sets up turn logic to start the game.
     *
     * @param out       The actor reference responsible for sending commands to the front-end.
     * @param gameState The current state of the game.
     */
    private void initializePlayers(ActorRef out, GameState gameState) {

        // initialize 2 players and add them to the gameState
        GameUnit humanPlayer = new GamePlayer(true);
        GameUnit aiPlayer = new GamePlayer(false);
        gameState.getOnBoardGameUnit().add(humanPlayer);
        gameState.getOnBoardGameUnit().add(aiPlayer);

        // execute UI sequence
        BasicCommands.setPlayer1Health(out, humanPlayer.getPlayer());
        BasicCommands.setPlayer2Health(out, aiPlayer.getPlayer());
        BasicCommands.setPlayer1Mana(out, humanPlayer.getPlayer());
        BasicCommands.setPlayer2Mana(out, aiPlayer.getPlayer());

        // draw the player unit onto the board
        Tile humanTile = gameState.getBoard().getTile(Constants.PLAYER_INITIAL_POSITION_X, Constants.PLAYER_INITIAL_POSITION_Y);
        Tile aiTile = gameState.getBoard().getTile(Constants.AI_INITIAL_POSITION_X, Constants.AI_INITIAL_POSITION_Y);
        humanPlayer.getUnit().setPositionByTile(humanTile);
        aiPlayer.getUnit().setPositionByTile(aiTile);
        GameLogic.drawUnitOnBoard(out, humanPlayer, humanTile);
        GameLogic.drawUnitOnBoard(out, aiPlayer, aiTile);

        // set turn logic
        gameState.setPlayerTurn(true);
        gameState.setTurn(gameState.getTurn() + 1);
        ((GamePlayer) gameState.getHumanPlayer()).updateMana(out, gameState.getTurn() + 1);
        BasicCommands.addPlayer1Notification(out, "Game begins! Have fun! This is turn no. " + gameState.getTurn(), 5);
        gameState.setGameInitialised(true);
    }

    /**
     * Builds essential game elements such as the game board, deck, and hand for the provided game state.
     *
     * @param gameState The current state of the game.
     */
    private void buildGameEssentials(GameState gameState) {
        gameState.setBoard(new Board());
        gameState.setDeck(new Deck());
        gameState.setHand(new Hand());
    }
}


