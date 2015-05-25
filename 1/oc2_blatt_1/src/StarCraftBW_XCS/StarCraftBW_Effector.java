package StarCraftBW_XCS;

import jnibwapi.Unit;
import General_XCS.IEffector;

public class StarCraftBW_Effector implements IEffector{

	private Unit unit;
	private Unit target;
	private Double distance;
	private int killedUnits = 0;

    private boolean actionFinished = false;
    private String executingAction = null;

    private final Object actionLock = new Object();



	public void setStats(Unit unit, Unit target, Double distance) {
		this.unit = unit;
		this.target = target;
		this.distance = distance;
	}




	public Unit getUnit() {
		return this.unit;
	}


	public Unit getTarget() {
		return this.target;
	}
	
	@Override
	public int execAction(String winningAction) {
        putNewActionForExecution(winningAction);
        int rew = startRewarding();
        return rew;
	}

    private void putNewActionForExecution(String action){

        synchronized (actionLock){
            while (executingAction !=null){
                try {
                    wait();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    System.out.println("Effector was interupted");
                }
            }
            executingAction = action;
            actionFinished = false;
            notify();
        }
    }

    public String getActionToExecute(){
        String help = null;
        synchronized (actionLock){
            while (executingAction == null){
                try {
                    wait();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    System.out.println("Effector was interupted");
                }
            }
            help = executingAction;
        }
        return help;
    }

    public void actionExecuted(){
        synchronized (actionLock){
            this.actionFinished = true;
            notify();
        }
    }

    private int startRewarding(){
        synchronized (actionLock){
            while (!actionFinished && executingAction == null){
                try {
                    wait();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    System.out.println("Effector was interupted");
                }
            }
            int reward = calcReward();
            executingAction = null;
            notify();
            return reward;
        }
    }

    private int calcReward(){



        int reward = 0;

        // TODO: Rewards richtig gewichten

        if (unit.isUnderAttack())
            reward += -1;

        if (unit.isAttackFrame())
            reward += 4;

        if(this.distance > StarCraftBW_Unit_Constants.ENEMY_WEAPONRANGE)
            reward += 1;

        if(this.distance <= StarCraftBW_Unit_Constants.ENEMY_WEAPONRANGE)
            reward += -1;

        if(this.distance < StarCraftBW_Unit_Constants.OWN_WEAPONRANGE)
            reward += 5;

        if(unit.getKillCount() > this.killedUnits){
            reward += 10;
            this.killedUnits++;
        }

        if(unit.isExists())
            reward += 1;

        System.out.println("Effector: calc Reward-> " + reward);
        return reward;
    }
}
