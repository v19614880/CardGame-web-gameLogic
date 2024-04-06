package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.card.GameUnit;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * <p>
 * {
 * messageType = “cardClicked”
 * position = <hand index position [1-6]>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class CardClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (gameState.isPlayerTurn() && gameState.gameNotLocked()) {

            // first get the clicked card
            int cardPos = message.get("position").asInt();
            GameUnit clickedCard = gameState.getHand().getCard(cardPos);

            // if user already clicked one card, clear all the highlight then return
            if (gameState.isCardClicked()) {
                gameState.clear(out);
                return;
            }

            // highlight the card in the hand and set the game state
            gameState.setCardClicked(true);
            gameState.setClickedCard(clickedCard);
            clickedCard.draw(out, 1);

            // highlight playable target tile
            gameState.getBoard().highlightPlayable(out, gameState.getClickedCard(), gameState);
        }
    }
}