package structures.card;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Skyrock Golem is a 2-cost creature with 4 attack and 2 health.
 */
public class SkyrockGolem extends GameUnit {
    public SkyrockGolem() {
        super(4, 2, BasicObjectBuilders.loadCard(StaticConfFiles.SKYROCK_GOLEM, CardIDs.SKYROCK_GOLEM.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(),card.getId(),Unit.class));
    }
}
