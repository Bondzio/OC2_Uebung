package Units;

import java.util.ArrayList;
import java.util.HashSet;

import bolding.RuleMachine;
import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.util.BWColor;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Ultralisk implements IMyUnit {
    final private JNIBWAPI bwapi;
    final private Unit unit;

    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;


    //for GOING_TO_RALLY_POINT
    private Position myPersonelDefencePoint;
    private boolean initPDefenacePoint = false;
    private int myPersonelDefencePointRadius = 100; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius
    private boolean initTmpPoint = false;
    private HashSet<Unit> units = new HashSet<>();
    private Unit nearEnemy = null;
    private ArrayList<IMyUnit> unitsUnderAttack = new ArrayList<IMyUnit>();
    private final RuleMachine ruleMachine;

    public Ultralisk(Unit unit, JNIBWAPI bwapi, RuleMachine ruleMachine) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.ruleMachine = ruleMachine;
    }


    public void step() {
        if(AnanasAI.currentFrame % 2 == 0)
            return;

        switch(currentUnitStatus){
            case START:
                currentUnitStatus = MyUnitStatus.GOING_TO_RALLY_POINT;
                break;
            case GOING_TO_RALLY_POINT:
                if(!unit.isIdle())
                    return;
                if(goingToDefPointFin()){
                    currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                //System.out.println( this.getClass().getName() + " entert def at Frame: " + AnanasAI.currentFrame);
                }
                break;
            case IN_DEF_MODE:
                defMode();
                break;
        }
    }



    /*
        #################################################
        ########### For GOING_TO_RALLY_POINT ##############
        #################################################
     */

    private boolean goingToDefPointFin() {
        drawMyLine();

        if(!initPDefenacePoint ){
            if(prepareForGoingToPersonalDefPos()) {
                myPersonelDefencePoint = CommonFunctions.getRndPosInDefCircle(AnanasAI.rallyPoint, AnanasAI.rallyPointRadius);
                initPDefenacePoint = true;
            }

        }
        else{
            if (isAtPersonalDefPoint()) {
                return true;
            } else {
                CommonFunctions.simpleUnitMove(unit, myPersonelDefencePoint);
            }
        }
        return false;
    }

    private boolean prepareForGoingToPersonalDefPos(){

        if(!initTmpPoint) {
            double[] vec = CommonFunctions.calculateVector(AnanasAI.middleOfMap.getPX(),AnanasAI.middleOfMap.getPY(),AnanasAI.hatcheryToDefend.getUnit().getPosition().getPX(),AnanasAI.hatcheryToDefend.getUnit().getPosition().getPY());
            myPersonelDefencePoint = new Position(AnanasAI.hatcheryToDefend.getUnit().getPosition().getPX() + (int) vec[0]/4,AnanasAI.hatcheryToDefend.getUnit().getPosition().getPY() + (int) vec[1]/4);
            initTmpPoint = true;
        }

        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),myPersonelDefencePoint) <= 50){
            return true;
        }
        else{
            CommonFunctions.simpleUnitMove(unit,myPersonelDefencePoint);
            return false;
        }
    }

    private boolean isAtPersonalDefPoint(){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),myPersonelDefencePoint)<=myPersonelDefencePointRadius)
            return true;
        else
            return false;
    }


    /*
        #################################################
        ########### For IN_DEF_MODE #####################
        #################################################
    */

    private void defMode(){
    	if(unit.isIdle()){    
		if(isUnitUnderAttack() || isEnemyUnitInRange()){
			if (nearEnemy != null)
	    		if(unit.getDistance(nearEnemy) < unit.getDistance(AnanasAI.enemyToAttack) && CommonFunctions.isEnemyAttackable(nearEnemy)){
	    			AnanasAI.enemyToAttack = nearEnemy;
	    		}
		}
		if(AnanasAI.enemyToAttack != null && AnanasAI.enemyToAttack.getDistance(AnanasAI.defencePoint) < 270 ){
			swarmMoveToTarget(AnanasAI.enemyToAttack);
		}else if (!isAtPersonalDefPoint()){
			unit.attack(AnanasAI.defencePoint, false);
		}
    	}
    }

    // Returns true, if an ally Unit is under attack
    private boolean isUnitUnderAttack(){
    	boolean unitUnderAttack = false;
    	unitsUnderAttack.clear();
    	
    	for(IMyUnit ally : AnanasAI.myUnits){
    		if(ally.getUnit().isUnderAttack() && unit.getDistance(ally.getUnit()) < 300){
    			unitsUnderAttack.add(ally);
    			unitUnderAttack = true;
    		}
    	}
    	return unitUnderAttack;
    }
    
    // Returns true, if an Enemy is in Range of the current Unit
    private boolean isEnemyUnitInRange(){
    	nearEnemy = null;
    	boolean enemyUnitInRange = false;
    	Unit nearest = null;
    	double minDistance = Double.POSITIVE_INFINITY;
    	
    	for(Unit enemy : bwapi.getEnemyUnits()){
    		double distance = enemy.getDistance(AnanasAI.defencePoint);
    		
    		if(distance < 600){
    			enemyUnitInRange = true;
                 if (distance < minDistance) {
                     minDistance = distance;
                     nearest = enemy;
                 }
    		}
    	}
    	nearEnemy = nearest;
    	return enemyUnitInRange;
    }
    

    public void swarmMoveToTarget(Unit target){
        swarmMoveToPosition(target.getPosition());
    }



    public void swarmMoveToPosition(Position target){
        units.clear();
        for(IMyUnit myUnit: AnanasAI.myUnits){
            if( (myUnit instanceof Zergling) || (myUnit instanceof Ultralisk))
                units.add(myUnit.getUnit());
        }


        double[] final_vector = ruleMachine.calcFlockVectorToPos(unit,units,new int[]{target.getPX(),target.getPY()});
        //double[] final_vector = {1000,1000};

        //System.out.println("Z ID:" + unit.getID() + " UnitsSize:" + units.size() + " Vec:" + Arrays.toString(final_vector));
        Position tragetPosition = new Position((int) final_vector[0],(int) final_vector[1]);
        //tragetPosition.makeValid();
        CommonFunctions.simpleUnitMove(unit,tragetPosition);
    }

    

    /*
        #################################################
        ########### BLUB ################################
        #################################################
    */

    private void drawMyLine(){
        CommonFunctions.drawLine(bwapi,unit,unit.getTargetPosition(),BWColor.Red);
    }

    public Unit getUnit(){
        return this.unit;
    }

}
