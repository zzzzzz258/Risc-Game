package ece651.riskgame.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClanTest {

    @Test
    void addTerritory() {
        Clan c = new Clan();
        c.addTerritory(new BasicTerritory("A"));
        assertTrue(c.isActive());
        assertEquals(new ArrayList<Territory>(Arrays.asList(new BasicTerritory("A"))), c.getOccupies());
    }

    @Test
    void isActive() {
        Clan c = new Clan();
        assertFalse(c.isActive());
        c.addTerritory(new BasicTerritory("A"));
        assertTrue(c.isActive());
    }

    @Test
    void test_Constructor() {
        List<Territory> l = new ArrayList<>(Arrays.asList(new BasicTerritory("A")));
        Clan c = new Clan(l);
        assertEquals(l, c.getOccupies());
    }

  @Test
  void test_occupyTerritory() {
    List<Territory> l = new ArrayList<>();
    l.add(new BasicTerritory("A"));
    Clan c = new Clan(l);
    assertTrue(c.occupyTerritory("A"));
    assertFalse(c.occupyTerritory("B"));
  }

}
