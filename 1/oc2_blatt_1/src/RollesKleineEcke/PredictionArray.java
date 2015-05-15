package RollesKleineEcke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PredictionArray {
    private HashMap<String ,PredictionArrayElement> pArray = new HashMap<String, PredictionArrayElement>();
    //private ArrayList<PredictionArrayElement> pArray = new ArrayList<PredictionArrayElement>();


    public PredictionArray(MatchSet mSet){
        this.createPredictionArray(mSet);
    }

    private void createPredictionArray(MatchSet mSet){
        /*
            After the For loop, The Map schould look like that:
            Map = {
                "Action 1" : PredictionArrayElement // where every classifier in the set belongs to Action 1
                "Action 2" : PredictionArrayElement
            }

        */
        for(Classifier c: mSet.getSet()){
            String action = c.getAction();

            if(pArray.containsKey(action))
                pArray.get(action).addNewClassifier(c);
            else{
                ClassifierSet tmp = new ClassifierSet();
                tmp.addNewClassifier(c);
                PredictionArrayElement pAElement = new PredictionArrayElement(tmp);
                pArray.put(action, pAElement);
            }

        }
    }


    public ActionSet getBestActionSet(){
        double maxValue = 0;
        PredictionArrayElement winner = null;

        for(Map.Entry<String,PredictionArrayElement> entry : pArray.entrySet()) {
            PredictionArrayElement currentElement = entry.getValue();
            double currentValue = currentElement.getCalculatedPredictionArrayValue();
            if (maxValue < currentValue) {
                maxValue = currentValue;
                winner = currentElement;
            }
        }

        ClassifierSet winnerSet = pArray.get(winner).getClassifierSet();
        return new ActionSet(winnerSet);
    }



    private class PredictionArrayElement {

        public PredictionArrayElement(ClassifierSet classifierSet) {
            for(Classifier c : classifierSet.getSet())
                this.addNewClassifier(c);
        }

        private ClassifierSet classifierSet;
        private String action;
        private Double calculatedPredictionArrayValue;

        private void calculatePredictionArrayValue(){
            double sumPreTimesFit = 0;
            double sumFitness = 0;

            for(Classifier c : classifierSet.getSet()){
                sumPreTimesFit += c.getPrediction() * c.getFitness() ;
                sumFitness += c.getFitness();
            }
            calculatedPredictionArrayValue = sumPreTimesFit / sumFitness;
        }

        private void setMyAction(){
            action = classifierSet.getSet().get(0).getAction();
        }

        public boolean addNewClassifier(Classifier newClassifier){
            return classifierSet.addNewClassifier(newClassifier);
        }

        public Double getCalculatedPredictionArrayValue() {
            calculatePredictionArrayValue();
            return calculatedPredictionArrayValue;
        }

        public String getAction() {
            setMyAction();
            return action;
        }

        public ClassifierSet getClassifierSet(){
            return this.classifierSet;
        }

    }
}
