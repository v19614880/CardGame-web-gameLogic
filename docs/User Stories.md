| #1 Statistics Interface                                                    |
| -------------------------------------------------------------------------- |
| Priority: Must have                                                        |
| Estimate: 4 SP                                                             |
| User Story:                                                                |
| As a human player                                                          |
| I want to see statistics for both sides of the battle and the game board   |
| so that player can better carry out next-step planning and strategies      |
| Acceptance Criteria:                                                       |
| Given the game begins                                                      |
| When player enters the game                                                |
| Then UI displays the statistics of both players, player deck, player hands |

| #2 Obtain Cards                                               |
| ------------------------------------------------------------- |
| Priority: Must have                                           |
| Estimate: 2 SP                                                |
| User Story:                                                   |
| As a human player                                             |
| I want to get three starting cards in my deck                 |
| so that I can use cards on my turn                            |
| Acceptance Criteria:                                          |
| Given the game begins                                         |
| When my character appears on the board                        |
| Then the game system randomly draws three cards to the player |

| #3 Display Card Information                                                         |
| ----------------------------------------------------------------------------------- |
| Priority: Must have                                                                 |
| Estimate: 2 SP                                                                      |
| User Story:                                                                         |
| As a human player                                                                   |
| I want to see the Name, Description, Resource cost, Attack, and Health of the cards |
| so that I can better allocate my resources and play the right deck.                 |
| Acceptance Criteria:                                                                |
| Given the game begins                                                               |
| When looking at my hand cards                                                       |
| Then the system displays the corresponding card information to the player           |

| #4 Deploy Avatar                                                                   |
| ---------------------------------------------------------------------------------- |
| Priority: Must have                                                                |
| Estimate: 2 SP                                                                     |
| User Story:                                                                        |
| As a human player                                                                  |
| I want to see my character's avatar placed in the [2,3] coordinates of the board   |
| so that I can place my cards correctly                                             |
| Acceptance Criteria:                                                               |
| Given the game begins                                                              |
| When the character is still alive                                                  |
| Then the system places the player character at the corresponding board coordinates |

| #5 Add Attributes to Avatar                                                                           |
| ----------------------------------------------------------------------------------------------------- |
| Priority: Must have                                                                                   |
| Estimate: 2 SP                                                                                        |
| User Story:                                                                                           |
| As a human player                                                                                     |
| I want to see my character's portrait placed on the board, with an initial attack of 2 and 20 health, |
| so that the player can Intuitively see their attributes                                               |
| Acceptance Criteria:                                                                                  |
| Given the game begins                                                                                 |
| When the character is still alive                                                                     |
| Then the system intuitively and correctly marks the data of the player character                      |

| #6 Display Mana                                                             |
| --------------------------------------------------------------------------- |
| Priority: Must have                                                         |
| Estimate: 2 SP                                                              |
| User Story:                                                                 |
| As a human player                                                           |
| I want to see a pool of resources called mana in the player Statistics area |
| so that I can plan the use of resources and mana                            |
| Acceptance Criteria:                                                        |
| Given the game begins                                                       |
| When the player clicks on the start game button                             |
| Then the UI will show the initial mana at the statistic area.               |

| #7 Refresh Mana                                                                               |
| --------------------------------------------------------------------------------------------- |
| Priority: Must have                                                                           |
| Estimate: 2 SP                                                                                |
| User Story:                                                                                   |
| As a human player                                                                             |
| I want to see my mana refreshed and equal to the number of turns +1 at the start of each turn |
| so that players can plan the use of resources and mana more intuitively and rationally.       |
| Acceptance Criteria:                                                                          |
| Given the turn begins                                                                         |
| When one player’s turn begins                                                                 |
| Then the value of Mana is set to the number of turns plus one.                                |

| #8 Deploy Enemy avatar                                                                  |
| --------------------------------------------------------------------------------------- |
| Priority: Must have                                                                     |
| Estimate: 2 SP                                                                          |
| User Story:                                                                             |
| As a player                                                                             |
| I want to see the enemy player’s avatar placed in a mirrored position on the board      |
| Acceptance Criteria:                                                                    |
| Given the game begins                                                                   |
| When the player clicks on the start game button                                         |
| Then UI displays the position of the enemy player in the mirrored position of the board |

| #9 Move Units                                                                |
| ---------------------------------------------------------------------------- |
| Priority: Must have                                                          |
| Estimate: 4 SP                                                               |
| User Story:                                                                  |
| As a player                                                                  |
| I want to move my unit on the board during my turn                           |
| so that I can attack or defend                                               |
| Acceptance Criteria:                                                         |
| Given the game begins                                                        |
| When each player's turn begins                                               |
| Then players can move his units two tiles cardinally, or one tile diagonally |
| And the units cannot leave the board                                         |

