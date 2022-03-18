/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ece651.riskgame.server;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class AppTest {
  @Test
  public void test_main_IOException() throws IOException, InterruptedException {
    App.main(new String[] { "0" });
    App.main(new String[] { "6" });

    RiskGame riskGame = new RiskGame(1);
    World world = Whitebox.getInternalState(riskGame, "world");
    Board board = Whitebox.getInternalState(world, "board");
    Map<String, Clan> clans = Whitebox.getInternalState(world, "clans");

    Thread th_server = new Thread() {
      @Override()
      public void run() {
        try {
          App.main(new String[] { "2" });
        } catch (Exception e) {

        }
      }
    };

    th_server.start();
    Thread.sleep(100); // this is abit of a hack
    Socket s1 = new Socket("0.0.0.0", 1651);
    assertTrue(s1.isConnected());
    s1.close();

  }
}
