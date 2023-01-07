import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author Davinne Valeria
 */
public class BigTwoGUI implements CardGameUI {
    private BigTwo game;
    private boolean[] selected;
    private int activePlayer;
    private JFrame frame;
    private JPanel bigTwoPanel;
    private JPanel lowerPanel1;
    private JPanel lowerPanel2;
    private JPanel playPanel;
    private JPanel textPanel;
    private JScrollPane msgScroller;
    private JScrollPane chatScroller;
    private JLabel message;
    private JMenuBar menuBar;
    private JMenuItem connectButton;
    private JMenuItem quitButton;
    private JMenu gameMenu;
    private JButton playButton;
    private JButton passButton;
    private JTextField chatInput;
    private ArrayList<Image> fullCards;
    private int[] cardIdx;
    private boolean activePanel = true;
    private JTextArea chatArea;

    /**
     * msgArea is a text area for showing the current game status as well as end of game messages.
     */
    public JTextArea msgArea;

    // load the character images and back card image
    private Image p0 = new ImageIcon("P0.png").getImage();
    private Image p1 = new ImageIcon("P1.png").getImage();
    private Image p2 = new ImageIcon("P2.png").getImage();
    private Image p3 = new ImageIcon("P3.png").getImage();
    private Image back = new ImageIcon("Cards/b.gif").getImage();
    private int yAxis = 0;
    private int selectedCard = -1;

    /**
     * Get the chat inputting space of the GUI
     * @return JTextField chat input space of the GUI
     */
    public JTextField getChatInput(){
        return this.chatInput;
    }

    /**
     * Add message to chat area when a player sends a message.
     * @param message message to be appended
     */
    public void sendChat(String message){
        this.chatArea.append(message+"\n");
    }

    /**
     * Get the frame of the gui.
     * @return JFrame frame of the gui.
     */
    public JFrame getFrame(){
        return this.frame;
    }
    /**
     * This is a constructor for creating a BigTwoGUI.
     * @param game a reference to a Big Two card game associates with this GUI
     * 
     */
    public BigTwoGUI (BigTwo game){
        this.game = game;
        fullCards = new ArrayList<Image>();
        selected = new boolean[13];
        cardIdx = null;
        for (int i = 0; i < 13; i++)
            selected[i] = false;

        // loading all the card images in one ArrayList
        for (int i = 0; i < 52; i++){
            Image image = new ImageIcon("Cards/" + (int)(i/4) + i%4 + ".gif" ).getImage();
            fullCards.add(image);
        }

        frame = new JFrame("Big Two Game");
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        connectButton = new JMenuItem("Connect");
        quitButton = new JMenuItem("Quit");
        textPanel = new JPanel(); 
        msgArea = new JTextArea(45,45);
        msgScroller = new JScrollPane(msgArea);
        chatArea = new JTextArea(45,45);
        chatScroller = new JScrollPane(chatArea);
		lowerPanel2 = new JPanel();
        chatInput = new JTextField(20);
        message = new JLabel("Message: ");

        // setting up the layout of the GUI
        gameMenu.add(connectButton);
        gameMenu.add(quitButton);       
        menuBar.add(gameMenu);
        textPanel.add(msgScroller);
        textPanel.add(chatScroller);
        lowerPanel2.add(message);
        lowerPanel2.add(chatInput);
        textPanel.add(lowerPanel2);
        
        connectButton.addActionListener(new ConnectMenuItemListener());
        quitButton.addActionListener(new QuitMenuItemListener());

        
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        msgArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        msgArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        msgArea.setLineWrap(true);
		msgArea.setEditable(false);

        // make a scroller for msgArea 
        msgScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		msgScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        chatArea.setForeground(Color.BLUE);
        chatArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatArea.setLineWrap(true);
        chatArea.setEditable(false);

        // make a scroller for chatArea
        chatScroller
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        lowerPanel2.setPreferredSize(new Dimension(500,38));
        chatInput.addActionListener(new ChatInputButtonListener());
    }

