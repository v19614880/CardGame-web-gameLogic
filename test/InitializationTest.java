import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initialize;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import structures.GameState;
import structures.basic.Position;
import structures.basic.Tile;
import structures.card.*;
import structures.game.Board;
import utils.BasicObjectBuilders;
import structures.Constants;


import java.util.List;


import static org.junit.Assert.*;


/**
 * This is an example of a JUnit test. In this case, we want to be able to test the logic
 * of our system without needing to actually start the web server. We do this by overriding
 * the altTell method in BasicCommands, which means whenever a command would normally be sent
 * to the front-end it is instead discarded. We can manually simulate messages coming from the
 * front-end by calling the processEvent method on the appropriate event processor.
 *
 * @author Richard
 */
public class InitializationTest {

    private Board board;
    private GameState gameState;
    private GameUnit gameUnit;

    /**
     * This test simply checks that a boolean variable is set in GameState when we call the
     * initialise method for illustration.
     */
    @Test
    public void checkInitalized() {

        CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
        BasicCommands.altTell = altTell; // specify that the alternative tell should be used

        GameState gameState = new GameState(); // create state storage
        Initialize initializeProcessor = new Initialize(); // create an initalize event processor

        assertFalse(gameState.isGameInitialised()); // check we have not initalized

        ObjectNode eventMessage = Json.newObject(); // create a dummy message
        initializeProcessor.processEvent(null, gameState, eventMessage); // send it to the initalize event processor

        assertTrue(gameState.isGameInitialised()); // check that this updated the game state

        Tile tile = BasicObjectBuilders.loadTile(3, 2); // create a tile
        BasicCommands.drawTile(null, tile, 0); // draw tile, but will use altTell, so nothing should happen

        List<GameUnit> playerCards = gameState.getDeck().getPlayerCards();

        List<GameUnit> AICards = gameState.getDeck().getAiCards();
        for (GameUnit card : playerCards) {
            System.out.println("Player: " + card.getCard().getCardname() + " Id: " + card.getCard().getId() + " isCreature " + card.getCard().isCreature());
        }
        for (GameUnit card : AICards) {
            System.out.println("AI: " + card.getCard().getCardname() + " Id: " + card.getCard().getId() + " isCreature " + card.getCard().isCreature());
        }
    }

