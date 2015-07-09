package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_XCS_Manager;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_Constants;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.util.BWColor;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Queen implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;
    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;
    private int countCastShit = 0;
    private int countKite = 0;
    private boolean parasiteDeployed = false;

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
        Unit closestEnemy = CommonFunctions.getClosestEnemy(unit);
        Unit closestEnemyQueen = CommonFunctions.getClosestEnemyZergQueen(unit);
        Unit closestEnemyScourge = CommonFunctions.getClosestEnemyScourge(unit);

        double distanceClosestEnemy = CommonFunctions.getDistanceBetweenUnits(unit, closestEnemy);
        double distanceClosestEnemyQueen = CommonFunctions.getDistanceBetweenUnits(unit, closestEnemyQueen);
        double distanceClosestEnemyScourge = CommonFunctions.getDistanceBetweenUnits(unit, closestEnemyScourge);

        queen_xcs_manager.getDetector().setDistance(distanceClosestEnemy);

//        if (isThereSomethingToReward)
//            allHydraManager.actionExecutionFin(unit, target, distance);


        String action = queen_xcs_manager.getNextPredictedAction();

        if (!isThereSomethingToReward)
            isThereSomethingToReward = true;


        if (action.equals("kite")) {

            if (distanceClosestEnemy <= StarCraftBW_Queen_Constants.HYDRALISK_WEAPONRANGE * 1.5) {
//                System.out.print("Distance: " + distanceClosestEnemy + "\n");
                CommonFunctions.advancedKiteQueen(bwapi, unit, closestEnemy, 150, 300);
            }

            this.countKite++;
        }
        else if (action.equals("cast")) {

            if (distanceClosestEnemyQueen <= StarCraftBW_Queen_Constants.OWN_CASTRANGE_PARASITE
                    && unit.getEnergy() >= StarCraftBW_Queen_Constants.OWN_ENERGYCOST_PARASITE
                    && parasiteDeployed != true) {
                unit.useTech(TechType.TechTypes.Parasite, closestEnemyQueen);
                parasiteDeployed = true;

            }
            else if (distanceClosestEnemyScourge <= StarCraftBW_Queen_Constants.OWN_CASTRANGE_ENSNARE
                    && unit.getEnergy() >= StarCraftBW_Queen_Constants.OWN_ENERGYCOST_ENSANRE){

                for (Unit scourge : CommonFunctions.getScourgesInCastrange(unit, 9)){

                    if (scourge.getEnsnareTimer() <= 0) {
                        unit.useTech(TechType.TechTypes.Ensnare, closestEnemyScourge);
                        System.out.print("Ensnare timer: " + closestEnemyScourge.getEnsnareTimer() + "\n");
                    }
                }
            }

            this.countCastShit++;
        }

        CommonFunctions.drawLine(bwapi, unit, closestEnemy.getTargetPosition(), BWColor.Red);
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
