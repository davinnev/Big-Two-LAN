/**
 * This class is a subclass of Hand and is used to model a hand of FullHouse in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class FullHouse extends Hand {
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public FullHouse(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method retrieves the top card of the set of cards.
     * @return the top card object
     */
    public Card getTopCard(){
    	// the top card must be the last card in the triplet in a full house hand (highest suit) as the cards have been sorted
    	// check whether if the triplet goes first to find the top card
        if (this.getCard(2).getRank() == this.getCard(0).getRank()) {
        	return this.getCard(2);
        }
        return this.getCard(4);
    }    
    
    /**
     * This method checks if this hand beats a specified hand.
     * @param hand a Hand object to be compared with this hand
     * @return true if this hand beats the other hand, false otherwise
     */
    public boolean beats(Hand hand){
    	// any FullHouse hand will always beat a Straight hand and a Flush hand
        if (hand.getType() == "Straight" || hand.getType() == "Flush") return true; 
        // check if the compared hand is also a FullHouse and compare the top cards
        else if (hand.getType() == "FullHouse" && 
        this.getTopCard().compareTo(hand.getTopCard()) == 1)
            return true;
        return false;
    }
    
    /**
     * This method checks if this hand is a valid FullHouse hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check the size first
        if (this.size() == 5) {
        	// check the equality of the rank of first two cards and last two cards
        	// check the third card's rank (match it with the first or last card)
            if 
            ((this.getCard(0).getRank() == this.getCard(1).getRank()) && 
            (this.getCard(3).getRank() == this.getCard(4).getRank()) && 
            (this.getCard(2).getRank() == this.getCard(0).getRank() || this.getCard(2).getRank() == this.getCard(4).getRank()))
            return true;
        }
        return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (FullHouse).
     * @return a string consists of the word "FullHouse"
     */
    public String getType() {
        return "FullHouse";
    }
}
