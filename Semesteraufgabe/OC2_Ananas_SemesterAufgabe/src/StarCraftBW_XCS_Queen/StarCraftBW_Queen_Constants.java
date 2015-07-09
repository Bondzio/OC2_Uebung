package StarCraftBW_XCS_Queen;

import jnibwapi.types.UnitType;

/**
 * Created by Papa on 09.07.2015.
 */
public class StarCraftBW_Queen_Constants {
    public static final int OWN_WEAPONRANGE = UnitType.UnitTypes.Zerg_Queen.getGroundWeapon().getMaxRange();
    public static final int HYDRALISK_WEAPONRANGE = UnitType.UnitTypes.Zerg_Hydralisk.getGroundWeapon().getMaxRange();
    public static final int OWN_HP = UnitType.UnitTypes.Zerg_Queen.getMaxHitPoints();
}
