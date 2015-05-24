package RollesKleineEcke;

import General_XCS.Classifier;
import General_XCS.ClassifierSet;

/**
 * Created by Rolle on 22.05.2015.
 */
public class TestUpdate {

    public static void main(String[] args) {
        Classifier first = new Classifier("First", 0, 0, 0, "0#0#", "This");
        Classifier second = new Classifier("second", 0, 0, 0, "0011", "is");
        Classifier third = new Classifier("third", 0, 0, 0, "0000", "Sparta");

        ClassifierSet cSet = new ClassifierSet();
        cSet.addNewClassifier(first);
        cSet.addNewClassifier(second);
        cSet.addNewClassifier(third);

        cSet.updateSet(10,10);

        for (Classifier c : cSet.getSet()){

            System.out.println(c.toString());
        }

    }


}
