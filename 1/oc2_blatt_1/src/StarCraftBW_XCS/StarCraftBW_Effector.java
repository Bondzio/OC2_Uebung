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
	private int killedUnits = 0;

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
	public int getReward() {
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

		
		return reward;
	}
}
