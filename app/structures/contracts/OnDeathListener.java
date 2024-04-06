package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;
import structures.card.GameUnit;

/**
 * Interface to be implemented by classes that wish to be notified when a GameUnit dies.
 */
public interface OnDeathListener {
    /**
     * Called when a GameUnit's health is reduced to 0 or below.
     */
    void onDeath(ActorRef out, GameState gameState);
}
