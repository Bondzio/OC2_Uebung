package AI;

import Units.*;
import bolding.ParamSet;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class AnanasAI {

    private final JNIBWAPI bwapi;
    public static HashSet<Unit> enemyUnits;
    public static HashSet<IMyUnit> myUnits;
    private int frame = 0;

    private RuleMachine ruleMachine;

    public AnanasAI(JNIBWAPI bwapi) {
        System.out.println("This is the ANANAS_AI ! :)");

        this.bwapi = bwapi;
        enemyUnits = new HashSet<>();
        myUnits = new HashSet<>();

        this.ruleMachine = new RuleMachine(new ParamSet(1,0.3,0.3,10,3,4,1));
    }

    public void doStepAll(){

        for (IMyUnit u : myUnits) {
            u.step();
        }
    }


    public void addUnit(int unitID){
        Unit unit = bwapi.getUnit(unitID);

        if(unit.getPlayer() != bwapi.getSelf()){
            enemyUnits.add(unit);
        }
        else{
            UnitType type = unit.getType();
            if(type == UnitType.UnitTypes.Zerg_Zergling){
                Zergling zergling = new Zergling(unit,this.bwapi,this.ruleMachine);
                myUnits.add(zergling);
                System.out.println("ADDED ZERG");
            }
            else if(type == UnitType.UnitTypes.Zerg_Hydralisk){
                Hydralisk hydralisk = new Hydralisk(unit,this.bwapi);
                myUnits.add(hydralisk);
                System.out.println("ADDED Hydralisk");
            }
            else if(type == UnitType.UnitTypes.Zerg_Ultralisk){
                Ultralisk ultralisk = new Ultralisk(unit,this.bwapi);
                myUnits.add(ultralisk);
                System.out.println("ADDED Ultralisk");
            }
            else if(type == UnitType.UnitTypes.Zerg_Queen){
                Queen queen = new Queen(unit,this.bwapi);
                myUnits.add(queen);
                System.out.println("ADDED Queen");
            }
            else if(type == UnitType.UnitTypes.Zerg_Scourge){
                Scourge scourge = new Scourge(unit,this.bwapi);
                myUnits.add(scourge);
                System.out.println("ADDED Scourge");
            }
        }

    }


    public void removeUnit(int unitID){
        Unit rmUnit = null;
        for (Unit u : enemyUnits) {
            if (u.getID() == unitID) {
                rmUnit = u;
                break;
            }
        }
        for (IMyUnit u : myUnits) {

            if (u.getUnit().getID() == unitID) {
                rmUnit = u.getUnit();
                break;
            }
        }

        enemyUnits.remove(rmUnit);
    }

    public void matchEnd(boolean winner){
//        Unit vultureUnit = vulture.getMyUnit();
//        int hpVulture = vultureUnit.getHitPoints();
//        int kills= vultureUnit.getKillCount();
//
//        int cAttackMove = vulture.getCountAttackMove();
//        int cKite = vulture.getCountKite();
//
//        vulture.xcs_Manager.makeNewMatchStat(frame,hpVulture,kills,cAttackMove,cKite);
//        vulture.xcs_Manager.cleanUp();
//        matches++;
//        System.out.println("MatchOver - Matches:" + matches);
    }
}
