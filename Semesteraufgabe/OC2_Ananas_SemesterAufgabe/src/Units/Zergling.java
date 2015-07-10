package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;

import java.util.ArrayList;
import java.util.HashSet;


public class Zergling implements IMyUnit{
    final private JNIBWAPI bwapi;
    private final  Unit unit;
    private final RuleMachine ruleMachine;
    private HashSet<Unit> units = new HashSet<>();


    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;


    //for GOING_TO_RALLY_POINT
    private Position myPersonalRallyPoint;
    private Position tmpPoint;
    private String phase = "start";
//    private boolean initPDefenacePoint = true;
//    private boolean initTmpPoint = true;
    private boolean initTmpPoint = false;
    private boolean initPDefenacePoint = false;
    private Unit nearEnemy = null;
    private ArrayList<IMyUnit> unitsUnderAttack = new ArrayList<IMyUnit>();
    
    private int myPersonelDefencePointRadius = 70; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius

    //for IN_DEF_MODE



    public Zergling(Unit unit, JNIBWAPI bwapi, RuleMachine ruleMachine) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.ruleMachine = ruleMachine;
    }



    @Override
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
                    unit.burrow();
                    if(unit.isBurrowed()) {
                        currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                        //System.out.println(this.getClass().getName() + " entert def at Frame: " + AnanasAI.currentFrame);
                    }
                }

                break;
            case IN_DEF_MODE:
            	if(AnanasAI.currentFrame % 2 == 0)
                    return;
                defMode();
                break;
        }

    }

    /*
        #################################################
        ########### For GOING_TO_RALLY_POINT ##############
        #################################################
     */

    private boolean goingToDefPointFin(){

        if(unit.getID() % 2 == 0 && AnanasAI.currentFrame <= 100){
            //phase = "goToMyPersonalRallyPoint";
            return false;
        }



        Position target = null;
        switch (phase){
            case "start":
                target = createStartPoint();
                if(gotToPoint(target)){
                    unit.stop(false);
                    initTmpPoint = false;
                    phase = "goToTmpPoint";
                }
                break;
            case "goToTmpPoint":
                target = creatTmpPoint();
                if(gotToPoint(target)){
                    unit.stop(false);
                    phase = "goToMyPersonalRallyPoint";
                }
                break;
            case "goToMyPersonalRallyPoint":
                target = createPersonalRallyPoint();
                if(gotToPoint(target)){
                    unit.stop(false);
                    return true;
                }
        }
        return false;
    }


    private boolean isAtPersonalDefPoint(Position target,int radius){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),target) <=radius)
            return true;
        else
            return false;
    }

    private boolean gotToPoint(Position target){
        int ackRadius = 0;
        switch (phase){
            case "start":
                ackRadius = 100;
                break;
            case "goToTmpPoint":
                ackRadius = 70;
                break;
            case "goToMyPersonalRallyPoint":
                ackRadius = myPersonelDefencePointRadius;
                break;
        }

        if(isAtPersonalDefPoint(target,ackRadius)){
            return true;
        }
        else{
            if(unit.isIdle()){
                switch (phase){
                    case "start":
                    case "goToTmpPoint":
                    	CommonFunctions.simpleUnitMove(unit,target);
                        break;
                    case "goToMyPersonalRallyPoint":
                        swarmMoveToPosition(target);
                        break;
                }
            }
            return false;
        }
    }

    private Position createStartPoint(){
        if(!initTmpPoint){
            double[] vec = CommonFunctions.calculateVector(AnanasAI.middleOfMap.getPX(), AnanasAI.middleOfMap.getPY(), unit.getPosition().getPX(), unit.getPosition().getPY());
            Position tmpPos = new Position(unit.getPosition().getPX() + (int) vec[0]/2, unit.getPosition().getPY() + (int) vec[1]/2);
            //tmpPoint = CommonFunctions.getRndPosInDefCircle(tmpPos,40);
            tmpPoint = tmpPos;
            initTmpPoint = true;
        }
        return tmpPoint;
    }


    private Position creatTmpPoint(){
        if(!initTmpPoint){
            double[] vec = CommonFunctions.calculateVector(AnanasAI.middleOfMap.getPX(),AnanasAI.middleOfMap.getPY(),AnanasAI.hatcheryToDefend.getUnit().getPosition().getPX(),AnanasAI.hatcheryToDefend.getUnit().getPosition().getPY());
            Position tmpPos = new Position(AnanasAI.hatcheryToDefend.getUnit().getPosition().getPX() + (int) vec[0]/2,AnanasAI.hatcheryToDefend.getUnit().getPosition().getPY() + (int) vec[1]/2);
            tmpPoint = CommonFunctions.getRndPosInDefCircle(tmpPos,40);
            initTmpPoint = true;
        }
        return tmpPoint;
    }

    private Position createPersonalRallyPoint(){
        if(!initPDefenacePoint){
            myPersonalRallyPoint = null;
            myPersonalRallyPoint = CommonFunctions.getRndPosInDefCircle(AnanasAI.rallyPoint, AnanasAI.rallyPointRadius);
            initPDefenacePoint = true;
        }
        return myPersonalRallyPoint;
    }
    /*
        #################################################
        ########### For IN_DEF_MODE #####################
        #################################################
    */

    
    private void defMode(){
    	
//    	if(unit.isIdle()){    		
	    	if(isUnitUnderAttack() || isEnemyUnitInRange()){
	    		if (nearEnemy != null)
		    		if(unit.getDistance(nearEnemy) < unit.getDistance(AnanasAI.enemyToAttack) && CommonFunctions.isEnemyAttackable(nearEnemy)){
		    			AnanasAI.enemyToAttack = nearEnemy;
		    		}
	    	}
	    	if(AnanasAI.enemyToAttack != null && AnanasAI.enemyToAttack.getDistance(AnanasAI.defencePoint) < 270 ){
	    		if(unit.isBurrowed())
	    			unit.unburrow();
	    		else
	    			swarmMoveToTarget(AnanasAI.enemyToAttack);
	    	}else{
	    		unit.burrow();
	    	}
//    	}
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
            if(myUnit instanceof Zergling)
                units.add(myUnit.getUnit());
        }


        double[] final_vector = ruleMachine.calcFlockVectorToPos(unit,units,new int[]{target.getPX(),target.getPY()});
        //double[] final_vector = {1000,1000};

        //System.out.println("Z ID:" + unit.getID() + " UnitsSize:" + units.size() + " Vec:" + Arrays.toString(final_vector));
        Position tragetPosition = new Position((int) final_vector[0],(int) final_vector[1]);
        //tragetPosition.makeValid();
        CommonFunctions.simpleUnitMove(unit, tragetPosition);
    }





    public Unit getUnit(){
        return this.unit;
    }



//    private void drawMyLine(){
//        CommonFunctions.drawLine(bwapi,unit,unit.getTargetPosition(),BWColor.Green);
//    }




}
