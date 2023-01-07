/**
 * This class is a subclass of Hand and is used to model a hand of Quad in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class Quad extends Hand {
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public Quad(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method retrieves the top card of the set of cards.
     * @return the top card object
     */
    public Card getTopCard(){
    	// check if the cards with the same rank is in index 0-3 or 1-4
    	// the top card must be in index 3 or 4 (highest suit) as the cards have been sorted
        if (this.getCard(1).getRank() == this.getCard(0).getRank()) 
            return this.getCard(3);
        return this.getCard(4);
    }
    
    /**
     * This method checks if this hand beats a specified hand.
     * @param hand a Hand object to be compared with this hand
     * @return true if this hand beats the other hand, false otherwise
     */
    public boolean beats(Hand hand){
    	// any Quad hand will always beat a Straight hand, a Flush hand, and a FullHouse hand
        if (hand.getType() == "Straight" ||
        hand.getType() == "Flush" ||
        hand.getType() == "FullHouse") return true;
        // check if the compared hand is also a Quad and compare the top cards
        else if (hand.getType() == "Quad"){
            if (this.getTopCard().compareTo(hand.getTopCard()) == 1) return true;
        }
        return false;
    }
    
    /**
     * This method checks if this hand is a valid Quad hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check the size
        if (this.size() == 5){
        	
        	// this if condition check if the 4 same ranked cards is in index 0 to 3
            if (this.getCard(1).getRank() == this.getCard(0).getRank()){
            	
            	// check the equality of the ranks from card index 1 to 3
                for(int i = 1; i < 4; i++){
                    if (this.getCard(i).getRank() != this.getCard(0).getRank()) return false;
                }
            return true;
            }
            
            // this else if condition check if the 4 same ranked cards is in index 1 to 4
            else if (this.getCard(1).getRank() == this.getCard(4).getRank()){
            	
            	// check the equality of the ranks from card index 1 to 4
                for(int i = 1; i < 4; i++){
                    if (this.getCard(i).getRank() != this.getCard(4).getRank()) return false;
                }
            return true;
            }
        }
        return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (Quad).
     * @return a string consists of the word "Quad"
     */
    public String getType() {
        return "Quad";
    }
    
}
