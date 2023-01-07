/**
 * This class is a subclass of the Card class and is used to model a card used in a Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */

public class BigTwoCard extends Card{
	
	/**
	 * A constructor for building a card with the specified suit and rank.
	 * @param suit card's suit which is an integer between 0 and 3
	 * @param rank card's rank which is an integer between 0 and 12
	 */
    public BigTwoCard(int suit, int rank){
        super(suit, rank); // we use Card's constructor as the superclass of BigTwoCard class
    }
    
    /**
     * A method for comparing the order of this card with the specified card by using the comparison method used in BigTwo game.
     * @param card the card that is compared with this card (this object)
     * @return returns a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card
     */
    public int compareTo(Card card){
    	
    	// if both of the rank are the same, we compare the suit
        if (this.rank == card.rank) {
            if (this.suit > card.suit) return 1;
            else if (this.suit < card.suit) return -1;
            else return 0;
        }
        
        // if both of them are either ranked A or Two, the card with number Two is greater than the A card
        if (this.rank < 2 && card.rank < 2) {
            if (this.rank > card.rank) return 1;
            else return -1;}
        
        // if both are neither A nor Two, compare the rank as usual
        else if (this.rank > 1 && card.rank > 1) {
            if (this.rank > card.rank) return 1;
            else return -1;}
        
        // if one of them is A or Two, it is greater than the other one
        else if (this.rank < 2 && card.rank > 1)
            return 1;
        else if (this.rank > 1 && card.rank < 2)
            return -1;
        return 0;
    }
}
