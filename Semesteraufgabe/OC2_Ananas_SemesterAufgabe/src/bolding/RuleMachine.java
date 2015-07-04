package bolding;

import jnibwapi.Unit;


import java.util.ArrayList;
import java.util.Arrays;
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



    public double[] calcFlockVectorToPos(Unit ownUnit,  HashSet<Unit> ownUnits, int[] pos){
        //rule 1
        double[] vector_ruleOne = calculateVector(pos[0],pos[1], ownUnit.getPosition().getPX(),ownUnit.getPosition().getPY());

        //rule 2 not needed

        //rule 3
        double[] vector_ruleThree = moveToCentroidColumnFormation(ownUnit, ownUnits);

        //rule 4
        double[] vector_ruleFour = moveToCentroidOfLineFormation(ownUnit, ownUnits);

        double[] ownUnitPos = new double[]{ownUnit.getX(), ownUnit.getY()};

        double final_vector_x = ownUnitPos[0] + pSet.getW1() * vector_ruleOne[0] + pSet.getW3() * vector_ruleThree[0] + pSet.getW4() * vector_ruleFour[0];
        double final_vector_y = ownUnitPos[1] + pSet.getW1() * vector_ruleOne[1] + pSet.getW3() * vector_ruleThree[1] + pSet.getW4() * vector_ruleFour[1];

        double[] final_vector = {final_vector_x,final_vector_y};

        //if(vector_ruleThree[0] != vector_ruleFour[0] || vector_ruleThree[1] != vector_ruleFour[1])
        //System.out.println("Marine " + ownUnit.getID() + ": V1:" + Arrays.toString(vector_ruleOne)+" V3:" + Arrays.toString(vector_ruleThree) +" V4:" + Arrays.toString(vector_ruleFour) + " Final:" +  Arrays.toString(final_vector));
        return final_vector;
    }


    public double[] calcFlockVectorToEnemy(Unit ownUnit,  HashSet<Unit> ownUnits, Unit enemy){
        //rule 1
        double[] vector_ruleOne = moveToEnemy(ownUnit, enemy);

        //rule 2 not needed

        //rule 3
        double[] vector_ruleThree = moveToCentroidColumnFormation(ownUnit, ownUnits);

        //rule 4
        double[] vector_ruleFour = moveToCentroidOfLineFormation(ownUnit, ownUnits);

        double[] ownUnitPos = new double[]{ownUnit.getX(), ownUnit.getY()};

        double final_vector_x = ownUnitPos[0] + pSet.getW1() * vector_ruleOne[0] + pSet.getW3() * vector_ruleThree[0] + pSet.getW4() * vector_ruleFour[0];
        double final_vector_y = ownUnitPos[1] + pSet.getW1() * vector_ruleOne[1] + pSet.getW3() * vector_ruleThree[1] + pSet.getW4() * vector_ruleFour[1];

        double[] final_vector = {final_vector_x,final_vector_y};

        //if(vector_ruleThree[0] != vector_ruleFour[0] || vector_ruleThree[1] != vector_ruleFour[1])
            //System.out.println("Marine " + ownUnit.getID() + ": V1:" + Arrays.toString(vector_ruleOne)+" V3:" + Arrays.toString(vector_ruleThree) +" V4:" + Arrays.toString(vector_ruleFour) + " Final:" +  Arrays.toString(final_vector));
        return final_vector;
    }

    // ################## FOR RULE ONE ########################################
    public double[] moveToEnemy(Unit ownUnit, Unit target){

        int x = ownUnit.getX();
        int y = ownUnit.getY();

        int enemy_x = target.getX();
        int enemy_y = target.getY();

        int vector_x = enemy_x - x;
        int vector_y = enemy_y - y;

        //System.out.println("enemy: (" + vector_x + ", " + vector_y + ")");
        return new double[] {vector_x,vector_y};
    }


    // ################## FOR RULE THREE ######################################
    public double[] moveToCentroidColumnFormation(Unit ownUnit, HashSet<Unit> units){
        double[] result = {0.0,0.0};
        //1.Step find all my Neighbors within the range of r_sig
        ArrayList<Unit> myNeighbors = findMyNeighbors(ownUnit, units);

        if(myNeighbors.size() <= 0)
            // no need to go on
            return result;

        //2.Step create all the sets of Characters ( S_j )
        ArrayList<ArrayList<Unit>> setOfSets = createSetsOfCharactersForRuleThree(ownUnit,myNeighbors);

        //3.Step find the best cohesion vector
        double[] maxCohDelta = findMaxCohVector(ownUnit, setOfSets, calculateCentroid(myNeighbors));

        //4.Step calculate a separation Delta in order to diversify the resultant formation
        double[] sepDelta = calculateSeparationDelta(ownUnit, myNeighbors);


        //Last step add both results from 3.Step and 4.Step and we are finished
        result = addVector(maxCohDelta,sepDelta);


        //System.out.println("centerCol: (" + result[0] + ", " + result[1] + ")");
        return result;
    }


    private ArrayList<Unit> findMyNeighbors(Unit ownUnit, HashSet<Unit> units){
        ArrayList<Unit> myNeighbors = new ArrayList<Unit>();
        double distance = 0;
        for (Unit unit : units){
            distance = getDistance(ownUnit, unit);
            if(distance <= pSet.getRangeOfNeighborhood() && unit.getID() != ownUnit.getID())
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
    public double[] moveToCentroidOfLineFormation(Unit ownUnit, HashSet<Unit> units){
        double[] result = {0.0,0.0};

        //1.Step find all my Neighbors within the range of r_sig
        ArrayList<Unit> myNeighbors = findMyNeighbors(ownUnit, units);

        if(myNeighbors.size() <= 0)
            // no need to go on
            return result;

        //2.Step create all the sets of Characters ( S_j )
        ArrayList<ArrayList<Unit>> setOfSets = createSetsOfCharactersForRuleFour(ownUnit, myNeighbors);


        //3.Step find the best cohesion vector
        double[] maxCohDelta = findMaxCohVector(ownUnit, setOfSets,calculateCentroid(myNeighbors));


        //4.Step calculate a separation Delta in order to diversify the resultant formation
        double[] sepDelta = calculateSeparationDelta(ownUnit, myNeighbors);

        //Last step add both results from 3.Step and 4.Step and we are finished
        result = addVector(maxCohDelta, sepDelta);

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

    private double[] findMaxCohVector(Unit ownUnit,ArrayList<ArrayList<Unit>> setOfSets,double[] centroidPos){
        double maxValue = 0.;
        double result;

        double[] winningChoesionDelta = {0,0};

        // search through all sets of charecters
        for (ArrayList<Unit> S: setOfSets){

            // search the spesific S_j set
            for (Unit neighborInS: S){

                double[] cohesionDelta = caclulateCohesionDelta(centroidPos,new double[]{neighborInS.getX(), neighborInS.getY()});
                double[] tempVector = addVector(new double[]{ownUnit.getX(),ownUnit.getY()},cohesionDelta);
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
    private double[] calculateVector(double pos_one_x, double pos_one_y, double pos_two_x, double pos_two_y){
        double diffX = pos_one_x - pos_two_x;
        double diffY = pos_one_y - pos_two_y;
        return new double[]{diffX,diffY};
    }

    private double[] addVector(double[] vector_one, double[] vector_two){
        double x = vector_one[0] + vector_two[0];
        double y = vector_one[1] + vector_two[1];
        return new double[]{x,y};
    }

    private double calcVectorLength(double vector_x, double vectro_y){
        double result = Math.pow(vector_x, 2) + Math.pow(vectro_y, 2);
        return Math.sqrt(result);
    }

    private double[] calculateCentroid(ArrayList<Unit> neighbors){
        double[] vector_sum = {0,0};

        for(Unit neighbor: neighbors){
            double[] currentVector = {neighbor.getX(),neighbor.getY()};
            vector_sum = addVector(vector_sum,currentVector);
        }

        double centroid_x = (1.0/neighbors.size()) * vector_sum[0];
        double centroid_y = (1.0/neighbors.size()) * vector_sum[1];
        return new double[]{centroid_x,centroid_y};
    }

    private double[] calculateSeparationDelta(Unit ownUnit, ArrayList<Unit> myNeighbors){
        double[] seperationDelta = {0,0};
        double[] temp = {0,0};
        for(Unit neighbor: myNeighbors){
            double[] currentV = calculateVector(neighbor.getX(),neighbor.getY(),ownUnit.getX(),ownUnit.getY());
            temp = addVector(temp,currentV);
        }
        seperationDelta[0] = (-1.)* temp[0];
        seperationDelta[1] = (-1.)* temp[1];
        return seperationDelta;
    }

    private double[] caclulateCohesionDelta(double[] centroidPos, double[] poiPos){
        double cohesionDelta_x = centroidPos[0] - poiPos[0];
        double cohesionDelta_y = centroidPos[1] - poiPos[1];
        return new double[]{cohesionDelta_x,cohesionDelta_y};
    }
}