    /**
     * This is a method for setting the index of the active player (i.e., the player having control of the GUI).
     * @param activePlayer the index of the current player
     */
    public void setActivePlayer(int activePlayer){
        this.activePlayer = activePlayer;
    }

    /**
     * This is a method for repainting the GU
     */
	public void repaint(){ 
        frame.setSize(1500,1200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        playPanel = new JPanel();
        playButton = new JButton("Play");
        passButton = new JButton("Pass");
        lowerPanel1 = new JPanel();
        bigTwoPanel = new BigTwoPanel();

        // setting up the layout
        lowerPanel1.add(playButton);
        lowerPanel1.add(passButton);
        playPanel.add(bigTwoPanel);
        playPanel.add(lowerPanel1);

        bigTwoPanel.setPreferredSize(new Dimension(940,1000));
        bigTwoPanel.setBackground(new Color(75, 156, 88));
        bigTwoPanel.addMouseListener(new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {  
                yAxis = 0;
                
                if (activePanel == true){ // check if user interaction is allowed
                    // card-selecting process:
                    // check if the mouse event is on one of the cards (the user clicks a card) and the card is not selected in the selected array
                    if (((e.getX() >= 180 && e.getX() <= (180+(game.getPlayerList().get(activePlayer).getNumOfCards()*45))) && selected[(int) ((e.getX()-180) / 45)] == false) || ((e.getX() > (180+game.getPlayerList().get(activePlayer).getNumOfCards()*45)) && (e.getX() <= (180+game.getPlayerList().get(activePlayer).getNumOfCards()*45+45)) && (selected[game.getPlayerList().get(activePlayer).getNumOfCards()-1] == false))){
                        if (((int) ((e.getY()-20)/155) == activePlayer) ){

                            // set the selected card to true in selected array
                            if (e.getX() <= (180+(game.getPlayerList().get(activePlayer).getNumOfCards()*45)) ){
                                selected[(int) ((e.getX()-180) / 45)] = true;
                            }
                                
                            else{
                                selected[game.getPlayerList().get(activePlayer).getNumOfCards()-1] = true;
                            }
                            
                            
                        }

                        // use a for loop to do the upward animation to move the card
                        for (int i = 0; i < 20; i++) {
                            yAxis--;
                            bigTwoPanel.repaint();
                
                            try {
                                Thread.sleep(1);
                            } catch (Exception ex) { }
                        }
                    }

                    // card-deselecting process
                    // check if the mouse event is on one of the cards (the user clicks a card) and the card is selected in the selected array 
                    else if ((((e.getX() >= 180 && e.getX() <= (180+(game.getPlayerList().get(activePlayer).getNumOfCards()*45))) && selected[(int) ((e.getX()-180) / 45)] == true) || ((e.getX() > (180+game.getPlayerList().get(activePlayer).getNumOfCards()*45)) && (e.getX() <= (180+game.getPlayerList().get(activePlayer).getNumOfCards()*45+45)) && (selected[game.getPlayerList().get(activePlayer).getNumOfCards()-1] == true)))){
                        
                        if (((int) ((e.getY()-20)/155) == activePlayer) ){
                            
                            // set the selected card to false in selected array
                            if (e.getX() <= (180+(game.getPlayerList().get(activePlayer).getNumOfCards()*45)) ){
                                selected[(int) ((e.getX()-180) / 45)] = false;
                            }
                                
                            else{
                                selected[game.getPlayerList().get(activePlayer).getNumOfCards()-1] = false;
                            }
                            
                        }
                        
                        // use a for loop to do the downward animation to move the card
                        for (int i = 0; i < 20; i++) {
                            yAxis++;
                            bigTwoPanel.repaint();
                
                            try {
                                Thread.sleep(1);
                            } catch (Exception ex) { }
                        }
                    }
                }
            }  
        });
        

        playButton.addActionListener(new PlayButtonListener());
        passButton.addActionListener(new PassButtonListener());
        playPanel.setLayout(new BoxLayout(playPanel, BoxLayout.Y_AXIS));   

        frame.add(textPanel, BorderLayout.EAST);
        frame.add(playPanel, BorderLayout.WEST);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        frame.pack();

        if (game.getCurrentPlayerIdx() == activePlayer)
        {   
            enable();} // enable user interaction
    }
 
