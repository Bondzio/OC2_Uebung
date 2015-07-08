package Test;

import FileHandler.SimpleFileHandler;
import bolding.ParamSetCollection;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Test {

    public static void main(String[] args){
        double distance = 300;
        int convDistance = (int) distance;
//        if (convDistance > 160)
//            convDistance = 161; // as Binary 10100001


        String ret = Integer.toBinaryString(convDistance);
        System.out.println(ret);
        System.out.println(ret.length());
    }
}
