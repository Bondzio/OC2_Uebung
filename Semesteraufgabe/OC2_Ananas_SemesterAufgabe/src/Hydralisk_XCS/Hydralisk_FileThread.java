package Hydralisk_XCS;

import General_XCS.ClassifierSet;
import General_XCS.PopulationSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Created by Rolle on 25.05.2015.
 */
public class Hydralisk_FileThread extends Thread{


    private ClassifierSet cSet = null;
    private String filePath_cSet = "saves.txt";

    private Hydralisk_MatchStats mStats = null;
    private String filePath_mStats = "stats.txt";


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
        // For keeping the thread Alive
        System.out.println("FileThread: started");
        stop = false;
        try {
            while (!stop) {
                saveClassifierSet();
                saveMatchStats();
            }
        } catch (InterruptedException e) {
            stop = true;
//            System.out.println("FileThread: Was interupted");
        }
//        System.out.println("FileThread:  has been stopped");
        // do it once
//        try {
//            savePopulationSet();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    public synchronized void putClassifierSetToSave(PopulationSet toSave) throws InterruptedException {
        if(stop)
            return;

        while (cSet != null) {
            System.out.println("FileThread: waiting to set new PopulationSet");
            wait();
        }

        System.out.println("FileThread: found PopulationSet to save");
        this.cSet = toSave;
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    public synchronized void putMatchStatsToSave(Hydralisk_MatchStats mStats) throws InterruptedException {
        if(stop)
            return;

        while (this.mStats != null) {
            System.out.println("FileThread: waiting to set new MatchesStats");
            wait();
        }

        System.out.println("FileThread: found MatchesStats to save");
        this.mStats = mStats;
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    private synchronized void saveClassifierSet() throws InterruptedException {
        notify();
        while (cSet == null) {
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }

        System.out.println("FileThread: save file");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(cSet);
        //System.out.println(jString);
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
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }

        System.out.println("FileThread: save file");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(mStats);
        //System.out.println(jString);
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
//        System.out.println("FileThread: Try to load File");
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

    public synchronized Hydralisk_MatchStats getSavedMatchStats() {
//        System.out.println("FileThread: Try to load MatchStats File");
        Gson gson = new Gson();

        if(!(new File(filePath_mStats)).exists())
            return null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath_mStats));
            Hydralisk_MatchStats mStats = gson.fromJson(br, Hydralisk_MatchStats.class);
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
