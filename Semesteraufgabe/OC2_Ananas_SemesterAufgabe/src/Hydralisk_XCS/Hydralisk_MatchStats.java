package Hydralisk_XCS;

import java.util.ArrayList;

/**
 * Created by Rolle on 29.05.2015.
 */
public class Hydralisk_MatchStats {


    private ArrayList<Hydralisk_MatchStat> mStats;
    private int matchIdCounter = 0;

    public Hydralisk_MatchStats(){
        mStats = new ArrayList<Hydralisk_MatchStat>();
    }

    public Hydralisk_MatchStats(ArrayList<Hydralisk_MatchStat> mStats){
        for(Hydralisk_MatchStat mSet: mStats){
            this.addMatchStat(mSet);
        }
    }

    public void makeNewMatchStat(int frames, int hp, int kills, int countAttackMove, int countKite){
        Hydralisk_MatchStat mStat = new Hydralisk_MatchStat(matchIdCounter,frames,hp,kills,countAttackMove,countKite);
        matchIdCounter++;
        this.mStats.add(mStat);
    }



    public void setMatchStats(ArrayList<Hydralisk_MatchStat> mStats) {
        this.mStats = mStats;
    }

    public void addMatchStat(Hydralisk_MatchStat mStat){
        this.mStats.add(mStat);
    }

    public ArrayList<Hydralisk_MatchStat> getMatchStats(){
        return this.mStats;
    }


}
