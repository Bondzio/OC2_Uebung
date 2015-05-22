package StarCraftBW_XCS;

import General_XCS.XCS;

/**
 * Created by Rolle on 16.05.2015.
 */
public class StarCraBW_XCS {

    /*
        Constants
     */

    private String[] actionSet = {"move", "kite"};


    private StarCraBW_DistanceDetector distanceDetector = new StarCraBW_DistanceDetector();
    private StarCraBW_Effector effector = new StarCraBW_Effector();
    private XCS xcs;

    public StarCraBW_XCS() {

        this.xcs = new XCS(actionSet,effector,distanceDetector);

    }

    public StarCraBW_DistanceDetector getDetector(){return this.distanceDetector;}

    public void run(double distance){
        xcs.runMultiStepLearning();
    }

}