    @Test
    public void testRockPulveriserProvoking() {
        GameState gameState = new GameState();
        RockPulveriser playerProvoker = new RockPulveriser();
        playerProvoker.getUnit().setPosition(new Position(1, 1));
        gameState.getOnBoardGameUnit().add(playerProvoker);

        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (!(x == 1 && y == 1)) { // Skip the provoker's position
                    GamePlayer aiUnit = new GamePlayer(false); // AI unit
                    aiUnit.getUnit().setPosition(new Position(x, y));
                    gameState.getOnBoardGameUnit().add(aiUnit);
                }
            }
        }

        playerProvoker.onProvoke(null, gameState);

        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            if (unit != playerProvoker) {
                assertTrue("AI unit at " + unit.getUnit().getPosition() + " should be provoked", unit.isProvoked());
            }
        }
    }

    @Test
    public void testIroncliffeGuardianProvoking() {
        GameState gameState = new GameState();
        IroncliffeGuardian aiProvoker = new IroncliffeGuardian();
        aiProvoker.getUnit().setPosition(new Position(1, 1));
        gameState.getOnBoardGameUnit().add(aiProvoker);

        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (!(x == 1 && y == 1)) { // Skip the provoker's position
                    GamePlayer playerUnit = new GamePlayer(true); // Player unit
                    playerUnit.getUnit().setPosition(new Position(x, y));
                    gameState.getOnBoardGameUnit().add(playerUnit);
                }
            }
        }

        aiProvoker.onProvoke(null, gameState);

        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            if (unit != aiProvoker) {
                assertTrue("Player unit at " + unit.getUnit().getPosition() + " should be provoked", unit.isProvoked());
            }
        }
    }

    @Test
    public void testSilverguardKnightProvoking() {
        GameState gameState = new GameState();
        SilverguardKnight aiProvoker = new SilverguardKnight();
        aiProvoker.getUnit().setPosition(new Position(1, 1));
        gameState.getOnBoardGameUnit().add(aiProvoker);

        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (!(x == 1 && y == 1)) {
                    GamePlayer playerUnit = new GamePlayer(true); // Player unit
                    playerUnit.getUnit().setPosition(new Position(x, y));
                    gameState.getOnBoardGameUnit().add(playerUnit);
                }
            }
        }

        aiProvoker.onProvoke(null, gameState);

        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            if (unit != aiProvoker) {
                assertTrue("Player unit at " + unit.getUnit().getPosition() + " should be provoked", unit.isProvoked());
            }
        }
    }

    @Test
    public void testSwampEntanglerProvoking() {
        GameState gameState = new GameState();
        SwampEntangler aiProvoker = new SwampEntangler();
        aiProvoker.getUnit().setPosition(new Position(1, 1));
        gameState.getOnBoardGameUnit().add(aiProvoker);

        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (!(x == 1 && y == 1)) {
                    GamePlayer playerUnit = new GamePlayer(true); // Player unit
                    playerUnit.getUnit().setPosition(new Position(x, y));
                    gameState.getOnBoardGameUnit().add(playerUnit);
                }
            }
        }

        aiProvoker.onProvoke(null, gameState);

        for (GameUnit unit : gameState.getOnBoardGameUnit()) {
            if (unit != aiProvoker) {
                assertTrue("Player unit at " + unit.getUnit().getPosition() + " should be provoked", unit.isProvoked());
            }
        }
    }

    /**
     * Board class testing
     */
    @Before
    public void setUp() {
        board = new Board();
        gameState = new GameState();
        gameUnit = new GamePlayer(true);

    }

    @After
    public void teardown() {
        system = null;
    }


    @Test
    public void testInit() {
        for (int x = 0; x < board.getBoard_X(); x++) {
            for (int y = 0; y < board.getBoard_Y(); y++) {
                assertNotNull("Tile should not be null", board.getTiles()[x][y]);
            }
        }
    }

    @Test
    public void testIsValidPosition() {
        assertTrue("Position inside the board should be valid", board.isValidPosition(new Position(2, 3)));
        assertFalse("Position outside the board should be invalid", board.isValidPosition(new Position(-1, 5)));
    }

    @Test
    public void testIsNotOccupied() {
        assertTrue("Unoccupied position should be true", board.isNotOccupied(new Position(2, 3), gameState));
    }

    @Test
    public void testIsPlayable() {
        gameState.getPositionsToHighlight().add(new Position(2, 3));
        assertTrue("Highlighted position should be playable", board.isPlayable(new Position(2, 3), gameState));
    }

    @Test
    public void testGetTile() {
        assertNotNull("getTile should return a Tile object", board.getTile(2, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getTile(-1, 5);
        });
    }

    @Test
    public void testGetTileByPosition() {
        assertEquals("getTileByPosition should return correct Tile object", board.getTile(2, 3), board.getTileByPosition(new Position(2, 3)));
    }

    @Test
    public void testDetermineYFirst() {
        assertTrue("Should prioritize Y movement",
                board.determineYFirst(new Position(0, 0), new Position(0, 3)));
        assertFalse("Should prioritize X movement",
                board.determineYFirst(new Position(0, 0), new Position(3, 0)));
    }

    /**
     * GameUnit class test
     */

    @Test
    public void testGameUnitInitialization() {
        assertNotNull("Unit should be initialized", gameUnit);
        assertEquals("Initial health should be set correctly", Constants.MAX_PLAYER_HEALTH, gameUnit.getHealth());
    }

    @Test
    public void testGameUnitStateChange() {
        int newHealth = 5;
        gameUnit.updateHealth(null, newHealth, gameState);
        assertEquals("Health should be updated", newHealth, gameUnit.getHealth());
    }

    /**
     * Test special behaviors (e.g., onDeath) of GameUnit.
     */
    @Test
    public void testGameUnitSpecialBehavior() {
        GameUnit unit = new GamePlayer(true);
        GameState gameState = new GameState();
        unit.updateHealth(null, 0, gameState);
    }

    @Test
    public void testUpdateAttack() {
        int newAttack = 10;
        gameUnit.updateAttack(null, newAttack);
        assertEquals("Attack value should be updated", newAttack, gameUnit.getAttack());
    }

    @Test
    public void testProvokedStatus() {
        gameUnit.setProvoked(true);
        assertTrue("Unit should be marked as provoked", gameUnit.isProvoked());
    }


}





