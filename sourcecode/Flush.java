/**
 * This class is a subclass of Hand and is used to model a hand of Flush in Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class Flush extends Hand{
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * @param player player player that owns this Hand object
	 * @param cards cards a list of cards that is being played
	 */
    public Flush(CardGamePlayer player, CardList cards){
    	// this method use the constructor of the superclass, which is Hand
        super(player, cards);
    }
    
    /**
     * This method checks if this hand beats a specified hand.
     * @param hand a Hand object to be compared with this hand
     * @return true if this hand beats the other hand, false otherwise
     */
    public boolean beats(Hand hand){
        if (hand.getType() == "Straight") return true; // as any Flush hand will always beat a Straight hand
    	// check if the compared hand is also a flush and compare the greater top card 
        // (suit first, then based on the rank if the suits are the same)
        else if (hand.getType() == "Flush"){
            if (this.getTopCard().getSuit() > hand.getTopCard().getSuit()) return true;
            else if (this.getTopCard().getSuit() == hand.getTopCard().getSuit()){
                if (this.getTopCard().compareTo(hand.getTopCard()) == 1) return true;
            }
        }
        return false;
    }
    
    /**
     * This method checks if this hand is a valid Flush hand.
     * @return true if the hand is valid, false otherwise
     */
    public boolean isValid(){
    	// check the size
        if (this.size() == 5) {
        	// store the first card's suit and compare it with the rest of the cards
            int suitBase = this.getCard(0).getSuit();
            for (int i = 1; i < 5; i++){
                if (this.getCard(i).getSuit() != suitBase) return false;
            }
            return true;
        }
        else return false;
    }
    
    /**
     * This method returns a string specifying the type of this hand (Flush).
     * @return a string consists of the word "Flush"
     */
    public String getType() {
        return "Flush";
    }
}