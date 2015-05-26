package General_XCS;

/**
 * Created by Rolle on 11.05.2015.
 */
public class Classifier {

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    /**
     * The reward prediction value of this classifier.
     */
    private double prediction;
    private double predictionError;
    private double fitness;

    /**
     * The condition of this classifier.
     */
    private String condition;

    /**
     * The action of this classifier.
     */
    private String action;

    /**
     * The numerosity of the classifier. This is the number of micro-classifier this macro-classifier represents.
     * Wie oft der gleiche Classifier vorkommt!
     */
    private int numerosity;


    public String getName() {
        return name;
    }

    public double getPrediction() {
        return prediction;
    }

    public double getPredictionError() {
        return predictionError;
    }

    public double getFitness() {
        return fitness;
    }

    public String getCondition() {
        return condition;
    }

    public String getAction() {
        return action;
    }



    public Classifier(String name, double prediction, double predictionError, double fitness, String condition, String action) {
        this.name = name;
        this.prediction = prediction;
        this.predictionError = predictionError;
        this.fitness = fitness;
        this.condition = condition;
        this.action = action;
        this.numerosity = 1;
    }

    public String toString(){
        String s = "Name: " + name + " ";
        s += "p: " + prediction + " ";
        s += "err: " + predictionError + " ";
        s += "f: " + fitness + " ";
        s += "con: " + condition + " ";
        s += "act: " + action + ";";

        return s;
    }

    /**
     * Updates the prediction error of the classifier according to P.
     *
     * @param rewardP The actual Q-payoff value (actual reward + max of predicted reward in the following situation).
     */
    public double updatePreError(double rewardP)
    {
        predictionError += XCS_Constants.BETA * (Math.abs(rewardP - prediction) - predictionError);

        return predictionError * numerosity;
    }


    /**
     * Updates the prediction of the classifier according to P.
     *
     * @param rewardP The actual Q-payoff value (actual reward + max of predicted reward in the following situation).
     */
    public double updatePrediction(double rewardP)
    {
        prediction += XCS_Constants.BETA * (rewardP-prediction);
        return prediction*numerosity;
    }


    /**
     * Returns the accuracy of the classifier.
     * The accuracy is determined from the prediction error of the classifier using Wilson's
     * power function as published in 'Get Real! XCS with continuous-valued inputs' (1999)
     *
     */
    public double getAccuracy()
    {
        double accuracy;

        if(predictionError <= (double)XCS_Constants.EPSILON_ZERO){
            accuracy = 1.;
        }else{
            accuracy = XCS_Constants.ALPHA * Math.pow( predictionError / XCS_Constants.EPSILON_ZERO , -XCS_Constants.NU);
        }
        return accuracy;
    }


    /**
     * Updates the fitness of the classifier according to the relative accuracy.
     *
     * @param accSum The sum of all the accuracies in the action set
     * @param accuracy The accuracy of the classifier.
     */
    public double updateFitness(double accSum, double accuracy) {

        fitness += XCS_Constants.BETA * ( ((accuracy * (double) numerosity) / accSum )- fitness);

        return fitness;
    }


    public void addNumerosity(int num){
        this.numerosity += num;
    }

    public int getNumerosity(){return this.numerosity;}


    
}
