package myStupidAI.bolding;

import java.util.ArrayList;

/**
 * Created by Rolle on 26.06.2015.
 */
public class ParamSetCollection {
    private final ArrayList<ParamSet> params;

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
        ArrayList<ParamSet> elementsToRemove = findElementsToRemove();

        for (ParamSet pSet: elementsToRemove)
            params.remove(pSet);

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
