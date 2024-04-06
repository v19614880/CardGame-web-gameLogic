package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.Ability;
import structures.contracts.DeathWatchListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Shadow Watcher is a creature with a cost of 3, possessing 3 attack and 2 health. Its ability, Deathwatch,
 * triggers whenever a unit dies, regardless of allegiance. As a result, Shadow Watcher permanently gains +1 attack and +1 health,
 * with the health increase also raising its maximum health limit.
 */
public class ShadowWatcher extends GameUnit implements DeathWatchListener {

    public ShadowWatcher() {
        super(3, 2, BasicObjectBuilders.loadCard(StaticConfFiles.SHADOW_WATCHER, CardIDs.SHADOW_WATCHER.getId(), Card.class), true);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void deathWatch(ActorRef out, GameState gameState) {

        super.setMaxHealth(super.getMaxHealth() + 1);
        int newAttack = this.getAttack() + 1;
        this.updateAttack(out, newAttack);
        int newHealth = this.getHealth() + 1;
        this.updateHealth(out, newHealth, gameState);
        BasicCommands.addPlayer1Notification(out, "ShadowWatcher gains +1 attack and +1 health", 2);
        GameLogic.forceWait(2000);
    }
}

