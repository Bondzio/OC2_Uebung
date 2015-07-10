package Test;

import FileHandler.SimpleFileHandler;
import General_XCS.Classifier;
import General_XCS.PopulationSet;
import bolding.ParamSetCollection;

import java.util.*;

/**
 * Created by Rolle on 03.07.2015.
 */
public class Test {

    public static void main(String[] args){
        SimpleFileHandler simpleFileHandler = new SimpleFileHandler(null,null,"Hydralisk_XCS_PopulationSet");

        PopulationSet pSet = simpleFileHandler.laodPopulationSet();

        ArrayList<Classifier> pSetAsArrayList = pSet.getSet();


        Collections.sort(pSetAsArrayList, new Comparator<Classifier>() {
            @Override
            public int compare(Classifier o1, Classifier o2) {
                double diff = o1.getFitness() - o2.getFitness();
                if (diff < 0.0)
                    return -1;
                else if (diff == 0.0)
                    return 0;
                else
                    return +1;

            }
        });
        for(Classifier c : pSetAsArrayList)

            System.out.println(c.getId() +":\t\t" + c.getFitness());


        System.out.println("########################");
        System.out.println("Size was: " + pSet.getSet().size());
    }
}
