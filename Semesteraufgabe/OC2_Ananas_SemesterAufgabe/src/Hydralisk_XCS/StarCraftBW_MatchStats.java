package Hydralisk_XCS;

import java.util.ArrayList;

/**
 * Created by Rolle on 29.05.2015.
 */
public class StarCraftBW_MatchStats {


    private ArrayList<StarCraftBW_MatchStat> mStats;
    private int matchIdCounter = 0;

    public StarCraftBW_MatchStats(){
        mStats = new ArrayList<StarCraftBW_MatchStat>();
    }

    public StarCraftBW_MatchStats(ArrayList<StarCraftBW_MatchStat> mStats){
        for(StarCraftBW_MatchStat mSet: mStats){
            this.addMatchStat(mSet);
        }
    }

    public void makeNewMatchStat(int frames, int hp, int kills, int countAttackMove, int countKite){
        StarCraftBW_MatchStat mStat = new StarCraftBW_MatchStat(matchIdCounter,frames,hp,kills,countAttackMove,countKite);
        matchIdCounter++;
        this.mStats.add(mStat);
    }



    public void setMatchStats(ArrayList<StarCraftBW_MatchStat> mStats) {
        this.mStats = mStats;
    }

    public void addMatchStat(StarCraftBW_MatchStat mStat){
        this.mStats.add(mStat);
    }

    public ArrayList<StarCraftBW_MatchStat> getMatchStats(){
        return this.mStats;
    }


}
