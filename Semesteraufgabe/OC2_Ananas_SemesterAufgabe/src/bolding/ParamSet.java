package bolding;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rolle on 26.06.2015.
 */
public class ParamSet{

    private final double w1;
    private final double w3;
    private final double w4;

    private final int rangeOfNeighborhood; // r_sig from the paper

    private final int coulumFormationFileNumber;
    private final int lineFormationRankNumber;



    private int reward;

    public ParamSet(double w1, double w3, double w4, int rangeOfNeighborhood, int coulumFormationFileNumber, int lineFormationRankNumber, int reward) {
        this.w1 = w1;
        this.w3 = w3;
        this.w4 = w4;
        this.rangeOfNeighborhood = rangeOfNeighborhood;
        this.coulumFormationFileNumber = coulumFormationFileNumber;
        this.lineFormationRankNumber = lineFormationRankNumber;
        this.reward = reward;
    }

    public ParamSet(HashMap<String,String> memberAsMap){
        this.w1 = Double.valueOf(memberAsMap.get("w1").split(";")[0]);
        this.w3 = Double.valueOf(memberAsMap.get("w3").split(";")[0]);
        this.w4 = Double.valueOf(memberAsMap.get("w4").split(";")[0]);

        this.rangeOfNeighborhood = Integer.valueOf(memberAsMap.get("rangeOfNeighborhood").split(";")[0]);
        this.coulumFormationFileNumber = Integer.valueOf(memberAsMap.get("coulumFormationFileNumber").split(";")[0]);
        this.lineFormationRankNumber = Integer.valueOf(memberAsMap.get("lineFormationRankNumber").split(";")[0]);
        this.reward = Integer.valueOf(memberAsMap.get("reward").split(";")[0]);
    }



    public double getW1() {
        return w1;
    }

    public double getW3() {
        return w3;
    }

    public double getW4() {
        return w4;
    }

    public double getRangeOfNeighborhood() {
        return rangeOfNeighborhood;
    }

    public int getCoulumFormationFileNumber() {
        return coulumFormationFileNumber;
    }

    public int getLineFormationRankNumber() {
        return lineFormationRankNumber;
    }

    public int getReward() {
        return reward;
    }

    public HashMap<String,String> getMembersAsMap(){
        String delimiter = ";";
        HashMap<String,String> memberAsMap = new HashMap<String, String>();
        memberAsMap.put("w1", Double.toString(this.w1) + delimiter + "D" );
        memberAsMap.put("w3", Double.toString(this.w3) + delimiter + "D" );
        memberAsMap.put("w4", Double.toString(this.w4) + delimiter + "D" );
        memberAsMap.put("rangeOfNeighborhood", Integer.toString(this.rangeOfNeighborhood) + delimiter + "I" );
        memberAsMap.put("coulumFormationFileNumber", Integer.toString(this.coulumFormationFileNumber)+ delimiter + "I" );
        memberAsMap.put("lineFormationRankNumber", Integer.toString(this.lineFormationRankNumber)+ delimiter + "I" );
        memberAsMap.put("reward", Integer.toString(this.reward)+ delimiter + "I" );

        return memberAsMap;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

}
