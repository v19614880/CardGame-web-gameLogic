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
 * Shadowdancer is a 5-cost creature with 5 attack and 4 health. Its Deathwatch ability activates whenever any unit dies,
 * friend or foe, allowing it to deal 1 damage to the enemy avatar and heal itself for 1 point.
 */
public class ShadowDancer extends GameUnit implements DeathWatchListener {
    public ShadowDancer() {
        super(5, 4, BasicObjectBuilders.loadCard(StaticConfFiles.SHADOWDANCER, CardIDs.SHADOWDANCER.getId(), Card.class), true);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void deathWatch(ActorRef out, GameState gameState) {
        GameUnit aiPlayer = gameState.getAIPlayer();
        GameUnit humanPlayer = gameState.getHumanPlayer();
        aiPlayer.updateHealth(out, aiPlayer.getHealth() - 1, gameState);
        GameLogic.playEffect(out, StaticConfFiles.F_1_PROJECTILES, gameState.getBoard().getTileByPosition(aiPlayer.getUnit().getPosition()));
        humanPlayer.updateHealth(out, humanPlayer.getHealth() + 1, gameState);
        GameLogic.playEffect(out, StaticConfFiles.F_1_BUFF, gameState.getBoard().getTileByPosition(humanPlayer.getUnit().getPosition()));
        aiPlayer.onDeath(out, gameState);
        BasicCommands.addPlayer1Notification(out, this.getCard().getCardname() + " ability applied.", 2);
        GameLogic.forceWait(2000);
    }
}