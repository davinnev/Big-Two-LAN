/**
 * This class is a subclass of Hand and is used to model a hand of Triple in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class Triple extends Hand {
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public Triple(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method checks if this hand is a valid Triple hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check if it is a triple by checking the size and equality of the ranks
        if (this.size() == 3 && this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() == this.getCard(2).getRank()) return true;
        else return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (Triple).
     * @return a string consists of the word "Triple"
     */
    public String getType() {
        return "Triple";
    }
}
