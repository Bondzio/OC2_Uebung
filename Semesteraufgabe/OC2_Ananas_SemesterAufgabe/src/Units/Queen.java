package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_XCS_Manager;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_Constants;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.util.BWColor;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Queen implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;
    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;
    private int countAttackMove = 0;
    private int countKite = 0;

    //for GOING_TO_DEF_POINT
    private int ackRadius = 40;

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
                defMode();
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
        Unit target = CommonFunctions.getClosestEnemy(unit);
        double distance = CommonFunctions.getDistanceBetweenUnits(unit, target);

        queen_xcs_manager.getDetector().setDistance(distance);

//        if (isThereSomethingToReward)
//            allHydraManager.actionExecutionFin(unit, target, distance);


        String action = queen_xcs_manager.getNextPredictedAction();

        if (!isThereSomethingToReward)
            isThereSomethingToReward = true;


        if (action.equals("kite")) {

            if (distance <= StarCraftBW_Queen_Constants.HYDRALISK_WEAPONRANGE * 2) {
                System.out.print(StarCraftBW_Queen_Constants.HYDRALISK_WEAPONRANGE + "\n");
                CommonFunctions.advancedKiteQueen(bwapi, unit, target, 150, 300);
            }

//            dummKite(target);
//            //kiteInOppositeDir(target,distance);
            this.countKite++;
        }
        else if (action.equals("attackMove")) {
//            attackMove(target);
            this.countAttackMove++;
        }
        CommonFunctions.drawLine(bwapi, unit, target.getTargetPosition(), BWColor.Red);
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
