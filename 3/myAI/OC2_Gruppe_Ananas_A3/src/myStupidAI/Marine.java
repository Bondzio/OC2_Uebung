package myStupidAI;

import jnibwapi.JNIBWAPI;
import jnibwapi.model.Unit;
import jnibwapi.types.WeaponType;


import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Stefan Rudolph on 18.02.14.
 */
public class Marine {

    final private JNIBWAPI bwapi;
    private final HashSet<Unit> enemyUnits;
    final private Unit unit;
    private int id;

    public Marine(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits, int id) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.id = id;
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
    
    private void move(Unit target,HashSet<Marine> marines){
    	//TODO: Implement the flocking behavior in this method.
    	//rule 1
        int[] vector_ruleOne = moveToEnemy(target);



        int final_vector_x = vector_ruleOne[0];
        int final_vector_y = vector_ruleOne[1];

        int myCurrentX = unit.getX();
        int myCurrentY = unit.getY();
        bwapi.move(unit.getID(), (myCurrentX + final_vector_x), (myCurrentY + final_vector_y)); // old version
    }

    private int[] moveToEnemy(Unit target){
        //Position myPosition = unit.getPosition();
        int x = unit.getX();
        int y = unit.getY();

        //Position enemyPostition = target.getPosition();
        int enemy_x = target.getX();
        int enemy_y = target.getY();

        int vector_x = enemy_x - x;
        int vector_y = enemy_y - y;

        return new int[] {vector_x,vector_y};
    }

    private void moveToCentroidColumnFormation(HashSet<Marine> marines){
        double rangeOfNeighborhood = 10;
        int widthOfColumnFormation = 5;

        ArrayList<Unit> myNeighbors = new ArrayList<Unit>();
        double distance =0;
        for (Marine marine: marines){
            Unit m = marine.getUnit();
            distance = getDistance(m);
            if(distance <= rangeOfNeighborhood)
                myNeighbors.add(m);
        }

        ArrayList<ArrayList<Unit>> setOfSets = new ArrayList<ArrayList<Unit>>();

        double topCirclePos = unit.getY() + rangeOfNeighborhood;
        double bottomCirclePos = unit.getY() - rangeOfNeighborhood;
        double currentFramePos = topCirclePos;
        while(currentFramePos > bottomCirclePos){
            ArrayList<Unit> set = new ArrayList<>();
            for(Unit neighbor: myNeighbors){
                if(neighbor.getY()<= currentFramePos && neighbor.getY()> (currentFramePos - widthOfColumnFormation))
                    set.add(neighbor);
            }
            setOfSets.add(set);
            currentFramePos -= widthOfColumnFormation;
        }



    }


    private double getDistanceToNeighbor(int neighborpos_x, int neighborpos_y){
        int diffX = unit.getX() - neighborpos_x;
        int diffY = unit.getY() - neighborpos_y;
        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);
        return Math.sqrt(result);
    }

    private int[] calculateCentroid(ArrayList<int[]> neighbors){
        int vector_sumX = 0;
        int vector_sumY = 0;
        for(int[] neighbor: neighbors){
            vector_sumX += neighbor[0];
            vector_sumY += neighbor[1];
        }

        int centroid_x = (1/neighbors.size()) * vector_sumX;
        int centroid_y = (1/neighbors.size()) * vector_sumY;
        return new int[]{centroid_x,centroid_y};
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
