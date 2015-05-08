import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.WeaponType;

import java.util.HashSet;

public class Vulture {

    final private JNIBWAPI bwapi;
    private final HashSet<Unit> enemyUnits;
    final private Unit unit;
    private int id;

    public Vulture(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits, int id) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.id = id;
    }

    public void step() {
        Unit target = getClosestEnemy();

        kite(target);
        System.out.println("kite");

        if (unit.getOrderID() != 10 && !unit.isAttackFrame() && !unit.isStartingAttack() && !unit.isAttacking() && target != null) {
            if (bwapi.getWeaponType(WeaponType.WeaponTypes.Fragmentation_Grenade.getID()).getMaxRange() > getDistance(target) - 20) {
               bwapi.attack(unit.getID(), target.getID());
            }
//            else if (getDistance(target) < 20) {
//
//            }
            else {
                move(target);
                System.out.println("move");
            }
        }
    }

    private void kite(Unit target){
        bwapi.move(unit.getID(), target.getX() - (bwapi.getWeaponType(WeaponType.WeaponTypes.Fragmentation_Grenade.getID()).getMaxRange()) / 2, target.getY() - (bwapi.getWeaponType(WeaponType.WeaponTypes.Fragmentation_Grenade.getID()).getMaxRange()) / 2);
    }

    private void move(Unit target){
        bwapi.move(unit.getID(), target.getX(), target.getY());
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
}
