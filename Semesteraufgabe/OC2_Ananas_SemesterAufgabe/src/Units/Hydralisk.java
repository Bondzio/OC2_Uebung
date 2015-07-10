package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import Hydralisk_XCS.AllHydralisk_XCS_Manager;
import Hydralisk_XCS.Hydralisk_Unit_Constants;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;



/**
 * Created by Rolle on 03.07.2015.
 */
public class Hydralisk implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;

    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;

    //for GOING_TO_RALLY_POINT
    private int ackRadius = 65; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius


    //for IN_DEF_MODE
    private AllHydralisk_XCS_Manager allHydraManager;
    private boolean isThereSomethingToReward = false;
    private Position pointToDefend = null;


    public Hydralisk(Unit unit, JNIBWAPI bwapi,AllHydralisk_XCS_Manager allHydraManager ) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.allHydraManager = allHydraManager;
    }


    public void step() {
        switch(currentUnitStatus){
            case START:
                currentUnitStatus = MyUnitStatus.GOING_TO_RALLY_POINT;
                break;
            case GOING_TO_RALLY_POINT:
                if(!unit.isIdle())
                    break;
                if(goingToDefPointFin()){
                    currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                    //System.out.println(this.getClass().getName() + " entert def at Frame: " + AnanasAI.currentFrame);
                }
                break;
            case IN_DEF_MODE:
                if(AnanasAI.currentFrame % 2 == 0)
                    return;
                if(pointToDefend == null)
                    createMyDefPoint();
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
        drawMyLine();

        if(unit.getID() % 2 == 0 && AnanasAI.currentFrame <= 10){
            return false;
        }

        Position defPoint = AnanasAI.rallyPoint;
        //Position defPoint = AnanasAI.hatcheryToDefend.getUnit().getPosition(); // slower!

        CommonFunctions.simpleUnitMove(unit, defPoint);
        if(isAtPersonalDefPoint(defPoint)){
            unit.stop(false);
            return true;
        }
        else{
            return false;
        }

    }

    /*
    #################################################
    ########### For IN_DEF_MODE #####################
    #################################################
    */

    private void defMode(){
        double distance = CommonFunctions.getDistianceBetweenPositions(unit.getPosition(), pointToDefend);

        allHydraManager.giveDetectorSomethingToDetected(unit.getID(), distance);


        if (isThereSomethingToReward)
            allHydraManager.actionExecutionFin(this,unit.getTarget(),distance,AnanasAI.hatcheryToDefend);


        String action = allHydraManager.getNextPredictedAction(unit.getID());

        if (!isThereSomethingToReward)
            isThereSomethingToReward = true;



        if(unit.isBurrowed()){
            unit.unburrow();
            isThereSomethingToReward = false;
            return;
        }

        if (action.equals("attackMoveToClosestEnemy")) {
            attackMoveToClosestEnemy();
        }
        else if (action.equals("attackMoveToClosestFlyingEnemy")) {
            attackMoveToClosestFlyingEnemy();
        }
        else if (action.equals("moveToDefPoint")) {
           moveToPos(pointToDefend);
        }
        else if (action.equals("supportFriend")) {
            supportFriend();
        }
        else if (action.equals("protectDefPoint")) {
            protectDefPoint();
        }
        else if (action.equals("burrow")) {
            unit.burrow();
        }
    }

    private void attackMoveToClosestEnemy(){
        Unit cloesestEnemy = CommonFunctions.getClosestEnemy(bwapi, unit);
        if(cloesestEnemy != null)
            attackMove(cloesestEnemy);
    }

    private void attackMoveToClosestFlyingEnemy(){
        Unit cloesestFlyingEnemy = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for(Unit enemy: AnanasAI.enemyUnits){
            if(enemy.getType() == UnitType.UnitTypes.Zerg_Queen || enemy.getType() == UnitType.UnitTypes.Zerg_Scourge ){
                double distance = CommonFunctions.getDistanceBetweenUnits(unit, enemy);
                if (distance < minDistance) {
                    minDistance = distance;
                    cloesestFlyingEnemy = enemy;
                }
            }
        }

        if(cloesestFlyingEnemy != null)
            attackMove(cloesestFlyingEnemy);
    }

    private void supportFriend(){
        Unit closestFriendlyUnit = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for(IMyUnit friend: AnanasAI.myUnits){
            Unit friendAsUnit = friend.getUnit();
            if(friendAsUnit.getType() == UnitType.UnitTypes.Zerg_Queen || friendAsUnit.getType() == UnitType.UnitTypes.Zerg_Scourge ){
                continue;
            }
            else{
                double distance = CommonFunctions.getDistanceBetweenUnits(unit, friendAsUnit);
                if (distance < minDistance && friendAsUnit.getTarget() != null && distance <= (Hydralisk_Unit_Constants.Hydralisk_WEAPONRANGE*2)) {
                    minDistance = distance;
                    closestFriendlyUnit = friendAsUnit;
                }
            }
        }

        if(closestFriendlyUnit != null)
            attackMove(closestFriendlyUnit.getTarget());
    }

    private void protectDefPoint(){
        Unit clossestEnemyToDefPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for(Unit enemy: AnanasAI.enemyUnits){
            if(enemy.getType() == UnitType.UnitTypes.Zerg_Queen || enemy.getType() == UnitType.UnitTypes.Zerg_Scourge ){
                continue;
            }
            else{
                double distance = CommonFunctions.getDistianceBetweenPositions(enemy.getPosition(), pointToDefend);
                if (distance < minDistance && enemy.isAttacking()) {
                    minDistance = distance;
                    clossestEnemyToDefPoint = enemy;
                }
            }
        }

        if(clossestEnemyToDefPoint != null)
            attackMove(clossestEnemyToDefPoint);
    }

    private void attackMove(Unit target) {
        unit.attack(target, false);
    }

    private void moveToPos(Position position) {
        if(!isAtPersonalDefPoint(position))
            unit.move(position, false);
    }


    private boolean isAtPersonalDefPoint(Position defPoint){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),defPoint)<=ackRadius)
            return true;
        else
            return false;
    }

    private void createMyDefPoint(){
        this.pointToDefend = AnanasAI.hatcheryToDefend.getUnit().getPosition();
    }

    public Unit getUnit(){
        return this.unit;
    }


    private void drawMyLine(){
        CommonFunctions.drawLine(bwapi,unit,unit.getTargetPosition(),BWColor.Blue);
    }

}

