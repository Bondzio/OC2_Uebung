package Test;

import General_XCS.Classifier;
import General_XCS.ClassifierSet;
import General_XCS.PopulationSet;
import General_XCS.XCS;
import StarCraftBW_XCS.StarCraftBW_DistanceDetector;
import StarCraftBW_XCS.StarCraftBW_Effector;
import StarCraftBW_XCS.StarCraftBW_FileThread;

/**
 * Created by Rolle on 25.05.2015.
 */
public class TestFileThread {
    public static void main(String[] args) {

        Classifier first = new Classifier("First",5,0,40,"0#0#","This");
        Classifier second = new Classifier("second",8,0,2,"0011","This");
        Classifier third = new Classifier("third",4,0,3,"0011","is");
        Classifier fourth = new Classifier("fourth",2,0,4,"0000","Sparta");

        ClassifierSet cSet = new ClassifierSet();
        cSet.addNewClassifier(first);
        cSet.addNewClassifier(second);
        cSet.addNewClassifier(third);
        cSet.addNewClassifier(fourth);

        String[] aSet = {"move","kite"};

        PopulationSet pSet = new PopulationSet(cSet,aSet);

        StarCraftBW_FileThread fT = new StarCraftBW_FileThread();
        fT.start();

        for(int i=0; i <=10; i++){
            try {
                fT.putClassifierSetToSave(pSet);

                System.out.println("DOING STUFF");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        PopulationSet testP = fT.getSavedClassifierSet();


        fT.stopMe();

        for(Classifier c : testP.getSet()){
            System.out.println(c.toString());
        }




    }
}
