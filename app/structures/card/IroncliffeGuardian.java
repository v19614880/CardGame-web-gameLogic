package structures.card;

import akka.actor.ActorRef;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.Ability;
import structures.contracts.ProvokeListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;
import commands.BasicCommands;


/**
 * Ironcliffe Guardian is a 5-cost creature with 3 attack and an impressive 10 health.
 * Its ability, Provoke, confines enemy units in adjacent squares, restricting their movement and
 * limiting their attacks solely to this creature or other units with Provoke.
 */
public class IroncliffeGuardian extends GameUnit implements Ability, ProvokeListener {
    public IroncliffeGuardian() {
        super(3, 10, BasicObjectBuilders.loadCard(StaticConfFiles.IRONCLIFFE_GUARDIAN, CardIDs.IRONCLIFFE_GUARDIAN.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void perform(ActorRef out, GameState gameState) {

    }

    @Override
    public void onProvoke(ActorRef out, GameState gameState) {
        GameLogic.unitAffectedByProvoke(this, gameState);
    }
}
