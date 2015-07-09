package StarCraftBW_XCS_Queen;

import FileHandler.SimpleFileHandler;
import General_XCS.PopulationSet;
import General_XCS.XCS_Constants;
import jnibwapi.Unit;

/**
 * Created by Papa on 09.07.2015.
 */
public class StarCraftBW_Queen_XCS_Manager {

    private SimpleFileHandler simpleFileHandler = new SimpleFileHandler(null,null,"Queen_XCS_PopulationSet");
    private StarCraftBW_Queen_XCS xcs;
    private StarCraftBW_QueenMatchStats mStats = new StarCraftBW_QueenMatchStats();
    private StarCraftBW_QueenDetector dDetector= new StarCraftBW_QueenDetector();
    private StarCraftBW_QueenEffector theEffector = new StarCraftBW_QueenEffector();
    private String[] actionSet = {"cast","kite"};

    public StarCraftBW_Queen_XCS_Manager() {
        this.xcs = new StarCraftBW_Queen_XCS(actionSet,theEffector,dDetector);
    }

    public String getNextPredictedAction(){
        xcs.cleanCurrentAction();
        xcs.execPredictAction(true);
        return theEffector.getCurrentActionToExecute();
    }

    public void actionExecutionFin(Unit unit, Unit target, Double distance){
        theEffector.setStats(unit, target, distance);
        xcs.rewardCurrentAction();
    }

    public void setGA_Type(){
        PopulationSet pSet = xcs.getPopulationSet();
        pSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
        pSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_RANDOM_ONE_POINT);
        pSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
    }

    public StarCraftBW_QueenDetector getDetector(){
        return this.dDetector;
    }

    public StarCraftBW_QueenEffector getEffector() {
        return this.theEffector;
    }



    public void saveProgress(){
        simpleFileHandler.savePopulationSet(xcs.getPopulationSet());
    }

    public void loadOldProgress(){
        loadOldPopulationSet();
    }

    private void loadOldPopulationSet(){
        PopulationSet newPSet = simpleFileHandler.laodPopulationSet();

        if (newPSet == null)
            return;

        else if(newPSet.getSet().size() > 0)
            xcs.setPopulationSet(newPSet);

        setGA_Type();
    }

}