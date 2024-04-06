package structures.card;

import structures.Constants;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;
import structures.contracts.Ability;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * Wraithling is a basic creature with 1 attack and 1 health.
 */
public class Wraithling extends GameUnit {

    public Wraithling() {
        super(1, 1, BasicObjectBuilders.loadUnit(StaticConfFiles.WRAITHLING, Constants.WRAITHLING_ID, Unit.class), true);
        super.getUnit().setId(super.getUniqueUnitId());
    }
}
