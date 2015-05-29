package Test;

import General_XCS.Classifier;
import General_XCS.PopulationSet;
import StarCraftBW_XCS.StarCraftBW_Statistic;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Rolle on 25.05.2015.
 */
public class DER_BW_ANALISER {

    public static void main(String[] args) {
        printStats();

    }

    private static void printClassifier(){
        Gson gson = new Gson();
        try{
            BufferedReader br = new BufferedReader(new FileReader("saves.txt"));
            PopulationSet pSet = gson.fromJson(br, PopulationSet.class);
            System.out.println("FileThread: File loaded");

            for(Classifier c : pSet.getSet()){
                System.out.println(c.toString());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printStats(){
        StarCraftBW_Statistic stats = new StarCraftBW_Statistic();
        stats.calcStats();
    }

}
