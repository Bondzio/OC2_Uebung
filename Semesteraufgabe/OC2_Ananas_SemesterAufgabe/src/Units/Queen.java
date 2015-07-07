package Units;

import AI.AnanasAI;
import AI.MyUnitStatus;
import Common.CommonFunctions;
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



    public Queen(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;

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


    private boolean goingToDefPointFin(){

        CommonFunctions.simpleUnitMove(unit, AnanasAI.hatcheryToDefend.getUnit().getPosition());

        if(isAtPersonalDefPoint()){
            return true;
        }
        else{
            return false;
        }
    }


    private boolean isAtPersonalDefPoint(){
        if(CommonFunctions.getDistianceBetweenPositions(unit.getPosition(),AnanasAI.hatcheryToDefend.getUnit().getPosition())<=ackRadius)
            return true;
        else
            return false;
    }

    public Unit getUnit(){
        return this.unit;
    }


}
