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

import static java.util.Arrays.asList;
import static java.util.Collections.min;

/**
 * Created by Rolle on 07.07.2015.
 */
public class CommonFunctions {

    public static Random r = new Random();
    private static HashSet<Unit> units = new HashSet<>();

    public static int getParasitedEnemysCount(JNIBWAPI bwapi) {
        int result = 0;

        for (Unit enemy : bwapi.getEnemyUnits()) {
            if (enemy.isParasited()){
                result++;
            }
        }

        return result;
    }

    public static Unit getClosestEnemy(JNIBWAPI bwapi, Unit friendly) {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : bwapi.getEnemyUnits()) {
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

    public static HashSet<Unit> getScourgesInCastrange(JNIBWAPI bwapi, Unit unit, int castrange) {
        HashSet<Unit> result = new HashSet<>();
        int castrangeInPixel = castrange * 32;

        for (Unit enemy : bwapi.getEnemyUnits()) {
            double distance = getDistanceBetweenUnits(unit, enemy);
            if (enemy.getType() == UnitTypes.Zerg_Scourge) {
                if (distance <= castrangeInPixel) {
                    result.add(enemy);
                }
            }
        }

        return result;
    }

    public static Unit getClosestEnemyZergQueen(Unit unit) {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : AnanasAI.enemyUnits) {
            double distance = getDistanceBetweenUnits(unit, enemy);
            if (enemy.getType() == UnitTypes.Zerg_Queen) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = enemy;
                }
            }
        }

        return result;
    }

    public static Unit getClosestEnemyScourge(Unit unit) {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : AnanasAI.enemyUnits) {
            double distance = getDistanceBetweenUnits(unit, enemy);
            if (enemy.getType() == UnitTypes.Zerg_Scourge) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = enemy;
                }
            }
        }

        return result;
    }

    public static Unit getClosestEnemyZergHatchery(Unit unit) {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit enemy : AnanasAI.enemyUnits) {
            double distance = getDistanceBetweenUnits(unit, enemy);
            if (enemy.getType() == UnitTypes.Zerg_Hatchery) {
                if (distance < minDistance) {
                    minDistance = distance;
                    result = enemy;
                }
            }
        }

        return result;
    }

    public static Unit getClosestFriendlyZergHatchery(Unit unit) {
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

    public static void advancedKiteQueen(JNIBWAPI bwapi, Unit unit, Unit target, int kitingDistance, int scanDistance) {

        if (target != null) {
            int tiles = scanDistance / 64;
            Position posMy = unit.getPosition();
            Position posEnemy = target.getPosition();
            int myX = posMy.getPX();
            int myY = posMy.getPY();
            int enemyX = posEnemy.getPX();
            int enemyY = posEnemy.getPY();

            //vector from us to target
            int evadeX = myX - enemyX;
            int evadeY = myY - enemyY;

            //normalization
            double length = Math.sqrt(Math.pow(evadeX, 2) + Math.pow(evadeY, 2));
            double tempX = kitingDistance / length * evadeX;
            double tempY = kitingDistance / length * evadeY;
            double tempScanX = (scanDistance / 2) / length * evadeX;
            double tempScanY = (scanDistance / 2) / length * evadeY;
            evadeX = (int)tempX;
            evadeY = (int)tempY;
            int scanX = (int)tempScanX;
            int scanY = (int)tempScanY;

            //store infornt position
            Position front = new Position(myX - evadeX, myY - evadeY);
            front = front.makeValid();
            Position frontScan = new Position(myX - scanX, myY - scanY);
            frontScan = frontScan.makeValid();

            //position behind us
            evadeX = myX + evadeX;
            evadeY = myY + evadeY;
            scanX = myX + scanX;
            scanY = myY + scanY;

            //rotate behind->us vector 90 degrees counterclockwise
            int toRotateX = myX - evadeX;
            int toRotateY = myY - evadeY;
            int temp = toRotateX;
            toRotateX = -toRotateY;
            toRotateY = temp;
            int toRotateScanX = myX - scanX;
            int toRotateScanY = myY - scanY;
            temp = toRotateScanX;
            toRotateScanX = -toRotateScanY;
            toRotateScanY = temp;

            //store behind position
            Position back = new Position(evadeX, evadeY);
            back = back.makeValid();
            Position backScan = new Position(scanX, scanY);
            backScan = backScan.makeValid();

            //store 90 counterclockwise position
            Position left = new Position(myX + toRotateX, myY + toRotateY);
            left = left.makeValid();
            Position leftScan = new Position(myX + toRotateScanX, myY + toRotateScanY);
            leftScan = leftScan.makeValid();

            //store 90 clockwise position
            Position right = new Position(myX - toRotateX, myY - toRotateY);
            right = right.makeValid();
            Position rightScan = new Position(myX - toRotateScanX, myY - toRotateScanY);
            rightScan = rightScan.makeValid();

            //get list of units in each area
            ArrayList<Unit> lefts = getUnitsInRadius(bwapi, leftScan, tiles * 32);
            ArrayList<Unit> rights = getUnitsInRadius(bwapi, rightScan, tiles * 32);
            ArrayList<Unit> backs = getUnitsInRadius(bwapi, backScan, tiles * 32);
            ArrayList<Unit> fronts = getUnitsInRadius(bwapi, frontScan, tiles * 32);

            //store the unit counts
            int leftCount = lefts.size();
            int rightCount = rights.size();
            int backCount = backs.size();
            int frontCount = fronts.size();
//            System.out.println("leftCount: " + leftCount);
//            System.out.println("rightCount: " + rightCount);
//            System.out.println("backCount: " + backCount);
//            System.out.println("frontCount: " + frontCount);

            //find area with least amount
            int least = min(asList(leftCount, rightCount, backCount, frontCount));
//            System.out.println("least: " + least);

            //move to that area
            if (least == leftCount && least == rightCount && least == backCount && least == frontCount) {
                int rnd = (int) (Math.random() * 4);

                if (rnd < 1 && bwapi.hasPath(unit.getPosition(), back)) {
                    unit.move(back, false);
//                    backCounts += 1;
//                    System.out.println("moving back");
                }
                else if (rnd < 2 && rnd >= 1 && bwapi.hasPath(unit.getPosition(), left)) {
                    unit.move(left, false);
//                    leftCounts += 1;
//                    System.out.println("moving left");
                }
                else if (rnd < 3 && rnd >= 2 && bwapi.hasPath(unit.getPosition(), right)) {
                    unit.move(right, false);
//                    rightCounts += 1;
//                    System.out.println("moving right");
                }
                else if (bwapi.hasPath(unit.getPosition(), front)) {
                    unit.move(front, false);
//                    frontCounts += 1;
//                    System.out.println("moving front");
                }
            }

            else if (least == backCount && bwapi.hasPath(unit.getPosition(), back)) {
                unit.move(back, false);
//                backCounts += 1;
//                System.out.println("moving back");
            }
            else if (least == rightCount && bwapi.hasPath(unit.getPosition(), right)) {
                unit.move(right, false);
//                rightCounts += 1;
//                System.out.println("moving right");
            }
            else if (least == leftCount && bwapi.hasPath(unit.getPosition(), left)) {
                unit.move(left, false);
//                leftCounts += 1;
//                System.out.println("moving left");
            }
            else if (least == frontCount && bwapi.hasPath(unit.getPosition(), front)) {
                unit.move(front, false);
//                frontCounts += 1;
//                System.out.println("moving front");
            }
        }
    }

    public static ArrayList<Unit> getUnitsInRectangle(JNIBWAPI bwapi, int left, int top, int right, int bottom) {
        ArrayList<Unit> unitFinderResults = new ArrayList<Unit>();

        for (int i = left; i < right; i += 32) {

            for (int j = top; j < bottom; j += 32) {
                Position tilePosition = new Position(i / 32, j / 32, Position.PosType.BUILD);
                tilePosition.makeValid();
                Position topLeft = new Position(i - 16, j - 16);
                Position bottomRight = new Position(i + 16, j + 16);
//                bwapi.drawBox(topLeft, bottomRight, BWColor.Blue, false, false);

                for (Unit unit : bwapi.getUnitsOnTile(tilePosition)) {
                    UnitType type = unit.getType();

                    if (type == UnitType.UnitTypes.Zerg_Hydralisk || type == UnitType.UnitTypes.Zerg_Scourge) {
                        if (unit.getPlayer() != bwapi.getSelf()) {
                            if (!unitFinderResults.contains(unit)) {
                                unitFinderResults.add(unit);
                            }
                        }
                    }
                }
            }
        }

        return unitFinderResults;
    }

    public static ArrayList<Unit> getUnitsInRadius(JNIBWAPI bwapi, Position center, int radius) {
        int x = center.getPX();
        int y = center.getPY();

        return getUnitsInRectangle(bwapi, x - radius, y - radius, x + radius, y + radius);
    }
}
