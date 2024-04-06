package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.Constants;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.basic.Unit;
import structures.contracts.OnSummonListener;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Nightsorrow Assassin is a 3-cost creature with 4 attack and 2 health.
 * Its Opening Gambit ability triggers whenever a unit is summoned onto the battlefield,
 * allowing it to destroy an enemy unit in an adjacent square that has health below its maximum value.
 */
public class NightsorrowAssassin extends GameUnit implements OnSummonListener {
    public NightsorrowAssassin() {
        super(4, 2, BasicObjectBuilders.loadCard(StaticConfFiles.NIGHTSORROW_ASSASSIN, CardIDs.NIGHTSORROW_ASSASSIN.getId(), Card.class), true);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void onSummon(ActorRef out, GameState gameState) {
        Position position = gameState.getClickedPosition();
        int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}};

        List<GameUnit> candidateUnits = new ArrayList<>();
        for (int[] dir : dirs) {
            Position newPos = new Position(position.getTilex() + dir[0], position.getTiley() + dir[1]);
            GameUnit gameUnit = gameState.findUnitByPosition(newPos);
            if (gameUnit != null && gameUnit.getHealth() < gameUnit.getMaxHealth() && gameUnit.getID() != Constants.AI_ID && !gameUnit.isFriendlyUnit()) {
                candidateUnits.add(gameUnit);
            }
        }

        if (!candidateUnits.isEmpty()) {
            BasicCommands.addPlayer1Notification(out, this.getCard().getCardname() + " triggering open gambit", 2);
            GameUnit gameUnit = candidateUnits.get(new Random().nextInt(candidateUnits.size()));
            GameLogic.playEffect(out, StaticConfFiles.F_1_SOULSHATTER, gameState.getBoard().getTileByPosition(gameUnit.getUnit().getPosition()));
            gameUnit.updateHealth(out, 0, gameState);
            gameUnit.onDeath(out, gameState);
        }
    }
}
