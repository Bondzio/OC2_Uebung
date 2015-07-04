package Units;

import AI.AnanasAI;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Zergling implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;
    private final RuleMachine ruleMachine;
    private int mission;


    private Position topRightHatchery = new Position(3520, 368);
    private Position botRightHatchery = new Position(3520, 2704);
    private Position topLeftHatchery = new Position(576, 368);
    private Position botLeftHatchery = new Position(576, 2704);

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
        //Unit enemy = getClosestEnemy();
        //move(enemy);

        Position targetPosition = unit.getTargetPosition();
        bwapi.drawLine(unit.getPosition(), targetPosition, BWColor.Green, false);

        if (mission == 1) {
            specialMission(mission);
        }
        else if (mission == 2) {
            specialMission(mission);
        }

//        String tP = "X:" + Integer.toString(targetPosition.getPX()) + " | " + "Y:" + Integer.toString(targetPosition.getPY());
//
//        Unit closestEnemyZergHatchery = getClosestEnemyZergHatchery();
//        Position closestEnemyZergHatcheryPosition = closestEnemyZergHatchery.getPosition();
//        closestEnemyZergHatcheryPosition.makeValid();
//        String cEZHP = "X:" + Integer.toString(closestEnemyZergHatcheryPosition.getPX()) + " | " + "Y:" + Integer.toString(closestEnemyZergHatcheryPosition.getPY());
//
//        Unit closestFriendlyZergHatchery = getClosestFriendlyZergHatchery();
//        Position closestFriendlyZergHatcheryPosition = closestFriendlyZergHatchery.getPosition();
//        closestFriendlyZergHatcheryPosition.makeValid();
//        String cFZHP = "X:" + Integer.toString(closestFriendlyZergHatcheryPosition.getPX()) + " | " + "Y:" + Integer.toString(closestFriendlyZergHatcheryPosition.getPY());
//
//        if (unit.isMoving()){
//            bwapi.printText(cFZHP);
//            bwapi.printText(cEZHP);
//            bwapi.printText(tP);
//        }
    }

    public void specialMission(int mission){
        if (mission == 1) {
            topRightHatchery.makeValid();
            unit.attack(topRightHatchery, false);
        }
        else if (mission == 2) {
            botRightHatchery.makeValid();
            unit.attack(botRightHatchery, false);
        }
    }

    public void move(Unit target){
        HashSet<Unit> units = new HashSet<>();
        for(IMyUnit myUnit: AnanasAI.myUnits){
            if(myUnit instanceof Zergling)
                units.add(myUnit.getUnit());
        }

        double[] final_vector = ruleMachine.calcFlockVectorToPos(unit,units,new int[]{1,1});


        bwapi.move(unit.getID(), (int) final_vector[0], (int) final_vector[1]);
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
