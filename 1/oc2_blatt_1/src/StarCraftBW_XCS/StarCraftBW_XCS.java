package StarCraftBW_XCS;

import General_XCS.XCS;

public class StarCraftBW_XCS {

	private String[] actionSet = {"move", "kite"};

    private StarCraftBW_DistanceDetector distanceDetector = new StarCraftBW_DistanceDetector();
    private StarCraftBW_Effector effector = new StarCraftBW_Effector();
    private XCS xcs;

    public StarCraftBW_XCS() {
        this.xcs = new XCS(actionSet,effector,distanceDetector);
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
}
