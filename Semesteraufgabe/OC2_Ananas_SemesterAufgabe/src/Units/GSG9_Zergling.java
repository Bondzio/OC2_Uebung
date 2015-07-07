package Units;

import AI.AnanasAI;
import Common.CommonFunctions;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.HashSet;

/**
 * Created by Rolle on 07.07.2015.
 */
public class GSG9_Zergling implements IMyUnit{

    final private JNIBWAPI bwapi;
    final private Unit unit;
    private int mission;



    public GSG9_Zergling(Unit unit, JNIBWAPI bwapi,int mission) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.mission = mission;
    }

    public void step() {

        Position targetPosition = unit.getTargetPosition();
        bwapi.drawLine(unit.getPosition(), targetPosition, BWColor.Green, false);


        specialMissions(mission);

    }

    public void specialMissions(int mission){
        Position[] hatcheries = getEnemyHatcheries();

        if (mission == 1 || mission == 2) {
            if (unit.isIdle()) {
                if (bwapi.getEnemyUnits().isEmpty()) {
                    unit.attack(hatcheries[mission-1], false);
                }
                else {
                    for (Unit enemy : bwapi.getEnemyUnits()) {
                        unit.attack(enemy.getPosition(), false);
                        break;
                    }
                }
            }
        }
    }


    private Position[] getEnemyHatcheries(){
        Position[] redHatcheries = new Position[] {new Position(516, 368), new Position(516, 2704)};
        Position[] blueHatcheries = new Position[] {new Position(3420,368),  new Position(3420, 2704)};

        if(bwapi.getSelf().getID() == 0)
            return blueHatcheries;
        else
            return redHatcheries;
    }

    private Unit getClosestEnemyZergHatchery() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : AnanasAI.enemyUnits) {
            double distance = CommonFunctions.getDistanceBetweenUnits(unit,enemy);
            if (enemy.getType() == UnitType.UnitTypes.Zerg_Hatchery) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = enemy;
                }
            }
        }

        return result;
    }

    private Unit getClosestFriendlyZergHatchery() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (IMyUnit myUnit : AnanasAI.myUnits) {
            double distance = CommonFunctions.getDistanceBetweenUnits(unit,myUnit.getUnit());
            if (myUnit instanceof Hatchery) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = myUnit.getUnit();
                }
            }
        }

        return result;
    }


    public Unit getUnit(){
        return this.unit;
    }



}
