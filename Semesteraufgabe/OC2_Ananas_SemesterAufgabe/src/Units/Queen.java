package Units;

import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.HashSet;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Queen implements IMyUnit{
    final private JNIBWAPI bwapi;

    final private Unit unit;

    public Queen(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;

    }


    public void step() {

    }



    public Unit getUnit(){
        return this.unit;
    }
}
