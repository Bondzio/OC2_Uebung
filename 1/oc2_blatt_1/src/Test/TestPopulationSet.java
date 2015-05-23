package Test;

import General_XCS.Classifier;
import General_XCS.ClassifierSet;
import General_XCS.MatchSet;
import General_XCS.PopulationSet;

/**
 * Created by Rolle on 11.05.2015.
 */
public class TestPopulationSet {

    public static void main(String[] args) {
        Classifier first = new Classifier("First",0,0,0,"0#0#","This");
        Classifier second = new Classifier("second",0,0,0,"0011","is");
        Classifier third = new Classifier("third",0,0,0,"0000","Sparta");

        ClassifierSet cSet = new ClassifierSet();
        cSet.addNewClassifier(first);
        cSet.addNewClassifier(second);
        cSet.addNewClassifier(third);

        PopulationSet pSet = new PopulationSet(cSet);

        String Matcher = "0000";

        MatchSet mSet = pSet.findMatchingClassifier(Matcher);

        for (String s : mSet.toString().split(";")){
            System.out.println(s);
        }
    }
}
