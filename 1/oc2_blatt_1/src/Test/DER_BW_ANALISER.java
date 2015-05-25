package Test;

import General_XCS.Classifier;
import General_XCS.PopulationSet;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Rolle on 25.05.2015.
 */
public class DER_BW_ANALISER {

    public static void main(String[] args) {
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


}
