package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import org.yaml.snakeyaml.scanner.Constant;
import structures.Constants;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;


/**
 * True Strike costs 1 and inflicts 2 damage upon an enemy unit.
 */
public class TrueStrike extends GameUnit implements Ability {
    public TrueStrike() {
        super(BasicObjectBuilders.loadCard(StaticConfFiles.TRUE_STRIKE, CardIDs.TRUE_STRIKE.getId(), Card.class), false);
    }

    @Override
    public void perform(ActorRef out, GameState gameState) {
        Position p = gameState.getClickedPosition();
        GameUnit targetUnit = gameState.findUnitByPosition(p);
        if (targetUnit != null) {
            // Deal 2 damage to the target unit
            int newHealth = targetUnit.getHealth() - 2;
            targetUnit.setHealth(newHealth);
            if (targetUnit instanceof GamePlayer) {
                if (targetUnit.getID() == Constants.PLAYER_ID) {
                    BasicCommands.setPlayer1Health(out, targetUnit.getPlayer());
                } else if (targetUnit.getID() == Constants.AI_ID) {
                    BasicCommands.setPlayer2Health(out, targetUnit.getPlayer());
                }
                BasicCommands.setUnitHealth(out, targetUnit.getUnit(), newHealth);
            } else {
                BasicCommands.setUnitHealth(out, targetUnit.getUnit(), newHealth);
            }
            // Output message indicating the spell has been cast
            GameLogic.playEffect(out, StaticConfFiles.F_1_PROJECTILES, gameState.getBoard().getTileByPosition(p));
            BasicCommands.addPlayer1Notification(out, "True Strike dealt 2 damage to the unit.", 2);
            targetUnit.onDeath(out, gameState);
        }
    }
}
