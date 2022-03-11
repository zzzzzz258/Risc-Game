package ece651.riskgame.client;

import java.util.Map;

import ece651.riskgame.shared.Board;
import ece651.riskgame.shared.Clan;
import ece651.riskgame.shared.GameInfo;
import ece651.riskgame.shared.Territory;

public class GameTextView implements GameView {
  private final Board theBoard;
  private final Map<String, Clan> clans;
  /**
   * Constructs a GameTextView, given the board it will display
   * @param toDisplay is the Board to display  
   */
  public GameTextView(GameInfo toDisplay) {
    this.theBoard = toDisplay.getBoard();
    this.clans = toDisplay.getPlayers();
  }

  protected String displayTerritory(Territory t) {
    StringBuilder line = new StringBuilder();
    line.append(t.getName());
    line.append(" (next to: ");
    String sep = "";
    for (Territory neighbour: theBoard.getNeighbors(t)) {
      line.append(sep);
      line.append(neighbour.getName());
      sep = ", ";
    }
    line.append(")\n");
    return line.toString();
  }
  /**
   * Display the territories of a clan in text
   * @return a string that represents the clan  
   */
  protected String displayClan(String color) {
    StringBuilder ans = new StringBuilder();
    ans.append(color);
    ans.append(" player:\n");
    ans.append(getDelimiter());
    Clan currentClan = clans.get(color);
    for (Territory occupy: currentClan.getOccupies()) {
      ans.append(displayTerritory(occupy));
    }
    return ans.toString();
  }
  /**
   * Display the game in text
   * @return a string that represents the board  
   */
  public String displayGame() {
    StringBuilder ans = new StringBuilder();
    for (String color: clans.keySet()) {
      ans.append(displayClan(color));
    }
    return ans.toString();
  }
  
  public static String getDelimiter() {
    return "-------------\n";
  }
}