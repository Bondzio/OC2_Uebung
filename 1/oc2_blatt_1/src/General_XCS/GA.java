package General_XCS;





import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rolle on 05.06.2015.
 */
public class GA {

    private final Random r = new Random();


    public String[] onePoint_Crossover(int splitPos, String father_Con, String mother_Con){


        String[] child_Con_Array = new String[2];


        child_Con_Array[0] = father_Con.substring(0,splitPos) + mother_Con.substring(splitPos);
        child_Con_Array[1] = mother_Con.substring(0,splitPos) + father_Con.substring(splitPos);

        return child_Con_Array;
    }

    public String[] random_OnePoint_Crossover(String father_Con, String mother_Con){
        int rndSpitPos = r.nextInt(father_Con.length());
        return onePoint_Crossover(rndSpitPos,father_Con,mother_Con);
    }

    public String random_OnePoint_SingleMutation(String con){
        int rndMutPos = r.nextInt(con.length());
        String[] tmp = con.split("");
        String s = tmp[rndMutPos];
        if(s.equals("0"))
            tmp[rndMutPos] = "1";
        else
            tmp[rndMutPos] = "0";

        return String.join("", tmp);
    }

    public ArrayList<Classifier> findBest_Parents(String action, ArrayList<Classifier> copyOfPopulation ){
        ArrayList<Classifier> foundClassifiers = new ArrayList<Classifier>();
        int count = 0;
        while (count < 2){
            double bestFitness = 0.;
            Classifier bestClassifier = null;
            for(Classifier c: copyOfPopulation){
                if(bestFitness < c.getFitness() && action.equals(c.getAction())){
                    bestFitness = c.getFitness();
                    bestClassifier = c;
                }
            }

            copyOfPopulation.remove(bestClassifier);
            foundClassifiers.add(bestClassifier);
            count++;
        }
        return foundClassifiers;
    }

//    public ArrayList<Classifier> getOnePointCrossoverChildClassifier(ArrayList<ArrayList<Classifier>> parentList){
//
//    }
}
