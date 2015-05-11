package RollesKleineEcke;

/**
 * Created by Rolle on 11.05.2015.
 */
public class TestPredictionArray {

    public static void main(String[] args) {

        Classifier first = new Classifier("First",1,0,9,"0#0#","This");
        Classifier second = new Classifier("second",1,0,2,"0011","This");
        Classifier third = new Classifier("third",1,0,3,"0011","is");
        Classifier fourth = new Classifier("fourth",1,0,4,"0000","Sparta");

        ClassifierSet cSet = new ClassifierSet();
        cSet.addNewClassifier(first);
        cSet.addNewClassifier(second);
        cSet.addNewClassifier(third);
        cSet.addNewClassifier(fourth);

        MatchSet mSet = new MatchSet(cSet);

        PredictionArray pArray = new PredictionArray(mSet);
        ActionSet aSet = pArray.getActionSet();

        for (String s : aSet.toString().split(";")){
            System.out.println(s);
        }
    }
}
