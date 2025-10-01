package assignment1;


//the hornet class extends the insect class
public class Hornet extends Insect {
	
	// These are the parameters of a hornet. Fire damage is also initialized here. 
    private int attackDamage;
    private boolean isTheQueen = false;
    private static int numOfQueens = 0;
    public static int BASE_FIRE_DMG = 1;

    public Hornet(Tile position, int hp, int attackDamage) {
        super(position, hp);
        this.attackDamage = attackDamage;
    }

    public boolean isTheQueen() { return isTheQueen; }

    // this promotes the current only existing hornet to queen status
    public void promote() {
        if (!isTheQueen && numOfQueens == 0) {
            isTheQueen = true;
            numOfQueens = 1;
        }
    }

    @Override
    public boolean takeAction() {
        int acts = isTheQueen ? 2 : 1; // the int "acts" is 2 for queen (she can act twice), every other insect acts once
        boolean didSomething = false;

        for (int k = 0; k < acts; k++) {
            Tile pos = getPosition();
            if (pos == null) break; // removed from play

            // very important! Fire damage is at the start of each hornets round
            if (pos.isOnThePath() && pos.isOnFire()) {
                this.takeDamage(BASE_FIRE_DMG);
                if (getPosition() == null) break; // died in fire before action
                pos = getPosition();
            }

            // If already at hive and no bee then do nothing// hornets won
            if (pos.isHive() && pos.getBee() == null) {
                return false;
            }

            // if bee is on same tile then sting it (attack)
            HoneyBee bee = pos.getBee();
            if (bee != null) {
                bee.takeDamage(attackDamage);
                didSomething = true;
            } else {
                // No bee so move towards hive if not null
                Tile next = pos.towardTheHive();
                if (next != null) {
                    pos.removeInsect(this);
                    next.addInsect(this); // this is a fancy way of having this insect removed and then added to the next 
                    didSomething = true;
                } else {
                    //at hive with no bee , nothing to do
                }
            }

            // if this was a queen , which moves twice, and the second act moved away from a fire tile, then it wouldn't continue taking damage!!
        }
        return didSomething;
    }

    @Override
    // it's the same duplicate/bugs checker that i had in Insect
    public boolean equals(Object object) {
        if (!(object instanceof Hornet)) return false;
        Hornet other = (Hornet) object;
        return super.equals(other) && this.attackDamage == other.attackDamage;
    }
}
