package Units;

import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;

import java.util.ArrayList;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static java.util.Collections.min;

import StarCraftBW_XCS.StarCraftBW_Unit_Constants;
import StarCraftBW_XCS.StarCraftBW_XCS_Manager;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;
import java.util.ArrayList;
import java.util.HashSet;
import static java.util.Collections.min;
import static java.util.Arrays.asList;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Hydralisk implements IMyUnit{
    final private JNIBWAPI bwapi;
    final private Unit unit;

    public Hydralisk(Unit unit, JNIBWAPI bwapi) {
        this.unit = unit;
        this.bwapi = bwapi;
    }


    public void step() {

    }



    public Unit getUnit(){
        return this.unit;
    }

}

