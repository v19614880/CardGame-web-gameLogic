package structures.card;

import akka.actor.ActorRef;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.basic.Unit;
import structures.contracts.OnSummonListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;


/**
 * Gloom Chaser is a creature with a cost of 2, 3 attack, and 1 health.
 * Its Opening Gambit ability summons a Wraithling behind it whenever a unit is summoned onto the battlefield, provided the space is unoccupied.
 */
public class GloomChaser extends GameUnit implements OnSummonListener {
    public GloomChaser() {
        super(3, 1, BasicObjectBuilders.loadCard(StaticConfFiles.GLOOM_CHASER, CardIDs.GLOOM_CHASER.getId(), Card.class), true);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void onSummon(ActorRef out, GameState gameState) {
       // Get the position of the Gloom Chaser
        Position position = this.getUnit().getPosition();

        // Check if the summoned unit is Gloom Chaser itself
        if (position != null) {
            // Get the position behind the Gloom Chaser
            Position wraithlingPosition = new Position(position.getTilex() - 1, position.getTiley() );

            // Check if the position is valid and not occupied
            if (gameState.getBoard().isValidPosition(wraithlingPosition) && gameState.getBoard().isNotOccupied(wraithlingPosition, gameState)) {
                // Summon a Wraithling at the position behind the Gloom Chaser
                GameLogic.summonWraithling(out, wraithlingPosition, gameState);
            }
        }
    }
}