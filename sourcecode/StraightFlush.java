/**
 * This class is a subclass of Hand and is used to model a hand of StraightFlush in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class StraightFlush extends Hand{
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public StraightFlush(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
      
    /**
     * This method checks if this hand beats a specified hand.
     * @param hand a Hand object to be compared with this hand
     * @return true if this hand beats the other hand, false otherwise
     */
    public boolean beats(Hand hand){
    	// any StraightFlush hand will always beat a Straight hand, a Flush hand, a FullHouse hand, and a Quad hand
        if (hand.getType() == "Straight" ||
        hand.getType() == "Flush" || 
        hand.getType() == "FullHouse" ||
        hand.getType() == "Quad") return true;
        // check if the compared hand is also a StraightFlush and compare the top cards
        else if (hand.getType() == "StraightFlush" && this.getTopCard().compareTo(hand.getTopCard()) == 1)
            return true;
        return false;
    }
    
    /**
     * This method checks if this hand is a valid StraightFlush hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check the size
        if (this.size() == 5) {
        	// mark the starting rank and suit
            int minRank = this.getCard(0).getRank();
            int suitBase = this.getCard(0).getSuit();
            
            // the if statement below handle the case to avoid Two and A forming a Straight with a Three
            if (minRank > 1){
            	
            	// add 1 to the minRank variable every iteration and match it with the rank of the i-th card
            	// and compare the i-th card's suit with the starting suit (suitBase)
                for (int i = 1; i < 5; i++){
                    if ((++minRank)%13 != (this.getCard(i).getRank()) || (this.getCard(i).getSuit() != suitBase))
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (StraightFlush).
     * @return a string consists of the word "StraightFlush"
     */
    public String getType() {
        return "StraightFlush";
    }
}
