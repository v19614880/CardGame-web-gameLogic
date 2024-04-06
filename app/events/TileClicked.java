package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Position;

import structures.card.GamePlayer;
import structures.GameLogic;
import structures.card.GameUnit;
import structures.card.WraithlingSwarm;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * <p>
 * {
 * messageType = “tileClicked”
 * tilex = <x index of the tile>
 * tiley = <y index of the tile>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class TileClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
        Position clickedPosition = new Position(message.get("tilex").asInt(), message.get("tiley").asInt());
        if (gameState.gameNotLocked()) {
            GameLogic.processClickedTile(clickedPosition, gameState);
            if (gameState.isPlayerTurn()) {
                // check if a card in hand is clicked and game board is highlighted
                if (gameState.isCardClicked() && gameState.isHighlighted()) {
                    // enter logic to play the creature onto the board or spell onto target
                    if (gameState.getBoard().isPlayable(clickedPosition, gameState)) {
                        int manaCost = gameState.getClickedCard().getCard().getManacost();
                        int playerMana = gameState.getHumanPlayer().getPlayer().getMana();
                        if (playerMana >= manaCost) {
                            if (gameState.getClickedCard() instanceof WraithlingSwarm) {
                                GameLogic.playWraithlingSwarm(out, clickedPosition, gameState);
                            } else {
                                GameLogic.play(out, clickedPosition, gameState);
                                GameLogic.updatePlayerMana(out, (GamePlayer) gameState.getHumanPlayer(), playerMana - manaCost);
                            }
                        } else {
                            BasicCommands.addPlayer1Notification(out, "Not enough mana", 2);
                            gameState.clear(out);
                        }
                    } else {
                        BasicCommands.addPlayer1Notification(out, "Invalid position", 2);
                        gameState.clear(out);
                    }
                } else {
                    // enter logic to highlight movable or attack
                    // if gameState is un-highlighted, and clicked position has a friendly unit, highlight movable
                    if (!gameState.isHighlighted()) {
                        if (gameState.getClickedUnit() != null && gameState.getBoard().isFriendlyUnitPosition(clickedPosition, gameState)) {
                            gameState.getBoard().highlightMovable(out, clickedPosition, gameState);
                        }
                    } else {
                        // if gameState is highlighted, check the tile mode
                        // enter logic for moving or attack logic
                        switch (gameState.getBoard().getTileByPosition(clickedPosition).getMode()) {
                            case 1:
                                GameLogic.move(out, gameState, clickedPosition);
                                break;
                            case 2:
                                GameUnit source = gameState.findUnitByPosition(gameState.getHighlighted());
                                GameUnit target = gameState.findUnitByPosition(clickedPosition);
                                GameLogic.processAttack(out, source, target, gameState);
                                break;
                            default:
                                gameState.clear(out);
                                break;
                        }
                    }
                }
            }
        } else {
            GameLogic.playWraithlingSwarm(out, clickedPosition, gameState);
        }
    }
}