| #10 Highlight Movable Range                                   |
| ------------------------------------------------------------- |
| Priority: Must have                                           |
| Estimate: 2 SP                                                |
| User Story:                                                   |
| As a player                                                   |
| I want to see the movable range of my units when I click them |
| so that I can plan the actions of the unit                    |
| Acceptance Criteria:                                          |
| Given the game begins                                         |
| When players click on their units                             |
| Then highlight the movable range                              |

| #11 Move within Range                                         |
| ------------------------------------------------------------- |
| Priority: Must have                                           |
| Estimate: 2 SP                                                |
| User Story:                                                   |
| As a player                                                   |
| I want to move the unit to the selected tile within the range |
| Acceptance Criteria:                                          |
| Given the player’s turn begins                                |
| And players click a unit                                      |
| When Players click on the movable area of the unit            |
| Then The unit moves to the clicked position                   |

| #12 Attack Enemies                                                |
| ----------------------------------------------------------------- |
| Priority: Must have                                               |
| Estimate: 4 SP                                                    |
| User Story:                                                       |
| As a player                                                       |
| I want to attack the opponent's units                             |
| so that I can defeat my opponent                                  |
| Acceptance Criteria:                                              |
| Given the player’s turn begins                                    |
| When before or after player move a unit                           |
| Then unit can attack enemy unit in an adjacent tile or diagonally |
| And the unit cannot be moved during the remainder of the turn     |

| #13 Attack after Moving                                                  |
| ------------------------------------------------------------------------ |
| Priority: Must have                                                      |
| Estimate: 2 SP                                                           |
| User Story:                                                              |
| As a player                                                              |
| I want to attack the enemy unit if I click on it when the unit has moved |
| Acceptance Criteria:                                                     |
| Given the player’s turn begins                                           |
| And the player already moved the unit                                    |
| And the enemy unit is in range                                           |
| When the player clicks on the enemy unit                                 |
| Then the player unit attacks                                             |

| #14 Attack before Moving                                                    |
| --------------------------------------------------------------------------- |
| Priority: Must have                                                         |
| Estimate: 2 SP                                                              |
| User Story:                                                                 |
| As a player                                                                 |
| I want to attack the enemy unit if I click on it when the unit is not moved |
| Acceptance Criteria:                                                        |
| Given the player’s turn begins                                              |
| And the player has not moved the unit yet                                   |
| And the enemy unit is in range                                              |
| When the player clicks on the enemy unit                                    |
| Then the player unit moves and attacks                                      |

| #15 Damage system                                                                                                                                                              |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Priority: Must have                                                                                                                                                            |
| Estimate: 5 SP                                                                                                                                                                 |
| User Story:                                                                                                                                                                    |
| As a player, I want the game to have a damage system. When a unit attacks, it should reduce the enemy target's health by an amount equal to the attacking unit's attack value. |
| Acceptance Criteria:                                                                                                                                                           |
| Given when a player's unit attacks an enemy target in the game,                                                                                                                |
| When the attack impacts the enemy target,                                                                                                                                      |
| Then the enemy’s health is reduced according to the attack value of my unit                                                                                                    |

| #16 Counterattack                                                                                                                                                                                             |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Priority: Must have                                                                                                                                                                                           |
| Estimate: 5 SP                                                                                                                                                                                                |
| User Story:                                                                                                                                                                                                   |
| As a player, I want the game to have a counter-attack system. If the defending unit survives, it should be able to counter-attack, reducing the attacking unit's health by the defending unit's attack value. |
| Acceptance Criteria:                                                                                                                                                                                          |
| Given when a player's unit attacks an enemy target in the game,                                                                                                                                               |
| When the attack impacts the enemy target,                                                                                                                                                                     |
| Then the enemy’s health is reduced according to the attack value of my unit                                                                                                                                   |
| And the enemy counter attacks                                                                                                                                                                                 |
| And my unit’s health is reduced according to the attack value of the enemy unit                                                                                                                               |

| #17 Play Card on Tile                                                                 |
| ------------------------------------------------------------------------------------- |
| Priority: Must have                                                                   |
| Estimate: 5 SP                                                                        |
| User Story:                                                                           |
| As a player, when I play the game, I want to able to play my card on the target tile. |
| Acceptance Criteria:                                                                  |
| Given that it is a player's turn in the game,                                         |
| When the player chooses to use a card,                                                |
| Then the card is highlighted                                                          |
| And the available tile is highlighted as well                                         |

