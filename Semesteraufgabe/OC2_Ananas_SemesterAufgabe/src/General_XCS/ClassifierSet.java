package General_XCS;

import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class ClassifierSet {

    protected ArrayList<Classifier> myCollection;


    public ClassifierSet(){
        myCollection = new ArrayList<Classifier>();
    }

    /**
     * Special function for updating the fitnesses of the classifiers in the set.
     *
     * @return  ArrayList<Classifier> returns a copy of the classifier set
     */
    public ArrayList<Classifier> getSet() {
        return new ArrayList<Classifier>(myCollection);
    }

    
    protected boolean addNewClassifier(Classifier newClassifier){
        return myCollection.add(newClassifier);
    }

    /**
     * Special function for updating the fitnesses of the classifiers in the set.
     *
     */
    private void updateFitnessSet()
    {
        double accuracySum=0.;
        double[] accuracies = new double[myCollection.size()];

        //First, calculate the accuracies of the classifier and the accuracy sums
        for(int i=0; i< myCollection.size(); i++){
            accuracies[i]= myCollection.get(i).getAccuracy();
            accuracySum += accuracies[i] * myCollection.get(i).getNumerosity();
        }



        //Next, update the fitnesses accordingly
        for(int i=0; i<myCollection.size(); i++){
            myCollection.get(i).updateFitness(accuracySum, accuracies[i]);
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

        for(int i=0; i< myCollection.size(); i++){
           // myColletction.get(i).increaseExperience();
            myCollection.get(i).updatePreError(P);
            myCollection.get(i).updatePrediction(P);
           // myColletction.get(i).updateActionSetSize(numerositySum);
        }
        updateFitnessSet();

       /* if(cons.doActionSetSubsumption)
            doActionSetSubsumption();
            */
    }

}
