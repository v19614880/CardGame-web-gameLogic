package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Beam Shock, with a cost of 0, stuns a targeted enemy non-avatar unit,
 * preventing it from moving or attacking on the subsequent turn.
 */
public class BeamShock extends GameUnit implements Ability {

    public BeamShock() {
        super(BasicObjectBuilders.loadCard(StaticConfFiles.BEAM_SHOCK, CardIDs.BEAM_SHOCK.getId(), Card.class), false);
    }

    @Override
    public void perform(ActorRef out, GameState gameState) {
        Position targetPosition = gameState.getClickedPosition();
        GameUnit target = gameState.findUnitByPosition(targetPosition);

        if (target != null) {
            target.setBeamShocked(true);
            GameLogic.playEffect(out, StaticConfFiles.F_1_MARTYRDOM, gameState.getBoard().getTileByPosition(targetPosition));
            BasicCommands.addPlayer2Notification(out, "Beam shock applied", 2);
        }
    }
}
