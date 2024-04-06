package structures.game;

import structures.GameState;
import structures.basic.Position;
import structures.card.GameUnit;

import java.util.*;

/**
 * Provides AI-related logic functionalities for managing AI actions within the game.
 */
public class AILogic {

    /**
     * Draws a card for the AI player from the AI-specific deck and adds it to the AI's hand.
     *
     * @param gameState current state of the game.
     */
    public static void drawAICard(GameState gameState) {
        gameState.getAIHand().add(gameState.getDeck().drawAICard());
    }

    /**
     * Selects a game unit (card) from the AI's hand that can be played within the current mana constraints.
     * This method iterates through the AI player's hand to find all cards whose mana cost does not exceed
     * the AI player's current available mana and randomly selects one to play.
     *
     * @param gameState The current state of the game.
     * @return A randomly selected {@link GameUnit} card from the AI's hand that can be played within the current
     * mana constraints, or null if no such card exists.
     */
    public static GameUnit cardSelector(GameState gameState) {
        List<GameUnit> eligibleUnits = new ArrayList<>();

        for (GameUnit gameUnit : gameState.getAIHand()) {
            if (gameUnit.getCard().getManacost() <= gameState.getAIPlayer().getPlayer().getMana()) {
                eligibleUnits.add(gameUnit);
            }
        }

        if (!eligibleUnits.isEmpty()) {
            Random random = new Random();
            GameUnit selectedUnit = eligibleUnits.get(random.nextInt(eligibleUnits.size()));
            System.out.println("AI selected: " + selectedUnit.getCard().getCardname() + " to play");
            return selectedUnit;
        } else {
            System.out.println("AI selected nothing to play.");
            return null;
        }
    }

    /**
     * Selects a random position from a list of playable positions in the given game state.
     * If there are no highlighted positions, the method returns {@code null}.
     *
     * @param gameState The current state of the game.
     * @return A {@code Position} object representing a randomly selected playable location from
     * the list of highlighted positions. Returns {@code null} if there are no positions
     * to select from.
     */
    public static Position selectPlayable(GameState gameState) {

        List<Position> positionList = new ArrayList<>(gameState.getPositionsToHighlight());

        if (!positionList.isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(positionList.size());
            return positionList.get(randomIndex);
        }
        return null;
    }

    /**
     * This method is used to choose the target for a GameUnit based on the current state of the game.
     *
     * @param whoever   The GameUnit that needs to choose a target.
     * @param gameState The current state of the game.
     * @return The target position of the GameUnit.
     * <p>
     * The method first creates two empty set collections for attack positions and move positions.
     * For each position in the highlighted positions, it checks the mode of the tile at that position.
     * If the mode is 2 (indicating an enemy unit within range), it adds the position to attackPositions set.
     * If the mode is 1 (indicating a friendly unit within range), it adds the position to movePositions set.
     * <p>
     * If there are no positions in the attackPositions, meaning there are no enemies within the attack range, it uses AILogic to return the closest position to the human player.
     * Otherwise, it randomly picks a position from the attackPositions for the GameUnit to attack.
     */
    public static Position chooseTarget(GameUnit whoever, GameState gameState) {

        Set<Position> attackPositions = new HashSet<>();
        Set<Position> movePositions = new HashSet<>();

        // if position highlighted as red, meaning enemy position within range, add to attackPositions
        for (Position p : gameState.getPositionsToHighlight()) {
            if (gameState.getBoard().getTileByPosition(p).getMode() == 2) {
                GameUnit unit = gameState.findUnitByPosition(p);
                if (unit != null && !unit.isProvoked()) {
                    attackPositions.add(p);
                }
            } else if (gameState.getBoard().getTileByPosition(p).getMode() == 1) {
                movePositions.add(p);
            }
        }

        // if empty, meaning enemy out of range, then return the closet position to the human player.
        if (attackPositions.isEmpty()) {
            return AILogic.closestToHumanPlayer(whoever, new ArrayList<>(movePositions), gameState);
        } else {
            Random rand = new Random();
            List<Position> positionList = new ArrayList<>(attackPositions);
            int randomIndex = rand.nextInt(attackPositions.size());
            return positionList.get(randomIndex);
        }
    }

    private static Position closestToHumanPlayer(GameUnit whoever, List<Position> positionList, GameState gameState) {
        Position humanPlayerPosition = gameState.getHumanPlayer().getUnit().getPosition();
        positionList.add(whoever.getUnit().getPosition());
        Position closestPosition = null;
        int smallestDistance = Integer.MAX_VALUE;

        for (Position candidatePosition : positionList) {
            int distance = calculateDistance(candidatePosition, humanPlayerPosition);

            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestPosition = candidatePosition;
            }
        }
        return closestPosition;
    }

    private static int calculateDistance(Position p1, Position p2) {
        return Math.abs(p1.getTilex() - p2.getTilex()) + Math.abs(p1.getTiley() - p2.getTiley());
    }
}
