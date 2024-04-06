package structures.card;

import akka.actor.ActorRef;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Position;
import structures.basic.Unit;
import structures.contracts.Ability;
import structures.contracts.DeathWatchListener;
import structures.game.Board;
import utils.BasicObjectBuilders;
import utils.CardIDs;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Bloodmoon Priestess is a 4-cost creature with 3 attack and 3 health. Its Deathwatch ability activates whenever any unit dies,
 * summoning a Wraithling on a randomly chosen unoccupied adjacent tile. If all adjacent tiles are occupied, the ability remains inactive.
 */
public class BloodmoonPriestess extends GameUnit implements DeathWatchListener {
    public BloodmoonPriestess() {
        super(3, 3, BasicObjectBuilders.loadCard(StaticConfFiles.BLOODMOON_PRIESTESS, CardIDs.BLOODMOON_PRIESTESS.getId(), Card.class), true);
        Card card = super.getCard();
        card.setId(super.getUniqueUnitId());
        super.setUnit(BasicObjectBuilders.loadUnit(card.getUnitConfig(), card.getId(), Unit.class));
    }

    @Override
    public void deathWatch(ActorRef out, GameState gameState) {

        List<Position> nearPosition = new ArrayList<>();
        int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}};
        Position currentPosition = this.getUnit().getPosition();

        Board board = gameState.getBoard();
        for (int[] dir : dirs) {
            Position newPos = new Position(currentPosition.getTilex() + dir[0], currentPosition.getTiley() + dir[1]);
            if (board.isNotOccupied(newPos, gameState) && board.isValidPosition(newPos)) {
                nearPosition.add(newPos);
            }
        }

        if (!nearPosition.isEmpty()) {
            Position randomPosition = nearPosition.get(new Random().nextInt(nearPosition.size()));
            GameLogic.summonWraithling(out, randomPosition, gameState);
        }
    }
}

