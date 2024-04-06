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
 * Rock Pulveriser is a 2-cost creature with 1 attack and 4 health.
 * Its unique ability, Provoke, restricts enemy units in adjacent squares,
 * preventing them from moving and limiting their attacks solely to this creature or other units with Provoke.
 */
public class RockPulveriser extends GameUnit implements Ability, ProvokeListener {

    public RockPulveriser() {
        super(1, 4, BasicObjectBuilders.loadCard(StaticConfFiles.ROCK_PULVERISER, CardIDs.ROCK_PULVERISER.getId(), Card.class), true);
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
