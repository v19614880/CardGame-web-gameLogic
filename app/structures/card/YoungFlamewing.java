package structures.card;

import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;


/**
 * Young Flamewing is a 4-cost creature with 5 attack and 4 health. It possesses the ability Flying,
 * which grants it the capability to move to any unoccupied space on the board, ignoring obstacles and terrain.
 */
public class YoungFlamewing extends GameUnit {
    public YoungFlamewing() {
        super(5, 4, BasicObjectBuilders.loadCard(StaticConfFiles.YOUNG_FLAMEWING, CardIDs.YOUNG_FLAMEWING.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }
}
