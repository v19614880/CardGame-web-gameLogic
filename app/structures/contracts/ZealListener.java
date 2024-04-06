package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * Zeal (When you’re the owning player’s avatar takes damage, trigger the
 * following effect): This unit gains +2 attack permanently.
 */
public interface ZealListener {

    void onZeal(ActorRef out, GameState gameState);

}
