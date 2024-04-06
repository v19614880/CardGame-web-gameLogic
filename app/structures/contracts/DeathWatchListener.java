package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * Represents a listener for monitoring unit deaths in the game.
 * Classes implementing this interface define actions to be taken upon the death of a unit.
 */
public interface DeathWatchListener {

    void deathWatch(ActorRef out, GameState gameState);

}
