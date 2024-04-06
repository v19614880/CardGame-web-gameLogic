package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Horn of the Forsaken is a spell that costs 1. It grants the player's avatar an artifact with 3 robustness.
 * When the avatar takes damage, the artifact's robustness decreases by 1. Once the robustness reaches 0,
 * the artifact is destroyed. Additionally, when the player's avatar deals damage to an enemy unit,
 * this spell summons a Wraithling on a randomly selected unoccupied adjacent tile.
 */
public class HornOfTheForsaken extends GameUnit implements Ability {
    public HornOfTheForsaken() {
        super(BasicObjectBuilders.loadCard(StaticConfFiles.HORN_OF_THE_FORSAKEN, CardIDs.HORN_OF_THE_FORSAKEN.getId(), Card.class), true);
    }

    @Override
    public void perform(ActorRef out, GameState gameState) {
        GameUnit humanPlayer = gameState.getHumanPlayer();
        ((GamePlayer) humanPlayer).setBuff(3);
        Position p = humanPlayer.getUnit().getPosition();
        BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.F_1_BUFF), gameState.getBoard().getTileByPosition(p));
        BasicCommands.addPlayer1Notification(out, "Artifact buff added. Remaining: " + ((GamePlayer) humanPlayer).getBuff(), 2);
    }
}
