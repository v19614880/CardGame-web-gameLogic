package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * Represents a listener for monitoring when a unit is summoned in the game.
 * Implementing classes define actions to be taken when a unit is summoned.
 */
public interface OnSummonListener {
    void onSummon(ActorRef out, GameState gameState);
}
