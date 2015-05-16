package General_XCS;

import java.util.ArrayList;

/**
 * Created NOT by Rolle on 11.05.2015.
 */
public class PredictionArrayFromMaYoR{

	private MatchSet matchSet;
	private double[] predictionArray;
	private ArrayList<String> actionList = new ArrayList<String>();
	
	// The sum of the fitnesses of classifiers that represent each entry in the prediction array
	private double[] nr;

	
	public PredictionArrayFromMaYoR(MatchSet set) {
		this.matchSet = set;
		this.createPredictionArray();
	}
	
	private void createPredictionArray(){
		// fill actionList
		for(Classifier c : this.matchSet.getSet() ){
			if(!actionList.contains(c.getAction()))
				actionList.add(c.getAction());
		}

		// set sizes
		int size = actionList.size();
		predictionArray = new double[size];
		nr = new double[size];
		
		// init Arrays
		for (int i = 0; i < size; i++) {
			predictionArray[i] = 0.;
			nr[i] = 0.;
		}
		
		// calculate predictions
		for(Classifier c : this.matchSet.getSet() ){
			int index = actionList.indexOf(c.getAction());
			predictionArray[index] += (c.getPrediction() * c.getFitness());
			nr[index] += c.getFitness();
		}
		
		for (int i = 0; i < size; i++) {
			if (nr[i] != 0) 
				predictionArray[i] /= nr[i];
			else
				predictionArray[i] = 0;
		}
	}

	// Returns the highest value in the prediction array
	public ActionSet getBestActionSet() {

		double max = 0;
		int maxIndex = 0;
		for(int i = 0; i < actionList.size(); i++){
			if(max < predictionArray[i]){
				max = predictionArray[i];
				maxIndex = i;
			}
		}	
        return new ActionSet(getWinningClassifier(maxIndex));
	}
	
	// Selects an action in the prediction array by roulette wheel selection
	public ActionSet getRouletteActionSet() {
		double predictionSum = 0.;
		int i;
		for (i = 0; i < predictionArray.length; i++)
			predictionSum += predictionArray[i];

		predictionSum *= Math.random();
		double acceptanceValue = 0.;
		for (i = 0; acceptanceValue < predictionSum; i++) {
			acceptanceValue += predictionArray[i];
		}
		return new ActionSet(getWinningClassifier(i-1));
	}
	
	// Get the winning Classifier Set
	private ClassifierSet getWinningClassifier(int index){
		ClassifierSet cs = new ClassifierSet();
		for(Classifier c : matchSet.getSet() ){
			if(c.getAction() == actionList.get(index)){
				cs.addNewClassifier(c);
			}
		}
		return cs;
	}
}