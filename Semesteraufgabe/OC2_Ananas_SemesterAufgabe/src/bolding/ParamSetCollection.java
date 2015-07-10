package bolding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Rolle on 26.06.2015.
 */
public class ParamSetCollection {
    private final ArrayList<ParamSet> params;
    private int maxParamSize = 50000;

    public ParamSetCollection(ArrayList<ParamSet> params) {
        this.params = params;
    }

    public ParamSetCollection(){
        params = new ArrayList<>();
    }

    public void addNewParamSet(ParamSet param){
        this.params.add(param);
    }

    public ArrayList<ParamSet> getParams() {
        return params;
    }

    public void cleanUp(){
        //removeAllZeroRewards();
        if(params.size() >= maxParamSize)
            removePercentOfLowestRewarded();

    }

    private void removeAllZeroRewards(){
        ArrayList<ParamSet> elementsToRemove = findElementsToRemove();

        for (ParamSet pSet: elementsToRemove)
            params.remove(pSet);
    }

    private void removePercentOfLowestRewarded(){
        //ArrayList<ParamSet> paramSorted = params;
        Collections.sort(params, new Comparator<ParamSet>() {
            @Override
            public int compare(ParamSet o1, ParamSet o2) {
                double diff = o1.getReward() - o2.getReward();
                if (diff < 0.0)
                    return -1;
                else if (diff == 0.0)
                    return 0;
                else
                    return +1;

            }
        });

        double elementsToDelete = params.size() - params.size() * 0.2; // delete 20% so we got room for new ones
        for(int i = 0; i < elementsToDelete; i++)
            params.remove(0);
    }

    private ArrayList<ParamSet> findElementsToRemove(){
        ArrayList<ParamSet> elementsToRemove = new ArrayList<>();
        for(ParamSet pSet : params){
            if(pSet.getReward() <= 0)
                elementsToRemove.add(pSet);
        }
        return elementsToRemove;
    }
}
