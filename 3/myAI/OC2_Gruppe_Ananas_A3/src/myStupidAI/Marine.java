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

        //rule 3
        int[] vector_ruleThree = moveToCentroidColumnFormation(marines);



        int final_vector_x = vector_ruleOne[0] + vector_ruleThree[0];
        int final_vector_y = vector_ruleOne[1] + vector_ruleThree[1];

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

    private int[] moveToCentroidColumnFormation(HashSet<Marine> marines){
        double rangeOfNeighborhood = 300;
        double widthOfColumnFormation = (2*rangeOfNeighborhood)/4;

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
        double currentFramePosTop = topCirclePos;
        double currentFramePosBottom = 0;
        while(currentFramePosTop > bottomCirclePos){
            currentFramePosBottom = currentFramePosTop - widthOfColumnFormation;
            ArrayList<Unit> set = new ArrayList<>();
            for(Unit neighbor: myNeighbors){
                if(neighbor.getY()<= currentFramePosTop && neighbor.getY()> currentFramePosBottom)
                    set.add(neighbor);
            }
            setOfSets.add(set);
            currentFramePosTop -= widthOfColumnFormation;
        }

        int[] maxCohDelta = findMaxCohVector(setOfSets,calculateCentroid(myNeighbors));
        int[] sepDelta = calculateSeparationDelta(myNeighbors);
        int[] result = addVector(maxCohDelta,sepDelta);

        return result;
    }


    private int[] findMaxCohVector(ArrayList<ArrayList<Unit>> setOfSets,int[] centroidPos){
        double maxValue = 0;
        double result = 0;
        int[] winningChoesionDelta = null;
        for (ArrayList<Unit> S: setOfSets){
            result = 0;
            for (Unit neighborInS: S){
                int[] tempVector = {0,0};
                int[] cohesionDelta = caclulateCohesionDelta(centroidPos,new int[]{neighborInS.getX(), neighborInS.getY()});
                tempVector[0] = unit.getX() + cohesionDelta[0];
                tempVector[1] = unit.getY() + cohesionDelta[1];
                result = S.size() / calcVectorLength(tempVector[0],tempVector[1]);
                if(result > maxValue){
                    maxValue = result;
                    winningChoesionDelta = cohesionDelta;
                }
            }
        }

        return winningChoesionDelta;
    }


    private double calcVectorLength(int vector_x, int vectro_y){
        double result = Math.pow(vector_x, 2) + Math.pow(vectro_y, 2);
        return Math.sqrt(result);
    }

    private int[] caclulateCohesionDelta(int[] centroidPos, int[] poiPos){
        int cohesionDelta_x = centroidPos[0] - poiPos[0];
        int cohesionDelta_y = centroidPos[1] - poiPos[1];
        return new int[]{cohesionDelta_x,cohesionDelta_y};
    }

    private int[] calculateCentroid(ArrayList<Unit> neighbors){
        int vector_sumX = 0;
        int vector_sumY = 0;
        for(Unit neighbor: neighbors){
            vector_sumX += neighbor.getX();
            vector_sumY += neighbor.getY();
        }

        int centroid_x = (1/neighbors.size()) * vector_sumX;
        int centroid_y = (1/neighbors.size()) * vector_sumY;
        return new int[]{centroid_x,centroid_y};
    }

    private int[] calculateSeparationDelta(ArrayList<Unit> myNeighbors){
        int[] seperationDelta = {0,0};
        int[] temp = {0,0};
        for(Unit neighbor: myNeighbors){
            int [] currentV = calculateVector(neighbor.getX(),neighbor.getY(),unit.getX(),unit.getY());
            temp = addVector(temp,currentV);
        }
        seperationDelta[0] = - temp[0];
        seperationDelta[1] = - temp[1];
        return seperationDelta;
    }

    /**
     *  One - Two
     */
    private int[] calculateVector(int pos_one_x, int pos_one_y, int pos_two_x, int pos_two_y){
        int diffX = pos_one_x - pos_two_x;
        int diffY = pos_one_y - pos_two_y;
        return new int[]{diffX,diffY};
    }

    private int[] addVector(int[] vector_one, int[] vector_two){
        int x = vector_one[0] + vector_two[0];
        int y = vector_one[1] + vector_two[1];
        return new int[]{x,y};
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
