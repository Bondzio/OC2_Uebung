package RollesKleineEcke;


/**
 * Created by Rolle on 15.05.2015.
 */
public class TestRolletWheel {

    public static void main(String[] args) {

        Classifier first = new Classifier("First", 5, 0, 40, "0#0#", "This");
        Classifier second = new Classifier("second", 8, 0, 2, "0011", "This");
        Classifier third = new Classifier("third", 4, 0, 3, "0011", "is");
        Classifier fourth = new Classifier("fourth", 9, 0, 4, "0000", "Sparta");

        ClassifierSet cSet = new ClassifierSet();
        cSet.addNewClassifier(first);
        cSet.addNewClassifier(second);
        cSet.addNewClassifier(third);
        cSet.addNewClassifier(fourth);

        ActionSet aSet = new ActionSet(cSet);

        Classifier c = aSet.rouletteActionWinner();

        System.out.println(c);

    }

}
