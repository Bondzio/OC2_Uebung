package StarCraftBW_XCS;

import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.WeaponType;
import General_XCS.IEffector;

public class StarCraftBW_Effector implements IEffector{

	private Unit unit;
	private Unit target;

	@Override
	public void setStats(Unit unit, Unit target) {
		this.unit = unit;
		this.target = target;
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
		// TODO: implement Rewards - first: execute Action (see unit)
		
		return 0;
	}

	private void kite() {
		// TODO: kite
	}

	private void move() {
		// TODO: move
	}
}
