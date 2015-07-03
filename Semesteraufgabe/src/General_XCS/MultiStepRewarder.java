package General_XCS;

public class MultiStepRewarder {
    private ActionSet prevActionSet = null;
    private double prevReward= 0.0;

    public void reward(ActionSet currentActionSet, double currentPArrayMaxValue, double currentReward){
        if(prevActionSet != null ){
            prevActionSet.updateSet(currentPArrayMaxValue, prevReward);
        }

        this.prevActionSet = currentActionSet;
        this.prevReward = currentReward;
    }
}