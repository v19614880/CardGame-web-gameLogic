package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Position;
import structures.card.GamePlayer;
import structures.card.GameUnit;
import structures.game.AILogic;

import java.util.List;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * <p>
 * {
 * messageType = “endTurnClicked”
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class EndTurnClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
        if (gameState.gameNotLocked()) {
            gameState.clear(out);
            gameState.setGameLock(true);
            gameState.setPlayerTurn(false);
            GameLogic.clearIsMovedStatus(gameState);
            GameLogic.clearHasAttackedStatus(gameState);
            GameLogic.resetMana(out, (GamePlayer) gameState.getHumanPlayer());

            // AI logic starts here
            BasicCommands.addPlayer2Notification(out, "AI's turn begins!", 2);

            // set AI mana
            ((GamePlayer) gameState.getAIPlayer()).updateMana(out, gameState.getTurn() + 1);
            // draw one card for AI if turn greater than 1.
            if (gameState.getTurn() > 1) {
                AILogic.drawAICard(gameState);
            }

            // each turn, AI will utilize one card to play, if playable.
            GameUnit gameUnit = AILogic.cardSelector(gameState);

            // if card playable, play.
            if (gameUnit != null) {
                gameState.setClickedCard(gameUnit);
                gameState.getBoard().highlightPlayable(out, gameUnit, gameState);

                // wait 1 second for highlighting
                GameLogic.forceWait(1000);

                // select target position and play
                Position positionToPlay = AILogic.selectPlayable(gameState);
                if (positionToPlay != null) {
                    gameState.setClickedPosition(positionToPlay);
                    GameLogic.play(out, positionToPlay, gameState);
                }
            }
            GameLogic.forceWait(1000);

            // each turn, onboard AI units will randomly select a player unit to attack, if within range, or move closer to the human player's position.
            List<GameUnit> onBoardAIUnits = gameState.getAllOnboardAIUnits();
            for (GameUnit aiUnit : onBoardAIUnits) {
                Position aiUnitPosition = aiUnit.getUnit().getPosition();
                gameState.setClickedUnit(aiUnit);
                if (!aiUnit.isMoved() || aiUnit.hasNotAttacked()) {
                    gameState.getBoard().highlightMovable(out, aiUnitPosition, gameState);
                    System.out.println(aiUnit.getID() + " highlighting movable positions");
                    GameLogic.forceWait(2000);

                    // move or attack
                    Position target = AILogic.chooseTarget(aiUnit, gameState);
                    if (target != null) {
                        GameUnit targetUnit = gameState.findUnitByPosition(target);
                        switch (gameState.getBoard().getTileByPosition(target).getMode()) {
                            case 1:
                                if (!aiUnit.isMoved()) {
                                    GameLogic.move(out, gameState, target);
                                    GameLogic.forceWait(2000);
                                }
                                break;
                            case 2:
                                if (aiUnit.hasNotAttacked()) {
                                    GameLogic.processAttack(out, aiUnit, targetUnit, gameState);
                                    GameLogic.forceWait(2000);
                                }
                                break;
                            default:
                                gameState.clear(out);
                                break;
                        }
                    }
                }
            }


            // ending AI turn
            gameState.clear(out);
            GameLogic.clearIsMovedStatus(gameState);
            GameLogic.clearHasAttackedStatus(gameState);
            GameLogic.resetMana(out, (GamePlayer) gameState.getAIPlayer());

            // AI Logic ends
            // update the global provoke status
            GameLogic.provokedStatus(out, gameState);

            // start a new turn
            gameState.setTurn(gameState.getTurn() + 1);
            ((GamePlayer) gameState.getHumanPlayer()).updateMana(out, gameState.getTurn() + 1);
            gameState.getHand().drawCard(gameState.getDeck(), out);
            BasicCommands.addPlayer1Notification(out, "Begin turn: " + gameState.getTurn(), 2);
            GameLogic.processBeamShock(gameState);
            gameState.setPlayerTurn(true);
            gameState.setGameLock(false);

        }
    }
}
