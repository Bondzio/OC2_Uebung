package Hydralisk_XCS;

/**
 * Created by Rolle on 29.05.2015.
 */
public class Hydralisk_MatchStat {
    public final int id;
    public final int frames;
    public final int hp;
    public final int kills;
    public final int countAttackMove;
    public final int countKite;

    public Hydralisk_MatchStat(int id, int frames, int hp, int kills, int countAttackMove, int countKite){
        this.id = id;
        this.frames = frames;
        this.hp = hp;
        this.kills = kills;
        this.countAttackMove = countAttackMove;
        this.countKite = countKite;

    }
}
