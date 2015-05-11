package RollesKleineEcke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PredictionArray {
    private HashMap<String ,ClassifierSet> helperMap = new HashMap<String, ClassifierSet>();


    public PredictionArray(MatchSet mSet){
        this.createHelperMap(mSet);
    }

    private void createHelperMap(MatchSet mSet){
        for(Classifier c: mSet.getSet()){
            String action = c.getAction();

            if(helperMap.containsKey(action))
                helperMap.get(action).addNewClassifier(c);
            else{
                ClassifierSet tmp = new ClassifierSet();
                tmp.addNewClassifier(c);
                helperMap.put(action, tmp);
            }

        }

    }

    private ActionSet createActionSet(){
        double global = 0 ;
        String winningAction = "";

        for(Map.Entry<String,ClassifierSet> entry : helperMap.entrySet()){
            ClassifierSet value = entry.getValue();
            for(Classifier c : value.getSet()){
                double sum = c.getPrediction() * c.getFitness();
                if(sum >= global){
                    global = sum;
                    winningAction = c.getAction();
                }

            }

        }
        return new ActionSet(helperMap.get(winningAction));
    }

    public ActionSet getActionSet(){
        return createActionSet();
    }

}
