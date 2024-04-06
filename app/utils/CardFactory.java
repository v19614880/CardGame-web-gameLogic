package utils;

import structures.card.*;


public class CardFactory {
    public static GameUnit createCard(CardIDs id) {
        switch (id) {
            case BAD_OMENS:
                return new BadOmen();
            case HORN_OF_THE_FORSAKEN:
                return new HornOfTheForsaken();
            case GLOOM_CHASER:
                return new GloomChaser();
            case SHADOW_WATCHER:
                return new ShadowWatcher();
            case WRAITHLING_SWARM:
                return new WraithlingSwarm();
            case NIGHTSORROW_ASSASSIN:
                return new NightsorrowAssassin();
            case ROCK_PULVERISER:
                return new RockPulveriser();
            case DARK_TERMINUS:
                return new DarkTerminus();
            case BLOODMOON_PRIESTESS:
                return new BloodmoonPriestess();
            case SHADOWDANCER:
                return new ShadowDancer();
            case SKYROCK_GOLEM:
                return new SkyrockGolem();
            case SWAMP_ENTANGLER:
                return new SwampEntangler();
            case SILVERGUARD_KNIGHT:
                return new SilverguardKnight();
            case SABERSPINE_TIGER:
                return new SaberspineTiger();
            case BEAM_SHOCK:
                return new BeamShock();
            case YOUNG_FLAMEWING:
                return new YoungFlamewing();
            case SILVERGUARD_SQUIRE:
                return new SilverguardSquire();
            case IRONCLIFFE_GUARDIAN:
                return new IroncliffeGuardian();
            case SUNDROP_ELIXIR:
                return new SundropElixir();
            case TRUE_STRIKE:
                return new TrueStrike();
        }
        return null;
    }
}