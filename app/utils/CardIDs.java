package utils;

/**
 * Represents the IDs of various cards in the game.
 */
public enum CardIDs {
    BAD_OMENS(1),
    HORN_OF_THE_FORSAKEN(2),
    GLOOM_CHASER(3),
    SHADOW_WATCHER(4),
    WRAITHLING_SWARM(5),
    NIGHTSORROW_ASSASSIN(6),
    ROCK_PULVERISER(7),
    DARK_TERMINUS(8),
    BLOODMOON_PRIESTESS(9),
    SHADOWDANCER(10),
    SKYROCK_GOLEM(11),
    SWAMP_ENTANGLER(12),
    SILVERGUARD_KNIGHT(13),
    SABERSPINE_TIGER(14),
    BEAM_SHOCK(15),
    YOUNG_FLAMEWING(16),
    SILVERGUARD_SQUIRE(17),
    IRONCLIFFE_GUARDIAN(18),
    SUNDROP_ELIXIR(19),
    TRUE_STRIKE(20),
    ;
    private final int id;
    CardIDs(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
