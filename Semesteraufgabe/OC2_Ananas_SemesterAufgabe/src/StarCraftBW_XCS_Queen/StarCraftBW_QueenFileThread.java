package StarCraftBW_XCS_Queen;

import General_XCS.ClassifierSet;
import General_XCS.PopulationSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Created by Papa on 07.07.2015.
 */
public class StarCraftBW_QueenFileThread extends Thread{
    private ClassifierSet cSet = null;
    private String filePath_cSet = "queen_saves.txt";
    private StarCraftBW_QueenMatchStats mStats = null;
    private String filePath_mStats = "queen_stats.txt";

    private boolean stop = false;

    public String getFilePath_cSet() {
        return filePath_cSet;
    }

    public String getFilePath_mStats() {
        return filePath_mStats;
    }

    public void setFilePath_cSet(String filePath_cSet) {
        this.filePath_cSet = filePath_cSet;
    }

    public void setFilePath_mStats(String filePath_mStats) {
        this.filePath_mStats = filePath_mStats;
    }

    @Override
    public void run() {
        stop = false;

        try {
            while (!stop) {
                saveClassifierSet();
                saveMatchStats();
            }
        } catch (InterruptedException e) {
            stop = true;
        }
    }

    public synchronized void putClassifierSetToSave(PopulationSet toSave) throws InterruptedException {
        if(stop)
            return;

        while (cSet != null) {
            wait();
        }

        this.cSet = toSave;
        notify();
    }

    public synchronized void putMatchStatsToSave(StarCraftBW_QueenMatchStats mStats) throws InterruptedException {
        if(stop)
            return;

        while (this.mStats != null) {
            wait();
        }

        this.mStats = mStats;
        notify();
    }

    private synchronized void saveClassifierSet() throws InterruptedException {
        notify();

        while (cSet == null) {
            wait();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(cSet);
        cSet = null;

        try {
            PrintWriter writer = new PrintWriter(filePath_cSet);
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized void saveMatchStats() throws InterruptedException {
        notify();

        while (mStats == null) {
            wait();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(mStats);
        mStats = null;

        try {
            PrintWriter writer = new PrintWriter(filePath_mStats);
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized PopulationSet getSavedPopulationSet() {
        Gson gson = new Gson();

        if(!(new File(filePath_cSet)).exists())
            return null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath_cSet));
            PopulationSet pSet = gson.fromJson(br, PopulationSet.class);
            System.out.println("FileThread: File loaded");
            return pSet;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized StarCraftBW_QueenMatchStats getSavedMatchStats() {
        Gson gson = new Gson();

        if(!(new File(filePath_mStats)).exists())
            return null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath_mStats));
            StarCraftBW_QueenMatchStats mStats = gson.fromJson(br, StarCraftBW_QueenMatchStats.class);
            System.out.println("FileThread: MatchStats File loaded");
            return mStats;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void stopMe(){
        this.interrupt();
    }
}