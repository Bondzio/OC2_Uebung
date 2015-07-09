package StarCraftBW_XCS_Queen;

import General_XCS.*;

/**
 * Created by Papa on 09.07.2015.
 */
public class StarCraftBW_Queen_XCS extends XCS {
    private PredictionArray currentPArray;
    private ActionSet currentASet;

    public StarCraftBW_Queen_XCS(String[] actionSet, IEffector effector, IDetector detector) {
        super(actionSet, effector, detector);
    }

    public void execPredictAction(boolean saveForReward){
        String binaryStringRep = detector.getDetected();
        MatchSet mSet = this.populationSet.findMatchingClassifier(binaryStringRep);
        PredictionArray pArray = new PredictionArray(mSet);
        //ActionSet aSet = pArray.getBestActionSet();
        ActionSet aSet = pArray.getRouletteActionSet();
        String winningAction = aSet.getWinningAction();

        if(saveForReward){
            currentASet = aSet;
            currentPArray = pArray;
        }

        effector.execAction(winningAction);
    }

    public void rewardCurrentAction(){

        if (currentASet == null && currentPArray == null){
            System.out.println("StarCraftBW_XCS: there is nothing to be Rewarded");
            return;
        }

        double currentReward = effector.getRewardForExecutedAction();
        mStepRewarder.reward(currentASet, currentPArray.getBestValue(),currentReward);
    }

    public void cleanCurrentAction(){
        currentASet = null;
        currentPArray = null;
    }
}