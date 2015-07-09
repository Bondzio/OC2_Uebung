package StarCraftBW_XCS_Queen;

/**
 * Created by Papa on 07.07.2015.
 */
public class StarCraftBW_QueenMatchStat {
    public final int id;
    public final int frames;
    public final int hp;
    public final int kills;
    public final int countAttackMove;
    public final int countKite;

    public StarCraftBW_QueenMatchStat(int id, int frames, int hp, int kills, int countAttackMove, int countKite){
        this.id = id;
        this.frames = frames;
        this.hp = hp;
        this.kills = kills;
        this.countAttackMove = countAttackMove;
        this.countKite = countKite;
    }
}
