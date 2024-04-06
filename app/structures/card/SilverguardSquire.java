package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.OnSummonListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;
import structures.basic.Position;


/**
 * Silverguard Squire is a 1-cost creature with 1 attack and 1 health. Its Opening Gambit ability activates whenever
 * a unit is summoned onto the battlefield. It grants any adjacent allied unit that is directly in front or behind
 * the owning player's avatar +1 attack and +1 health permanently, increasing their maximum health as well.
 */
public class SilverguardSquire extends GameUnit implements OnSummonListener {
    public SilverguardSquire() {
        super(1, 1, BasicObjectBuilders.loadCard(StaticConfFiles.SILVERGUARD_SQUIRE, CardIDs.SILVERGUARD_SQUIRE.getId(), Card.class), false);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void onSummon(ActorRef out, GameState gameState) {
        Position avatarPosition = gameState.getAIPlayer().getUnit().getPosition();

        Position left = new Position(avatarPosition.getTilex() - 1, avatarPosition.getTiley());
        Position right = new Position(avatarPosition.getTilex() + 1, avatarPosition.getTiley());

        GameUnit leftUnit = gameState.findUnitByPosition(left);
        GameUnit rightUnit = gameState.findUnitByPosition(right);

        if (leftUnit != null && !leftUnit.isFriendlyUnit()) {
            leftUnit.setMaxHealth(leftUnit.getMaxHealth() + 1);
            leftUnit.updateHealth(out, leftUnit.getHealth() + 1, gameState);
            leftUnit.updateAttack(out, leftUnit.getAttack() + 1);
            GameLogic.playEffect(out, StaticConfFiles.F_1_BUFF, gameState.getBoard().getTileByPosition(leftUnit.getUnit().getPosition()));
            BasicCommands.addPlayer2Notification(out, this.getCard().getCardname() + " ability applied", 2);
        }

        if (rightUnit != null && !rightUnit.isFriendlyUnit()) {
            rightUnit.setMaxHealth(rightUnit.getMaxHealth() + 1);
            rightUnit.updateHealth(out, rightUnit.getHealth() + 1, gameState);
            rightUnit.updateAttack(out, rightUnit.getAttack() + 1);
            GameLogic.playEffect(out, StaticConfFiles.F_1_BUFF, gameState.getBoard().getTileByPosition(rightUnit.getUnit().getPosition()));
            BasicCommands.addPlayer2Notification(out, this.getCard().getCardname() + " ability applied", 2);
        }
        GameLogic.forceWait(2000);
    }
}