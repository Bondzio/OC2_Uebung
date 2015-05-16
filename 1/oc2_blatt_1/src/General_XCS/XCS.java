package General_XCS;

import RollesKleineEcke.PredictionArray;

/**
 * Created by Rolle on 16.05.2015.
 */
public class XCS {

     private PopulationSet populationSet;
     private ActionSetRewarder aSetRewarder;
    private MultiStepRewarder mStepRewarder;

    public XCS(String[] actionSet) {
        this.populationSet = new PopulationSet(actionSet);
        this.aSetRewarder = new ActionSetRewarder();
        this.mStepRewarder = new MultiStepRewarder(aSetRewarder);

    }

    public String runMultiStepLearning(String binaryStringRep){
        MatchSet mSet = this.populationSet.findMatchingClassifier(binaryStringRep);

        PredictionArray pArray = new PredictionArray(mSet);

        ActionSet aSet = pArray.getBestActionSet();

        mStepRewarder.addActionSet(aSet);

        return aSet.getSet().get(0).getAction();
    }

}
