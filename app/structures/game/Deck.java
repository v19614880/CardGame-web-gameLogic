package structures.game;

import structures.card.GameUnit;
import utils.CardFactory;
import utils.CardIDs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * A deck of cards used in the game.
 */
public class Deck {
    List<GameUnit> playerCards = new ArrayList<>();
    List<GameUnit> aiCards = new ArrayList<>();

    /**
     * Adding cards to the player's and AI's decks.
     */
    public Deck() {
        for(CardIDs id : CardIDs.values()) {
            for (int i = 0; i < 2; i++) {
                GameUnit card = CardFactory.createCard(id);
                if (Objects.requireNonNull(card).isFriendlyUnit()) {
                    playerCards.add(card);
                } else {
                    aiCards.add(card);
                }
            }
        }
        Collections.shuffle(playerCards);
        Collections.shuffle(aiCards);
    }

    public List<GameUnit> getPlayerCards() {
        return playerCards;
    }

    public List<GameUnit> getAiCards() {
        return aiCards;
    }
    /**
     * Draws a card from the player's deck.
     * @return The drawn player card, or null if no card left.
     */
    public GameUnit drawPlayerCard() {
        if (playerCards.isEmpty())
            return null; // No card left
        return playerCards.remove(playerCards.size() - 1);
    }
    /**
     * Draws a card from the AI's deck.
     * @return The drawn AI card, or null if no card left.
     */
    public GameUnit drawAICard() {
        if (aiCards.isEmpty())
            return null; // No card left
        return aiCards.remove(aiCards.size() - 1);
    }
}
