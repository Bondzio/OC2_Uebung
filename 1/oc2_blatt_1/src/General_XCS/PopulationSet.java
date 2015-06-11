package General_XCS;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PopulationSet extends ClassifierSet{

    private String[] actionSet;
    private int idCounter = 0;
    private GA genericAlgorithm = new GA();

    /*
     FOR GA
     */
    private String parent_select_method_type = XCS_Constants.GEN_ALGO_FIND_BEST_PARENT;
    private String crossover_method_type = XCS_Constants.GEN_ALGO_ONE_POINT_CROSSOVER;
    private String mutation_method_type = XCS_Constants.GEN_ALGO_RANDOM_ONE_POS_MUTATION;


    public PopulationSet(ClassifierSet population,String[] actionSet) {
        super();
        for(Classifier c : population.getSet())
            this.addNewClassifier(c);

        this.actionSet = actionSet;
    }

    public PopulationSet(String[] actionSet) {
        this.actionSet = actionSet;
    }
    
    
    public MatchSet findMatchingClassifier(String matcher){
//        ArrayList<Classifier> cSet = population.getSet();
        String[] aMatcher = matcher.split("");

        ClassifierSet newSet = new ClassifierSet();

        String[] conAsArray;
        while(true){
            boolean foundSomething = false;
            for(Classifier c: this.myColletction){
                String cConditon = c.getCondition();
                conAsArray = cConditon.split("");

                if(this.isEqual(aMatcher,conAsArray)) {
                    newSet.addNewClassifier(c);
                    foundSomething = true;
                }
            }
            if (!foundSomething)
                covering(matcher);
            else{
                doGA();
                break;
            }


        }

        return new MatchSet(newSet);
        
    }

    private boolean isEqual(String[] aMatcher, String[] conAsArray){
    	if (aMatcher.length != conAsArray.length)
    		return false;
    	
        for(int i = 0; i<conAsArray.length; i++){
            String sFromCon = conAsArray[i];
            String sFromMat = aMatcher[i];
            if(sFromCon.equals("#") || sFromCon.equals(sFromMat))
                continue;
            else
                return false;
        }
        return true;
    }

    private void covering(String matcher){

        for(String action : this.actionSet){
            String cName = "Cov_Classifier" + "_" + Integer.toString(idCounter++);

            Classifier newC = new Classifier(
                    cName,
                    XCS_Constants.DEFAULT_CLASSIFIER_PREDICTION,
                    XCS_Constants.DEFAULT_CLASSIFIER_PREDICTION_ERR,
                    XCS_Constants.DEFAULT_CLASSIFIER_FITNESS,
                    matcher,
                    action);

            this.addNewClassifier(newC);
        }
    }

    private void createAndAddClassifier(String id,double pre,double pre_err,double fit,String con,String action){
        Classifier newC = new Classifier(id,pre,pre_err,fit,con,action);
        this.addNewClassifier(newC);
    }
    private void doGA(){
        ArrayList<Classifier> dummyClassifierList = genericAlgorithm.genAlgo_go(
                parent_select_method_type,
                crossover_method_type,
                mutation_method_type,
                this.getSet(),
                actionSet
        );

        for(Classifier dummyClassifier : dummyClassifierList){
            createAndAddClassifier(
               new String("GA_Classifier" + "_" + Integer.toString(idCounter++)),
                    dummyClassifier.getPrediction(),
                    dummyClassifier.getPredictionError(),
                    dummyClassifier.getFitness(),
                    dummyClassifier.getCondition(),
                    dummyClassifier.getAction()
            );
        }

    }

//    private void ga_OnePoint_Crossover(){
//
//        ArrayList<ArrayList<Classifier>> parentList = new ArrayList<ArrayList<Classifier>>();
//
//        for(String action : actionSet){
//            parentList.add(genericAlgorithm.findBest_Parents(action, this.getSet()));
//        }
//
//        for(ArrayList<Classifier> parents : parentList){
//            Classifier father = parents.get(0);
//            Classifier mother = parents.get(1);
//
//            String[] child_con_Array = genericAlgorithm.onePoint_Crossover(4,father.getCondition(),mother.getCondition());
//
//            for(String child_Con : child_con_Array){
//                String cName = "GA_Classifier" + "_" + Integer.toString(idCounter++);
//                String action = father.getAction(); // mother got the same
//                double newPred = (father.getPrediction() + mother.getPrediction()) / 2;
//                double newPreErr = (father.getPredictionError() + mother.getPredictionError()) / 2;
//                double newFit = (father.getFitness() + mother.getFitness()) / 2;
//
//                Classifier classifier_Child = new Classifier(
//                        cName,
//                        newPred,
//                        newPreErr,
//                        newFit,
//                        child_Con,
//                        action);
//
//                this.addNewClassifier(classifier_Child);
//            }
//        }
//
//
//    }



}
