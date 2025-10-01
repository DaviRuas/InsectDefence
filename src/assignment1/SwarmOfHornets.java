package assignment1;

public class SwarmOfHornets {

    private Hornet[] hornets;
    private int numOfHornets;
    public static double QUEEN_BOOST = 5.0; // this is just a placeholder value for the queen's regen

    public int sizeOfSwarm() { return numOfHornets; }

    public SwarmOfHornets() {
    	this.numOfHornets = 0;
        this.hornets = new Hornet[2]; // placeholder value for the capacity of the swarm , start off small
    }


    public Hornet[] getHornets() {
        
    	//Returns ordered array compact, maintains the same order
        Hornet[] out = new Hornet[numOfHornets];
        for (int i = 0; i < numOfHornets; i++) out[i] = hornets[i];
        return out;
    }

    public Hornet getFirstHornet() {
        return (numOfHornets == 0) ? null : hornets[0];
    }

    public void addHornet(Hornet hornet) {
        if (hornet == null) return;

        // if hornet added is the queen , all other hornets get a regen boost (but not the queen)
        if (hornet.isTheQueen()) {
            for (int i = 0; i < numOfHornets; i++) {
                hornets[i].regenerateHealth(QUEEN_BOOST);
            }
        }

        BigEnough(numOfHornets + 1);
        hornets[numOfHornets++] = hornet;
    }

    //removes hornet while maintaining same order in the array
    public boolean removeHornet(Hornet hornet) {
        if (hornet == null || numOfHornets == 0) return false;
        for (int i = 0; i < numOfHornets; i++) {
            if (hornets[i] == hornet) { 
                // shift left
                for (int j = i + 1; j < numOfHornets; j++) {
                    hornets[j - 1] = hornets[j];
                }
                hornets[--numOfHornets] = null;
                return true;
            }
        }
        return false;
    }

    //This is to ensure adding a hornet does not produce an OutOfBounds error. Normally
    // we could use Hornet<> to resize the array, but as per the assignment specs, this function makes sure that it adds safelty and maintains the original order
    private void BigEnough(int cap) {
        if (hornets.length >= cap) return;
        int newCap = Math.max(hornets.length * 2, cap); //just to be safe
        Hornet[] n = new Hornet[newCap];
        for (int i = 0; i < numOfHornets; i++) n[i] = hornets[i];
        hornets = n;
    }
}
