package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Queen implements IMyUnit{
    final private JNIBWAPI bwapi;

    final private Unit unit;
    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;

    //for GOING_TO_DEF_POINT
    private int ackRadius = 40; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius

    //for IN_DEF_MODE
    private StarCraftBW_Queen_XCS_Manager queen_xcs_manager;
    private boolean isThereSomethingToReward = false;

    public Queen(Unit unit, JNIBWAPI bwapi, StarCraftBW_Queen_XCS_Manager queen_xcs_manager ) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.queen_xcs_manager = queen_xcs_manager;
    }

    @Override
    public void step() {
        switch(currentUnitStatus){
            case START:
                currentUnitStatus = MyUnitStatus.GOING_TO_DEF_POINT;
                break;
            case GOING_TO_DEF_POINT:
                if(goingToDefPointFin())
                    currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                break;
            case IN_DEF_MODE:
//                unit.useTech(TechType.TechTypes.Ensnare);
//                unit.useTech(TechType.TechTypes.Parasite);
                break;
        }
    }

    /*
    #################################################
    ########### For GOING_TO_DEF_POINT ##############
    #################################################
    */
    private boolean goingToDefPointFin(){

        Position defPoint = AnanasAI.defancePoint;
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
//        Unit target = getClosestEnemy();
        double distance = CommonFunctions.getDistanceBetweenUnits(unit,AnanasAI.hatcheryToDefend.getUnit());

        queen_xcs_manager.getDetector().setDistance(distance);

//        if (isThereSomethingToReward)
//            allHydraManager.actionExecutionFin(unit, target, distance);


        String action = queen_xcs_manager.getNextPredictedAction();

        if (!isThereSomethingToReward)
            isThereSomethingToReward = true;


        if (action.equals("kite")) {
//            //kite(target);
//            dummKite(target);
//            //kiteInOppositeDir(target,distance);
//            this.countKite++;
        }
        else if (action.equals("attackMove")) {
//            attackMove(target);
//            this.countAttackMove++;
        }
    }

    private boolean isAtPersonalDefPoint(Position defPoint){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),defPoint)<=ackRadius)
            return true;
        else
            return false;
    }

    public Unit getUnit(){
        return this.unit;
    }
}
