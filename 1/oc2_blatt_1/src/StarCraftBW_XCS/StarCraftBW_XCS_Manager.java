package StarCraftBW_XCS;

import General_XCS.PopulationSet;
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
            System.out.println("XCS_Manager: INTERUPTED");
        }
    }

    public void loadOldProgress(){
        loadOldPopulationSet();
        loadOldMatchStats();
    }

    private void loadOldPopulationSet(){
        PopulationSet newPSet = fileThread.getSavedPopulationSet();


        if (newPSet == null){
            System.out.println("Manager: Loaded Set was empty");
            return;
        }
        else if(newPSet.getSet().size() > 0){
            System.out.println("Manager: Loaded old pSet");
            xcs.setPopulationSet(newPSet);
        }
        else{
            System.out.println("Manager: loaded pSet was empty");
        }
    }

    private void loadOldMatchStats(){
        StarCraftBW_MatchStats newMStats = fileThread.getSavedMatchStats();


        if (newMStats == null){
            System.out.println("Manager: Loaded MatchStats was empty");
            return;
        }
        else if(newMStats.getMatchStats().size() > 0){
            System.out.println("Manager: Loaded old MatchStats");
            this.mStats = newMStats;
        }
        else{
            System.out.println("Manager: loaded MatchStats was empty");
        }
    }

    private void saveOnlyOnce(){
        saveProgress();
        fileThread.stopMe();
    }

    public void cleanUp(){
        saveOnlyOnce();
    }
}