package FileHandler;

import General_XCS.ClassifierSet;
import General_XCS.PopulationSet;
import StarCraftBW_XCS.StarCraftBW_MatchStats;
import bolding.ParamSetCollection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Created by Rolle on 27.06.2015.
 */
public class SimpleFileHandler {

    private String filePath = "AnanasSaves\\";

    private String fileName_Bolding_Saves = "Paramset_saves.txt";
    private String fileName_MatchStats = "MatchStats.txt";
    private String fileName_XCS_PopulationSet = "XCS_Population.txt";



    public SimpleFileHandler(String fileName_Bolding_Saves, String fileName_MatchStats, String fileName_XCS_PopulationSet) {

        if(fileName_Bolding_Saves != null)
            this.fileName_Bolding_Saves = fileName_Bolding_Saves;
        if(fileName_MatchStats != null)
            this.fileName_MatchStats = fileName_MatchStats;
        if(fileName_XCS_PopulationSet != null)
            this.fileName_XCS_PopulationSet = fileName_XCS_PopulationSet;

        makeDir();
    }

    public SimpleFileHandler() {

        makeDir();
    }

    private void makeDir(){


        File theDir = new File("AnanasSaves");

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("SimpleFileHandler: creating directory AnanasSaves");
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("SimpleFileHandler: directory AnanasSaves created");
            }
        }


    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void saveParamSetCollection(ParamSetCollection pSetCol){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(pSetCol);
        try{
            PrintWriter writer = new PrintWriter(filePath + fileName_Bolding_Saves);
            System.out.println("FileHandler.SimpleFileHandler: File "+filePath + fileName_Bolding_Saves+" loaded");
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ParamSetCollection loadParamSetCollection(){
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath + fileName_Bolding_Saves));
            ParamSetCollection pSetCol = gson.fromJson(br, ParamSetCollection.class);
            System.out.println("FileHandler.SimpleFileHandler: File "+filePath + fileName_Bolding_Saves+" loaded");
            return pSetCol;
        } catch (FileNotFoundException e) {
            System.out.println("FileHandler.SimpleFileHandler: no file found under path: " + filePath + fileName_Bolding_Saves);
            //e.printStackTrace();
        }
        return null;
    }

    public void savePopulationSet(PopulationSet populationSet) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(populationSet);
        try {
            PrintWriter writer = new PrintWriter(filePath + fileName_XCS_PopulationSet);
            writer.println(jString);
            writer.close();
            System.out.println("FileHandler.SimpleFileHandler: File "+filePath + fileName_XCS_PopulationSet+" loaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveMatchStats(StarCraftBW_MatchStats mStats){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(mStats);
        try {
            PrintWriter writer = new PrintWriter(filePath + fileName_MatchStats);
            System.out.println("FileHandler.SimpleFileHandler: File "+filePath + fileName_MatchStats+" loaded");
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public PopulationSet laodPopulationSet() {
        Gson gson = new Gson();

        if(!(new File(filePath + fileName_XCS_PopulationSet)).exists())
            return null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath + fileName_XCS_PopulationSet));
            PopulationSet pSet = gson.fromJson(br, PopulationSet.class);
            System.out.println("SimpleFileHandler: File "+ filePath + fileName_XCS_PopulationSet +" loaded");
            return pSet;
        } catch (FileNotFoundException e) {
            System.out.println("SimpleFileHandler: File "+ filePath + fileName_XCS_PopulationSet +" not Found");
            //e.printStackTrace();
        }
        return null;
    }

    public synchronized StarCraftBW_MatchStats loadMatchStats() {
//        System.out.println("FileThread: Try to load MatchStats File");
        Gson gson = new Gson();

        if(!(new File(filePath + fileName_MatchStats)).exists())
            return null;

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath + fileName_MatchStats));
            StarCraftBW_MatchStats mStats = gson.fromJson(br, StarCraftBW_MatchStats.class);
            System.out.println("FileThread: MatchStats File loaded");
            return mStats;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setFileName_Bolding_Saves(String fileName_Bolding_Saves) {
        this.fileName_Bolding_Saves = fileName_Bolding_Saves;
    }

    public void setFileName_MatchStats(String fileName_MatchStats) {
        this.fileName_MatchStats = fileName_MatchStats;
    }

    public void setFileName_XCS_PopulationSetSet(String fileName_XCS_PopulationSet) {
        this.fileName_XCS_PopulationSet = fileName_XCS_PopulationSet;
    }
}
