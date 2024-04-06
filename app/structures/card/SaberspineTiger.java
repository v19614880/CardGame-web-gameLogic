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
 * Saberspine Tiger is a 3-cost creature with 3 attack and 2 health.
 * Its ability, Rush, grants it the capability to move and attack during the turn it is summoned.
 */
public class SaberspineTiger extends GameUnit {
    public SaberspineTiger() {
        super(3, 2, BasicObjectBuilders.loadCard(StaticConfFiles.SABERSPINE_TIGER, CardIDs.SABERSPINE_TIGER.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
        super.setAttacked(false);
        super.setMoved(false);
    }
}
