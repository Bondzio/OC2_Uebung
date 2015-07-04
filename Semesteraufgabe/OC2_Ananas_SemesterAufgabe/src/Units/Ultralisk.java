package Units;

import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Ultralisk implements IMyUnit {
    final private JNIBWAPI bwapi;
    final private Unit unit;

    public Ultralisk(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;

    }


    public void step() {

    }



    public Unit getUnit(){
        return this.unit;
    }
}
