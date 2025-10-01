package assignment1;

public class Tile {

    // Fields
    private SwarmOfHornets swarm;        // #Of hornets
    private HoneyBee bee;                // At most 1 bee per tile
    private int food;                    // Amount of food on tile
    private boolean onThePath;           // If tile belongs to path
    private Tile towardTheHive;          // Next tile from this -> Hive
    private Tile towardTheNest;          // Next tile from this -> Nest
    private boolean isNest;              // If tile is a nest
    private boolean isHive;              // If tile is a hive
    private boolean onFire;              // Is tile on fire

    /* These define the state of the board. 
     * towardTheHive and towardTheNest allow insects to move 
     * without the map recalculating the path each time.
     */

    // ---------------- Constructors ----------------

    // Default constructor
    public Tile() {
        this(null, null, 0, false, null, null, false, false, false);
    }

    // Constructor expected by MiniTest
    public Tile(int food, boolean isHive, boolean isNest, boolean onThePath,
                Tile towardTheHive, Tile towardTheNest,
                HoneyBee bee, SwarmOfHornets swarm) {
        this.swarm = (swarm == null) ? new SwarmOfHornets() : swarm;
        this.bee = bee;
        this.food = food;
        this.onThePath = onThePath;
        this.towardTheHive = towardTheHive;
        this.towardTheNest = towardTheNest;
        this.isHive = isHive;
        this.isNest = isNest;
        this.onFire = false; // always false at start
    }

    // Full constructor
    public Tile(SwarmOfHornets swarm, HoneyBee bee, int food,
                boolean onThePath, Tile towardTheHive, Tile towardTheNest,
                boolean isHive, boolean isNest, boolean onFire) {
        this.swarm = (swarm == null) ? new SwarmOfHornets() : swarm;
        this.bee = bee;
        this.food = food;
        this.onThePath = onThePath;
        this.towardTheHive = towardTheHive;
        this.towardTheNest = towardTheNest;
        this.isHive = isHive;
        this.isNest = isNest;
        this.onFire = onFire;
    }

    // Hive & Nest 

    public boolean isHive() { return isHive; }
    public boolean isNest() { return isNest; }

    public void buildHive() { this.isHive = true; }
    public void buildNest() { this.isNest = true; }

    //  Path Mechanism 
    public boolean isOnThePath() { return onThePath; }

    public Tile towardTheHive() {
        if (!onThePath || isHive) return null;
        return towardTheHive;
    }

    public Tile towardTheNest() {
        if (!onThePath || isNest) return null;
        return towardTheNest;
    }

    public void createPath(Tile nextTowardHive, Tile nextTowardNest) {
        // if there's no endpoints then throw error
        if (nextTowardHive == null && !this.isHive) {
            throw new IllegalArgumentException("tile is invalid , towardTheHive is null but it's not the Hive");
        }
        if (nextTowardNest == null && !this.isNest) {
            throw new IllegalArgumentException("tile is invalid , towardTheHive is null but it's not the Hive");
        }
        this.onThePath = true;
        this.towardTheHive = nextTowardHive;
        this.towardTheNest = nextTowardNest;
    }

    // - Food mechanism 

    public int collectFood() {
        int out = this.food;
        this.food = 0;
        return out;
    }

    public void storeFood(int amount) {
        if (amount > 0) this.food += amount;
    }

    //  Bee & Hornet

    public int getNumOfHornets() {
        return (swarm == null) ? 0 : swarm.sizeOfSwarm();
    }

    public HoneyBee getBee() { return bee; }

    public Hornet getHornet() {
        return (swarm == null) ? null : swarm.getFirstHornet();
    }

    public Hornet[] getHornets() {
        return (swarm == null) ? new Hornet[0] : swarm.getHornets();
    }

    /**
     * Adds an insect to this tile if allowed.
     * Bees: max 1 bee per tile and bee cannot be placed on nest
     * Hornets: can only be placed on the path of hive -> nest
     */
    public boolean addInsect(Insect i) {
        if (i == null) return false;

        if (i instanceof HoneyBee) {
            if (this.bee != null || this.isNest) return false;
            this.bee = (HoneyBee) i;
            i.setPosition(this);
            return true;
        } else { // Hornet
            if (!this.onThePath) return false;
            if (this.swarm == null) this.swarm = new SwarmOfHornets();
            this.swarm.addHornet((Hornet) i);
            i.setPosition(this);
            return true;
        }
    }

    // Removes the insect and moves its position
    public boolean removeInsect(Insect i) {
        if (i == null) return false;

        if (i instanceof HoneyBee) {
            if (this.bee == i) {
                this.bee = null;
                i.setPosition(null);
                return true;
            }
            return false;
        } else {
            if (this.swarm == null) return false;
            boolean removed = this.swarm.removeHornet((Hornet) i);
            if (removed) i.setPosition(null);
            return removed;
        }
    }

    // ---------------- Fire Mechanism ----------------

    public void setOnFire() { this.onFire = true; }
    public boolean isOnFire() { return this.onFire; }
}
