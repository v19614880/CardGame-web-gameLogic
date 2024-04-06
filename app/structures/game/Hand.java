package structures.game;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.card.GameUnit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that implements the logic of the cards owned by the player
 */
public class Hand {
    private static final int MAX_HAND_SIZE = 6;
    private final List<GameUnit> cards = new LinkedList<>();
    public List<GameUnit> getCards() {
        return cards;
    }

    /**
     * Draw a card, specifically for the player, from the deck and visualizes it on the player's hand in the front-end.
     *
     * @param deck The deck from which to draw the card.
     * @param out  The actor responsible for sending commands to the front-end.
     */
    public void drawCard(Deck deck, ActorRef out) {
        if (this.cards.size() < MAX_HAND_SIZE) {
            GameUnit gameUnit = deck.drawPlayerCard();
            if (gameUnit != null) {
                this.cards.add(gameUnit);
                gameUnit.setCardPos(this.cards.size());
                gameUnit.draw(out, 0);
            }
        }
    }

    /**
     * Returns the card at the specified position in this hand.
     *
     * @param cardPos - the index of the card to be returned
     * @return the card at the specified position in this hand; returns null
     * if the position is invalid
     */
    public GameUnit getCard(int cardPos) {
        if (cardPos > 0 && cardPos <= cards.size()) {
            return this.cards.get(cardPos - 1);
        } else {
            System.out.println("Invalid position. No card at position " + cardPos + ".");
            return null;
        }
    }

    /**
     * Clears the visual representation of a 'clicked' state for all cards in the hand.
     *
     * @param out The actor responsible for sending commands to the front-end for visual updates.
     */
    public void clearClicked(ActorRef out) {
        for (GameUnit card : this.cards) {
            if (card.isClicked()) {
                card.draw(out, 0);
            }
        }
    }
}
