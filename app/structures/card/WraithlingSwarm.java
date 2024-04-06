package structures.card;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Wraithling Swarm is a 3-cost spell that summons 3 Wraithlings in sequence.
 */
public class WraithlingSwarm extends GameUnit {

    public int counter = 0;

    public WraithlingSwarm() {
        super(BasicObjectBuilders.loadCard(StaticConfFiles.WRAITHLING_SWARM, CardIDs.WRAITHLING_SWARM.getId(), Card.class), true);
    }
}
