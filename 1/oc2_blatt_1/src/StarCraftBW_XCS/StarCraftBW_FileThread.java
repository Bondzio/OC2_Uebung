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
        System.out.println("FileThread: started");
        stop = false;
        try {
            while (!stop) {
                saveClassifierSet();
            }
        } catch (InterruptedException e) {
            stop = true;
            System.out.println("FileThread: Was interupted");
        }
        System.out.println("FileThread:  has been stopped");
        // do it once
//        try {
//            saveClassifierSet();
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

        System.out.println("FileThread: found something to save");
        this.cSet = toSave;
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
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public synchronized PopulationSet getSavedPopulationSet() {
        System.out.println("FileThread: Try to load File");
        Gson gson = new Gson();
        
        if(!(new File(filePath)).exists())
        	return null;
        
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            PopulationSet pSet = gson.fromJson(br, PopulationSet.class);
            System.out.println("FileThread: File loaded");
            return pSet;
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
        return null;
    }

    public synchronized void stopMe(){
        this.interrupt();
    }




}
