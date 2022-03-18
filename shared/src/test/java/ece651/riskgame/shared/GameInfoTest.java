package ece651.riskgame.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GameInfoTest {
  @Test
  public void test_gameinfo() {
    Board b = new Board();
    Map<String, Clan> c = new HashMap<>();
    GameInfo gi = new GameInfo(b, c);

    assertSame(b, gi.getBoard());

    gi.setBoard(null);
    assertSame(null, gi.getBoard());

    Clan c1 = new Clan();
    c1.addTerritory(new BasicTerritory("Beijing"));
    Map<String, Clan> m = new HashMap<String, Clan>();
    m.put("gui", c1);
    gi.setPlayers(m);
    assertEquals(m, gi.getPlayers());
    assertEquals(m, gi.getClans());

    assertEquals(null, gi.getTerritoryOwnership("Shanghai"));
    assertEquals("gui", gi.getTerritoryOwnership("Beijing"));
  }

}
