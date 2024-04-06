package structures.card;

import akka.actor.ActorRef;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.basic.UnitAnimationType;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

/**
 * Dark Terminus costs 4 and enables the player to destroy an enemy creature while simultaneously
 * summoning a Wraithling on the tile of the vanquished foe.
 */
public class DarkTerminus extends GameUnit implements Ability {
    public DarkTerminus() {
        super(BasicObjectBuilders.loadCard(StaticConfFiles.DARK_TERMINUS, CardIDs.DARK_TERMINUS.getId(), Card.class), true);
    }

    @Override
    public void perform(ActorRef out, GameState gameState) {
        gameState.getBoard().clearHighlight(out, gameState);
        Position p = gameState.getClickedPosition();
        GameUnit gameUnit = gameState.findUnitByPosition(p);
        if (gameUnit != null && !p.equals(gameState.getAIPlayer().getUnit().getPosition()) && !gameUnit.isFriendlyUnit()) {
            GameLogic.playEffect(out, StaticConfFiles.F_1_SOULSHATTER, gameState.getBoard().getTileByPosition(p));
            gameUnit.updateHealth(out, 0, gameState);
            gameUnit.onDeath(out,gameState);
        }
        GameLogic.summonWraithling(out, p, gameState);
        gameState.clear(out);
    }
}
