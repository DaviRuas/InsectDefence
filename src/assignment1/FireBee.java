package assignment1;

public class FireBee extends HoneyBee {

	public static int BASE_HEALTH = 3;
	public static int BASE_COST = 5;
    private int attackRange; 

    public FireBee(Tile position, int attackRange) {
        super(position, BASE_HEALTH, BASE_COST);
        this.attackRange = attackRange;
    }

    @Override
    public boolean takeAction() {
        Tile tile = getPosition();
        if (tile == null) return false;
        if (!tile.isOnThePath()) return false;

        //gets tiles up to attackRange value specified
        Tile current = tile.towardTheNest();
        int steps = 1;
        while (current != null && steps <= attackRange) {
            // can't hit nest tiles so skips
            if (current.isNest()) break;

            // can only target hornets that aren't already on fire (the tile)
            if (current.getNumOfHornets() > 0 && !current.isOnFire()) {
                current.setOnFire();
                return true;
            }

            current = current.towardTheNest();
            steps++;
        }
        return false;
    }
}