    /**
     * This is an inner class that extends the JPanel class and implements the MouseListener interface.
     */
    class BigTwoPanel extends JPanel{ 
        /**
         * This is an overriden method inherited from the JPanel class to draw the card game table.
         */
        public void paintComponent(Graphics g){

            // maintain components of the panel
            super.paintComponent(g);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
            
            if (game.getPlayerList().get(0).getName() != null)
                g.drawImage(p0,50, 20, 90, 90, bigTwoPanel);
            if (game.getPlayerList().get(1).getName() != null)
                g.drawImage(p1,50, 175, 90, 90, bigTwoPanel);    
            if (game.getPlayerList().get(2).getName() != null) 
                g.drawImage(p2,50, 330, 90, 90, bigTwoPanel);
            if (game.getPlayerList().get(3).getName() != null)
                g.drawImage(p3,50, 485, 90, 90, bigTwoPanel);

            // use a for loop to draw the table set for all players
            for (int i = 0; i < 4; i++){
                g.setColor(Color.BLACK);
                g.drawRect(0, 145+i*155, 1000, 1);

                if (game.getPlayerList().get(i).getName() != null){
                    if (i == game.getCurrentPlayerIdx()){
                        g.setColor(Color.WHITE);
                        g.drawString(game.getPlayerList().get(i).getName(), 70, 130+i*155);
                    }

                    else if (i == activePlayer){
                        g.setColor(Color.BLUE);
                        g.drawString("You", 70, 130+i*155);
                    }
                    else{
                        g.setColor(Color.BLACK);
                        g.drawString(game.getPlayerList().get(i).getName(), 70, 130+i*155);
                    }
                }
            }

            CardList cList = game.getPlayerList().get(game.getClient().getPlayerID()).getCardsInHand();
            
            // use this for loop to draw the cards of all players
            for (int p = 0; p < 4; p++){
                if (p == activePlayer ){
                    for (int i = 0; i < game.getPlayerList().get(p).getNumOfCards(); i++){
                        
                        Image card = fullCards.get((cList.getCard(i).getRank()*4)+(cList.getCard(i).getSuit()));

                        if (i == selectedCard && selected[i] == true){
                            g.drawImage(card, (180+i*45), (20+activePlayer*155+yAxis), 90, 110, bigTwoPanel);
                        }

                        else if (i == selectedCard && selected[i] == false){
                            g.drawImage(card,(180+i*45), (activePlayer*155+yAxis), 90, 110, bigTwoPanel);
                        }

                        else if (selected[i] == false && i != selectedCard){
                            g.drawImage(card,(180+i*45), (20+activePlayer*155), 90, 110, bigTwoPanel);
                        }

                        else if (selected[i] == true){
                            g.drawImage(card,(180+i*45), (20+activePlayer*155-20), 90, 110, bigTwoPanel);
                        }
                    }
                }

                // if we are not currently drawing the active player, we draw the back part of the cards to make them invisible
                else {
                    for (int i = 0; i < game.getPlayerList().get(p).getNumOfCards(); i++){
                        g.drawImage(back,(180+i*45), (20+p*155), 90, 110, bigTwoPanel);
                    }
                }
            }

            // this part helps to draw the very bottom part of the BigTwoPanel
            // it draws the last successful move and show the owner of the move
            Hand lastHandOnTable = (game.getHandsOnTable().isEmpty()) ? null : game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
            if (lastHandOnTable != null){
                for (int i = 0; i < lastHandOnTable.size(); i++){
                    Image card = fullCards.get(lastHandOnTable.getCard(i).getRank()*4+ lastHandOnTable.getCard(i).getSuit());
                    g.drawImage(card, (50+i*45), 650, 90, 110, bigTwoPanel);
                }
                g.drawString("Played by " + lastHandOnTable.getPlayer().getName(), 50, 640);
            }
        }
    }

