/*
* This Java source file was generated by the Gradle 'init' task.
*/
package ece651.riskgame.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;

import ece651.riskgame.client.controllers.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RiscApplication extends Application {
  // GUIPlayer
  GUIPlayer guiPlayer;

  GameController gameController;
  
  /**
   * Method called to launch the frontend application
   */
  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException, ClassNotFoundException {
    String ip = "vcm-25372.vm.duke.edu";
    int port = 1651;
    // connect to server
    Socket serverSocket = null;
    try {
      serverSocket = new Socket(ip, port);
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host: " + ip);
      System.exit(1);
    } catch (ConnectException e) {
      System.err.println("Can not connect to server. Please contact 984-377-9836.");
      System.exit(1);
    }
    System.out.println("Connection Estabilished");

    guiPlayer = new GUIPlayer(serverSocket);
    guiPlayer.initializeGame();

    gameController = new GameController(guiPlayer);

    URL xmlResource = getClass().getResource("/ui/main.fxml");

    FXMLLoader loader = new FXMLLoader(xmlResource);
    loadControllers(loader);
    
    Pane gp = loader.load();

    Scene scene = new Scene(gp, 1138, 823);
    gameController.setScene(scene);
    
    URL cssResource = getClass().getResource("/ui/css/main.css");
    scene.getStylesheets().add(cssResource.toString());

    initialize(scene);
    
    stage.setScene(scene);
    stage.show();        
  }

  /**
   * Call initializing functions at the beginning of the game
   */
  private void initialize(Scene scene) throws IOException, ClassNotFoundException {
    gameController.setUsername(scene, guiPlayer.getColor());
    setAvailableTerritories(scene, guiPlayer.getTerritoryNames()); 
    gameController.updateTerritoryColors();
    gameController.setPlacementPaneLabels();
    gameController.setHint();
  }
  
  
  private void loadControllers(FXMLLoader loader) {
    HashMap<Class<?>, Object> controllers = new HashMap<>();    
    controllers.put(GameController.class, gameController);
    loader.setControllerFactory((c) -> {
      return controllers.get(c);
    });    
  }

  /**
   * Set available territories based on game ppl
   */
  private void setAvailableTerritories(Scene scene, Set<String> names) {
    Set<Node> nodes = scene.getRoot().lookupAll("Button");
    
    nodes.stream().filter(node -> (node.getId() != null))
      .filter(node -> ((Button) node).getId().toString().endsWith("Territory"))
      .filter(node -> !names.contains(((Button)node).getText())).forEach(node -> {
        node.setVisible(false);
        });
  }

}
