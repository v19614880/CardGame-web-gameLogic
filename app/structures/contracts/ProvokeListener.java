package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;


/**
 *
 * Provoke (Enemy units in adjacent squares cannot move and can only attack
 * this creature or other creatures with Provoke).
 *
 */


public interface ProvokeListener {
    void onProvoke(ActorRef out, GameState gameState);
}
