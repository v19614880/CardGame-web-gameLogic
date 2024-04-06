package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.Constants;
import structures.GameLogic;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Unit;
import structures.contracts.OnHitListener;
import structures.game.Board;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import structures.contracts.ZealListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a player unit in the game. Extends {@link GameUnit} class and implements {@link OnHitListener}.
 * A player unit possesses attributes such as health, mana, buffs, and abilities specific to player actions.
 * It handles updates to player health and mana, as well as special effects triggered upon taking damage.
 * Additionally, it implements logic associated with specific player actions, such as summoning units upon being hit.
 */
public class GamePlayer extends GameUnit implements OnHitListener {
    private int buff;

    public GamePlayer(boolean isFriendlyUnit) {
        super(new Player(), BasicObjectBuilders.loadUnit(isFriendlyUnit ? StaticConfFiles.HUMAN_AVATAR : StaticConfFiles.AI_AVATAR,
                isFriendlyUnit ? Constants.PLAYER_ID : Constants.AI_ID, Unit.class), isFriendlyUnit);
        super.setMoved(false);
        super.setAttacked(false);
    }

    public int getBuff() {
        return buff;
    }

    public void setBuff(int buff) {
        this.buff = buff;
    }


    /**
     * Updates the mana of the player and update this change on the client side.
     *
     * @param out  An ActorRef object used for communication with the client.
     * @param mana The updated mana value to be assigned to the player.
     */
    public void updateMana(ActorRef out, int mana) {
        this.getPlayer().setMana(Math.min(mana, Constants.MAX_PLAYER_MANA));
        if (super.getUnit().getId() == Constants.PLAYER_ID) {
            BasicCommands.setPlayer1Mana(out, this.getPlayer());
        } else if (super.getUnit().getId() == Constants.AI_ID) {
            BasicCommands.setPlayer2Mana(out, this.getPlayer());
        }
    }

    /**
     * Updates the health of the unit, performs special effects if applicable, and triggers events related to health changes.
     * If the unit has a positive buff and its health decreases, plays a special effect animation and decreases the buff count.
     * Otherwise, updates the unit's health as usual and triggers zeal-related events if the unit is an AI unit.
     *
     * @param out       The actor reference responsible for sending commands to the front-end.
     * @param health    The new health value of the unit.
     * @param gameState The current state of the game.
     */
    @Override
    public void updateHealth(ActorRef out, int health, GameState gameState) {
        if (this.buff > 0 && health < this.getPlayer().getHealth()) { // meaning if health is decreased
            this.buff--;
            Position p = super.getUnit().getPosition();
            BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.F_1_BUFF), gameState.getBoard().getTileByPosition(p));
            BasicCommands.addPlayer1Notification(out, "Remaining Artifact: " + this.buff, 2);
        } else {
            int oldHealth = this.getHealth();
            super.updateHealth(out, health, gameState);
            if (super.getUnit().getId() == Constants.AI_ID) {
                if (this.getHealth() < oldHealth) {
                    List<GameUnit> zealUnits = gameState.getZealUnits();
                    for (GameUnit unit : zealUnits) {
                        ((ZealListener) unit).onZeal(out, gameState);
                    }
                }
            }
        }
    }

    /**
     * Implements the on-hit logic associated with the spell card "Horn of the Forsaken".
     * <p>
     * This method is triggered when the player takes damage while having the "Horn of the Forsaken" buff active.
     * It evaluates the surrounding tiles of the player's current position to find valid and unoccupied tiles.
     * From these valid positions, one is chosen at random, and a Wraithling unit is summoned to that location.
     * </p>
     * <p>
     * For more detailed information on the mechanics and effects of the "Horn of the Forsaken" spell card, refer to
     * the {@link structures.card.HornOfTheForsaken} class.
     * </p>
     *
     * @param out       An ActorRef object used for communication with the client, primarily for visual and game state updates.
     * @param gameState The current state of the game, containing all relevant information such as the board, units, and game logic.
     */
    @Override
    public void onHit(ActorRef out, GameState gameState) {
        List<Position> positionList = new ArrayList<>();
        if (this.buff > 0 && super.getID() == Constants.PLAYER_ID) {
            Board board = gameState.getBoard();
            int[][] dirs = {{0, -1}, {1, -1}, {-1, -1}, {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}};
            Position position = super.getUnit().getPosition();
            for (int[] dir : dirs) {
                Position newPos = new Position(position.getTilex() + dir[0], position.getTiley() + dir[1]);
                if (board.isNotOccupied(newPos, gameState) && board.isValidPosition(newPos)) {
                    positionList.add(newPos);
                }
            }
            if (!positionList.isEmpty()) {
                Position randomPosition = positionList.get(new Random().nextInt(positionList.size()));
                GameLogic.summonWraithling(out, randomPosition, gameState);
            }
        }
    }
}