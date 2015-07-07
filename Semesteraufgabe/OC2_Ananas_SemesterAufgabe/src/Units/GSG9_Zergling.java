package Units;

import AI.AnanasAI;
import Common.CommonFunctions;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.HashSet;

/**
 * Created by Rolle on 07.07.2015.
 */
public class GSG9_Zergling implements IMyUnit{

    final private JNIBWAPI bwapi;
    final private Unit unit;
    private int mission;



    public GSG9_Zergling(Unit unit, JNIBWAPI bwapi,int mission) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.mission = mission;
    }

    public void step() {
        Position targetPosition = unit.getTargetPosition();
        bwapi.drawLine(unit.getPosition(), targetPosition, BWColor.Green, false);


        specialMissions(mission);

    }

    public void specialMissions(int mission){
        Position[] hatcheries = CommonFunctions.getEnemyHatcheries(bwapi);

        if (mission == 1 || mission == 2) {
            if (unit.isIdle()) {
                if (unit.getDistance(CommonFunctions.getClosestEnemyZergHatchery(unit)) > 200) {
                    unit.move(hatcheries[mission - 1], false);
                }
                else{
                    unit.attack(CommonFunctions.getClosestEnemyZergHatchery(unit), false);
                }
            }
        }
    }

    public Unit getUnit(){
        return this.unit;
    }

}
