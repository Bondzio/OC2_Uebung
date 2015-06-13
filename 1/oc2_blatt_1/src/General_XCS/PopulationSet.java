package General_XCS;



import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PopulationSet extends ClassifierSet{

    private String[] actionSet;
    private int idCounter = 0;
    private int ga_counter = 0;



    /*
     FOR GA
     */
    private GA genericAlgorithm = new GA();
    private String parent_select_method_type = XCS_Constants.GEN_ALGO_PARENT_FIND_BEST;
    private String crossover_method_type = XCS_Constants.GEN_ALGO_CROSSOVER_ONE_POINT;
    private String mutation_method_type = XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS;
    private int ga_classifier_creation_threshold = 10000;
    private int ga_cooldown_time = 10;
    private int ga_min_pop_size = 100;

    public void setParent_select_method_type(String parent_select_method_type) {
        this.parent_select_method_type = parent_select_method_type;
    }

    public void setCrossover_method_type(String crossover_method_type) {
        this.crossover_method_type = crossover_method_type;
    }

    public void setMutation_method_type(String mutation_method_type) {
        this.mutation_method_type = mutation_method_type;
    }

    public void setGa_classifier_creation_threshold(int ga_classifier_creation_threshold) {
        this.ga_classifier_creation_threshold = ga_classifier_creation_threshold;
    }

    public void setGa_cooldown_time(int ga_cooldown_time) {
        this.ga_cooldown_time = ga_cooldown_time;
    }

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
            for(Classifier c: this.myCollection){
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
        if(ga_counter < ga_cooldown_time){
            ga_counter++;
            return;
        }
        else
            ga_counter = 0;

        
        if( myCollection.size() >= ga_classifier_creation_threshold || myCollection.size() < ga_min_pop_size)
            return;

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
        return;
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
