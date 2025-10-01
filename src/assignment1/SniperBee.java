package assignment1;

public class SniperBee extends HoneyBee {

	public static int BASE_COST = 6;
	public static int BASE_HEALTH = 2;
    private int attackDamage;
    private int piercingPower;
    private boolean aiming = true; // so this bee alternates between 2 states: shooting and aiming



    public SniperBee(Tile position, int attackDamage, int piercingPower) {
        super(position, BASE_HEALTH, BASE_COST);
        this.attackDamage = attackDamage;
        this.piercingPower = piercingPower;
    }

    @Override
    public boolean takeAction() {
        Tile tile = getPosition();
        if (tile == null || !tile.isOnThePath()) return false;
        
        if (!aiming) {
        	aiming = true; //if shooting then turn gets used up and the aiming goes back to true (currently aiming)
        	
        	// finds the first non-empty swarm on the path of the nest (but not at the nest already)
            Tile current = tile.towardTheNest();
            while (current != null && !current.isNest()) {
                if (current.getNumOfHornets() > 0) {
                    // Damage the first n hornets in the swarm (pierce damage)
                    Hornet[] list = current.getHornets(); 
                    int n = Math.min(piercingPower, list.length);
                    for (int i = 0; i < n; i++) {
                        if (list[i] != null) list[i].takeDamage(attackDamage);
                    }
                    return true; // shot fired
                }
                current = current.towardTheNest();
            }
            // No target this time
            return false;
        	
        } else {
            aiming = false; //turn skipped for aiming
            return false;
        }
    }
}
