package General_XCS;

import jnibwapi.Unit;


public interface IEffector {

    public int getReward();
    public void setStats(Unit unit, Unit target, Double distance);
    public Unit getUnit();
    public Unit getTarget();
    
}
