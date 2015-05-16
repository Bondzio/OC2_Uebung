package General_XCS;

/**
 * Created by Rolle on 11.05.2015.
 */
public class Classifier {

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private double prediction;
    private double predictionError;
    private double fitness;

    private String condition;
    private String action;


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
    
}
