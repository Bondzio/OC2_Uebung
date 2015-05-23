package StarCraftBW_XCS;

import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.WeaponType;
import General_XCS.IEffector;

public class StarCraftBW_Effector implements IEffector{

	private Unit unit;
	private Unit target;
	private Double distance;
	private static final int WEAPONRANGE = UnitType.UnitTypes.Terran_Vulture.getGroundWeapon().getMaxRange();
	private static final int ENEMYRANGE = UnitType.UnitTypes.Protoss_Zealot.getGroundWeapon().getMaxRange();

	@Override
	public void setStats(Unit unit, Unit target, Double distance) {
		this.unit = unit;
		this.target = target;
		this.distance = distance;
	}

	@Override
	public Unit getUnit() {
		return this.unit;
	}

	@Override
	public Unit getTarget() {
		return this.target;
	}
	
	@Override
	public int getReward(String winningAction) {
		int reward = 1;
		
		// TODO: implement Rewards - first: execute Action (see unit)
//		if(winningAction == "move")
//			move();
//			
//		if(winningAction == "kite")
//			kite();
		
//		if(unit.isUnderAttack())
//            reward += -1;
//		
//        if(unit.isAttackFrame())
//            reward += +1.5;
//		
//        if(unit.attack(this.target,false))
//            reward += +2;
        
		
		return reward;
	}

	private void kite() {
		System.out.println("kite");
		
		unit.move(new Position(target.getPosition().getPX() - WEAPONRANGE, target.getPosition().getPY() - WEAPONRANGE), false);
	}

	private void move() {
		System.out.println("move");

		if (WEAPONRANGE <= this.distance)
			unit.attack(target, false);
		else
			unit.move(new Position(target.getPosition().getPX(), target.getPosition().getPY()), false);
	}

}
