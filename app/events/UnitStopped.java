package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * Indicates that a unit instance has stopped moving. 
 * The event reports the unique id of the unit.
 * 
 * { 
 *   messageType = “unitStopped”
 *   id = <unit id>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class UnitStopped implements EventProcessor{
	/**
	 * Processes game events during gameplay.
	 *
	 * @param out       The actor reference responsible for sending commands to the front-end.
	 * @param gameState The current state of the game.
	 * @param message   The JSON message containing the event data.
	 */
	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if (gameState.isPlayerTurn()) {
			gameState.setGameLock(false);
		}
	}

}
