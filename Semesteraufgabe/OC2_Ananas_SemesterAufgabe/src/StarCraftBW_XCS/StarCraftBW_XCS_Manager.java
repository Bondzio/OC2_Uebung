package StarCraftBW_XCS;

import General_XCS.PopulationSet;
import General_XCS.XCS_Constants;
import jnibwapi.Unit;

public class StarCraftBW_XCS_Manager {

    private StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
    private StarCraftBW_XCS xcs;
    private StarCraftBW_MatchStats mStats = new StarCraftBW_MatchStats();


    private StarCraftBW_DistanceDetector dDetector= new StarCraftBW_DistanceDetector();
    private StarCraftBW_Effector theEffector = new StarCraftBW_Effector();
    private String[] actionSet = {"attackMove","kite"};


    public StarCraftBW_XCS_Manager() {
        this.xcs = new StarCraftBW_XCS(actionSet,theEffector,dDetector);
        fileThread.start();
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

    public void setGA_Type(String typeName){
        PopulationSet populationSet = xcs.getPopulationSet();
        switch (typeName){
            case "ga1":
                populationSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_FIND_BEST);
                populationSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_ONE_POINT);
                populationSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
                break;
            case "ga2":
                populationSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
                populationSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_ONE_POINT);
                populationSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
                break;
            case "ga3":
                populationSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
                populationSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_RANDOM_ONE_POINT);
                populationSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
                break;
            case "ga4":
                populationSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
                populationSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_ONE_POINT);
                populationSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_NONE);
                break;
        }
        fileThread.setFilePath_cSet(typeName + "_saves");
        fileThread.setFilePath_mStats(typeName + "_stats");
    }


    public StarCraftBW_DistanceDetector getDetector(){
        return this.dDetector;
    }

    public StarCraftBW_Effector getEffector() {
        return this.theEffector;
    }

    public void makeNewMatchStat(int frames, int hp, int kills, int countAttackMove, int countKite) {
        this.mStats.makeNewMatchStat(frames,hp,kills,countAttackMove,countKite);
    }

    public void saveProgress(){
        try {
            fileThread.putClassifierSetToSave(xcs.getPopulationSet());
            fileThread.putMatchStatsToSave(this.mStats);

        } catch (InterruptedException e) {
            System.err.println("XCS_Manager: SAVING INTERUPTED");
        }
    }

    public void loadOldProgress(){
        loadOldPopulationSet();
        loadOldMatchStats();
    }

    private void loadOldPopulationSet(){
        PopulationSet newPSet = fileThread.getSavedPopulationSet();

        if (newPSet == null)
            return;
        
        else if(newPSet.getSet().size() > 0)
            xcs.setPopulationSet(newPSet); 
    }

    private void loadOldMatchStats(){
        StarCraftBW_MatchStats newMStats = fileThread.getSavedMatchStats();

        if (newMStats == null)
            return;

        else if(newMStats.getMatchStats().size() > 0)
            this.mStats = newMStats;
    }

    private void saveOnlyOnce(){
        saveProgress();
        fileThread.stopMe();
    }

    public void cleanUp(){
        saveOnlyOnce();
    }
}