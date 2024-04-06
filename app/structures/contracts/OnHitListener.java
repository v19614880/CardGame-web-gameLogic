package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * Represents a listener for monitoring when a unit is hit in the game.
 * Implementing classes define actions to be taken when a unit is hit.
 */
public interface OnHitListener {
    void onHit(ActorRef out, GameState gameState);
}
