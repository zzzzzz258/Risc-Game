package ece651.riskgame.shared;

import java.io.Serializable;

public class UpgradeUnitAction implements Action, Serializable {
    private final String territoryName;
    private final int baseLevel;
    private final int targetLevel;
    private final int num;

    public UpgradeUnitAction(String territoryName, int baseLevel, int targetLevel, int num) {
        this.territoryName = territoryName;
        this.baseLevel = baseLevel;
        this.targetLevel = targetLevel;
        this.num = num;
    }

    @Override
    public void apply(Actable world) {
        int cost = Unit.getLevelUpCost(baseLevel, targetLevel) * num;
        Clan clan = world.getClans().get(world.getTerritoryOwnership(territoryName));
        clan.getResource().costFood(cost);
        Territory territory = world.getBoard().getTerritory(territoryName);
        Unit baseUnit = new BasicUnit(num, baseLevel);
        Unit targetUnit = new BasicUnit(num, targetLevel);
        territory.decUnit(baseUnit);
        territory.addUnit(targetUnit);
    }

    @Override
    public void clientApply(Actable game) {
        apply(game);
    }
}