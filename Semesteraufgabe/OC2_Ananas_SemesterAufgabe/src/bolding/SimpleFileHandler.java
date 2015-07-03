package bolding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Created by Rolle on 27.06.2015.
 */
public class SimpleFileHandler {

    private String filePath = "flock_paramSet";


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
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(jString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ParamSetCollection loadParamSetCollection(){
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            ParamSetCollection pSetCol = gson.fromJson(br, ParamSetCollection.class);
            System.out.println("SimpleFileHandler: File loaded");
            return pSetCol;
        } catch (FileNotFoundException e) {
            System.out.println("SimpleFileHandler: no file found under path: " + filePath);
            //e.printStackTrace();
        }
        return null;
    }
}
