import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game.
 * @author Davinne Valeria
 *
 */
public class BigTwo implements CardGame {
    private int numOfPlayers;
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private int currentPlayerIdx;
    private BigTwoGUI ui;
    private int passCounter = 0;
    private BigTwoClient client;
    
    /**
     * A constructor to create a BigTwo game.
     */
    public BigTwo(){
    	
    	// Create the ArrayLists
        this.playerList = new ArrayList<CardGamePlayer>();
        this.handsOnTable = new ArrayList<Hand>();        
        // Insert the players by creating CardGamePlayer objects then add them to playerList.
        for (int i = 0; i < 4; i++){
            CardGamePlayer player = new CardGamePlayer();
            this.playerList.add(player);
        }
        
        // Create a BigTwoUI object to provide the user interface.
        ui = new BigTwoGUI(this);
        client = new BigTwoClient(this, ui);
        client.connect();
    }

    /**
     * A method to facilitate connection to server, used by GUI
     */
    public void connectGUI(){
        this.client.connect();
    }
    
    /**
     * A method for getting the number of players (getter method).
     * @return number of players
     */
    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }

    /**
     * A method for getting the player ID of the client.
     * @return player ID of the client
     */
    public int getPlayerID(){
        return client.getPlayerID();
    }
    
    /**
     * A method for getting the BigTwoClient item of the client.
     * @return BigTwoClient client of the player
     */
    public BigTwoClient getClient(){
        return this.client;
    }
    
    /**
     * A method for retrieving the deck of cards being used.
     * @return deck used in the game
     */
    public Deck getDeck(){
        return this.deck;
    }
    
    /**
     * A method for retrieving the list of players.
     * @return ArrayList<CardGamePlayer> list of players
     */
    public ArrayList<CardGamePlayer> getPlayerList(){
        return this.playerList;
    }
    
    /**
     * A method for retrieving the list of hands played on the table.
     * @return ArrayList<Hand> hands played during the game
     */
    public ArrayList<Hand> getHandsOnTable(){
        return this.handsOnTable;
    }
    
    /**
     * A method for retrieving the index of the current player
     * @return int index of current playing player
     */
    public int getCurrentPlayerIdx(){
        return this.currentPlayerIdx;
    }
    
    /**
     * A method to help sending message to the server.
     * @param msgType message type
     * @param player (depends on message type)
     * @param message (depends on message type)
     */
    public void sendMessage(int msgType, int player, String message){
        client.sendMessage(new GameMessage(msgType, player, message));
        if (msgType == CardGameMessage.MSG)
            ui.getChatInput().setText("");
    }

    /**
     * A method for starting/restarting the game with a given shuffled deck of cards.
     * @param deck shuffled deck of cards used in the game
     */
    public void start(Deck deck){
    	
    	// remove all cards from all players
        for (int i = 0; i < 4; i++){
            playerList.get(i).removeAllCards();
        }
        
        // remove all cards on the table
        for (int i = 0; i < handsOnTable.size(); i++){
            handsOnTable.get(i).removeAllCards();
        }
        
        // distribute the cards to players
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 13; j++){
                playerList.get(i).addCard(deck.getCard(j+(i*13)));
                
                // setting the player with Three of Diamonds as the first player to make a move
                if (deck.getCard(j+(i*13)).getSuit() == 0 && deck.getCard(j+(i*13)).getRank() == 2){
                    this.currentPlayerIdx = i;
                }
            }
            
            // sort cards in players' hand
            playerList.get(i).sortCardsInHand();
        }
        
        // print the user interface and get input from the first player
        ui.repaint();
        ui.promptActivePlayer();
        ui.setActivePlayer(client.getPlayerID());
    }
    
    /**
     * A method for making a move by a player with the specified index using the cards specified by the list of indices.
     * @param playerIdx index of the active player in playerList
     * @param cardIdx indices of cards (from the player's cardList) that are being played by the current player
     */
    public void makeMove(int playerIdx, int[] cardIdx){
        // send message to server
        client.sendMessage(new GameMessage(CardGameMessage.MOVE, -1, cardIdx));
        checkMove(playerIdx, cardIdx);
    }
    
    /**
     *  A method for checking a move made by a player.
     *  @param playerIdx index of the active player in playerList
     *  @param cardIdx indices of cards (from the player's cardList) that are being played by the current player
     */
    public synchronized void checkMove(int playerIdx, int[] cardIdx){
        
        if (playerIdx == currentPlayerIdx ){ // check whether it is one specific player's turn to play the card (the active player)
        	
            if (playerList.get(playerIdx).play(cardIdx) == null){ // if the player chooses to pass
            	passCounter++; 
                if (passCounter >= 4 || handsOnTable.size() == 0){ 
                	// prohibit player to pass if it is the first move of the game by the first player 
                	// or if there are already 3 consecutive passes
                    ui.printMsg("Not a legal move!!!\n");
                    ui.promptActivePlayer();
                }
                
                // if it is a legal pass, continue with the next player
                else{
                ui.printMsg("{Pass}\n");
                currentPlayerIdx = (currentPlayerIdx+1) % 4;
                ui.setActivePlayer(currentPlayerIdx);
                ui.repaint();
                ui.promptActivePlayer();}
            }
            

            else{
            	
            	// compose a suitable Hand type to check the move
                CardList currentCards = playerList.get(playerIdx).play(cardIdx);
                Hand currentHand = composeHand(playerList.get(playerIdx), currentCards);
                
                /*
                 * currentHand is legal and can be placed on the table if:
                 * 1. it is the first move of the first player
                 * 2. it beats the latest set of cards on the table
                 * 3. there are 3 legal consecutive passes before
                 */
                
                if (currentHand != null && (handsOnTable.size() == 0 || handsOnTable.size() != 0 && 
                currentHand.beats(handsOnTable.get(handsOnTable.size() - 1)) ||
                passCounter >= 3)){

                    boolean rightFirstMove = false;

                   if (handsOnTable.size() == 0){
                        for (int i = 0; i < currentHand.size(); i++){
                            if (currentHand.getCard(i).getRank() == 2 && currentHand.getCard(i).getSuit() == 0){
                                rightFirstMove = true;
                            }
                        }
                    

                        if (rightFirstMove == false){
                            ui.printMsg("Not a legal move!!!\n");
                            ui.repaint();
                            ui.promptActivePlayer();
                        }
                    }
                   
                    if (handsOnTable.size() != 0 || handsOnTable.size() == 0 && rightFirstMove == true){
                        ui.msgArea.append
                            ("{" + currentHand.getType() + "} ");
                        String string = "";
                        for (int i = 0; i < currentHand.size(); i++) {
                            string = string + "[" + currentHand.getCard(i) + "]";
                        }
                        ui.msgArea.append(string+"\n");
                        
                        passCounter = 0;
                        playerList.get(playerIdx).removeCards(currentCards); // remove the cards played from player's hand
                        
                        // check if the last move ends the game (a player has no cards left)
                        // then print the winning statement (the winner and cards left on non-winner players)
                        if (endOfGame()){
                            ui.disable();
                            int numOfCards;
                            handsOnTable.add(currentHand);
                            currentHand.print(true, false);
                            ui.repaint();
                            /**ui.printMsg("Game ends\n");
                            for (int i = 0; i < 4; i++){
                                if (playerList.get(i).getNumOfCards() == 0)
                                    ui.printMsg("Player " + i + " wins the game.\n");
                                
                                else {
                                    numOfCards = playerList.get(i).getNumOfCards();
                                    if (numOfCards == 1)
                                        ui.printMsg("Player " + i + " has " + playerList.get(i).getNumOfCards() + " card in hand.\n");
                                    else if (numOfCards > 1)
                                        ui.printMsg("Player " + i + " has " + playerList.get(i).getNumOfCards() + " cards in hand.\n");
                                }
                            }**/
                            String message = "Game ends\n";
                            for (int i = 0; i < 4; i++){
                                if (playerList.get(i).getNumOfCards() == 0)
                                    message = message + ("Player " + i + " wins the game.\n");
                                
                                else {
                                    numOfCards = playerList.get(i).getNumOfCards();
                                    if (numOfCards == 1)
                                        message = message + ("Player " + i + " has " + numOfCards + " card in hand.\n");
                                    else if (numOfCards > 1)
                                        message = message +("Player " + i + " has " + numOfCards + " cards in hand.\n");
                                }
                            }
                            JOptionPane.showMessageDialog(ui.getFrame(), message, "Game Result", JOptionPane.PLAIN_MESSAGE);
                            sendMessage(CardGameMessage.READY, -1, null);
                        }
                        
                        // if the game is not ended yet, put the list of cards on the table
                        // then continue with the next player
                        else {
                            handsOnTable.add(currentHand);
                            currentPlayerIdx = (currentPlayerIdx+1) % 4;
        
                        
        
                            ui.setActivePlayer(currentPlayerIdx);
                            ui.repaint();
                            ui.promptActivePlayer(); }
                    }
                }
                
                // if currentHand is not legal (cannot be placed on the table), ask the current player to input again
                else{

                    ui.printMsg("Not a legal move!!!\n");
                    ui.repaint();
                    ui.promptActivePlayer();
                }
            }
        }
    }
    
    
    /**
     * A method for checking if the game ends.
     * @return true if the game has ended and false if it has not
     */
    public boolean endOfGame(){
        for (int i = 0; i < 4; i++){
        	// if someone has no cards left, the game ends
            if (playerList.get(i).getNumOfCards() == 0)
                return true;
        }
        return false;
    }
    
    /**
     * A method for starting a Big Two card game.
     * @param Args not used in this application
     */
    public static void main(String[] Args){
    	// create a new game object and a new shuffled deck
        BigTwo game = new BigTwo();
        // start the game
    }
    
    /**
     * A method for returning a valid hand from the specified list of cards of the player.
     * @param player current active player
     * @param cards list of cards being played
     * @return a Hand object which specify the type of combination formed by the cards (e.g. Single, FullHouse, etc.)
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards){
    	
    	// make object of combination types with a Hand reference type to apply polymorphism concept
        Hand isSingle = new Single(player, cards);
        if (isSingle.isValid()) return isSingle;

        Hand isPair = new Pair(player, cards);
        if (isPair.isValid()) return isPair;

        Hand isTriple = new Triple(player, cards);        
        if (isTriple.isValid()) return isTriple;

        Hand isStraight = new Straight(player, cards);
        if (isStraight.isValid()) return isStraight;

        Hand isFlush = new Flush(player, cards);
        if (isFlush.isValid()) return isFlush;

        Hand isFullHouse = new FullHouse(player, cards);
        if (isFullHouse.isValid()) return isFullHouse;

        Hand isQuad = new Quad(player, cards);
        if (isQuad.isValid()) return isQuad;

        Hand isStraightFlush = new StraightFlush(player, cards);
        if (isStraightFlush.isValid()) return isStraightFlush;

        return null;
    }
}