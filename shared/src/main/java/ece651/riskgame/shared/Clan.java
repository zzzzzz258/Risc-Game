package ece651.riskgame.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a player.
 * Keep a list of territory occupied by this Clan
 */
public class Clan implements Serializable {
    private final List<Territory> occupies;
    private int maxTechLevel;
    private Resource resource;
    static public int[] COST = new int[] {0, 50, 75, 125, 200, 300};

    public Clan() {
      this(new ArrayList<>());
    }

    public Clan(List<Territory> occupies) {
      this.occupies = occupies;
      this.maxTechLevel = 1;
      this.resource = new Resource(new int[] {40, 100});
    }

    public Clan(int maxTechLevel, Resource resource) {
        this.occupies = new ArrayList<>();
        this.maxTechLevel = maxTechLevel;
        this.resource = resource;
    }

    public int getMaxTechLevel() {
        return maxTechLevel;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void addTerritory(Territory t) {
        occupies.add(t);
    }

    public boolean isActive() {
        return occupies.size() != 0;
    }

    public List<Territory> getOccupies() {
        return occupies;
    }

    /**
     * @param name of territory
     * @return true if succeed
     */
    public boolean occupyTerritory(String name) {
        for (Territory territory : occupies) {
            if (territory.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void removeTerritory(Territory t) {
        occupies.remove(t);
    }

    public void upgradeLevel() {
        resource.costGold(COST[maxTechLevel]);
        maxTechLevel ++;
    }

    public void getTerritoryProduction() {
        for (Territory t : occupies) {
            resource.addResource(t.getProduction());
        }
    }
}
