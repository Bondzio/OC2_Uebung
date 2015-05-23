import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.WeaponType;

import java.util.HashSet;

public class Vulture {

    final private JNIBWAPI bwapi;
    private final HashSet<Unit> enemyUnits;
    final private Unit unit;
    final private StarCraftBW_XCS xcs = new StarCraftBW_XCS();

    public Vulture(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
    }

    public void step() {
        Unit target = getClosestEnemy();
        double distance = getDistance(target);

        System.out.println("distance: " + distance);

        //XCS
        //TODO: Persistieren!
        xcs.getDetector().setDistance(distance);
        xcs.getEffector().setStats(this.unit, target, distance);   
        
        String action = xcs.run();
        

        
        if(action == "kite")
        	kite(target);
        else if(action == "move")
        	move(target, distance);

    }


    private void kite(Unit target){
    	System.out.println("kite");
    	//TODO: Testen: weniger weit wegfahren (nur die Haelfte z.B.)
    	unit.move(new Position(target.getPosition().getPX() - StarCraftBW_Unit_Constants.OWN_WEAPONRANGE, target.getPosition().getPY() - StarCraftBW_Unit_Constants.OWN_WEAPONRANGE), false);
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
    

//  private void oldMove(Unit target){
//      bwapi.move(unit.getID(), target.getX() - (bwapi.getWeaponType(WeaponType.WeaponTypes.Fragmentation_Grenade.getID()).getMaxRange() / 2), target.getY() - (bwapi.getWeaponType(WeaponType.WeaponTypes.Fragmentation_Grenade.getID()).getMaxRange() / 2));
//  }

//    private double oldGetDistance(Unit enemy) {
//        int myX = unit.getX();
//        int myY = unit.getY();
//
//        int enemyX = enemy.getX();
//        int enemyY = enemy.getY();
//
//        int diffX = myX - enemyX;
//        int diffY = myY - enemyY;
//
//        double result = Math.pow(diffX, 2) + Math.pow(diffY, 2);
//
//        return Math.sqrt(result);
//    }



//    //Old Methode
//    public int getID() {
//        return unit.getID();
//    }

    
//  public void oldStep() {
//  Unit target = getClosestEnemy();
//  double distance = getDistance(target);
//  System.out.println("target: " + target);
//  System.out.println("distance: " + distance);
//
//  
//  if (unit.getOrderID() != 10 && !unit.isAttackFrame() && !unit.isStartingAttack() && !unit.isAttacking() && target != null) {
//      if (bwapi.getWeaponType(WeaponType.WeaponTypes.Fragmentation_Grenade.getID()).getMaxRange() > getDistance(target)) {
//         bwapi.attack(unit.getID(), target.getID());
//      }
//      else {
//          move(target);
//          //System.out.println("search");
//      }
//  }
//  else if (getDistance(target) < bwapi.getWeaponType(WeaponType.WeaponTypes.Psi_Blades.getID()).getMaxRange() + 75) {
//      kite(target);
//      //System.out.println("kite");
//  }
//  
//
//}
}
