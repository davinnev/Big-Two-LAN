/**
 * This class is a subclass of Hand and is used to model a hand of Straight in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class Straight extends Hand {
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public Straight(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method checks if this hand is a valid Straight hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check if the size is correct
        if (this.size() == 5) {
        	// mark the smallest rank
            int minRank = this.getCard(0).getRank();
            
            // the if statement below handle the case to avoid Two and A forming a Straight with a Three
            if (minRank > 1){
            	
            	// add 1 to the minRank variable every iteration and match it with the rank of the i-th card
                for (int i = 1; i < 5; i++){
                    if ((++minRank)%13 != (this.getCard(i).getRank()))
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (Straight).
     * @return a string consists of the word "Straight"
     */
    public String getType() {
        return "Straight";
    }
}
