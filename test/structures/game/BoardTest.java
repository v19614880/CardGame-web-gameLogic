package structures.game;

import org.junit.Test;
import structures.Constants;
import static org.junit.Assert.*;
import structures.GameState;
import structures.basic.Position;
import structures.basic.Tile;

public class BoardTest {
    private Board board;
    private GameState gameStateMock;

    @Test
    public void getBoard_X() {
        assertEquals(Constants.BOARD_SIZE_X, board.getBoard_X());
    }

    @Test
    public void getBoard_Y() {
        assertEquals(Constants.BOARD_SIZE_X, board.getBoard_X());
    }

    @Test
    public void getTiles() {
        assertEquals(Constants.BOARD_SIZE_Y, board.getBoard_Y());
    }

    @Test
    public void populateTiles() {
        Tile[][] tiles = board.getTiles();
        assertEquals(Constants.BOARD_SIZE_X, tiles.length);
        assertEquals(Constants.BOARD_SIZE_Y, tiles[0].length);
    }

    @Test
    public void isPlayable() {
        // Prepare mock objects
        Position position = new Position(2, 2);
        // Set up the gameStateMock as needed

        // Test when the position is playable
        assertTrue(board.isPlayable(position, gameStateMock));
    }

    @Test
    public void isFriendlyUnitPosition() {
        Position position = new Position(2, 2);
        // Set up the gameStateMock as needed

        // Test when the position contains a friendly unit
        assertTrue(board.isFriendlyUnitPosition(position, gameStateMock));
    }

    @Test
    public void isNotOccupied() {
        Position position = new Position(2, 2);
        // Set up the gameStateMock as needed

        // Test when the position is occupied
        assertFalse(board.isNotOccupied(position, gameStateMock));
    }

    @Test
    public void isValidPosition() {
        assertTrue(board.isValidPosition(new Position(2, 2)));

        // Test invalid position
        assertFalse(board.isValidPosition(new Position(-1, 2)));
    }

    @Test
    public void checkMove() {
        Position currentPosition = new Position(2, 2);
        Position targetPosition = new Position(3, 3);
        // Set up the gameStateMock as needed

        // Invoke method
        boolean result = board.checkMove(gameStateMock, currentPosition, targetPosition);
    }
}