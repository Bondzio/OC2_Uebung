package StarCraftBW_XCS_Queen;

import jnibwapi.types.UnitType;

/**
 * Created by Papa on 09.07.2015.
 */
public class StarCraftBW_Queen_Constants {
    public static final int OWN_CASTRANGE_PARASITE = 12 * 32;
    public static final int OWN_CASTRANGE_ENSNARE = 9 * 32;
    public static final int OWN_ENERGYCOST_PARASITE = 75;
    public static final int OWN_ENERGYCOST_ENSANRE = 75;
    public static final int HYDRALISK_WEAPONRANGE = UnitType.UnitTypes.Zerg_Hydralisk.getGroundWeapon().getMaxRange();
    public static final int OWN_MAXHP = UnitType.UnitTypes.Zerg_Queen.getMaxHitPoints();
    public static final int OWN_MAXENERGY = UnitType.UnitTypes.Zerg_Queen.getMaxEnergy();
}
