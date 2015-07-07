package Common;

import AI.AnanasAI;
import jnibwapi.Position;
import jnibwapi.Unit;

/**
 * Created by Rolle on 07.07.2015.
 */
public class CommonFunctions {


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

    public static void simpleUnitMove(Unit unitToMove, Position destination){
        unitToMove.move(destination,false);
    }
}
