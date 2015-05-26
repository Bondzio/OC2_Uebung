import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.ArrayList;
import java.util.HashSet;


public class Vulture {

    final private JNIBWAPI bwapi;
    final private HashSet<Unit> enemyUnits;
    final private Unit unit;
    final public StarCraftBW_XCS_Manager xcs_Manager = new StarCraftBW_XCS_Manager();
    private int leftCounts = 0;
    private int rightCounts = 0;
    private int backCounts = 0;
    private boolean first = true;

    public Vulture(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits) {

        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;

    }


    private void printStuff(double distance) {

        System.out.println("distance: " + distance);
        bwapi.drawCircle(unit.getPosition(), StarCraftBW_Unit_Constants.OWN_WEAPONRANGE, BWColor.Red, false, false);
        bwapi.drawLine(unit.getPosition(), unit.getTargetPosition(), BWColor.Green, false);
        System.out.println("moved left total: " + leftCounts);
        System.out.println("moved right total: " + rightCounts);
        System.out.println("moved back total: " + backCounts);

    }

    public void step() {

        Unit target = getClosestEnemy();
        double distance = getDistance(target);
        printStuff(distance);

        xcs_Manager.getDetector().setDistance(distance);

        if (!first) {
            xcs_Manager.actionExecutionFin(unit, target, distance);
        } else {
            xcs_Manager.loadOldProgress();
            first = false;
        }

        String action = xcs_Manager.getNextPredictedAction();

        if (action.equals("kite"))
//            kite(target);
            kite(target, distance);
        else if (action.equals("move"))
            move(target, distance);
    }

//    private void kite(Unit target){
//    	System.out.println("kite");
//    	unit.move(new Position(target.getPosition().getPX() - 80, target.getPosition().getPY() - 80), false);
//    }

    private void kite(Unit target, double distance) {
        System.out.println("kite");

        if (target != null) {
            if (distance > 155) {
                unit.attack(target, false);
            } else {
                Position posMy = unit.getPosition(); //our unit position
                Position posEnemy = target.getPosition(); //enemy position

                int myX = posMy.getPX();
                int myY = posMy.getPY();
                int enemyX = posEnemy.getPX();
                int enemyY = posEnemy.getPY();

                //vector from us to target
                int evadeX = myX - enemyX;
                int evadeY = myY - enemyY;

                //position behind us
                evadeX = myX + evadeX;
                evadeY = myY + evadeY;

                //rotate behind->us vector 90 degrees counterclockwise
                int toRotateX = myX - evadeX;
                int toRotateY = myY - evadeY;
                int temp = toRotateX;
                toRotateX = -toRotateY;
                toRotateY = temp;

                //store 90 counterclockwise position
                Position left = new Position(evadeX + toRotateX, evadeY + toRotateY);
                left = left.makeValid();

                //store 90 clockwise position
                Position right = new Position(evadeX - toRotateX, evadeY - toRotateY);
                right = right.makeValid();

                //store behind position
                Position back = new Position(evadeX, evadeY);
                back = back.makeValid();

                //get list of units in each area
                ArrayList<Unit> lefts = getUnitsInRadius(left, 80);
                ArrayList<Unit> rights = getUnitsInRadius(right, 80);
                ArrayList<Unit> backs = getUnitsInRadius(back, 80);

                //store the unit counts
                int leftCount = lefts.size();
                int rightCount = rights.size();
                int backCount = backs.size();

//                System.out.println("leftCount: " + leftCount);
//                System.out.println("rightCount: " + rightCount);
//                System.out.println("backCount: " + backCount);

                //find area with least amount
                int least;
                if (rightCount < leftCount) least = rightCount;
                else least = leftCount;
                if (least < backCount) least = backCount;
//                System.out.println("least: " + least);

                //move to that area
//                if (least == leftCount && least == rightCount && least == backCount && least != 0 && bwapi.hasPath(unit, back)) {
//                    unit.move(back, false);
//                    System.out.println("moving back");
//                    backCounts += 1;
//                }
                if (least == leftCount) {

                    unit.move(left, false);
//                    System.out.println("moving left");
                    leftCounts += 1;
                } else if (least == rightCount) {
                    unit.move(right, false);
//                    System.out.println("moving right");
                    rightCounts += 1;
                } else {
                    unit.move(back, false);
//                    System.out.println("moving back");
                    backCounts += 1;
                }
            }
        }
    }

    private ArrayList<Unit> getUnitsInRectangle(int left, int top, int right, int bottom) {

        ArrayList<Unit> unitFinderResults = new ArrayList<Unit>();
//        Position topLeft = new Position(left, top);
//        Position bottomRight = new Position(right, bottom);
//        bwapi.drawBox(topLeft, bottomRight, bwColor.Blue, false, false);

        // Have the unit finder do its stuff
        for (int i = left; i < right; i++) {

            for (int j = top; j < bottom; j++) {
                Position tilePosition = new Position(i / 32, j / 32, Position.PosType.BUILD);
                tilePosition.makeValid();

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

        // Return results
        return unitFinderResults;
    }

    private ArrayList<Unit> getUnitsInRadius(Position center, int radius) {

        int x = center.getPX();
        int y = center.getPY();

        return this.getUnitsInRectangle(x - radius,
                y - radius,
                x + radius,
                y + radius);

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