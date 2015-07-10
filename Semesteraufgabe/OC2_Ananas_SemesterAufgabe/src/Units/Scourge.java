package Units;

import java.util.ArrayList;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Scourge implements IMyUnit {
    final private JNIBWAPI bwapi;

    final private Unit unit;
    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;

    //for GOING_TO_RALLY_POINT
    private int ackRadius = 40; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius



    public Scourge(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;

    }


    @Override
    public void step() {
        switch(currentUnitStatus){
            case START:
                currentUnitStatus = MyUnitStatus.GOING_TO_RALLY_POINT;
                break;
            case GOING_TO_RALLY_POINT:
                if(goingToDefPointFin())
                    currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                break;
            case IN_DEF_MODE:
            	defMode();
                break;
        }
    }


    private void defMode() {
		Unit unitToAttack = null;
		double minDistance = Double.POSITIVE_INFINITY;
		
//    	if(unit.isIdle()){
    		for(Unit enemy : bwapi.getEnemyUnits()){
    			if(isEnemyAttackable(enemy)){
    				double distance = unit.getDistance(enemy);
                    if (distance < minDistance) {
                        minDistance = distance;
                        unitToAttack = enemy;
                    }    				
    			}
    		}
    		if(unitToAttack != null)
    			unit.attack(unitToAttack, false);
    		else
    			unit.attack(AnanasAI.defencePoint, false);
//		}
		
	}

    
    private boolean isEnemyAttackable(Unit enemy){
    	boolean attackable = false;
    	
    	ArrayList<UnitType> attackableEnemys = new ArrayList<UnitType>();
    	attackableEnemys.add(UnitTypes.Zerg_Scourge);
    	attackableEnemys.add(UnitTypes.Zerg_Queen);
    
    	for(UnitType ut : attackableEnemys){
    		if(ut.equals(enemy.getType())){
    			attackable = true;
    		}
    	}
    	return attackable;
    }

	private boolean goingToDefPointFin(){
		CommonFunctions.simpleUnitMove(unit, AnanasAI.hatcheryToDefend.getUnit().getPosition());

        if(isAtPersonalDefPoint()){
            return true;
        }
        else{
            return false;
        }
    }


    private boolean isAtPersonalDefPoint(){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),AnanasAI.hatcheryToDefend.getUnit().getPosition())<=ackRadius)
            return true;
        else
            return false;
    }

    public Unit getUnit(){
        return this.unit;
    }
}
