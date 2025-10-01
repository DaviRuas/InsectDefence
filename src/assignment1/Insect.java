package assignment1;


//abstract class because it's the blueprint for every other insect (hornets and honeybees)
public abstract class Insect {

    private Tile position;   
    private int health;

    //constructors
    public Insect(Tile position, int hp) {
        this.health = hp;
        this.position = null; // this position can only be set here to keep it consistent
        if (position != null) {
            boolean valid = position.addInsect(this);
            if (!valid) throw new IllegalArgumentException("Insect cannot be placed here// invalid tile.");
        }
    }

    public final int getHealth() { return health; }
    public final Tile getPosition() { return position; }
    public void setPosition(Tile t) { this.position = t; } //this is used to remove remove or add insects
    
    
    //This is %health regen (current heath) floored to 0
    public void regenerateHealth(double percent) {
        if (percent == 0.0) return;
        int delta = (int) Math.floor((this.health * percent) / 100.0);
        if (delta == 0 && percent > 0) delta = 0; // floor to 0
        this.health += delta;
    }

    //if health is <=0 , then removes insect from the game
    public void takeDamage(int dmg) {
        if (dmg <= 0) return;
        this.health -= dmg;
        if (this.health <= 0) {
            // remove from tile 
            if (this.position != null) {
                Tile t = this.position;
                t.removeInsect(this);
            } else {
                this.position = null;
            }
        }
    }
    
    public abstract boolean takeAction();
    
    //This is to avoid bugs. Essentially, avoids duplicates ( 2 insects with the same health and position are the same entity)
    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        Insect other = (Insect) o;
        return this.health == other.health && this.position == other.position;
    }
}
