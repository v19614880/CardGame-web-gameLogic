package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.DeathWatchListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Bad Omen is a cost-free creature with 0 attack and 1 health.
 * Its unique ability, Deathwatch, grants it +1 attack permanently whenever any unit dies
 */
public class BadOmen extends GameUnit implements DeathWatchListener {

    public BadOmen() {
        super(0, 1, BasicObjectBuilders.loadCard(StaticConfFiles.BAD_OMEN, CardIDs.BAD_OMENS.getId(), Card.class), true);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void deathWatch(ActorRef out, GameState gameState) {
        int newAttack = this.getAttack() + 1;
        this.updateAttack(out, newAttack);
        BasicCommands.setUnitAttack(out, this.getUnit(), newAttack);

        BasicCommands.addPlayer1Notification(out, "BadOmen gains +1 attack", 2);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
