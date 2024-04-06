# MSc IT + Masters Team Project - 2023 - 24

This is the repository for the COMPSCI5074 2023-24 MSc IT + Masters Team Project.

## Team Alpha

| Student ID | Name          |
| ---------- | ------------- |
| 2840879Q   | Qin, Xuesen   |
| 2840781T   | Tsai, Yi Hsiu |
| 2840937W   | Wu, Fangxu    |
| 2940985X   | Xu, Haifan    |
| 2840473B   | Bao, Jingzhen |

### Clarifications

| Commits with             | is from                    |
| ------------------------ | -------------------------- |
| minicoderwen             | Xu, Haifan                 |
| 373441770@qq.com         | 2940985X@student.gla.ac.uk |
| ---                      | ---                        |
| doluludadada             | YiHsiu Tsai                |
| doluludadada@outlook.com | 2840781T@student.gla.ac.uk |

- Sprint reports can also be found at the docs directory, as well as the user stories.
- assets directory is ignored by git, so it is not included in the repository.

## Project Description

This project is a single-player strategy card game that offers players the opportunity to go head-to-head with the AI. Through cleverly designed game mechanics and card systems, players are required to strategically summon units, cast spells, and defeat their opponents through tactical layouts with limited resources. This exercise is based on the real game [Duelyst II](https://store.steampowered.com/app/2004320/Duelyst_II/).

## Player Card

### Creatures

#### Bad Omen

- Cost: 0
- Attack: 0
- Health: 1
- Abilities: Deathwatch (whenever a unit, friendly or enemy dies, trigger the following effect): This unit gains +1 attack permanently.

#### Gloom Chaser

- Cost: 2
- Attack: 3
- Health: 1
- Abilities: Opening Gambit (whenever a unit is summoned onto the battlefield, trigger the following effect): Summon a Wraithling directly behind this unit (to its left for the human player). If the space is occupied, then this ability has no effect.

#### Rock Pulveriser

- Cost: 2
- Attack: 1
- Health: 4
- Abilities: Provoke (Enemy units in adjacent squares cannot move and can only attack this creature or other creatures with Provoke).

#### Shadow Watcher

- Cost: 3
- Attack: 3
- Health: 2
- Abilities: Deathwatch (whenever a unit, friendly or enemy dies, trigger the following effect): This unit gains +1 attack and +1 health permanently (this increases the creature's maximum health).

#### Nightsorrow Assassin

- Cost: 3
- Attack: 4
- Health: 2
- Abilities: Opening Gambit (whenever a unit is summoned onto the battlefield, trigger the following effect): Destroy an enemy unit in an adjacent square that is below its maximum health.

#### Bloodmoon Priestess

- Cost: 4
- Attack: 3
- Health: 3
- Abilities: Deathwatch (whenever a unit, friendly or enemy dies, trigger the following effect): Summon a Wraithling on a randomly selected unoccupied adjacent tile. If there are no unoccupied tiles, then this ability has no effect.

#### Shadowdancer

- Cost: 5
- Attack: 5
- Health: 4
- Abilities: Deathwatch (whenever a unit, friendly or enemy dies, trigger the following effect): Deal 1 damage to the enemy avatar and heal yourself for 1.

### Spells

#### Horn of the Forsaken

- Cost: 1
- Effects:
  - Artifact 3: When cast the artifact is equipped to the player’s avatar with 3 robustness. Whenever the player’s avatar takes damage from any source, decrease this artifact’s robustness by 1 (regardless of the amount of damage taken). When this artifact’s robustness reaches 0, the artifact is destroyed and its effects no longer trigger.
  - On Hit (whenever this unit deals damage to an enemy unit): Summon a Wraithling on a randomly selected unoccupied adjacent tile. If there are no unoccupied tiles, then this ability has no effect.

#### Wraithling Swarm

- Cost: 3
- Effects:
  - Summon 3 Wraithlings in sequence.

#### Dark Terminus

- Cost: 4
- Effects:
  - Destroy an enemy creature.
  - Summon a Wraithling on the tile of the destroyed creature.

### Tokens

#### Wraithling

- Attack: 1
- Health: 1

---

## Player 2 (AI) Card

### Creatures

#### Swamp Entangler

- Cost: 1
- Attack: 0
- Health: 3
- Abilities: Provoke (Enemy units in adjacent squares cannot move and can only attack this creature or other creatures with Provoke).

#### Silverguard Squire

- Cost: 1
- Attack: 1
- Health: 1
- Abilities: Opening Gambit (whenever a unit is summoned onto the battlefield, trigger the following effect): Give any adjacent allied unit that is directly in front (left) or behind (right) the owning player’s avatar +1 attack and +1 health permanently (this increases those creatures maximum health).

#### Skyrock Golem

- Cost: 2
- Attack: 4
- Health: 2

#### Saberspine Tiger

- Cost: 3
- Attack: 3
- Health: 2
- Abilities:
  - Rush (Can move and attack on the turn it is summoned)

#### Silverguard Knight

- Cost: 3
- Attack: 1
- Health: 5
- Abilities:
  - Zeal (When your avatar takes damage, trigger the following effect): This unit gains +2 attack permanently.
  - Provoke (Enemy units in adjacent squares cannot move and can only attack this creature or other creatures with Provoke).

#### Young Flamewing

- Cost: 4
- Attack: 5
- Health: 4
- Abilities: Flying (This unit can move to any unoccupied space on the board).

#### Ironcliffe Guardian

- Cost: 5
- Attack: 3
- Health: 10
- Abilities: Provoke (Enemy units in adjacent squares cannot move and can only attack this creature or other creatures with Provoke).

### Spells

#### Sundrop Elixir

- Cost: 1
- Effects: Heal allied unit for 4 health (this does not increase its maximum health).

#### True Strike

- Cost: 1
- Effects: Deal 2 damage to an enemy unit.

#### Beam Shock

- Cost: 0
- Effects: Stun (the target unit cannot move or attack next turn) target enemy non-avatar unit.
