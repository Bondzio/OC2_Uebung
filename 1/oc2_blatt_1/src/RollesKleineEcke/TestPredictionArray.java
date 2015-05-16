package RollesKleineEcke;

/**
 * Created by Rolle on 11.05.2015.
 */
public class TestPredictionArray {

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

        MatchSet mSet = new MatchSet(cSet);

        System.out.println("---MaYoRs-Shit---");
        System.out.println("---BestPrediction---");
        PredictionArrayFromMaYoR pArray = new PredictionArrayFromMaYoR(mSet);
        ActionSet aSet = pArray.getBestActionSet();
        for (String s : aSet.toString().split(";")){
            System.out.println(s); 
        }
        
        System.out.println("---Roulette---");
        PredictionArrayFromMaYoR pArray2 = new PredictionArrayFromMaYoR(mSet);
        ActionSet aSet2 = pArray2.getRouletteActionSet();
        for (String s : aSet2.toString().split(";")){
            System.out.println(s);
        }
        
        
        System.out.println("\n---Rolles-Shit---");
        System.out.println("---BestPrediction---");
        PredictionArray pArray3 = new PredictionArray(mSet);
        ActionSet aSet3 = pArray3.getBestActionSet();
        for (String s : aSet3.toString().split(";")){
            System.out.println(s);
        }
        
        System.out.println("---Roulette---");
        PredictionArray pArray4 = new PredictionArray(mSet);
        ActionSet aSet4 = pArray4.getRouletteActionSet();
        for (String s : aSet4.toString().split(";")){
            System.out.println(s);
        }
    }
}
