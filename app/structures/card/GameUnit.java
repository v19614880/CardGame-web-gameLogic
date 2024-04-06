package structures.card;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.Constants;
import structures.GameLogic;
import structures.GameState;
import structures.basic.*;
import structures.contracts.DeathWatchListener;
import structures.contracts.OnDeathListener;

import java.util.List;

/**
 * The GameUnit class represents a unit in the card game. This unit can be a player or a type of card.
 * Each GameUnit holds information about the card, attack and health values, maximum health, position, friendly, and provoked and movement status.
 * A unique ID identifies each GameUnit.
 * Different constructors are available for different types of units, such as player, creature card, spell card, or token.
 *
 * @author Team Alpha
 * @version 1.0
 * @since 2024.03.11
 */

public abstract class GameUnit implements OnDeathListener {
    private static int unitIdCounter = 1000;
    private final int uniqueUnitId;
    private final Card card;
    private Unit unit;
    private final Player player;
    private final boolean isPlayer;
    private int attack;
    private int health;
    private int maxHealth;
    private int cardPos;
    private final boolean isFriendlyUnit;
    private boolean isClicked;
    private boolean isMoved = true;
    private boolean attacked = true;
    private boolean isProvoked;

    private boolean isBeamShocked;

    public boolean isBeamShocked() {
        return isBeamShocked;
    }

