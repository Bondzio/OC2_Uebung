package Units;

import AI.AnanasAI;
import Common.CommonFunctions;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Zergling implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;
    private final RuleMachine ruleMachine;


    private HashSet<Unit> units = new HashSet<>();

    public Zergling(Unit unit, JNIBWAPI bwapi, RuleMachine ruleMachine) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.ruleMachine = ruleMachine;
    }

    public void step() {
        Unit enemy = CommonFunctions.getClosestEnemy(unit);

        Position targetPosition = unit.getTargetPosition();
        bwapi.drawLine(unit.getPosition(), targetPosition, BWColor.Green, false);

        move(enemy);

    }



    public void move(Unit target){

        units.clear();
        for(IMyUnit myUnit: AnanasAI.myUnits){
            if(myUnit instanceof Zergling)
                units.add(myUnit.getUnit());
        }


        double[] final_vector = ruleMachine.calcFlockVectorToPos(unit,units,new int[]{2000,2000});
        //double[] final_vector = {1000,1000};

        //System.out.println("Z ID:" + unit.getID() + " UnitsSize:" + units.size() + " Vec:" + Arrays.toString(final_vector));
        Position tragetPosition = new Position((int) final_vector[0],(int) final_vector[1]);
        tragetPosition.makeValid();
        CommonFunctions.simpleUnitMove(unit,tragetPosition);
    }






    public Unit getUnit(){
        return this.unit;
    }
}
