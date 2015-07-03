package Units;

import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Zergling {
    final private JNIBWAPI bwapi;
    final private HashSet<Unit> enemyUnits;
    final private Unit unit;

    public Zergling(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
    }


    public void step() {

    }



    public Unit getMyUnit(){
        return this.unit;
    }
}
