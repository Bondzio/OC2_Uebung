package StarCraftBW_XCS;

/**
 * Created by Rolle on 11.05.2015.
 */
public class StarCraBW_DistanceDetector {
    
    private int valtureMaxRange = 160;
    private int zealotMaxRange = 15;


    public String detect(double distance){
        int convDistance = (int) distance;
        if (convDistance > 160)
            convDistance = 161; // as Binary 10100001

        return Integer.toBinaryString(convDistance);
    }
}
