package Units;

import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.HashSet;

/**
 * Created by Papa on 04.07.2015.
 */
public class Hatchery implements IMyUnit {
    final private JNIBWAPI bwapi;

    final private Unit unit;

    public Hatchery(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;

    }


    public void step() {

    }



    public Unit getUnit(){
        return this.unit;
    }
}