    /**
     * This is an inner class that implements the ActionListener interface. 
     * It mplements the actionPerformed() method from the ActionListener interface to handle events for player's chat input.
     */
    class ChatInputButtonListener implements ActionListener{
        /**
         * This method moves the text inside chatInput is moved into chatArea when an event occurs.
         */
        public synchronized void actionPerformed(ActionEvent event){
            //chatArea.append("Player " + activePlayer + ": " + chatInput.getText()+"\n");
            //chatInput.setText("");
            game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG, -1,chatInput.getText()));
        }
    }

    /**
     * This is a method for printing the specified string to the message area of the GUI.
     */
	public void printMsg(String msg){
        msgArea.append(msg);
    }

    /**
     * This is a method for clearing the message area of the GUI. 
     */
	public void clearMsgArea(){
        msgArea.setText("");
        chatArea.setText("");
    }

    /**
     * This is a method for resetting the GUI.
     */
	public void reset(){
        // deselect all cards
        for (int i = 0; i < selected.length; i++){
            selected[i] = false;
        }
        //clear all messages
        clearMsgArea();
        // enable user interactions
        enable();
    }


    /**
     * This is a method for enabling user interactions with the GUI.
     */
	public void enable(){
        playButton.setEnabled(true);
        passButton.setEnabled(true);
        //activePanel = true;
    }

    /**
     * This is an inner class that implements the ActionListener interface to handle events related to the Play buton.
     */
    class PlayButtonListener implements ActionListener {
        /**
         * This method is implemented from the ActionListener interface to handle button-click events for the Play button.
         */
        public void actionPerformed(ActionEvent event){

            // count how many selected cards
		    int count = 0;
            for (int j = 0; j < selected.length; j++) {
                if (selected[j] == true) {
                    count++;
                }
            }

            // put all the selected card indexes into an array
            // then use it as an argument to call makeMove()
            if (count != 0) {
                cardIdx = new int[count];
                count = 0;
                for (int j = 0; j < selected.length; j++) {
                    if (selected[j]) {
                        cardIdx[count] = j;
                        count++;
                    }
                }
            }

            // check if the move is successful
            BigTwoGUI.this.game.makeMove(activePlayer, cardIdx);
        }
    }

    /**
     * This is an inner class that implements the ActionListener interface to handle events related to the Pass buton.
     */
    class PassButtonListener implements ActionListener {
        /**
         * This method is implemented from the ActionListener interface to handle button-click events for the Pass button.
         */
        public void actionPerformed(ActionEvent event){
            BigTwoGUI.this.game.makeMove(activePlayer, null);
        }
    }

    /**
     * This is an inner class that implements the ActionListener interface to handle events related to the Connect buton.
     */
    class ConnectMenuItemListener implements ActionListener{
        /**
         * This method is implemented from the ActionListener interface to handle button-click events for the Connect button.
         */
        public void actionPerformed(ActionEvent event){
            game.connectGUI();
        }
    }

    /**
     * This is an inner class that implements the ActionListener interface to handle events related to the Quit buton.
     */
    class QuitMenuItemListener implements ActionListener{
        /**
         * This method is implemented from the ActionListener interface to handle button-click events for the Quit button.
         */
        public void actionPerformed(ActionEvent event){
            System.exit(0);
        }
    }

    /**
     * This is a method for disabling user interactions with the GUI
     */
	public void disable(){
        playButton.setEnabled(false);
        passButton.setEnabled(false);
        //activePanel = false;
    }

    /**
     * This is a method for prompting the active player to select cards and make his/her move.
     */
	public void promptActivePlayer(){
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;
        cardIdx = null;
        selectedCard = -1;
        printMsg(game.getPlayerList().get(game.getCurrentPlayerIdx()).getName()+ "'s turn: \n");
    }
}
