package General_XCS;

import General_XCS.PredictionArray;

import java.io.Serializable;

public class XCS implements Serializable{



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

    public XCS(ClassifierSet cSet,String[] actionSet,IEffector effector,IDetector detector){
        this.populationSet = new PopulationSet(cSet,actionSet);
        this.mStepRewarder = new MultiStepRewarder();
        this.effector = effector;
        this.detector = detector;
    }


    public void doOneMultiStepLearning(){

        String binaryStringRep = detector.getDetected();

        
        MatchSet mSet = this.populationSet.findMatchingClassifier(binaryStringRep);

        PredictionArray pArray = new PredictionArray(mSet);

        //ActionSet aSet = pArray.getBestActionSet();
        ActionSet aSet = pArray.getRouletteActionSet();

        String winningAction = aSet.getWinningAction();
        
        double currentReward = effector.execAction(winningAction);

        mStepRewarder.reward(aSet, pArray.getBestValue(),currentReward);

    }



    public PopulationSet getPopulationSet() {
        return populationSet;
    }


    public void setPopulationSet(PopulationSet pSet) {
        this.populationSet = pSet;
    }
}
