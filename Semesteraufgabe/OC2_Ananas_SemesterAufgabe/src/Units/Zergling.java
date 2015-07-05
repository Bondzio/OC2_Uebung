package Units;

import AI.AnanasAI;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Zergling implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;
    private final RuleMachine ruleMachine;
    private int mission;

    private HashSet<Unit> units = new HashSet<>();

    public Zergling(Unit unit, JNIBWAPI bwapi, RuleMachine ruleMachine) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.ruleMachine = ruleMachine;
    }

    public Zergling(Unit unit, JNIBWAPI bwapi, RuleMachine ruleMachine, int mission) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.ruleMachine = ruleMachine;
        this.mission = mission;
    }

    public void step() {
        Unit enemy = getClosestEnemy();

        Position targetPosition = unit.getTargetPosition();
        bwapi.drawLine(unit.getPosition(), targetPosition, BWColor.Green, false);

       // move(enemy);

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

    public void move(Unit target){

        units.clear();
        for(IMyUnit myUnit: AnanasAI.myUnits){
            if(myUnit instanceof Zergling)
                units.add(myUnit.getUnit());
        }


        double[] final_vector = ruleMachine.calcFlockVectorToPos(unit,units,new int[]{4000,1000});
        //double[] final_vector = {1000,1000};

        //System.out.println("Z ID:" + unit.getID() + " UnitsSize:" + units.size() + " Vec:" + Arrays.toString(final_vector));
        Position tragetPosition = new Position((int) final_vector[0],(int) final_vector[1]);
        tragetPosition.makeValid();
        unit.move(tragetPosition, false);
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
            double distance = getDistance(enemy);
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
            double distance = getDistance(myUnit.getUnit());
            if (myUnit instanceof Hatchery) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = myUnit.getUnit();
                }
            }
        }

        return result;
    }

    private Unit getClosestEnemy() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : AnanasAI.enemyUnits) {
            double distance = getDistance(enemy);

            if (distance < minDistance) {
                minDistance = distance;
                result = enemy;
            }
        }

        return result;
    }

    private double getDistance(Unit enemy) {
        int myX = unit.getPosition().getPX();
        int myY = unit.getPosition().getPY();
        int enemyX = enemy.getPosition().getPX();
        int enemyY = enemy.getPosition().getPY();
        int diffX = myX - enemyX;
        int diffY = myY - enemyY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }

    public Unit getUnit(){
        return this.unit;
    }
}
