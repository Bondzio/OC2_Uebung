package myStupidAI;

import jnibwapi.JNIBWAPI;
import jnibwapi.model.Unit;
import jnibwapi.types.WeaponType;


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

    private final double rangeOfNeighborhood = 50; // r_sig from the paper
    private final int coulumFormationFileNumber = 4;
    private final double widthOfColumnFormation = (2*rangeOfNeighborhood)/coulumFormationFileNumber; // r_col from the paper

    private final int lineFormationRankNumber = 4;
    private final double heightOfLineFormation = (2*rangeOfNeighborhood)/lineFormationRankNumber; // r_lin from the paper




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

        //rule 2 not needed

        //rule 3
        int[] vector_ruleThree = moveToCentroidColumnFormation(marines);

        //rule 4
        int[] vector_ruleFour = moveToCentroidOfLineFormation(marines);


        int final_vector_x = vector_ruleOne[0] + vector_ruleThree[0] + vector_ruleFour[0];
        int final_vector_y = vector_ruleOne[1] + vector_ruleThree[1] + vector_ruleFour[1];

        int myCurrentX = unit.getX();
        int myCurrentY = unit.getY();
        bwapi.move(unit.getID(), (myCurrentX + final_vector_x), (myCurrentY + final_vector_y));
    }

    // ################## FOR RULE ONE ########################################
    private int[] moveToEnemy(Unit target){
        int x = unit.getX();
        int y = unit.getY();

        int enemy_x = target.getX();
        int enemy_y = target.getY();

        int vector_x = enemy_x - x;
        int vector_y = enemy_y - y;

        return new int[] {vector_x,vector_y};
    }


    // ################## FOR RULE THREE ######################################
    private int[] moveToCentroidColumnFormation(HashSet<Marine> marines){

        //1.Step find all my Neighbors within the range of r_sig
        ArrayList<Unit> myNeighbors = findMyNeighbors(marines);

        //2.Step create all the sets of Characters ( S_j )
        ArrayList<ArrayList<Unit>> setOfSets = createSetsOfCharactersForRuleThree(myNeighbors);

        //3.Step find the best cohesion vector
        int[] maxCohDelta = findMaxCohVector(setOfSets,calculateCentroid(myNeighbors));

        //4.Step calculate a separation Delta in order to diversify the resultant formation
        int[] sepDelta = calculateSeparationDelta(myNeighbors);

        //Last step add both results from 3.Step and 4.Step and we are finished
        int[] result = addVector(maxCohDelta,sepDelta);

        return result;
    }

    private ArrayList<Unit> findMyNeighbors(HashSet<Marine> marines){
        ArrayList<Unit> myNeighbors = new ArrayList<Unit>();
        double distance =0;
        for (Marine marine: marines){
            Unit m = marine.getUnit();
            distance = getDistance(m);
            if(distance <= rangeOfNeighborhood)
                myNeighbors.add(m);
        }
        return myNeighbors;
    }

    private ArrayList<ArrayList<Unit>> createSetsOfCharactersForRuleThree(ArrayList<Unit> myNeighbors){
        ArrayList<ArrayList<Unit>> setOfSets = new ArrayList<ArrayList<Unit>>();

        // determin the boarder to look in
        double topCirclePos = unit.getY() - rangeOfNeighborhood;
        double bottomCirclePos = unit.getY() + rangeOfNeighborhood;

        // The Frame represents the red rectangle or one of the scanning windows, from the paper
        double currentFramePosTop = topCirclePos;
        double currentFramePosBottom;


        while(currentFramePosTop < bottomCirclePos){
            //keep the coulum width
            currentFramePosBottom = currentFramePosTop + widthOfColumnFormation;

            ArrayList<Unit> set = new ArrayList<>();
            // search the frame for neighbors
            for(Unit neighbor: myNeighbors){
                if(currentFramePosTop <= neighbor.getY() && neighbor.getY() < currentFramePosBottom)
                    // add the neighbors to the coresponding S_j set
                    set.add(neighbor);
            }
            //add the S_j set to a collection where all the other S_j sets are kept
            setOfSets.add(set);

            // move the frame downwards
            currentFramePosTop += widthOfColumnFormation;
        }
        return setOfSets;
    }


    // ################## FOR RULE FOUR ######################################
    private int[] moveToCentroidOfLineFormation(HashSet<Marine> marines){
        //1.Step find all my Neighbors within the range of r_sig
        ArrayList<Unit> myNeighbors = findMyNeighbors(marines);


        //2.Step create all the sets of Characters ( S_j )
        ArrayList<ArrayList<Unit>> setOfSets = createSetsOfCharactersForRuleFour(myNeighbors);


        //3.Step find the best cohesion vector
        int[] maxCohDelta = findMaxCohVector(setOfSets,calculateCentroid(myNeighbors));


        //4.Step calculate a separation Delta in order to diversify the resultant formation
        int[] sepDelta = calculateSeparationDelta(myNeighbors);


        //Last step add both results from 3.Step and 4.Step and we are finished
        int[] result = addVector(maxCohDelta, sepDelta);

        return result;
    }

    private ArrayList<ArrayList<Unit>> createSetsOfCharactersForRuleFour(ArrayList<Unit> myNeighbors){
        ArrayList<ArrayList<Unit>> setOfSets = new ArrayList<ArrayList<Unit>>();

        // determin the boarder to look in
        double rightCirclePos = unit.getX() + rangeOfNeighborhood;
        double leftCirclePos = unit.getX() - rangeOfNeighborhood;

        // The Frame represents the red rectangle or one of the scanning windows, from the paper
        double currentFramePosLeft = leftCirclePos;
        double currentFramePosRight;


        while(currentFramePosLeft < rightCirclePos){
            //keep the formation height
            currentFramePosRight = currentFramePosLeft + heightOfLineFormation;

            ArrayList<Unit> set = new ArrayList<>();
            // search the frame for neighbors
            for(Unit neighbor: myNeighbors){
                if( currentFramePosLeft <= neighbor.getX() && neighbor.getX() < currentFramePosRight)
                    // add the neighbors to the coresponding S_j set
                    set.add(neighbor);
            }
            //add the S_j set to a collection where all the other S_j sets are kept
            setOfSets.add(set);

            // move the frame rightwards
            currentFramePosLeft += heightOfLineFormation;
        }
        return setOfSets;
    }

    // ################## FOR RULE THREE & FOUR ##############################

    private int[] findMaxCohVector(ArrayList<ArrayList<Unit>> setOfSets,int[] centroidPos){
        double maxValue = 0.;
        double result;

        int[] winningChoesionDelta = {0,0};

        // search through all sets of charecters
        for (ArrayList<Unit> S: setOfSets){

            // search the spesific S_j set
            for (Unit neighborInS: S){

                int[] cohesionDelta = caclulateCohesionDelta(centroidPos,new int[]{neighborInS.getX(), neighborInS.getY()});
                int[] tempVector = addVector(new int[]{unit.getX(),unit.getY()},cohesionDelta);
                // calc the max
                result = S.size() / calcVectorLength(tempVector[0],tempVector[1]);


                if(result >= maxValue){

                    maxValue = result;
                    // save the best choesionDelta of them all
                    winningChoesionDelta = cohesionDelta;
                }
            }
        }
        return winningChoesionDelta;
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


    // ######################## UTIL ################################
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

    private double calcVectorLength(int vector_x, int vectro_y){
        double result = Math.pow(vector_x, 2) + Math.pow(vectro_y, 2);
        return Math.sqrt(result);
    }

    private int[] calculateCentroid(ArrayList<Unit> neighbors){
        int[] vector_sum = {0,0};

        for(Unit neighbor: neighbors){
            int[] currentVector = {neighbor.getX(),neighbor.getY()};
            vector_sum = addVector(vector_sum,currentVector);
        }

        int centroid_x = (1/neighbors.size()) * vector_sum[0];
        int centroid_y = (1/neighbors.size()) * vector_sum[1];
        return new int[]{centroid_x,centroid_y};
    }

    private int[] calculateSeparationDelta(ArrayList<Unit> myNeighbors){
        int[] seperationDelta = {0,0};
        int[] temp = {0,0};
        for(Unit neighbor: myNeighbors){
            int [] currentV = calculateVector(neighbor.getX(),neighbor.getY(),unit.getX(),unit.getY());
            temp = addVector(temp,currentV);
        }
        seperationDelta[0] = (-1)* temp[0];
        seperationDelta[1] = (-1)* temp[1];
        return seperationDelta;
    }

    private int[] caclulateCohesionDelta(int[] centroidPos, int[] poiPos){
        int cohesionDelta_x = centroidPos[0] - poiPos[0];
        int cohesionDelta_y = centroidPos[1] - poiPos[1];
        return new int[]{cohesionDelta_x,cohesionDelta_y};
    }
}
