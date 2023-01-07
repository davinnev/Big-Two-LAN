/**
 * This class is a subclass of the CardList class and is used to model a hand of cards.
 * 
 * @author Davinne Valeria
 *
 */
public abstract class Hand extends CardList{
    private final CardGamePlayer player;
    
    /**
     * A constructor for building a hand with the specified player and list of cards.
     * @param player player that owns this Hand object
     * @param cards a list of cards that is being played
     */
    public Hand(CardGamePlayer player, CardList cards){
        this.player = player; // set the player of this Hand object
        
        // add the cards to the ArrayList cards of this object, then sort it
        for (int i = 0; i < cards.size(); i++){
            this.addCard(cards.getCard(i));
        }
        this.sort();
    }
    
    /**
     * This method retrieves the player of this hand.
     * @return the CardGamePlayer object that plays this hand
     */
    public CardGamePlayer getPlayer(){
        return this.player;
    }
    
    /**
     * This method retrieves the top card of this hand.
     * @return a Card object that is the top card
     */
    public Card getTopCard(){
    	// if the hand is not empty, set the last card inside the ArrayList cards as the top card
        if (!this.isEmpty()){
            this.sort();
            return this.getCard(this.size() - 1);
        }
        return null;
    }
    
    /**
     * This method checks if this hand beats a specified hand.
     * @param hand a Hand object to be compared with this hand
     * @return true if this hand beats the other hand, false otherwise
     */
    public boolean beats(Hand hand){
        if (hand == null || !(hand.isValid()) || !(this.isValid()) || this.getType() != hand.getType())
            return false;
        else return (this.getTopCard().compareTo(hand.getTopCard()) > 0);
    }
    
    /**
     * This method checks if this is a valid hand.
     * @return true if the hand is valid, false otherwise
     */
    public abstract boolean isValid();
    
    /**
     * This method returns a string specifying the type of this hand.
     * @return a string of card combination type 
     */
    public abstract String getType();
}
