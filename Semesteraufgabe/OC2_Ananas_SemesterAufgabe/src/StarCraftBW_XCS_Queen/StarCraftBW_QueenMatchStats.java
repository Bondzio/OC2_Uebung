package StarCraftBW_XCS_Queen;

import java.util.ArrayList;

/**
 * Created by Papa on 07.07.2015.
 */
public class StarCraftBW_QueenMatchStats {
    private ArrayList<StarCraftBW_QueenMatchStat> mStats;
    private int matchIdCounter = 0;

    public StarCraftBW_QueenMatchStats(){
        mStats = new ArrayList<StarCraftBW_QueenMatchStat>();
    }

    public StarCraftBW_QueenMatchStats(ArrayList<StarCraftBW_QueenMatchStat> mStats){
        for(StarCraftBW_QueenMatchStat mSet: mStats){
            this.addMatchStat(mSet);
        }
    }

    public void makeNewMatchStat(int frames, int hp, int kills, int countAttackMove, int countKite){
        StarCraftBW_QueenMatchStat mStat = new StarCraftBW_QueenMatchStat(matchIdCounter,frames,hp,kills,countAttackMove,countKite);
        matchIdCounter++;
        this.mStats.add(mStat);
    }

    public void setMatchStats(ArrayList<StarCraftBW_QueenMatchStat> mStats) {
        this.mStats = mStats;
    }

    public void addMatchStat(StarCraftBW_QueenMatchStat mStat){
        this.mStats.add(mStat);
    }

    public ArrayList<StarCraftBW_QueenMatchStat> getMatchStats(){
        return this.mStats;
    }
}