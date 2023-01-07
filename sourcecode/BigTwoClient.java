import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.*;


/**
 * The BigTwoClient class implements the NetworkGame interface. It is used to model a Big Two game client that is responsible for establishing a connection and communicating with the Big Two game server.
 * @author Davinne Valeria
 */
public class BigTwoClient implements NetworkGame{
    /**
     * A constructor for creating a Big Two client
     * @param game A reference to a BigTwo object associated with this client 
     * @param gui A reference to a BigTwoGUI object associated the BigTwo object
     */
    public BigTwoClient(BigTwo game, BigTwoGUI gui){
        this.game = game;
        this.gui = gui;
    }

    private BigTwo game;
    private BigTwoGUI gui;
    private Socket sock;
    private ObjectOutputStream oos;
    private int playerID;
    private String playerName;
    private String serverIP;
    private int serverPort;

    /**
     * A method for getting the playerID (i.e., index) of the local player.
     * @return int index of local player
     */
    public int getPlayerID(){
        return this.playerID;
    }

    /**
     * A method for setting the playerID (i.e., index) of the local player.
     * @param playerID index of the local player
     */
    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    /**
     * A method for getting the name of the local player.
     * @return String name of local player
     */
    public String getPlayerName(){
        return this.playerName;
    }

    /**
     * A method for setting the name of the local player.
     * @param playerName name of the local player
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    /**
     *  A method for getting the IP address of the game server.
     * @return String IP address of the game server
     */
    public String getServerIP(){
        return this.serverIP;
    }

    /**
     * A method for setting the IP address of the game server.
     * @param serverIP IP address of the game server
     */
    public void setServerIP(String serverIP){
        this.serverIP = serverIP;
    }

    /**
     * A method for getting the TCP port of the game server.
     * @return int TCP port of the game server
     */
    public int getServerPort(){
        return this.serverPort;
    }

    /**
     * A method for setting the TCP port of the game server.
     * @param serverPort TCP port of the game server
     */
    public void setServerPort(int serverPort){
        this.serverPort = serverPort;
    }

    /**
     * A method for making a socket connection with the game server.
     */
    public synchronized void connect(){
        serverIP = "127.0.0.1";
        serverPort = 2396;
        playerName = JOptionPane.showInputDialog("Please enter your name: ");
        try {
			this.sock = new Socket(serverIP, serverPort);
            // create an ObjectOutputStream for sending messages to the game server
            oos = new ObjectOutputStream(sock.getOutputStream());
            // create a new thread for receiving messages from the game server
            Thread myThread = new Thread(new serverHandler());
            myThread.start();
			System.out.println("networking established");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        gui.repaint();
    }

    /**
     * A method for parsing the messages received from the game server.
     * @param message message received from the server
     */
    public synchronized void parseMessage(GameMessage message){
        switch (message.getType()){
            case CardGameMessage.PLAYER_LIST:
                //System.out.println(getPlayerName());
                setPlayerID(message.getPlayerID());
                for (int i = 0; i < 4; i++){
                    game.getPlayerList().get(i).setName(((String[]) message.getData())[i]);
                }
                sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
                break;
            case CardGameMessage.JOIN:
                //System.out.println(getPlayerName());
                if (message.getPlayerID() == this.playerID){    
                    //System.out.println("If else success");
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                else{
                    gui.printMsg((String) message.getData()+" joins the game \n");
                }
                 game.getPlayerList().get(message.getPlayerID()).setName((String) message.getData());
                 gui.repaint();
                break;
            case CardGameMessage.FULL:
                gui.printMsg("Server is full, you cannot join the game!\n");
                break;
            case CardGameMessage.QUIT:
                game.getPlayerList().get(message.getPlayerID()).setName("");
                if (game.endOfGame() == false){
                    gui.disable();
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                gui.repaint();
                break;
            case CardGameMessage.READY:
                gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName() + " is ready! \n");
                break;
            case CardGameMessage.START:
                gui.disable();
                game.start((BigTwoDeck) message.getData());
                gui.repaint();
                break; 
            case CardGameMessage.MOVE:
                game.checkMove(message.getPlayerID(), (int[]) message.getData());  
                break;
            case CardGameMessage.MSG:
                gui.sendChat((String) message.getData());  
         }
    }

    /**
     * A method for sending the specified message to the game server.
     * @param message message to be sent to the game server
     */
    public synchronized void sendMessage(GameMessage message){
        try {
            oos.writeObject(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * An inner class that implements the Runnable interface
     */
    class serverHandler implements Runnable {
        /**
         * Method from the Runnable interface 
         */
        public void run(){
            GameMessage message;
            try {
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                while ((message = (GameMessage) ois.readObject()) != null)
                    parseMessage(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

