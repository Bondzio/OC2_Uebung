package General_XCS;

import jnibwapi.Unit;


public interface IEffector {

    public int getReward(String winningAction);
    public void setStats(Unit unit, Unit target);
    public Unit getUnit();
    public Unit getTarget();
    
}