| #18 Spell Card System                                                                                                                                                                                                                            |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Priority: Must have                                                                                                                                                                                                                              |
| Estimate: 5 SP                                                                                                                                                                                                                                   |
| User Story:                                                                                                                                                                                                                                      |
| As a player, when a player uses an effect card, after selecting the target, the player's Mana should be deducted, and the effect should be applied to the chosen target. Additionally, the effect card should be removed from the player's hand. |
| Acceptance Criteria:                                                                                                                                                                                                                             |
| Given the player’s turn begins                                                                                                                                                                                                                   |
| When the player targets and uses the card                                                                                                                                                                                                        |
| Then the system should deduct the mana value                                                                                                                                                                                                     |
| And the card is spawned on the selected tile                                                                                                                                                                                                     |
| And the card is removed from the player hand                                                                                                                                                                                                     |

| #19 Play Spell Card                                              |
| ---------------------------------------------------------------- |
| Priority: Must have                                              |
| Estimate: 5 SP                                                   |
| User Story:                                                      |
| As a player, I want to be able to play spell cards               |
| Acceptance Criteria:                                             |
| Given the player’s turn begins                                   |
| When the spell card is in the player’s hand                      |
| Then the card is ready to be played                              |
| And there is no highlighted tile that suggests it can be spawned |

| #20 Highlight Spell Card                                                                             |
| ---------------------------------------------------------------------------------------------------- |
| Priority: Must have                                                                                  |
| Estimate: 5 SP                                                                                       |
| User Story:                                                                                          |
| As a user, when I select a spell card, it should highlight which units can be affected by this card. |
| Acceptance Criteria:                                                                                 |
| Given the player’s turn begins                                                                       |
| When the player selects the card                                                                     |
| Then the game should automatically highlight the units that can be affected by it                    |

| #21 Cast Spell to Target                                                                                                                     |
| -------------------------------------------------------------------------------------------------------------------------------------------- |
| Priority: Must have                                                                                                                          |
| Estimate: 2 SP                                                                                                                               |
| User Story:                                                                                                                                  |
| As a player, when I select a target using a spell, I want to cast the spell on that target, so that the effect of the spell can take effect. |
| Acceptance Criteria:                                                                                                                         |
| Given the player has chosen a spell to be cast                                                                                               |
| When the player clicks on the target                                                                                                         |
| Then the effect of that spell is applied to the target                                                                                       |
| And the mana has been decremented according to the spell                                                                                     |
| And the spell has been removed from the player’s hand                                                                                        |

| #22 End Turn                                                                     |
| -------------------------------------------------------------------------------- |
| Priority: Must have                                                              |
| Estimate: 1 SP                                                                   |
| User Story:                                                                      |
| As a player, I want to be able to end a turn when I click on the End Turn button |
| Acceptance Criteria:                                                             |
| Given the player finishes playing cards, moving, and attacking                   |
| When the player clicks on the End Turn button                                    |
| Then the turn is ended                                                           |
| And the enemy’s turn starts                                                      |

| #23 Draw a Card                                                            |
| -------------------------------------------------------------------------- |
| Priority: Must have                                                        |
| Estimate: 1 SP                                                             |
| User Story:                                                                |
| As a player, I want to draw a card at the end of each turn                 |
| Acceptance Criteria:                                                       |
| Given the player ends a turn by clicking on the button                     |
| When the turn ends                                                         |
| Then a new card is drawn from the card deck and added to the player’s hand |

| #24 Clear out Unused Mana                                     |
| ------------------------------------------------------------- |
| Priority: Must have                                           |
| Estimate: 2 SP                                                |
| User Story:                                                   |
| As a player, I want to clear out unused mana when a turn ends |
| Acceptance Criteria:                                          |
| Given the player clicks on the button to end the turn         |
| When the turn ends                                            |
| Then the mana is set to 0                                     |

| #25 Victory Condition                                                    |
| ------------------------------------------------------------------------ |
| Priority: Must have                                                      |
| Estimate: 2 SP                                                           |
| User Story:                                                              |
| As a player, I win the game when the enemy avatar is reduced to 0 health |
| Acceptance Criteria:                                                     |
| Given one attacks the opponent during a turn                             |
| When the enemy’s avatar is reduced to 0                                  |
| Then the game ends announcing the winner                                 |

| #26 AI                                                                 |
| ---------------------------------------------------------------------- |
| Priority: Must have                                                    |
| Estimate: 24 SP                                                        |
| User Story:                                                            |
| As a player,                                                           |
| I want to play against AI in a game                                    |
| So that the game can be more interesting                               |
| Acceptance Criteria:                                                   |
| Given the game begins                                                  |
| When the human player ends the turn                                    |
| Then the AI can automatically command units, cast spells and end turns |
