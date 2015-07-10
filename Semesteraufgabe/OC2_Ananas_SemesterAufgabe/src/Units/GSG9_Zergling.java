package Units;

import AI.AnanasAI;
import Common.CommonFunctions;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Rolle on 07.07.2015.
 */
public class GSG9_Zergling implements IMyUnit{

    final private JNIBWAPI bwapi;
    final private Unit unit;
    private int mission;



    public GSG9_Zergling(Unit unit, JNIBWAPI bwapi,int mission) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.mission = mission;
    }

    public void step() {
        Position targetPosition = unit.getTargetPosition();
        //bwapi.drawLine(unit.getPosition(), targetPosition, BWColor.Green, false);


        specialMissions(mission);

    }

    public void specialMissions(int mission){
        Position[] hatcheries = CommonFunctions.getEnemyHatcheries(bwapi);
        
        if (mission == 0 || mission == 1) {
        	if(unit.isUnderAttack() || isDangerousUnitInRange())
        		unit.burrow();
        	
        	else if (unit.isIdle() && !isDangerousUnitInRange()) {

        		if(unit.isBurrowed())
        			unit.unburrow();
            	
        		else if (unit.getDistance(getClosestEnemyZergHatchery(hatcheries)) > UnitTypes.Zerg_Zergling.getSightRange()) {
                    unit.attack(hatcheries[mission], false);
        		}
                else
                    unit.attack(getSightedHatchery(hatcheries), false);
            }
        }
    }

    // Checks if a enemy in range could hit our guy
    private boolean isDangerousUnitInRange(){
    	boolean enemyInRange = false;
    	ArrayList<UnitType> badEnemys = new ArrayList<UnitType>();
    	badEnemys.add(UnitTypes.Zerg_Zergling);
    	badEnemys.add(UnitTypes.Zerg_Ultralisk);
    	badEnemys.add(UnitTypes.Zerg_Hydralisk);

    	if(!bwapi.getEnemyUnits().isEmpty()){
    		for(Unit target : bwapi.getEnemyUnits()){
    			// Bad Enemy is in SightRange
    			if(unit.getDistance(target) <= unit.getType().getSightRange() && badEnemys.contains(target.getType()))
    				enemyInRange = true;
    		}
    	}
    	return enemyInRange;
    }
    
    private Position getClosestEnemyZergHatchery(Position[] hatcheries) {
    	Position result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Position pos : hatcheries) {
            double distance = unit.getDistance(pos);
                if (distance < minDistance) {
                    minDistance = distance;
                    result = pos;
                }
        }
        
        return result;
    }

    private Position getSightedHatchery(Position[] hatcheries) {
    	Position result = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for(Unit enemy : bwapi.getEnemyUnits()){
        	if(enemy.getType() == UnitTypes.Zerg_Hatchery){
        		double distance = unit.getDistance(enemy);
        		if (distance < minDistance) {
                    minDistance = distance;
                    result = enemy.getPosition();
                }
        	}
        }
        return result;
    }
    
    
    public Unit getUnit(){
        return this.unit;
    }

}
