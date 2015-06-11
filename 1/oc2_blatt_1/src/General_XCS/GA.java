package General_XCS;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Rolle on 05.06.2015.
 */
public class GA {

    private final Random r = new Random();


    public ArrayList<Classifier> genAlgo_go(String find_method_parents, String crossover_method, String mutation_method, ArrayList<Classifier> base_classifier_ArrayList, String[] aviable_actions){
        ArrayList<Classifier> dummy_Classifier = new ArrayList<Classifier>();
        for(String action: aviable_actions){
            System.out.printf("Finding Parents with Method: " + find_method_parents);
            Classifier[] parent_classifier = makeParentList(action, find_method_parents, base_classifier_ArrayList);
            System.out.println("Parents are: " + Arrays.toString(parent_classifier));
            System.out.println("Crossover Method used: " + crossover_method);
            String[] crossoverd_Children_Condition = makeCrossover(crossover_method, parent_classifier);
            System.out.println("Crossover Cons are: " + Arrays.toString(crossoverd_Children_Condition));
            System.out.println("Mutation method used: " + mutation_method);
            String[] mutaited_Children_Condtion = makeMutation(mutation_method, crossoverd_Children_Condition);
            System.out.println("Mutation Cons are: " + Arrays.toString(mutaited_Children_Condtion));

            for (String child_Con: mutaited_Children_Condtion){
                dummy_Classifier.add(createDummyClassifier(action,child_Con,parent_classifier));
            }
        }

        return dummy_Classifier;
    }


    /**
     * ############################################
     * ##########    Parent Find Part    ##########
     * ############################################
     */

    private Classifier[] makeParentList(String parent_Action, String find_method_parents, ArrayList<Classifier> base_classifier_ArrayList){
        Classifier[] parent_classifier = null;
        switch (find_method_parents){

            case XCS_Constants.GEN_ALGO_FIND_BEST_PARENT:
                parent_classifier = this.findBest_Parent(parent_Action,base_classifier_ArrayList);

                break;

            case "ROULET":
                break;
        }

        return parent_classifier;
    }

    private Classifier[] findBest_Parent(String action, ArrayList<Classifier> base_classifier_ArrayList ){
        Classifier[] foundClassifiers = new Classifier[2];
        ArrayList<Classifier> copyOfBase = new ArrayList<Classifier>(base_classifier_ArrayList);
        int count = 0;
        while (count < 2){
            double bestFitness = 0.;
            Classifier bestClassifier = null;
            for(Classifier c: copyOfBase){
                if(bestFitness < c.getFitness() && action.equals(c.getAction())){
                    bestFitness = c.getFitness();
                    bestClassifier = c;
                }
            }

            copyOfBase.remove(bestClassifier);
            foundClassifiers[count] = bestClassifier;
            count++;
        }
        return foundClassifiers;
    }

    /**
     * ############################################
     * ##########    Crossover Part      ##########
     * ############################################
     */

    private String[] makeCrossover(String crossover_method,Classifier[] parent_classifier){
        String[] crossoverd_Children_Condition = null;
        Classifier father = parent_classifier[0];
        Classifier mother = parent_classifier[1];

        switch (crossover_method){
            case XCS_Constants.GEN_ALGO_ONE_POINT_CROSSOVER:
                int splitPos = 4;
                crossoverd_Children_Condition = this.onePoint_Crossover(splitPos,father.getCondition(),mother.getCondition());
                break;
            case "ROULET":
                break;
        }
        return crossoverd_Children_Condition;
    }

    private String[] onePoint_Crossover(int splitPos, String father_Con, String mother_Con){


        String[] child_Con_Array = new String[2];


        child_Con_Array[0] = father_Con.substring(0,splitPos) + mother_Con.substring(splitPos);
        child_Con_Array[1] = mother_Con.substring(0,splitPos) + father_Con.substring(splitPos);

        return child_Con_Array;
    }

    private String[] random_OnePoint_Crossover(String father_Con, String mother_Con){
        int rndSpitPos = r.nextInt(father_Con.length());
        return onePoint_Crossover(rndSpitPos, father_Con, mother_Con);
    }


    /**
     * ############################################
     * ##########    Mutation Part       ##########
     * ############################################
     */

    private String[] makeMutation(String mutation_method, String[] children_condition){
        String[] mutaited_Children_Condition = null;
        switch (mutation_method){
            case XCS_Constants.GEN_ALGO_RANDOM_ONE_POS_MUTATION:
                mutaited_Children_Condition = this.random_OnePoint_SingleMutation(children_condition);
                break;
        }
        return mutaited_Children_Condition;
    }

    private String[] random_OnePoint_SingleMutation(String[] children_condition){
        String[] mutaited_Children_Condtion = new String[2];
        for (int i = 0; i < mutaited_Children_Condtion.length; i++){
            String child_con = children_condition[i];
            int rndMutPos = r.nextInt(child_con.length());
            String[] tmp = child_con.split("");
            String s = tmp[rndMutPos];
            if(s.equals("0"))
                tmp[rndMutPos] = "1";
            else
                tmp[rndMutPos] = "0";

            mutaited_Children_Condtion[i] = String.join("", tmp);
        }

        return mutaited_Children_Condtion;
    }

    /**
     * ############################################
     * #####   Create Dummy Classifier Part    ####
     * ############################################
     */


    private void fillConMap(String action, String[] con,HashMap<String,ArrayList<String>> conMap){
        for(String condtion : con){
            if(conMap.containsKey(action))
                conMap.get(action).add(condtion);
            else {
                ArrayList<String> conArrayList = new ArrayList<String>();
                conArrayList.add(condtion);
                conMap.put(action,conArrayList);
            }
        }
    }

    private Classifier createDummyClassifier(String action, String con, Classifier[] parent_classifier ){
        String cName = "DUMMY";
        Classifier father = parent_classifier[0];
        Classifier mother = parent_classifier[1];
        double newPred = (father.getPrediction() + mother.getPrediction()) / 2;
        double newPreErr = (father.getPredictionError() + mother.getPredictionError()) / 2;
        double newFit = (father.getFitness() + mother.getFitness()) / 2;
        Classifier classifier_Child = new Classifier(
                        cName,
                        newPred,
                        newPreErr,
                        newFit,
                        con,
                        action);
        return classifier_Child;
    }




//    public ArrayList<Classifier> getOnePointCrossoverChildClassifier(ArrayList<ArrayList<Classifier>> parentList){
//
//    }
}
