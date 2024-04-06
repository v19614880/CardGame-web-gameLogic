package structures.contracts;

import akka.actor.ActorRef;
import structures.GameState;

/**
 * Represents an ability that can be performed in the game.
 */
public interface Ability {
    void perform(ActorRef out, GameState gameState);
}