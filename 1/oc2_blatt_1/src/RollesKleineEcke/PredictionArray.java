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
        double globalWinner=0;
        double sumPmalF;
        double sumFitness;
        double pArrayCalc;
        String winningAction = "";


        for(Map.Entry<String,ClassifierSet> entry : helperMap.entrySet()){
            sumPmalF = 0 ;
            sumFitness = 0 ;

            ClassifierSet value = entry.getValue();
            String currentAction = "";
            for(Classifier c : value.getSet()){
                currentAction = c.getAction();
                sumPmalF += c.getPrediction() * c.getFitness() ;
                sumFitness += c.getFitness();
            }
            pArrayCalc = sumPmalF / sumFitness;

           // System.out.println("Current Action: " + currentAction);
            //System.out.println("pArrayCalc: " + pArrayCalc);
            //System.out.println("globalWinner: " + globalWinner);
            if(globalWinner < pArrayCalc){
                //System.out.println("pArrayCalc > globalWinner");
                globalWinner = pArrayCalc;
                winningAction = currentAction;
            }
        }
        return new ActionSet(helperMap.get(winningAction));
    }

    public ActionSet getActionSet(){
        return createActionSet();
    }

}
