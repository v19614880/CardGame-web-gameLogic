package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.card.GameUnit;
import structures.game.Board;

/**
 * Indicates that a unit instance has started a move.
 * The event reports the unique id of the unit.
 *
 * {
 *   messageType = “unitMoving”
 *   id = <unit id>
 * }
 *
 * @author Dr. Richard McCreadie
 *
 */

public class UnitMoving implements EventProcessor{

//	private Board board;

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		gameState.setGameLock(true);
//		try {
//			// 確保 gameState 和 gameState.getBoard() 不是 null
//			if (gameState == null || gameState.getBoard() == null) {
//				throw new IllegalStateException("GameState or Board is null");
//			}
//
//			int unitId = message.get("id").asInt();
//			int targetX = message.get("targetX").asInt();
//			int targetY = message.get("targetY").asInt();
//
//			Unit unitToMove = null;
//			for (GameUnit gameUnit : gameState.getOnBoardGameUnit()) {
//				if (gameUnit != null && gameUnit.getUnit() != null && gameUnit.getUnit().getId() == unitId) {
//					unitToMove = gameUnit.getUnit();
//					break;
//				}
//			}
//
//			if (unitToMove != null) {
//				Position targetPosition = new Position(targetX, targetY);
//				if (gameState.getBoard().isValidPosition(targetPosition)) {
//					unitToMove.setPosition(targetPosition);
//					// 更新前端界面的代碼
//				}
//			} else {
//				throw new IllegalStateException("No unit found with ID: " + unitId);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			// 更詳細的錯誤處理
//		}
	}
}
