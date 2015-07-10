package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_XCS_Manager;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_Constants;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.OrderType;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Queen implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;
    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;
    private Unit parasitedUnit = null;
    private boolean start = true;

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
        if(AnanasAI.currentFrame % 2 == 0)
            return;

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

    /*
    #################################################
    ########### For GOING_TO_DEF_POINT ##############
    #################################################
    */
    private boolean goingToDefPointFin(){
        Position defPoint = AnanasAI.rallyPoint;

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
        Unit closestEnemy = CommonFunctions.getClosestEnemy(bwapi, unit);
        Unit closestEnemyQueen = CommonFunctions.getClosestEnemyZergQueen(unit);
        Unit closestEnemyScourge = CommonFunctions.getClosestEnemyScourge(unit);
        double distanceClosestEnemy = Double.POSITIVE_INFINITY;
        double distanceClosestEnemyQueen = Double.POSITIVE_INFINITY;
        double distanceClosestEnemyScourge = Double.POSITIVE_INFINITY;

        if(closestEnemy != null) {
            distanceClosestEnemy = CommonFunctions.getDistanceBetweenUnits(unit, closestEnemy);
        }
        if(closestEnemyQueen != null) {
            distanceClosestEnemyQueen = CommonFunctions.getDistanceBetweenUnits(unit, closestEnemyQueen);
        }
        if(closestEnemyScourge != null){
            distanceClosestEnemyScourge = CommonFunctions.getDistanceBetweenUnits(unit, closestEnemyScourge);
        }

        HashSet<Unit> scourgesInCastrange = CommonFunctions.getScourgesInCastrange(bwapi, unit, 9);

        queen_xcs_manager.getDetector().setDistance(distanceClosestEnemy);

        if (!start) {
            queen_xcs_manager.actionExecutionFin(unit, closestEnemy, distanceClosestEnemy);
        }
        else {
            queen_xcs_manager.setGA_Type();
            queen_xcs_manager.loadOldProgress();
            start = false;
        }

        String action = queen_xcs_manager.getNextPredictedAction();

        if (!isThereSomethingToReward)
            isThereSomethingToReward = true;

        if (action.equals("kite")) {
            if (distanceClosestEnemy <= (StarCraftBW_Queen_Constants.HYDRALISK_WEAPONRANGE * 1.5)) {
                CommonFunctions.advancedKiteQueen(bwapi, unit, closestEnemy, StarCraftBW_Queen_Constants.HYDRALISK_WEAPONRANGE, 300);
            }
        }

        else if (action.equals("cast")) {

            if (distanceClosestEnemyQueen <= StarCraftBW_Queen_Constants.OWN_CASTRANGE_PARASITE - 16
                    && unit.getEnergy() >= StarCraftBW_Queen_Constants.OWN_ENERGYCOST_PARASITE
                    && unit.isIdle()
                    && !closestEnemyQueen.isParasited()){

                if (parasitedUnit == null){
                    unit.useTech(TechType.TechTypes.Parasite, closestEnemyQueen);
//                    System.out.print("test1" + "\n");
                    parasitedUnit = closestEnemyQueen;
                }

                else if (parasitedUnit.getType() != UnitType.UnitTypes.Zerg_Queen) {
                    unit.useTech(TechType.TechTypes.Parasite, closestEnemyQueen);
//                    System.out.print("test2" + "\n");
                    parasitedUnit = closestEnemyQueen;
                }
            }

            else if (distanceClosestEnemyScourge <= StarCraftBW_Queen_Constants.OWN_CASTRANGE_PARASITE - 16
                    && unit.getEnergy() >= StarCraftBW_Queen_Constants.OWN_ENERGYCOST_PARASITE
                    && unit.isIdle()
                    && !closestEnemyScourge.isParasited()
                    && CommonFunctions.getParasitedEnemysCount(bwapi) <= 0) {

                if (parasitedUnit == null) {
                    unit.useTech(TechType.TechTypes.Parasite, closestEnemyScourge);
//                    System.out.print("test3" + "\n");
                    parasitedUnit = closestEnemyScourge;
                }

                else if (parasitedUnit.getType() != UnitType.UnitTypes.Zerg_Queen
                        && parasitedUnit.getType() != UnitType.UnitTypes.Zerg_Scourge) {

                    unit.useTech(TechType.TechTypes.Parasite, closestEnemyScourge);
//                    System.out.print("test4" + "\n");
                    parasitedUnit = closestEnemyScourge;
                }
            }

            if (distanceClosestEnemyScourge <= StarCraftBW_Queen_Constants.OWN_CASTRANGE_ENSNARE - 16
                    && unit.getEnergy() >= StarCraftBW_Queen_Constants.OWN_ENERGYCOST_ENSANRE
                    && unit.isIdle()) {

                for (Unit scourge : scourgesInCastrange){
                    if (!scourge.isEnsnared()
                            && unit.getEnergy() >= StarCraftBW_Queen_Constants.OWN_ENERGYCOST_ENSANRE
                            && CommonFunctions.getDistanceBetweenUnits(scourge, unit) <= StarCraftBW_Queen_Constants.OWN_CASTRANGE_ENSNARE - 16) {
                        unit.useTech(TechType.TechTypes.Ensnare, scourge);
//                        System.out.print("test5" + "\n");
                    }
                }
            }
        }

        if (closestEnemy != null) {
            //CommonFunctions.drawLine(bwapi, unit, closestEnemy.getTargetPosition(), BWColor.Red);
        }
    }

    private boolean isAtPersonalDefPoint(Position defPoint){
        int ackRadius = 40;
        return CommonFunctions.getDistianceBetweenPositions(unit.getPosition(), defPoint) <= ackRadius;
    }

    public Unit getUnit(){
        return this.unit;
    }
}
