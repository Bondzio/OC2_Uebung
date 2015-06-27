package myStupidAI;

import jnibwapi.JNIBWAPI;
import jnibwapi.model.Unit;
import jnibwapi.types.WeaponType;
import myStupidAI.bolding.RuleMachine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Stefan Rudolph on 18.02.14.
 */
public class Marine {

    private final JNIBWAPI bwapi;
    private final HashSet<Unit> enemyUnits;
    private final  Unit unit;
    private int id;

    RuleMachine ruleMachine;


    public Marine(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits, int id, RuleMachine ruleMachine) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.id = id;
        this.ruleMachine = ruleMachine;
    }
    
    public Marine(){ 
    	this.unit = null;
    	this.bwapi = null;
    	this.enemyUnits = null;
    }


    public void step(HashSet<Marine> marines) {
        Unit target = getClosestEnemy();

        if (unit.getOrderID() != 10 && !unit.isAttackFrame() && !unit.isStartingAttack() && !unit.isAttacking() && target != null) {
            if (bwapi.getWeaponType(WeaponType.WeaponTypes.Gauss_Rifle.getID()).getMaxRange() > getDistance(target) - 20.0) {
                bwapi.attack(unit.getID(), target.getID());
            } else {
            	move(target,marines);
            }
        }
    }
    
    public void move(Unit target,HashSet<Marine> marines){
        HashSet<Unit> units = new HashSet<>();
        for(Marine marine: marines){
            units.add(marine.getUnit());
        }


    	//TODO: Implement the GA
    	//rule 1
        int[] vector_ruleOne = ruleMachine.moveToEnemy(unit, target);

        //rule 2 not needed

        //rule 3
        int[] vector_ruleThree = ruleMachine.moveToCentroidColumnFormation(unit, units);

        //rule 4
        int[] vector_ruleFour = ruleMachine.moveToCentroidOfLineFormation(unit, units);

        int[] ownPos = new int[]{unit.getX(), unit.getY()};
        double[] final_vector = ruleMachine.calcFinalVector(ownPos, vector_ruleOne, vector_ruleThree, vector_ruleFour);


        System.out.println("Marine "+ id + ": V1:" + Arrays.toString(vector_ruleOne)+" V3:" + Arrays.toString(vector_ruleThree) +" V4:" + Arrays.toString(vector_ruleFour) + " Final:" +  Arrays.toString(final_vector));
        bwapi.move(unit.getID(), (int) final_vector[0], (int) final_vector[1] );
    }


    private Unit getClosestEnemy() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Unit enemy : enemyUnits) {
            double distance = getDistance(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                result = enemy;
            }
        }

        return result;
    }

    private double getDistance(Unit enemy) {
        int myX = unit.getX();
        int myY = unit.getY();

        int enemyX = enemy.getX();
        int enemyY = enemy.getY();

        int diffX = myX - enemyX;
        int diffY = myY - enemyY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }

    public int getID() {
        return unit.getID();
    }

    public Unit getUnit(){ return this.unit; }





}
