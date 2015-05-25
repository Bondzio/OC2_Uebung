package StarCraftBW_XCS;

import General_XCS.PopulationSet;
import General_XCS.XCS;

public class StarCraftBW_XCS {

	private String[] actionSet = {"move", "kite"};

    private StarCraftBW_DistanceDetector distanceDetector = new StarCraftBW_DistanceDetector();
    private StarCraftBW_Effector effector = new StarCraftBW_Effector();
    private XCS xcs;
    private StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();


    public StarCraftBW_XCS() {
        this.xcs = new XCS(actionSet,effector,distanceDetector);
        fileThread.start();
    }

    public StarCraftBW_DistanceDetector getDetector(){
    	return this.distanceDetector;
    }

    public StarCraftBW_Effector getEffector(){
    	return this.effector;
    }
    
    public String run(){
        return xcs.runMultiStepLearning();
    }

    public void saveProgress(){
        try {
            fileThread.putClassifierSetToSave(xcs.getPopulationSet());
        } catch (InterruptedException e) {
        }
    }

    public void loadOldProgress(){
        PopulationSet newPSet = fileThread.getSavedClassifierSet();
        xcs.setPopulationSet(newPSet);
    }




}
