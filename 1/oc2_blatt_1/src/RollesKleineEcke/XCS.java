package RollesKleineEcke;

/**
 * Created by Rolle on 16.05.2015.
 */
public class XCS {

     private PopulationSet populationSet;

    public XCS(String[] actionSet) {
        this.populationSet = new PopulationSet(actionSet);
    }

    public String runMultiStepLearning(String binaryStringRep){
        MatchSet mSet = this.populationSet.findMatchingClassifier(binaryStringRep);

        PredictionArray pArray = new PredictionArray(mSet);

        ActionSet aSet = pArray.getBestActionSet();

        return aSet.getSet().get(0).getAction();
    }

}
