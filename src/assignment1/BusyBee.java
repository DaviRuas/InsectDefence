package assignment1;

public class BusyBee extends HoneyBee {

	public static int BASE_AMOUNT_COLLECTED = 1;
	public static int BASE_COST = 2;
    public static int BASE_HEALTH = 3;

    public BusyBee(Tile pos) {
        super(pos, BASE_HEALTH, BASE_COST);
    }

    @Override
    public boolean takeAction() {
        Tile tile = getPosition();
        if (tile==null) return false;
        tile.storeFood(BASE_AMOUNT_COLLECTED);
        return true;
    }
}
