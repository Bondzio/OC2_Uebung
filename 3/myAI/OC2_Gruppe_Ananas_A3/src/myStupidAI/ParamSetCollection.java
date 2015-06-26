package myStupidAI;

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
}
