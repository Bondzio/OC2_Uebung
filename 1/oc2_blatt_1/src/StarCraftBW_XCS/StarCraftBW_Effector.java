package StarCraftBW_XCS;

import jnibwapi.Unit;
import General_XCS.IEffector;

import java.util.ArrayList;

public class StarCraftBW_Effector implements IEffector{

    private String currentActionToExecute;
	private Unit unit;
	private Unit target;
	private Double distance;
	private int killedUnits = 0;


    private int currentHP=0;
    private int prevHP = -1;
    private int hpLost=0;


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
        hpLost = 0;

        if (prevHP == -1)
            prevHP = currentHP;
        else if(currentHP != prevHP) {
            hpLost = prevHP - currentHP;
            prevHP = currentHP;
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

        int reward = 0;

        reward = rewardIt();

        if(reward <= 0 )
            reward = 0;

        return reward;

    }

    private int oldReward(){
        int reward = 0;
        // TODO: Rewards richtig gewichten

        if (unit.isUnderAttack())
            //System.out.println("###########################Attack reward####################################");
            reward += -400;

        if (unit.isUnderAttack() && this.currentActionToExecute == "move")
            reward += -100;

        if (unit.isUnderAttack() && this.currentActionToExecute == "kite")
            reward += 500;

        if (unit.isAttacking()){
            System.out.println("###########################Attack reward####################################");
            reward += 300;
        }


//        if(this.distance > StarCraftBW_Unit_Constants.ENEMY_WEAPONRANGE)
//            reward += 1;
//
//        if(this.distance <= StarCraftBW_Unit_Constants.ENEMY_WEAPONRANGE)
//            reward += -1;

        if(this.distance > StarCraftBW_Unit_Constants.OWN_WEAPONRANGE && this.currentActionToExecute == "kite")
            reward += -300;

        if(this.distance > StarCraftBW_Unit_Constants.OWN_WEAPONRANGE && this.currentActionToExecute == "move")
            reward += 250;

        if(unit.getKillCount() > this.killedUnits){
            reward += 1000;
            this.killedUnits++;
        }

        if(!unit.isExists())
            reward += -1000;


        return reward;
    }

    private int rewardIt(){
        int calcReward = 0;

        if(this.distance <= StarCraftBW_Unit_Constants.OWN_WEAPONRANGE)
            calcReward += 50;



        if(hpLost > 0 && currentActionToExecute.equals("attackMove")) {
            calcReward += -100;
        }
        else if (hpLost > 0 && currentActionToExecute.equals("kite")){

            calcReward += 200;
        }

        if(unit.isStartingAttack())
            calcReward += 50;


        return calcReward;
    }

}
