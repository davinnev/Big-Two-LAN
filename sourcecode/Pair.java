/**
 * This class is a subclass of Hand and is used to model a hand of Pair in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class Pair extends Hand {
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public Pair(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method checks if this hand is a valid Pair hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check if it is a pair by checking the size and equality of the ranks
        if (this.size() == 2 && this.getCard(0).getRank() == this.getCard(1).getRank()) return true;
        else return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (Pair).
     * @return a string consists of the word "Pair"
     */
    public String getType() {
        return "Pair";
    }
}
