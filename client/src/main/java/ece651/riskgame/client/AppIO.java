/*
* This Java source file was generated by the Gradle 'init' task.
*/
package ece651.riskgame.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ece651.riskgame.shared.Action;
import ece651.riskgame.shared.BasicUnit;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Move;
import ece651.riskgame.shared.Territory;
import ece651.riskgame.shared.Unit;

public class AppIO {

  private DemoGUIPlayer player;
  private ObjectInputStream socketIn;
  private ObjectOutputStream socketOut;

  String color;
  GameInfo gameInfo;

  public String getColor() {
    return color;
  }

  public GameInfo getGameInfo() {
    return gameInfo;
  }
  
  public AppIO (String[] args) throws IOException {
    String ip = args[0];
    int port = -1;
    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Argument" + args[1] + " must be an integer.");
      System.exit(1);
    }
    // connect to server
    Socket serverSocket = null;
    ObjectInputStream socketIn = null;
    ObjectOutputStream socketOut = null;
    try {
      serverSocket = new Socket(ip, port);
      socketIn = new ObjectInputStream(serverSocket.getInputStream());
      socketOut = new ObjectOutputStream(serverSocket.getOutputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host: " + ip);
      System.exit(1);
    } catch (ConnectException e) {
      System.err.println("Can not connect to server. Please contact 984-377-9836.");
      System.exit(1);
    }
    System.out.println("Connection Estabilished");

    //recv allocated player color
    //recv GameInfo
    //String color = null;
    //GameInfo game = null;
    try {
      color = (String) socketIn.readObject();
      gameInfo = (GameInfo) socketIn.readObject();
    } catch (ClassNotFoundException e) {
      System.err.println("Class Not Found when reading Object through socket");
      System.exit(1);
    }
    DemoGUIPlayer player = new DemoGUIPlayer(color, gameInfo);
    //this(player, socketIn, socketOut);
  }

  public void startGame() {
    /*
    app.doPlacementPhase();
    app.doActionPhase();
    //game over
    if (player.isGameOver()) {
      app.doGameOverPhase();
    }
    //player lost
    else {
      app.doPostDeathPhase();
    }
       
    serverSocket.close();
    System.exit(0);
    */
  }

  /**
   * constructor of app
   * @param player is the current player of the game on this client program
   * @param socketIn is the objectInputStream of the socket
   * @param socketOut is the objectOutputStream of the socket
   */
  public AppIO(DemoGUIPlayer player, ObjectInputStream socketIn, ObjectOutputStream socketOut) {
    this.player = player;
    this.socketIn = socketIn;
    this.socketOut = socketOut;
  }

  /**
   * recvGame is used to recieve the game function from the socket to server
   * @throws IOException when nothing fetched from objectstream input
   */
  protected GameInfo recvGame() throws IOException{
    GameInfo game;
    try {
      game = (GameInfo) socketIn.readObject();
      return game;
    } catch (ClassNotFoundException e) {
      System.err.println("Class Not Found when reading Object through socket");
      System.exit(1);
      return null;
    }
  }

  /**
   * doPlacementPhase is used to send the initial placements to the server
   * @throws IOException when the initial units to allocate is not recieved
   */
  
  @SuppressWarnings("unchecked")
  public void doPlacementPhase() throws IOException {
    List<Unit> toPlace = null;
    try {
      toPlace = (List<Unit>) socketIn.readObject();
    } catch (ClassNotFoundException e) {
      System.err.println("Class Not Found when reading Object through socket");
      System.exit(1);
    }
    List<Move> placements = player.readPlacementPhase(toPlace);
    //adapting from list of moves to map(territory string to list of placed units)
    Map<String, List<Unit>> serverPlacements = new HashMap<>();
    List<Territory> occupies = player.getOccupies();
    for (Territory occupy : occupies) {
      serverPlacements.put(occupy.getName(), new ArrayList<Unit>(Arrays.asList(new BasicUnit(0))));
    }
    for (Move placement: placements) {
      serverPlacements.get(placement.getToTerritory()).add(placement.getUnit());
    }

    socketOut.writeObject(serverPlacements);
    socketOut.flush();
    socketOut.reset();

    printWaitingMsg();
  }

  /**
   * doActionPhase is used to read valid actions from client and send them to the server
   * @throws IOException when latest game is not recieved
   */
  public void doActionPhase() throws IOException {
    GameInfo game = recvGame();
    player.update(game);
    while (!player.isLost() && !player.isGameOver()) {
      List<Action> actions = player.readActionsPhase();
      socketOut.writeObject(actions);
      socketOut.flush();
      socketOut.reset();

      printWaitingMsg();
      game = recvGame();
      player.update(game);
    }
  }

  /**
   * doGameOverPhase is used to declare the result and close I/O streams
   * @throws IOException when I/O streams fail to close
   */
  public void doGameOverPhase() throws IOException {
    player.doGameOverPhase();
    socketIn.close();
    socketOut.close();
  }
  /**
   * doSpectationPhase is used when client choose to speculate the game after dead
   * @return IOException when the latest game is not recieved
   */
  public void doSpectationPhase() throws IOException{
    GameInfo game;
    while(!player.isGameOver()) {
      player.doOneSpectation();
      
      printWaitingMsg();

      game = recvGame();
      player.update(game);      
    }
  }
  
  /**
   * doPostDeathPhase is to let user choose speculate or quit after dead
   * client will spectate the rest of them if type "S"
   * client will quit the game if type "Q"
   * @throws IOException when nothing is typed
   */
  public void doPostDeathPhase() throws IOException {
    String choice = player.getPostDeathChoice();
    if (choice.equals("S")) {
      doSpectationPhase();
      doGameOverPhase();
    }
    else if (choice.equals("Q")) {
      doGameOverPhase();
    }
    else {
    }
  }
  //TODO:Time elapse
  private void printWaitingMsg() {
    System.out.println("Waiting for other players...");
  }
}