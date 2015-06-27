package myStupidAI.bolding;

import jnibwapi.model.Unit;
import myStupidAI.Marine;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Rolle on 27.06.2015.
 */
public class RuleMachine {


    private final ParamSet pSet;

    private final double widthOfColumnFormation; // r_col from the paper
    private final double heightOfLineFormation; // r_lin from the paper



    public RuleMachine(ParamSet pSet) {
        this.pSet = pSet;
        this.widthOfColumnFormation = (2*pSet.getRangeOfNeighborhood())/pSet.getCoulumFormationFileNumber();
        this.heightOfLineFormation = (2*pSet.getRangeOfNeighborhood())/pSet.getLineFormationRankNumber();
    }


    public double[] calcFinalVector(int[] ownUnitPos, int[] vector_rule_one, int[] vector_rule_three,int[] vector_rule_four){

        double final_vector_x = ownUnitPos[0] + pSet.getW1() * vector_rule_one[0] + pSet.getW3() * vector_rule_three[0] + pSet.getW4() * vector_rule_four[0];
        double final_vector_y = ownUnitPos[1] + pSet.getW1() * vector_rule_one[1] + pSet.getW3() * vector_rule_three[1] + pSet.getW4() * vector_rule_four[1];

        return new double[]{final_vector_x,final_vector_y};
    }

    // ################## FOR RULE ONE ########################################
    public int[] moveToEnemy(Unit self, Unit target){

        int x = self.getX();
        int y = self.getY();

        int enemy_x = target.getX();
        int enemy_y = target.getY();

        int vector_x = enemy_x - x;
        int vector_y = enemy_y - y;

        //System.out.println("enemy: (" + vector_x + ", " + vector_y + ")");
        return new int[] {vector_x,vector_y};
    }


    // ################## FOR RULE THREE ######################################
    public int[] moveToCentroidColumnFormation(Unit ownUnit, HashSet<Unit> units){

        //1.Step find all my Neighbors within the range of r_sig
        ArrayList<Unit> myNeighbors = findMyNeighbors(ownUnit, units);

        //2.Step create all the sets of Characters ( S_j )
        ArrayList<ArrayList<Unit>> setOfSets = createSetsOfCharactersForRuleThree(ownUnit,myNeighbors);

        //3.Step find the best cohesion vector
        int[] maxCohDelta = findMaxCohVector(ownUnit, setOfSets, calculateCentroid(myNeighbors));

        //4.Step calculate a separation Delta in order to diversify the resultant formation
        int[] sepDelta = calculateSeparationDelta(ownUnit, myNeighbors);
        //int[] sepDelta = {0,0};

        //Last step add both results from 3.Step and 4.Step and we are finished
        int[] result = addVector(maxCohDelta,sepDelta);


        //System.out.println("centerCol: (" + result[0] + ", " + result[1] + ")");
        return result;
    }

    private ArrayList<Unit> findMyNeighbors(Unit ownUnit, HashSet<Unit> units){
        ArrayList<Unit> myNeighbors = new ArrayList<Unit>();
        double distance = 0;
        for (Unit unit : units){
            distance = getDistance(ownUnit, unit);
            if(distance <= pSet.getRangeOfNeighborhood())
                myNeighbors.add(unit);
        }
        return myNeighbors;
    }

    private ArrayList<ArrayList<Unit>> createSetsOfCharactersForRuleThree(Unit ownUnit, ArrayList<Unit> myNeighbors){
        ArrayList<ArrayList<Unit>> setOfSets = new ArrayList<ArrayList<Unit>>();

        // determin the boarder to look in
        double topCirclePos = ownUnit.getY() - pSet.getRangeOfNeighborhood();
        double bottomCirclePos = ownUnit.getY() + pSet.getRangeOfNeighborhood();

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
    public int[] moveToCentroidOfLineFormation(Unit ownUnit, HashSet<Unit> units){
        //1.Step find all my Neighbors within the range of r_sig
        ArrayList<Unit> myNeighbors = findMyNeighbors(ownUnit, units);


        //2.Step create all the sets of Characters ( S_j )
        ArrayList<ArrayList<Unit>> setOfSets = createSetsOfCharactersForRuleFour(ownUnit, myNeighbors);


        //3.Step find the best cohesion vector
        int[] maxCohDelta = findMaxCohVector(ownUnit, setOfSets,calculateCentroid(myNeighbors));


        //4.Step calculate a separation Delta in order to diversify the resultant formation
        int[] sepDelta = calculateSeparationDelta(ownUnit, myNeighbors);
        //int[] sepDelta = {0,0};

        //Last step add both results from 3.Step and 4.Step and we are finished
        int[] result = addVector(maxCohDelta, sepDelta);

        //System.out.println("centerLine: (" + result[0] + ", " + result[1] + ")");
        return result;
    }

    private ArrayList<ArrayList<Unit>> createSetsOfCharactersForRuleFour(Unit ownUnit, ArrayList<Unit> myNeighbors){
        ArrayList<ArrayList<Unit>> setOfSets = new ArrayList<ArrayList<Unit>>();

        // determin the boarder to look in
        double rightCirclePos = ownUnit.getX() + pSet.getRangeOfNeighborhood();
        double leftCirclePos = ownUnit.getX() - pSet.getRangeOfNeighborhood();

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

    private int[] findMaxCohVector(Unit ownUnit,ArrayList<ArrayList<Unit>> setOfSets,int[] centroidPos){
        double maxValue = 0.;
        double result;

        int[] winningChoesionDelta = {0,0};

        // search through all sets of charecters
        for (ArrayList<Unit> S: setOfSets){

            // search the spesific S_j set
            for (Unit neighborInS: S){

                int[] cohesionDelta = caclulateCohesionDelta(centroidPos,new int[]{neighborInS.getX(), neighborInS.getY()});
                int[] tempVector = addVector(new int[]{ownUnit.getX(),ownUnit.getY()},cohesionDelta);
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


    private double getDistance(Unit self, Unit enemy) {
        int myX = self.getX();
        int myY = self.getY();

        int enemyX = enemy.getX();
        int enemyY = enemy.getY();

        int diffX = myX - enemyX;
        int diffY = myY - enemyY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }

    public ParamSet getpSet() {
        return pSet;
    }

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

    private int[] calculateSeparationDelta(Unit ownUnit, ArrayList<Unit> myNeighbors){
        int[] seperationDelta = {0,0};
        int[] temp = {0,0};
        for(Unit neighbor: myNeighbors){
            int [] currentV = calculateVector(neighbor.getX(),neighbor.getY(),ownUnit.getX(),ownUnit.getY());
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
