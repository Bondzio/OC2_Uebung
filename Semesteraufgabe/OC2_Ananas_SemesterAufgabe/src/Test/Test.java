package Test;

import FileHandler.SimpleFileHandler;
import bolding.ParamSetCollection;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Test {

    public static void main(String[] args){
        SimpleFileHandler sH = new SimpleFileHandler();

        sH.saveParamSetCollection(new ParamSetCollection());
    }
}
