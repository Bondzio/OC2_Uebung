package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.TechType;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Zergling implements IMyUnit{
    final private JNIBWAPI bwapi;
    private final  Unit unit;
    private final RuleMachine ruleMachine;
    private HashSet<Unit> units = new HashSet<>();


    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;


    //for GOING_TO_DEF_POINT
    private Position myPersonelDefencePoint;
//    private boolean initPDefenacePoint = true;
//    private boolean initTmpPoint = true;
    private boolean initTmpPoint = false;
    private boolean initPDefenacePoint = false;

    private int myPersonelDefencePointRadius = 70; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius

    //for IN_DEF_MODE



    public Zergling(Unit unit, JNIBWAPI bwapi, RuleMachine ruleMachine) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.ruleMachine = ruleMachine;
    }



    @Override
    public void step() {

        switch(currentUnitStatus){
            case START:
                currentUnitStatus = MyUnitStatus.GOING_TO_DEF_POINT;
                break;
            case GOING_TO_DEF_POINT:
                if(!unit.isIdle())
                    break;
                if(goingToDefPointFin()){
                    unit.burrow();
                    if(unit.isBurrowed()) {
                        currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                        System.out.println(this.getClass().getName() + " entert def at Frame: " + AnanasAI.currentFrame);
                    }
                }

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
        drawMyLine();

//        if(prepareForGoingToPersonalDefPos())
//            return false;
//
//        if(!initPDefenacePoint) {
//            myPersonelDefencePoint = CommonFunctions.getRndPosInDefCircle(AnanasAI.defancePoint, AnanasAI.defancePointRadius);
//            initPDefenacePoint = true;
//        }
//
//        if(isAtPersonalDefPoint()){
//            CommonFunctions.simpleUnitMove(unit, unit.getPosition());
//            return true;
//        }
//        else{
//            swarmMoveToPosition(myPersonelDefencePoint);
//            return false;
//        }

        if(!initPDefenacePoint ){
            if(prepareForGoingToPersonalDefPos()) {
                myPersonelDefencePoint = null;
                myPersonelDefencePoint = CommonFunctions.getRndPosInDefCircle(AnanasAI.defancePoint, AnanasAI.defancePointRadius);
                initPDefenacePoint = true;
            }
        }
        else{
            if (isAtPersonalDefPoint()) {
                unit.stop(false);
                return true;
            } else {
                swarmMoveToPosition(myPersonelDefencePoint);
            }
        }
        return false;


    }

    private boolean prepareForGoingToPersonalDefPos(){

        if(!initTmpPoint) {
            double[] vec = CommonFunctions.calculateVector(AnanasAI.middleOfMap.getPX(),AnanasAI.middleOfMap.getPY(),AnanasAI.hatcheryToDefend.getUnit().getPosition().getPX(),AnanasAI.hatcheryToDefend.getUnit().getPosition().getPY());
            myPersonelDefencePoint = new Position(AnanasAI.hatcheryToDefend.getUnit().getPosition().getPX() + (int) vec[0]/2,AnanasAI.hatcheryToDefend.getUnit().getPosition().getPY() + (int) vec[1]/2);
            initTmpPoint = true;
        }

        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),myPersonelDefencePoint) <= 200){
            return true;
        }
        else{
            CommonFunctions.simpleUnitMove(unit, myPersonelDefencePoint);
            return false;
        }
    }

    private boolean isAtPersonalDefPoint(){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),myPersonelDefencePoint)<=myPersonelDefencePointRadius)
            return true;
        else
            return false;
    }


    private void defMode(){
//        if(!unit.isBurrowed())
//            unit.burrow();
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
        CommonFunctions.simpleUnitMove(unit,tragetPosition);
    }





    public Unit getUnit(){
        return this.unit;
    }



    private void drawMyLine(){
        CommonFunctions.drawLine(bwapi,unit,unit.getTargetPosition(),BWColor.Green);
    }




}
