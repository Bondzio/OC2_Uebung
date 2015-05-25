package StarCraftBW_XCS;

import General_XCS.ClassifierSet;
import General_XCS.PopulationSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Created by Rolle on 25.05.2015.
 */
public class StarCraftBW_FileThread extends Thread{


    private ClassifierSet cSet = null;
    private String filePath = "saves.txt";
    private boolean stop = false;


    @Override
    public void run() {
        // For keeping the thread Alive
        try {
            while (!stop) {
                saveClassifierSet();
            }
        } catch (InterruptedException e) {
        }
        System.out.println("FileThread has been stopped");
        // do it once
//        try {
//            saveClassifierSet();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    public synchronized void putClassifierSetToSave(PopulationSet toSave) throws InterruptedException {
        while (cSet != null && !stop) {
            System.out.println("Waiting for something to save!");
            wait();
        }

        if(stop){
            return;
        }

        System.out.println("found something to save");
        this.cSet = toSave;
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    private synchronized void saveClassifierSet() throws InterruptedException {
        notify();
        while (cSet == null && !stop) {
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }

        if(stop){
            return;
        }

        System.out.println("save file");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jString = gson.toJson(cSet);
        //System.out.println(jString);
        cSet = null;
        try {
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public synchronized PopulationSet getSavedClassifierSet() {
        Gson gson = new Gson();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            PopulationSet pSet = gson.fromJson(br, PopulationSet.class);
            return pSet;
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
        return null;
    }

    public synchronized void stopMe(){
        this.stop = true;
        notify();
    }


}
