package StarCraftBW_XCS;

import java.util.ArrayList;

/**
 * Created by Rolle on 29.05.2015.
 */
public class StarCraftBW_Statistic {


    private StarCraftBW_MatchStats matchStats;

    public StarCraftBW_Statistic(){
        StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
        matchStats = fileThread.getSavedMatchStats();
        fileThread.stopMe();
    }

    public void calcStats(){
        ArrayList<StarCraftBW_MatchStat> mStats = matchStats.getMatchStats();

        int stepSize = 5;

        double hpsum = 0;
        double killsum = 0;
        double countAttackMoveSum = 0;
        double countKiteSum = 0;
        double framesSum = 0;



        ArrayList<ArrayList<Double>> stats = new ArrayList<ArrayList<Double>>();

        for(int i = 1; i - 1 < mStats.size();i++){
            hpsum += mStats.get(i - 1).hp;
            killsum += mStats.get(i - 1).kills;
            countAttackMoveSum += mStats.get(i - 1).countAttackMove;
            countKiteSum += mStats.get(i - 1).countKite;
            framesSum += mStats.get(i - 1).frames;
            if(i % stepSize == 0){
                ArrayList<Double> element = new ArrayList<Double>();
                element.add(hpsum/stepSize);
                element.add(killsum/stepSize);
                element.add(countAttackMoveSum/stepSize);
                element.add(countKiteSum/stepSize);
                element.add(framesSum/stepSize);

                stats.add(element);

                hpsum = 0;
                killsum = 0;
                countAttackMoveSum = 0;
                countKiteSum = 0;
                framesSum = 0;
            }
        }

        for (ArrayList<Double> element: stats){
            System.out.println(element);
        }

    }





}
