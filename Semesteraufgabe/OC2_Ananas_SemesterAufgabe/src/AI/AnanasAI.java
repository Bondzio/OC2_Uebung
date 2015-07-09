package AI;

import Common.CommonFunctions;
import Hydralisk_XCS.AllHydralisk_XCS_Manager;
import StarCraftBW_XCS_Queen.StarCraftBW_Queen_XCS_Manager;
import Units.*;
import bolding.ParamSet;
import bolding.RuleMachine;
import com.sun.org.apache.xpath.internal.SourceTree;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class AnanasAI {


    public static HashSet<Unit> enemyUnits;
    public static HashSet<IMyUnit> myUnits;
    public static RuleMachine ruleMachine;

    private boolean matchStart;

    private final JNIBWAPI bwapi;
    private AllHydralisk_XCS_Manager allHydralisk_xcs_manager;
    private StarCraftBW_Queen_XCS_Manager queen_xcs_manager;

    private int counter;
    public static int currentFrame;



    //Defance Mod Params
    public static Position rallyPoint;
    public static int rallyPointRadius = 190;
    public static Position middleOfMap = new Position(2048,1536);
    private int vectorReducingFactor = 9;
    public static Hatchery hatcheryToDefend;
    public static Position defencePoint;



    public AnanasAI(JNIBWAPI bwapi) {
        System.out.println("This is the ANANAS_AI ! :)");

        this.bwapi = bwapi;

        init();
    }

    private void init(){
        enemyUnits = new HashSet<>();
        myUnits = new HashSet<>();
        counter = 0;
        currentFrame = 0;

        rallyPoint = null;

        defencePoint = null;
        //middleOfMap = new Position(2048,1536);
        vectorReducingFactor = 9;
        hatcheryToDefend = null;
        matchStart = true;

        this.ruleMachine = new RuleMachine(new ParamSet(1,0.3,0.3,30,3,4,1));
        this.allHydralisk_xcs_manager = new AllHydralisk_XCS_Manager(this.bwapi);
        this.queen_xcs_manager = new StarCraftBW_Queen_XCS_Manager();
    }

    public void doStepAll(){
        currentFrame++;

        if(matchStart)
            matchStart();

        for (IMyUnit u : myUnits) {
            u.step();
//            if(u instanceof Zergling)
//                u.step();

        }
    }

    /*
        #################################################
        ########### For MatchStart  #####################
        #################################################
    */
    private void matchStart(){

        if(rallyPoint == null)
            initRallyPoint();

        if(defencePoint == null)
            initDefencePoint();


        loadProgress();

        matchStart = false;
    }

    private void loadProgress(){
        this.allHydralisk_xcs_manager.loadProcess();
    }

    private void initRallyPoint(){
        for (IMyUnit u : myUnits) {
            if(u instanceof Hatchery){
                if(u.getUnit().getPosition().getPY() <= 500){
                    hatcheryToDefend = (Hatchery) u;
                    break;
                }
            }
        }

        double[] vec = CommonFunctions.calculateVector(middleOfMap.getPX(),middleOfMap.getPY(),hatcheryToDefend.getUnit().getPosition().getPX(),hatcheryToDefend.getUnit().getPosition().getPY());
        rallyPoint = new Position(hatcheryToDefend.getUnit().getPosition().getPX() + (int) vec[0]/vectorReducingFactor,hatcheryToDefend.getUnit().getPosition().getPY() + (int) vec[1]/vectorReducingFactor);
    }

    private void initDefencePoint(){
        for (IMyUnit u : myUnits) {
            if(u instanceof Hatchery){
                if(u.getUnit().getPosition().getPY() <= 500){
                    hatcheryToDefend = (Hatchery) u;
                    break;
                }
            }
        }
        defencePoint = new Position(hatcheryToDefend.getUnit().getPosition().getPX(),hatcheryToDefend.getUnit().getPosition().getPY());
    }


    /*
        #################################################
        ########### During Match  #######################
        #################################################
    */
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
                    GSG9_Zergling zerglingSpecial1 = new GSG9_Zergling(unit, this.bwapi,0);
                    myUnits.add(zerglingSpecial1);
                   // System.out.println("ADDED SPECIAL1 ZERG");
                }
                else if (counter == 1){
                    counter++;
                    GSG9_Zergling zerglingSpecial2 = new GSG9_Zergling(unit, this.bwapi,1);
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
                Hydralisk hydralisk = this.allHydralisk_xcs_manager.createHydralisk(unit);
                myUnits.add(hydralisk);
            }
            else if(type == UnitType.UnitTypes.Zerg_Ultralisk){
                Ultralisk ultralisk = new Ultralisk(unit,this.bwapi);
                myUnits.add(ultralisk);
            }
            else if(type == UnitType.UnitTypes.Zerg_Queen){
                Queen queen = new Queen(unit,this.bwapi,this.queen_xcs_manager);
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
                if(u.getUnit().getID() == hatcheryToDefend.getUnit().getID()){
                    System.out.println("Hatchery to Defend has been destroyed!!!!!!!!!!!!!!!!!!! SHAME!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
                else
                    rmUnit = u.getUnit();
                break;
            }
        }
        myUnits.remove(rmUnit);
    }


    /*
        #################################################
        ########### for MatchEnd  #######################
        #################################################
    */
    public void matchEnd(boolean winner){
        /*
            REINFOLGE BEACHTEN !!!!!
        */
        saveProgressForNextMatch();
        cleanUpForNextMatch(); // sollte immer letzte sein
    }

    private void cleanUpForNextMatch(){
        init();
    }

    private void saveProgressForNextMatch(){
        allHydralisk_xcs_manager.saveProgress();
        queen_xcs_manager.saveProgress();
    }


}
