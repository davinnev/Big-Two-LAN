/**
 * This class is a subclass of the Deck class and is used to model a deck of cards used in a Big Two card game.
 * 
 * @author Davinne Valeria
 *
 */
public class BigTwoDeck extends Deck{
	/**
	 * A method for initializing a deck of Big Two cards.
	 */
    public void initialize(){
    	
    	// remove the card, create 52 BigTwo cards and add them to the deck
        removeAllCards();
        for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
    }
}
