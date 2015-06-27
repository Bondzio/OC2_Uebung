package myStupidAI.bolding;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Rolle on 27.06.2015.
 */
public class fileAnalyser {

    public static void main(String[] args){
        SimpleFileHandler simpleFileHandler = new SimpleFileHandler();
        ParamSetCollection paramSetCollection = simpleFileHandler.loadParamSetCollection();

        Collections.sort(paramSetCollection.getParams(), new Comparator<ParamSet>() {
            public int compare(ParamSet p_one, ParamSet p_two) {
                int reward_p_one = p_one.getReward();
                int reward_p_two = p_two.getReward();
                if(reward_p_one> reward_p_two)
                    return 1;
                else if(reward_p_one == reward_p_two)
                    return 0;
                else
                    return -1;
            }
        });

        for(ParamSet pSet : paramSetCollection.getParams()){
            System.out.println(pSet.getMembersAsMap());
        }
    }
}
