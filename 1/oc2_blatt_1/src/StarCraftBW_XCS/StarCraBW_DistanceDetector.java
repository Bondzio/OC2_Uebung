package StarCraftBW_XCS;

import General_XCS.IDetector;

/**
 * Created by Rolle on 11.05.2015.
 */
public class StarCraBW_DistanceDetector implements IDetector{
    
    private int valtureMaxRange = 160;
    private int zealotMaxRange = 15;
    private double currentDistance = 0;

    public void setDistance(double distance){this.currentDistance = distance;}


    public String checkDistance(double env) {
        double distance = env;
        int convDistance = (int) distance;
        if (convDistance > 160)
            convDistance = 161; // as Binary 10100001

        return Integer.toBinaryString(convDistance);
    }

    @Override
    public String getDetected() {
        return checkDistance(this.currentDistance);
    }
}
