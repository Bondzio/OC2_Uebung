package General_XCS;

import java.util.LinkedList;

/**
 * Created by Rolle on 16.05.2015.
 */
public class MultiStepRewarder {

    private LinkedList<ActionSet> actionSetHistory = new LinkedList<ActionSet>();
    private ActionSetRewarder aSetRewarder;

    public MultiStepRewarder(ActionSetRewarder aSetRewarder) {
        this.aSetRewarder = aSetRewarder;
    }


    public void rewared(int reward){
        boolean firstElement = true;
        for(ActionSet currentASet : actionSetHistory){

            if (firstElement)
                firstElement = false;
            else
                reward = calcReducedReward(reward);
            
            aSetRewarder.rewaredActionSet(reward,currentASet);
        }
    }

    public void addActionSet(ActionSet aSet){
        if(actionSetHistory.size()>=2){
            actionSetHistory.removeLast();

        }
        actionSetHistory.addFirst(aSet);
    }

    private int calcReducedReward(int reward){
        /**
         * TODO: FORMULA
         */
        return reward -1;
    }
}
