package StarCraftBW_XCS;

import General_XCS.PopulationSet;
import jnibwapi.Unit;

/**
 * Created by Rolle on 25.05.2015.
 */
public class StarCraftBW_XCS_Manager {

    private StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
    private StarCraftBW_XCS xcs;


    private StarCraftBW_DistanceDetector dDetector= new StarCraftBW_DistanceDetector();
    private StarCraftBW_Effector theEffector = new StarCraftBW_Effector();
    private String[] actionSet = {"move","kite"};

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
        theEffector.setStats(unit,target,distance);
        xcs.rewardCurrentAction();
    }

    public StarCraftBW_DistanceDetector getDetector(){
        return this.dDetector;
    }

    public StarCraftBW_Effector getEffector() {
        return this.theEffector;
    }

    public void saveProgress(){
        try {
            fileThread.putClassifierSetToSave(xcs.getPopulationSet());
        } catch (InterruptedException e) {
        }
    }

    public void loadOldProgress(){
        PopulationSet newPSet = fileThread.getSavedPopulationSet();
        xcs.setPopulationSet(newPSet);
    }


    private void saveOnlyOnce(){
        saveProgress();
        fileThread.stopMe();
    }

    private void cleanUp(){

    }



}
