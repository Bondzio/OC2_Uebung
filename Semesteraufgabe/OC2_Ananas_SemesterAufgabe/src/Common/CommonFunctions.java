package Common;

import AI.AnanasAI;
import Units.Hatchery;
import Units.IMyUnit;
import Units.IMyUnit;
import javafx.geometry.Pos;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.JNIBWAPI;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by Rolle on 07.07.2015.
 */
public class CommonFunctions {

    public static Random r = new Random();
    private static HashSet<Unit> units = new HashSet<>();

    public static Unit getClosestEnemy(Unit friendly) {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : AnanasAI.enemyUnits) {
            double distance = getDistanceBetweenUnits(friendly, enemy);

            if (distance < minDistance) {
                minDistance = distance;
                result = enemy;
            }
        }

        return result;
    }


    public static double getDistanceBetweenUnits(Unit one, Unit two) {
        int myX = one.getPosition().getPX();
        int myY = one.getPosition().getPY();
        int enemyX = two.getPosition().getPX();
        int enemyY = two.getPosition().getPY();
        int diffX = myX - enemyX;
        int diffY = myY - enemyY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }


    public static int getDistianceBetweenPositions(Position one, Position two){
        double[] vec = calculateVector(one.getPX(),one.getPY(),two.getPX(),two.getPY());
        return (int) calcVectorLength(vec[0],vec[1]);
    }


    public static void simpleUnitMove(Unit unitToMove, Position destination){
        unitToMove.move(destination,false);
    }

    public static Position[] getEnemyHatcheries(JNIBWAPI bwapi){
        Position[] redHatcheries = new Position[] {new Position(516, 368), new Position(516, 2704)};
        Position[] blueHatcheries = new Position[] {new Position(3460,368),  new Position(3460, 2704)};
        
        if(bwapi.getSelf().getID() == 0)
            return blueHatcheries;
        else
            return redHatcheries;
    }

    
    
    private Unit getClosestFriendlyZergHatchery(Unit unit) {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (IMyUnit myUnit : AnanasAI.myUnits) {
            double distance = getDistanceBetweenUnits(unit, myUnit.getUnit());
            if (myUnit instanceof Hatchery) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = myUnit.getUnit();
                }
            }
        }

        return result;
    }


    /**
     *  One - Two
     */
    public static double[] calculateVector(double pos_one_x, double pos_one_y, double pos_two_x, double pos_two_y){
        double diffX = pos_one_x - pos_two_x;
        double diffY = pos_one_y - pos_two_y;
        return new double[]{diffX,diffY};
    }

    public static Position getRndPosInDefCircle(Position defencePoint, int defCircleRadius){
        Position defPos = null;
        while(true){
            int new_x = getRndIntInRange(defencePoint.getPX() - defCircleRadius, defencePoint.getPX() + defCircleRadius);
            int new_y = getRndIntInRange(defencePoint.getPY() - defCircleRadius, defencePoint.getPY() + defCircleRadius);
            double[] vec = {new_x - defencePoint.getPX(),new_y - defencePoint.getPY()};
            double length = calcVectorLength(vec[0],vec[1]);
            if(length <= defCircleRadius){
                defPos = new Position(new_x,new_y);
                break;
            }
        }
        return defPos;
    }

    /**
     * to is inclusive
     */
    public static int getRndIntInRange(int from, int to){
        return (from + r.nextInt(to-from + 1));
    }


    public static double calcVectorLength(double vector_x, double vectro_y){
        double result = Math.pow(vector_x, 2) + Math.pow(vectro_y, 2);
        return Math.sqrt(result);
    }

    public static void drawLine(JNIBWAPI bwapi, Unit unit, Position targetPosition, BWColor color){
        bwapi.drawLine(unit.getPosition(), targetPosition, color, false);
    }



}
