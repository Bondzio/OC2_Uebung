package AI;

import Common.CommonFunctions;
import Units.*;
import bolding.ParamSet;
import bolding.RuleMachine;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class AnanasAI {

    private final JNIBWAPI bwapi;
    public static HashSet<Unit> enemyUnits;
    public static HashSet<IMyUnit> myUnits;

    public static RuleMachine ruleMachine;


    private int counter = 0;
    private int currentFrame = 0;



    //Defance Mod Params
    public static Position defancePoint = null;
    public static int defancePointRadius = 190;
    public static Position middleOfMap = new Position(2048,1536);
    private int vectorReducingFactor = 9;
    public static Hatchery hatcheryToDefend = null;



    public AnanasAI(JNIBWAPI bwapi) {
        System.out.println("This is the ANANAS_AI ! :)");

        this.bwapi = bwapi;
        enemyUnits = new HashSet<>();
        myUnits = new HashSet<>();

        this.ruleMachine = new RuleMachine(new ParamSet(1,0.3,0.3,30,3,4,1));

    }

    public void doStepAll(){
        currentFrame++;
        if(defancePoint == null)
            initDefancePoint();

        for (IMyUnit u : myUnits) {
            u.step();
        }
    }



    private void initDefancePoint(){
        for (IMyUnit u : myUnits) {
            if(u instanceof Hatchery){
                hatcheryToDefend = (Hatchery) u;
                break;
            }
        }

        double[] vec = CommonFunctions.calculateVector(middleOfMap.getPX(),middleOfMap.getPY(),hatcheryToDefend.getUnit().getPosition().getPX(),hatcheryToDefend.getUnit().getPosition().getPY());
        defancePoint = new Position(hatcheryToDefend.getUnit().getPosition().getPX() + (int) vec[0]/vectorReducingFactor,hatcheryToDefend.getUnit().getPosition().getPY() + (int) vec[1]/vectorReducingFactor);
    }


    public void addUnit(int unitID){
        Unit unit = bwapi.getUnit(unitID);

        if(unit.getPlayer() != bwapi.getSelf()){
            enemyUnits.add(unit);
        }
        else{
            UnitType type = unit.getType();
            if(type == UnitType.UnitTypes.Zerg_Zergling){
                if (counter == 0){
                    counter++;
                    GSG9_Zergling zerglingSpecial1 = new GSG9_Zergling(unit, this.bwapi,1);
                    myUnits.add(zerglingSpecial1);
                   // System.out.println("ADDED SPECIAL1 ZERG");
                }
                else if (counter == 1){
                    counter++;
                    GSG9_Zergling zerglingSpecial2 = new GSG9_Zergling(unit, this.bwapi,2);
                    myUnits.add(zerglingSpecial2);
                    //System.out.println("ADDED SPECIAL2 ZERG");
                }
                else {
                    Zergling zergling = new Zergling(unit, this.bwapi, this.ruleMachine);
                    myUnits.add(zergling);
                    //System.out.println("ADDED ZERG");
                }
            }
            else if(type == UnitType.UnitTypes.Zerg_Hydralisk){
                Hydralisk hydralisk = new Hydralisk(unit,this.bwapi);
                myUnits.add(hydralisk);
            }
            else if(type == UnitType.UnitTypes.Zerg_Ultralisk){
                Ultralisk ultralisk = new Ultralisk(unit,this.bwapi);
                myUnits.add(ultralisk);
            }
            else if(type == UnitType.UnitTypes.Zerg_Queen){
                Queen queen = new Queen(unit,this.bwapi);
                myUnits.add(queen);
            }
            else if(type == UnitType.UnitTypes.Zerg_Scourge){
                Scourge scourge = new Scourge(unit,this.bwapi);
                myUnits.add(scourge);
            }
            else if(type == UnitType.UnitTypes.Zerg_Hatchery){
                Hatchery hatchery = new Hatchery(unit,this.bwapi);
                myUnits.add(hatchery);
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
        enemyUnits.remove(rmUnit);

        for (IMyUnit u : myUnits) {

            if (u.getUnit().getID() == unitID) {
                rmUnit = u.getUnit();
                break;
            }
        }
        myUnits.remove(rmUnit);
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
