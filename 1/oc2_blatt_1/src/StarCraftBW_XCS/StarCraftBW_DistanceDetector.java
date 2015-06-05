package StarCraftBW_XCS;

import General_XCS.IDetector;


public class StarCraftBW_DistanceDetector implements IDetector{
    
    private int valtureMaxRange = 160;
    private int zealotMaxRange = 15;
    private double currentDistance = 0;

    public void setDistance(double distance){this.currentDistance = distance;}


    public String convertDistance() {
        double distance = this.currentDistance;
        int convDistance = (int) distance;
        if (convDistance > 160)
            convDistance = 161; // as Binary 10100001

        String ret = makeBinaryStringFixLength(8,Integer.toBinaryString(convDistance));
        return ret;
    }

    private String makeBinaryStringFixLength(int length, String baseBString){
        String ret = "";
        if(baseBString.length() < length){
            String zeroSting = "";
            for(int i = 0; i < (length-baseBString.length()); i++){
                zeroSting += "0";
            }
            ret = zeroSting + baseBString;
        }
        else
            ret = baseBString;
        return ret;
    }
    
    public String getDetected() {
        return convertDistance();
    }
}
