package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.Ability;
import structures.contracts.ProvokeListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Swamp Entangler is a 1-cost creature with 0 attack and 3 health. Its ability, Provoke,
 * prevents enemy units in adjacent squares from moving and restricts their attacks solely
 * to this creature or other units with Provoke.
 */
public class SwampEntangler extends GameUnit implements ProvokeListener {
    public SwampEntangler() {
        super(0, 3, BasicObjectBuilders.loadCard(StaticConfFiles.SWAMP_ENTANGLER, CardIDs.SWAMP_ENTANGLER.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void onProvoke(ActorRef out, GameState gameState) {
        GameLogic.unitAffectedByProvoke(this, gameState);
    }
}
