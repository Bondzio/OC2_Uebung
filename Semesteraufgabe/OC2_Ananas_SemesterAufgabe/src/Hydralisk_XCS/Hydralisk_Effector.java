package Hydralisk_XCS;

import General_XCS.IEffector;
import Units.Hatchery;
import Units.Hydralisk;
import jnibwapi.Unit;

public class Hydralisk_Effector implements IEffector{

    private String currentActionToExecute;



	private int killedUnits = 0;


    private Double distanceToHatchery;;
    private Hydralisk myHydra;
    private Unit myTarget;

    private int hatcheryToDef_currentHP = 0;
    private int hatcheryToDef_prevHP = -1;
    private int hatcheryToDef_hpLost = 0;

    private int my_currentHP=0;
    private int my_prevHP = -1;
    private int my_hpLost=0;


    //Reward Values
    private int standard = 1;
    private int good = standard*5;
    private int veryGood = standard*15;
    private int theBest = standard*25;

    private int bad = standard *  -5;
    private int veryBad = standard * -15;
    private int theWorest = standard * -25;

    public String getCurrentActionToExecute() {
        return currentActionToExecute;
    }

    public void setCurrentActionToExecute(String currentActionToExecute) {
        this.currentActionToExecute = currentActionToExecute;
    }


	public void setStats(Hydralisk myHydra, Unit target, Double distanceToDefPoint, Hatchery hatchery) {

        this.myHydra = myHydra;
		this.myTarget = target;
		this.distanceToHatchery = distanceToDefPoint;


        hatcheryToDef_currentHP = hatchery.getUnit().getHitPoints();
        hatcheryToDef_hpLost = 0;

        if (hatcheryToDef_prevHP == -1)
            hatcheryToDef_prevHP = hatcheryToDef_currentHP;
        else if(hatcheryToDef_currentHP != hatcheryToDef_prevHP) {
            hatcheryToDef_hpLost = hatcheryToDef_prevHP - hatcheryToDef_currentHP;
            hatcheryToDef_prevHP = hatcheryToDef_currentHP;
        }

        my_currentHP = myHydra.getUnit().getHitPoints();
        my_hpLost = 0;

        if (my_prevHP == -1)
            my_prevHP = my_currentHP;
        else if(my_currentHP != my_prevHP) {
            my_hpLost = my_prevHP - my_currentHP;
            my_prevHP = my_currentHP;
        }

	}




//	public Unit getUnit() {
//		return this.unit;
//	}
//
//
//	public Unit getTarget() {
//		return this.target;
//	}
	
	@Override
	public void execAction(String winningAction) {
            this.currentActionToExecute = winningAction;
	}

    @Override
    public int getRewardForExecutedAction() {

        int reward = 0;

        reward += rewards_UnderAttack();
        reward += rewards_isAttacking();
        reward += rewards_DisToHatchery();
        reward += rewards_isMoving();

        if(reward <= 0 )
            reward = 0;

        return reward;

    }


    public int rewards_UnderAttack(){
        int reward = 0;
        Unit hydraAsUnit = myHydra.getUnit();

        if(hydraAsUnit.isUnderAttack() || my_hpLost > 0) {
            if (currentActionToExecute.equals("moveToDefPoint"))
                reward += standard;
            else if (currentActionToExecute.equals("burrow"))
                reward += good;
            else
                reward -= bad;
        }

        if( hatcheryToDef_hpLost > 0){
            if (currentActionToExecute.equals("moveToDefPoint"))
                reward += veryGood;
            else if (currentActionToExecute.equals("protectDefPoint"))
                reward += theBest;
            else
                reward -= veryBad;
        }

        return reward;
    }

    public int rewards_isAttacking(){
        int reward = 0;
        Unit hydraAsUnit = myHydra.getUnit();

        if(hydraAsUnit.isStartingAttack() && hydraAsUnit.isAttacking()) {
            if (currentActionToExecute.equals("supportFriend"))
                reward += veryGood;
            else if (currentActionToExecute.equals("protectDefPoint"))
                reward += theBest;
            else
                reward += good;
        }

        return reward;
    }

    public int rewards_DisToHatchery(){
        int reward = 0;

        if(distanceToHatchery <= 270)
            reward += standard;
        else{
            reward -= veryBad;
        }

        return reward;
    }


    public int rewards_isMoving(){
        int reward = 0;
        Unit hydraAsUnit = myHydra.getUnit();


        if(hydraAsUnit.isAccelerating()) {
            if (currentActionToExecute.equals("attackMoveToClosestFlyingEnemy"))
                reward += theBest;
            else if (currentActionToExecute.equals("supportFriend"))
                reward += veryGood;
            else if(currentActionToExecute.equals("attackMoveToClosestEnemy"))
                reward += veryGood;
            else
                reward += standard;
        }
        else
            reward += standard;

        return reward;
    }

////    private int oldReward(){
////        int reward = 0;
////        // TODO: Rewards richtig gewichten
////
////        if (unit.isUnderAttack())
////            //System.out.println("###########################Attack reward####################################");
////            reward += -400;
////
////        if (unit.isUnderAttack() && this.currentActionToExecute == "move")
////            reward += -100;
////
////        if (unit.isUnderAttack() && this.currentActionToExecute == "kite")
////            reward += 500;
////
////        if (unit.isAttacking()){
////            System.out.println("###########################Attack reward####################################");
////            reward += 300;
////        }
////
////
//////        if(this.distance > StarCraftBW_Unit_Constants.ENEMY_WEAPONRANGE)
//////            reward += 1;
//////
//////        if(this.distance <= StarCraftBW_Unit_Constants.ENEMY_WEAPONRANGE)
//////            reward += -1;
////
////        if(this.distance > StarCraftBW_Unit_Constants.OWN_WEAPONRANGE && this.currentActionToExecute == "kite")
////            reward += -300;
////
////        if(this.distance > StarCraftBW_Unit_Constants.OWN_WEAPONRANGE && this.currentActionToExecute == "move")
////            reward += 250;
////
////        if(unit.getKillCount() > this.killedUnits){
////            reward += 1000;
////            this.killedUnits++;
////        }
////
////        if(!unit.isExists())
////            reward += -1000;
////
////
////        return reward;
////    }
//
//    private int rewardIt(){
//        int calcReward = 0;
//
//        if(this.distance <= Hydralisk_Unit_Constants.Hydralisk_WEAPONRANGE)
//            calcReward += 50;
//
//
//
//        if(hpLost > 0 && currentActionToExecute.equals("attackMove")) {
//            calcReward += -100;
//        }
//        else if (hpLost > 0 && currentActionToExecute.equals("kite")){
//
//            calcReward += 200;
//        }
//
//        if(unit.isStartingAttack())
//            calcReward += 50;
//
//
//        return calcReward;
//    }

}
