package structures.basic;


/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 *
 * @author Dr. Richard McCreadie
 */
public class Card {
    int id;
    String cardname;
    int manacost;
    MiniCard miniCard;
    BigCard bigCard;
    boolean isCreature;
    String unitConfig;

    public Card() {
    }

    /**
     * Constructs a new Card object with the specified attributes.
     *
     * @param id         The unique identifier of the card.
     * @param cardname   The name of the card.
     * @param manacost   The mana cost required to play the card.
     * @param miniCard   The mini card representation of the card.
     * @param bigCard    The big card representation of the card.
     * @param isCreature A boolean indicating whether the card represents a creature.
     * @param unitConfig The configuration data for the unit represented by the card.
     */
    public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard, boolean isCreature, String unitConfig) {
        super();
        this.id = id;
        this.cardname = cardname;
        this.manacost = manacost;
        this.miniCard = miniCard;
        this.bigCard = bigCard;
        this.isCreature = isCreature;
        this.unitConfig = unitConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public int getManacost() {
        return manacost;
    }

    public void setManacost(int manacost) {
        this.manacost = manacost;
    }

    public MiniCard getMiniCard() {
        return miniCard;
    }

    public void setMiniCard(MiniCard miniCard) {
        this.miniCard = miniCard;
    }

    public BigCard getBigCard() {
        return bigCard;
    }

    public void setBigCard(BigCard bigCard) {
        this.bigCard = bigCard;
    }

    public boolean getIsCreature() {
        return isCreature;
    }

    public void setIsCreature(boolean isCreature) {
        this.isCreature = isCreature;
    }

    public void setCreature(boolean isCreature) {
        this.isCreature = isCreature;
    }

    public boolean isCreature() {
        return isCreature;
    }

    public String getUnitConfig() {
        return unitConfig;
    }

    public void setUnitConfig(String unitConfig) {
        this.unitConfig = unitConfig;
    }


}
