package assignment1;

public class AngryBee extends HoneyBee {

    public static int BASE_COST = 4;
    public static int BASE_HEALTH = 3;
    private int attackDamage;

    public AngryBee(Tile position, int attackDamage) {
        super(position, BASE_HEALTH, BASE_COST);
        this.attackDamage = attackDamage;
    }

    @Override
    public boolean takeAction() {
        Tile tile = getPosition();
        
        if (tile == null || !tile.isOnThePath()) return false;

        //bees can't sting hornets on the nest. So tries the first tile if it's not a nest
        if (!tile.isNest() && tile.getNumOfHornets() > 0) {
            Hornet h = tile.getHornet();
            if (h != null) {
                h.takeDamage(attackDamage);
                return true;
            }
        }
        //otherwise it will try the next tile (if its valid and it's not a nest)
        Tile next = tile.towardTheNest();
        if (next != null && !next.isNest() && next.getNumOfHornets() > 0) {
            Hornet h = next.getHornet();
            if (h != null) {
                h.takeDamage(attackDamage);
                return true;
            }
        }

        return false;
    }
}