    public void setBeamShocked(boolean beamShocked) {
        isBeamShocked = beamShocked;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean isProvoked() {
        return isProvoked;
    }

    public void setProvoked(boolean provoked) {
        isProvoked = provoked;
    }

    public boolean hasNotAttacked() {
        return !attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCardPos() {
        return cardPos;
    }

    public void setCardPos(int cardPos) {
        this.cardPos = cardPos;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this instanceof GamePlayer) {
            this.player.setHealth(health);
        }
    }

    public Card getCard() {
        return card;
    }

    public boolean isFriendlyUnit() {
        return isFriendlyUnit;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    /**
     * @return the ID of the GameUnit
     */
    public int getID() {
        if (this.unit != null) {
            return this.unit.getId();
        } else if (this.card != null) {
            return this.card.getId();
        } else {
            return -1;
        }
    }

    // Player
    public GameUnit(Player player, Unit unit, boolean isFriendlyUnit) {
        this.uniqueUnitId = ++unitIdCounter;
        this.player = player;
        this.health = player.getHealth();
        this.attack = Constants.INITIAL_AVATAR_ATTACK;
        this.maxHealth = Constants.MAX_PLAYER_HEALTH;
        this.isFriendlyUnit = isFriendlyUnit;
        this.card = null;
        this.unit = unit;
        this.isPlayer = true;
    }

    // Creature card
    public GameUnit(int attack, int health, Card card, boolean isFriendlyUnit) {
        this.uniqueUnitId = ++unitIdCounter;
        this.player = null;
        this.attack = attack;
        this.health = health;
        this.maxHealth = health;
        this.isFriendlyUnit = isFriendlyUnit;
        this.card = card;
        this.isPlayer = false;
    }

    // Spell card
    public GameUnit(Card card, boolean isFriendlyUnit) {
        this.uniqueUnitId = ++unitIdCounter;
        this.player = null;
        this.card = card;
        this.isFriendlyUnit = isFriendlyUnit;
        this.isPlayer = false;
        this.maxHealth = 0;
    }

    // Token
    public GameUnit(int attack, int health, Unit unit, boolean isFriendlyUnit) {
        this.uniqueUnitId = ++unitIdCounter;
        this.player = null;
        this.card = null;
        this.attack = attack;
        this.health = health;
        this.maxHealth = health;
        this.isFriendlyUnit = isFriendlyUnit;
        this.unit = unit;
        this.isPlayer = false;
    }

    /**
     * Draws the GameCard on the game board at the specified position with the given drawing mode.
     * <p>
     * This method utilizes the BasicCommands.drawCard function to visually represent the GameCard on the
     * game board. It specifies the card to be drawn, the position on the board where it should be displayed,
     * and the drawing mode that determines the visual representation.
     *
     * @param out  ActorRef, responsible for sending commands to the front-end.
     * @param mode 0: highlighted, 1: highlighted
     */
    public void draw(ActorRef out, int mode) {
        if (mode == 0) {
            this.isClicked = false;
        } else if (mode == 1) {
            this.isClicked = true;
        }
        BasicCommands.drawCard(out, this.getCard(), this.cardPos, mode);
    }

    /**
     * Returns the unique identifier for this {@code GameUnit} instance. The unique identifier is automatically
     * generated upon instantiation of the {@code GameUnit} or any of its subclasses, ensuring that each instance
     * has a distinct ID.
     *
     * @return an {@code int} representing the unique identifier of this {@code GameUnit} instance.
     */
    public int getUniqueUnitId() {
        return uniqueUnitId;
    }

    /**
     * Updates the attack value of the unit.
     *
     * @param out    Interact information with the front end.
     * @param attack The new attack value to be set for the unit.
     */
    public void updateAttack(ActorRef out, int attack) {
        this.setAttack(attack);
        BasicCommands.setUnitAttack(out, this.unit, attack);
    }


    /**
     * Updates the health value of the unit.
     *
     * @param out       TInteract information with the front end.
     * @param health    The new health value to be set for the unit.
     * @param gameState The current state of the game.
     */
    public void updateHealth(ActorRef out, int health, GameState gameState) {
        int calculatedHealth = Math.min(health, this.maxHealth);
        this.setHealth(calculatedHealth);
        if (this.unit.getId() == Constants.PLAYER_ID) {
            BasicCommands.setPlayer1Health(out, this.getPlayer());
            BasicCommands.setUnitHealth(out, this.unit, calculatedHealth);
        } else if (this.unit.getId() == Constants.AI_ID) {
            BasicCommands.setPlayer2Health(out, this.getPlayer());
            BasicCommands.setUnitHealth(out, this.unit, calculatedHealth);
        } else {
            BasicCommands.setUnitHealth(out, this.unit, calculatedHealth);
        }
    }

    /**
     * Handles the logic when a unit dies in the game.
     * If the unit's health reaches zero or below, removes the unit from the game state,
     * plays death animation, triggers game over conditions, and invokes death watch logic.
     *
     * @param out       The actor reference responsible for sending commands to the front-end.
     * @param gameState The current state of the game.
     */
    @Override
    public void onDeath(ActorRef out, GameState gameState) {
        if (this.health <= 0) {
            GameLogic.playAnimation(out, this.unit, UnitAnimationType.death);
            BasicCommands.deleteUnit(out, this.unit);
            gameState.getOnBoardGameUnit().remove(this);

            // Game over logic
            if (this.unit.getId() == Constants.PLAYER_ID) {
                // Logic for when the human player's unit dies (you lose)
                BasicCommands.addPlayer1Notification(out, "Game OVER!!! Ooopse, You Lose!", 5); // Placeholder example
                gameState.setPlayerTurn(false);
                gameState.setGameLock(true);
                System.exit(0);
            } else if (this.unit.getId() == Constants.AI_ID) {
                // Logic for when the AI player's unit dies (you win)
                BasicCommands.addPlayer1Notification(out, "Game OVER!!! Congratulations You Win!", 5); // Placeholder example
                gameState.setPlayerTurn(false);
                gameState.setGameLock(true);
                System.exit(0);
            }

            // Deathwatch logic
            List<GameUnit> deathWatchUnits = gameState.getDeathWatchUnits();
            for (GameUnit gameUnit : deathWatchUnits) {
                if (gameUnit instanceof DeathWatchListener) {
                    ((DeathWatchListener) gameUnit).deathWatch(out, gameState);
                }
            }
            GameLogic.provokedStatus(out, gameState);
        }
    }
}