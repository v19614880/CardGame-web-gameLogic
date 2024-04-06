package structures.card;

import akka.actor.ActorRef;
import structures.GameLogic;
import structures.GameState;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Position;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Sundrop Elixir, costing 1, restores 4 health to a targeted allied unit without altering its maximum health.
 */
public class SundropElixir extends GameUnit implements Ability {
    public SundropElixir() {
        super(BasicObjectBuilders.loadCard(StaticConfFiles.SUNDROP_ELIXIR, CardIDs.SUNDROP_ELIXIR.getId(), Card.class), false);
    }
    @Override
    public void perform(ActorRef out, GameState gameState) {
        // Find the target unit
        Position p = gameState.getClickedPosition();
        GameUnit targetUnit = gameState.findUnitByPosition(p);
        if (targetUnit !=null) {
            // Heal the target unit for 4 health
            int newHealth = targetUnit.getHealth() + 4;

            // Ensure the healed health does not exceed the unit's maximum health
            targetUnit.updateHealth(out, newHealth, gameState);

            BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.F_1_BUFF), gameState.getBoard().getTileByPosition(p));
            // Output message indicating the spell has been cast
            BasicCommands.addPlayer1Notification(out, "Sundrop Elixir healed target for 4 health!", 2); // Adjust duration as needed
            GameLogic.forceWait(2000);
        }
    }
}
