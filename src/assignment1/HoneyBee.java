package assignment1;

// Same as before, every insect extends the class Insect. HoneyBee is the blueprint for all other bees
public abstract class HoneyBee extends Insect {

    // % of dmg reduction when a bee is on the hive tile. Not specified so I'm keeping it at 50%
    public static double HIVE_DMG_REDUCTION = 50.0; 
    private int cost; // cost of honeybee

    public HoneyBee(Tile position, int hp, int cost) {
        super(position, hp);
        this.cost = cost;
    }

    public int getCost() { return cost; }

    //Insect.takeDamage doesn't know about hive defense so I need the TakeDamage to override it first 
    @Override
    public void takeDamage(int dmg) {
        if (dmg <= 0) return;
        Tile tile = getPosition();
        int actual = dmg;
        if (tile != null && tile.isHive()) {
            actual = (int)(dmg - (dmg * HIVE_DMG_REDUCTION));
            if (actual < 0) actual = 0;
        }
        super.takeDamage(actual);
    }
}
