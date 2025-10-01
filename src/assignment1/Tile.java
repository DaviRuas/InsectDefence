package assignment1;

public class Tile {

    //Fields
    private int food;                    // amount of food on tile
    private boolean isHive;              // If tile is a hive
    private boolean isNest;              // If tile is a nest
    private boolean onThePath;           // If tile belongs to path
    private Tile towardTheHive;          // Next tile from tile -> Hive
    private Tile towardTheNest;          // Next tile from tile -> Nest
    private HoneyBee bee;                //	To keep at most 1 bee per tile
    private SwarmOfHornets swarm;        // #Of hornets
    private boolean onFire;              // Is tile on fire

    /*These define the state of the board. towardTheHive and towardTheNest allows 
     * the insects to move without map needing to recalculate the path
     *
     */
    
    // Constructors
    public Tile() {
        this.food = 0;
        this.isHive = false;
        this.isNest = false;
        this.onThePath = false;
        this.towardTheHive = null;
        this.towardTheNest = null;
        this.bee = null;
        this.swarm = new SwarmOfHornets();
        this.onFire = false; //always set to false at start
    }

    // Full constructor 
    public Tile(int food, boolean isHive, boolean isNest, boolean onThePath,
                Tile towardTheHive, Tile towardTheNest, HoneyBee bee, SwarmOfHornets swarm) {
        this.food = food;
        this.isHive = isHive;
        this.isNest = isNest;
        this.onThePath = onThePath;
        this.towardTheHive = towardTheHive;
        this.towardTheNest = towardTheNest;
        this.bee = bee;
        this.swarm = (swarm == null) ? new SwarmOfHornets() : swarm; // This means that even if you pass null, there will always be a swarm present
        this.onFire = false;
    }

    // Hive and Nests
    public boolean isHive() { return isHive; }
    public boolean isNest() { return isNest; }

    public void buildHive() { this.isHive = true; }
    public void buildNest() { this.isNest = true; }

    // Path and connections
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
        // Validate nulls if not endpoint
        if (nextTowardHive == null && !this.isHive) {
            throw new IllegalArgumentException("towardTheHive null but tile is not the Hive");
        }
        if (nextTowardNest == null && !this.isNest) {
            throw new IllegalArgumentException("towardTheNest null but tile is not the Nest");
        }
        this.onThePath = true;
        this.towardTheHive = nextTowardHive;
        this.towardTheNest = nextTowardNest;
    }

    // Food
    public int collectFood() {
        int out = this.food;
        this.food = 0;
        return out;
    }

    public void storeFood(int amount) {
        if (amount > 0) this.food += amount;
    }

    // Bees and Hornets
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
     * Adds an insect to tile if action can be made
     * Bees cannot be placed on nest nor can there be more than 1 bee per tile.
     * Hornets can only be on the path of hive or nest
     */
    public boolean addInsect(Insect i) {
        if (i == null) return false;

        if (i instanceof HoneyBee) {
            // bee is already on tile or tile is the nest , aka.: reject action
            if (this.bee != null || this.isNest) return false;
            this.bee = (HoneyBee) i;
            i.setPosition(this);
            return true;
        } else { // Hornet
            if (!this.onThePath) return false; // hornets only on path
            if (this.swarm == null) this.swarm = new SwarmOfHornets();
            this.swarm.addHornet((Hornet) i);
            i.setPosition(this);
            return true;
        }
    }

    /**
     * Removes the insect object; sets its tile to null
     */
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

    // Fire
    public void setOnFire() { this.onFire = true; }
    public boolean isOnFire() { return this.onFire; }
}
