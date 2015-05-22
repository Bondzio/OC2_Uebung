package General_XCS;

import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class ClassifierSet {

    private ArrayList<Classifier> myColletction = new ArrayList<Classifier>();
    
    public ArrayList<Classifier> getSet() {
        return new ArrayList<Classifier>(myColletction);
    }

    
    public boolean addNewClassifier(Classifier newClassifier){
        return myColletction.add(newClassifier);
    }

    /**
     * Special function for updating the fitnesses of the classifiers in the set.
     *
     */
    private void updateFitnessSet()
    {
        double accuracySum=0.;
        double[] accuracies = new double[myColletction.size()];

        //First, calculate the accuracies of the classifier and the accuracy sums
        for(int i=0; i<myColletction.size(); i++){
            accuracies[i]= myColletction.get(i).getAccuracy();
            accuracySum+=accuracies[i]*myColletction.get(i).getNumerosity();
        }

        //Next, update the fitnesses accordingly
        for(int i=0; i<myColletction.size(); i++){
            myColletction.get(i).updateFitness(accuracySum, accuracies[i]);
        }
    }


    /**
     * Updates all parameters in the current set (should be the action set).
     * Essentially, reinforcement Learning as well as the fitness evaluation takes place in this set.
     * Moreover, the prediction error and the action set size estimate is updated. Also,
     * action set subsumption takes place if selected. As in the algorithmic description, the fitness is updated
     * after prediction and prediction error. However, in order to be more conservative the prediction error is
     * updated before the prediction.
     *
     * @param maxPrediction The maximum prediction value in the successive prediction array
     * (should be set to zero in single step environments).
     * @param reward The actual resulting reward after the execution of an action.
     */
    public void updateSet(double maxPrediction, double reward)
    {

        double P = reward + XCS_Constants.GAMMA*maxPrediction;

        for(int i=0; i<myColletction.size(); i++){
           // myColletction.get(i).increaseExperience();
            myColletction.get(i).updatePreError(P);
            myColletction.get(i).updatePrediction(P);
           // myColletction.get(i).updateActionSetSize(numerositySum);
        }
        updateFitnessSet();

       /* if(cons.doActionSetSubsumption)
            doActionSetSubsumption();
            */
    }

}
