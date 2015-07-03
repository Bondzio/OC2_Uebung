package Units;

import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Ultralisk {
    final private JNIBWAPI bwapi;
    final private HashSet<Unit> enemyUnits;
    final private Unit unit;

    public Ultralisk(Unit unit, JNIBWAPI bwapi, HashSet<Unit> enemyUnits) {
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
