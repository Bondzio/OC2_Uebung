package RollesKleineEcke;

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

        this.xcs = new XCS(actionSet);

    }

    public StarCraBW_Effector run(double distance){
        String detected = this.distanceDetector.detect(distance);

        String suggestioedAction = xcs.runMultiStepLearning(detected);

        effector.setChosenAction(suggestioedAction);

        return effector;
    }

}
