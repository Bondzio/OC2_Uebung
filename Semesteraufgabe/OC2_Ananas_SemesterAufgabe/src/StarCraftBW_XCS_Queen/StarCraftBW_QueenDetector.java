package StarCraftBW_XCS_Queen;

import General_XCS.IDetector;

/**
 * Created by Papa on 07.07.2015.
 */
public class StarCraftBW_QueenDetector implements IDetector {

    private double currentDistance = 0;
    private int maxBinaryStringLength = 9;

    public void setDistance(double distance){this.currentDistance = distance;}

    public String convertDistance() {
        double distance = this.currentDistance;
        int convDistance = (int) distance;
        if (convDistance > 320)
            convDistance = 321;

        String ret = makeBinaryStringFixLength(maxBinaryStringLength,Integer.toBinaryString(convDistance));
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