package Units;

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

    public Ultralisk(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;

    }


    public void step() {
        switch(currentUnitStatus){
            case START:
                currentUnitStatus = MyUnitStatus.GOING_TO_RALLY_POINT;
                break;
            case GOING_TO_RALLY_POINT:
                if(goingToDefPointFin()){
                    currentUnitStatus = MyUnitStatus.IN_DEF_MODE;
                System.out.println( this.getClass().getName() + " entert def at Frame: " + AnanasAI.currentFrame);
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
