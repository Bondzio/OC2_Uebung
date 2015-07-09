package StarCraftBW_XCS_Queen;

import General_XCS.IEffector;
import jnibwapi.Unit;
import jnibwapi.types.OrderType;

/**
 * Created by Papa on 07.07.2015.
 */
public class StarCraftBW_QueenEffector  implements IEffector {
    private String currentActionToExecute;
    private Unit unit;
    private Unit target;
    private Double distance;
    private int currentHP = 0;
    private int currentEnergy = 0;
    private int prevHP = -1;
    private int prevEnergy = -1;
    private int hpLost = 0;
    private int EnergyDelta = 0;

    public String getCurrentActionToExecute() {
        return currentActionToExecute;
    }

    public void setCurrentActionToExecute(String currentActionToExecute) {
        this.currentActionToExecute = currentActionToExecute;
    }

    public void setStats(Unit unit, Unit target, Double distance) {
        this.unit = unit;
        this.target = target;
        this.distance = distance;
        currentHP = unit.getHitPoints();
        currentEnergy = unit.getEnergy();
        hpLost = 0;
        EnergyDelta = 0;

        if (prevHP == -1) {
            prevHP = currentHP;
        }
        else if(currentHP != prevHP) {
            hpLost = prevHP - currentHP;
            prevHP = currentHP;
        }

        if (prevEnergy == -1) {
            prevEnergy = currentEnergy;
        }
        else if(currentEnergy != prevEnergy) {
            EnergyDelta = prevEnergy - currentEnergy;
            prevEnergy = currentEnergy;
        }
    }

    public Unit getUnit() {
        return this.unit;
    }

    public Unit getTarget() {
        return this.target;
    }

    @Override
    public void execAction(String winningAction) {
        this.currentActionToExecute = winningAction;
    }

    @Override
    public int getRewardForExecutedAction() {
        int reward = rewardIt();

        if(reward <= 0 )
            reward = 0;

        return reward;
    }

    private int rewardIt(){
        int calcReward = 0;

        if(this.distance >= StarCraftBW_Queen_Constants.HYDRALISK_WEAPONRANGE * 2)
            calcReward += 50;

        if(hpLost > 0 && currentActionToExecute.equals("cast")) {
            calcReward += -100;
        }
        else if (hpLost > 0 && currentActionToExecute.equals("kite")){

            calcReward += -200;
        }

        if(EnergyDelta > 0 && currentActionToExecute.equals("cast")) {
            calcReward += 100;
        }

        if(EnergyDelta < 0 && currentActionToExecute.equals("cast")) {
            calcReward += -10;
        }

        if(currentEnergy < StarCraftBW_Queen_Constants.OWN_MAXENERGY)
            calcReward += 50;

        return calcReward;
    }
}