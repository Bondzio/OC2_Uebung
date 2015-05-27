import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;
import java.util.ArrayList;
import java.util.HashSet;
import static java.util.Collections.min;
import static java.util.Arrays.asList;


public class Vulture {
    final private JNIBWAPI bwapi;
    final private HashSet<Unit> enemyUnits;
    final private Unit unit;
    final public StarCraftBW_XCS_Manager xcs_Manager = new StarCraftBW_XCS_Manager();
    private int leftCounts = 0;
    private int rightCounts = 0;
    private int backCounts = 0;
    private int frontCounts = 0;
    private boolean first = true;

    public Vulture(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
    }

    private void printStuff(double distance) {
        bwapi.drawCircle(unit.getPosition(), StarCraftBW_Unit_Constants.OWN_WEAPONRANGE, BWColor.Red, false, false);
        bwapi.drawLine(unit.getPosition(), unit.getTargetPosition(), BWColor.Green, false);
        System.out.println("distance: " + distance);
//        System.out.println("moved left total: " + leftCounts);
//        System.out.println("moved right total: " + rightCounts);
//        System.out.println("moved back total: " + backCounts);
//        System.out.println("moved front total: " + frontCounts);
    }

    public void step() {
        Unit target = getClosestEnemy();
        double distance = getDistance(target);
        xcs_Manager.getDetector().setDistance(distance);
        printStuff(distance);

        if (!first) {
            xcs_Manager.actionExecutionFin(unit, target, distance);
        }
        else {
            xcs_Manager.loadOldProgress();
            first = false;
        }

        String action = xcs_Manager.getNextPredictedAction();

        if (action.equals("kite"))
            kite(target);
        else if (action.equals("move"))
            move(target, distance);
    }

    private void kite(Unit target) {
        System.out.println("kite");

        if (target != null) {
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
            double tempX = 95 / length * evadeX;
            double tempY = 95 / length * evadeY;
            evadeX = (int)tempX;
            evadeY = (int)tempY;

            //store infornt position
            Position front = new Position(myX - evadeX, myY - evadeY);
            front = front.makeValid();

            //position behind us
            evadeX = myX + evadeX;
            evadeY = myY + evadeY;

            //rotate behind->us vector 90 degrees counterclockwise
            int toRotateX = myX - evadeX;
            int toRotateY = myY - evadeY;
            int temp = toRotateX;
            toRotateX = -toRotateY;
            toRotateY = temp;

            //store behind position
            Position back = new Position(evadeX, evadeY);
            back = back.makeValid();

            //store 90 counterclockwise position
            Position left = new Position(myX + toRotateX, myY + toRotateY);
            left = left.makeValid();

            //store 90 clockwise position
            Position right = new Position(myX - toRotateX, myY - toRotateY);
            right = right.makeValid();

            //get list of units in each area
            ArrayList<Unit> lefts = getUnitsInRadius(left, 64);
            ArrayList<Unit> rights = getUnitsInRadius(right, 64);
            ArrayList<Unit> backs = getUnitsInRadius(back, 64);
            ArrayList<Unit> fronts = getUnitsInRadius(front, 64);

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
                    backCounts += 1;
//                    System.out.println("moving back");
                }
                else if (rnd < 2 && rnd >= 1 && bwapi.hasPath(unit.getPosition(), left)) {
                    unit.move(left, false);
                    leftCounts += 1;
//                    System.out.println("moving left");
                }
                else if (rnd < 3 && rnd >= 2 && bwapi.hasPath(unit.getPosition(), right)) {
                    unit.move(right, false);
                    rightCounts += 1;
//                    System.out.println("moving right");
                }
                else if (bwapi.hasPath(unit.getPosition(), front)) {
                    unit.move(front, false);
                    frontCounts += 1;
//                    System.out.println("moving front");
                }
            }

            else if (least == backCount && bwapi.hasPath(unit.getPosition(), back)) {
                unit.move(back, false);
                backCounts += 1;
//                System.out.println("moving back");
            }
            else if (least == rightCount && bwapi.hasPath(unit.getPosition(), right)) {
                unit.move(right, false);
                rightCounts += 1;
//                System.out.println("moving right");
            }
            else if (least == leftCount && bwapi.hasPath(unit.getPosition(), left)) {
                unit.move(left, false);
                leftCounts += 1;
//                System.out.println("moving left");
            }
            else if (least == frontCount && bwapi.hasPath(unit.getPosition(), front)) {
                unit.move(front, false);
                frontCounts += 1;
//                System.out.println("moving front");
            }
        }
    }

    private ArrayList<Unit> getUnitsInRectangle(int left, int top, int right, int bottom) {
        ArrayList<Unit> unitFinderResults = new ArrayList<Unit>();

        for (int i = left; i < right; i += 32) {

            for (int j = top; j < bottom; j += 32) {
                Position tilePosition = new Position(i / 32, j / 32, Position.PosType.BUILD);
                tilePosition.makeValid();
                Position topLeft = new Position(i - 16, j - 16);
                Position bottomRight = new Position(i + 16, j + 16);
                bwapi.drawBox(topLeft, bottomRight, BWColor.Blue, false, false);

                for (Unit unit : bwapi.getUnitsOnTile(tilePosition)) {
                    UnitType type = unit.getType();

                    if (type == UnitType.UnitTypes.Protoss_Zealot) {

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

    private ArrayList<Unit> getUnitsInRadius(Position center, int radius) {
        int x = center.getPX();
        int y = center.getPY();

        return this.getUnitsInRectangle(x - radius, y - radius, x + radius, y + radius);
    }

    private void move(Unit target, double distance) {
        System.out.println("move");

        if (distance <= StarCraftBW_Unit_Constants.OWN_WEAPONRANGE)
            unit.attack(target, false);

        else
            unit.move(new Position(target.getPosition().getPX(), target.getPosition().getPY()), false);
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
        int myX = unit.getPosition().getPX();
        int myY = unit.getPosition().getPY();
        int enemyX = enemy.getPosition().getPX();
        int enemyY = enemy.getPosition().getPY();
        int diffX = myX - enemyX;
        int diffY = myY - enemyY;

        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);

        return Math.sqrt(result);
    }
}