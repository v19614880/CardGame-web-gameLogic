package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.Ability;
import structures.contracts.ProvokeListener;
import structures.contracts.ZealListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Silverguard Knight is a 3-cost creature with 1 attack and 5 health. It possesses two abilities:
 * Zeal: Whenever the owning player's avatar takes damage, this unit gains +2 attack permanently.
 * Provoke: This ability restricts enemy units in adjacent squares, preventing them from moving and limiting
 * their attacks solely to this creature or other units with Provoke.
 */
public class SilverguardKnight extends GameUnit implements ProvokeListener, ZealListener {
    public SilverguardKnight() {
        super(1, 5, BasicObjectBuilders.loadCard(StaticConfFiles.SILVERGUARD_KNIGHT, CardIDs.SILVERGUARD_KNIGHT.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void onProvoke(ActorRef out, GameState gameState) {
        GameLogic.unitAffectedByProvoke(this, gameState);
    }

    @Override
    public void onZeal(ActorRef out, GameState gameState) {
        int newAttack = this.getAttack() + 2;
        this.updateAttack(out, newAttack);

        BasicCommands.addPlayer1Notification(out, "Silver guard Knight gains +2 attack", 2);
        GameLogic.forceWait(2000);
    }

}
