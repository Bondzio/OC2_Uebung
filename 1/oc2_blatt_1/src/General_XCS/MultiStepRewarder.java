package General_XCS;

import java.util.LinkedList;

/**
 * Created by Rolle on 16.05.2015.
 */
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
