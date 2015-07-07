package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.ArrayList;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static java.util.Collections.min;

import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;
import java.util.ArrayList;
import java.util.HashSet;
import static java.util.Collections.min;
import static java.util.Arrays.asList;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Hydralisk implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;

    private MyUnitStatus currentUnitStatus = MyUnitStatus.START;

    //for GOING_TO_DEF_POINT
    private int ackRadius = 60; // if a unit is not able to reach its personel def point, it will accept a pos in a Cyrcle around the point with this radius



    public Hydralisk(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;
    }


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

    private boolean goingToDefPointFin(){
        drawMyLine();

        if(unit.getID() % 2 == 0 && AnanasAI.currentFrame <= 10){
            return false;
        }

        CommonFunctions.simpleUnitMove(unit, AnanasAI.defancePoint);
        if(isAtPersonalDefPoint()){
            unit.stop(false);
            return true;
        }
        else{
            return false;
        }

    }

    private void defMode(){
//        if(!unit.isBurrowed())
//            unit.burrow();
    }

    private boolean isAtPersonalDefPoint(){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),AnanasAI.defancePoint)<=ackRadius)
            return true;
        else
            return false;
    }

    public Unit getUnit(){
        return this.unit;
    }


    private void drawMyLine(){
        CommonFunctions.drawLine(bwapi,unit,unit.getTargetPosition(),BWColor.Blue);
    }

}

