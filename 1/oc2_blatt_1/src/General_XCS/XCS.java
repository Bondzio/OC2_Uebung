package General_XCS;

import General_XCS.PredictionArray;

public class XCS{

    private PopulationSet populationSet;
    private MultiStepRewarder mStepRewarder = new MultiStepRewarder();
    private IEffector effector;
    private IDetector detector;


    public XCS(String[] actionSet,IEffector effector,IDetector detector){
        this.populationSet = new PopulationSet(actionSet);
        this.mStepRewarder = new MultiStepRewarder();
        this.effector = effector;
        this.detector = detector;
    }

    public String runMultiStepLearning(){

        String binaryStringRep = detector.getDetected();

        
        MatchSet mSet = this.populationSet.findMatchingClassifier(binaryStringRep);

        PredictionArray pArray = new PredictionArray(mSet);

        //ActionSet aSet = pArray.getBestActionSet();
        ActionSet aSet = pArray.getRouletteActionSet();
        
        double currentReward = effector.getReward(aSet.getWinningAction());
        System.out.println("reward: " + currentReward);
        
        
        mStepRewarder.reward(aSet, pArray.getBestValue(),currentReward);

        return aSet.getSet().get(0).getAction();
    }

}
