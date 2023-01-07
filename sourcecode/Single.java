/**
 * This class is a subclass of Hand and is used to model a hand of Single in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class Single extends Hand {
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public Single(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method checks if this hand is a valid Single hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
        if (this.size() == 1) return true;
        else return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (Single).
     * @return a string consists of the word "Single"
     */
    public String getType() {
        return "Single";
    }
}
