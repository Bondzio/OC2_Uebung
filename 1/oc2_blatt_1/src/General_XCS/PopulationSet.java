package General_XCS;



import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PopulationSet extends ClassifierSet{

    private String[] actionSet;
    private int idCounter = 0;
    private GA genericAlgorithm = new GA();


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
                //ga_OnePoint_Crossover();
                genAlgo();
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

    private void genAlgo(){
        ArrayList<ArrayList<Classifier>> parentList = makeParentList("BEST");
        doGA_Type("ONE_POINT_CROSSOVER",parentList);
    }

    private ArrayList<ArrayList<Classifier>> makeParentList(String type){
        ArrayList<ArrayList<Classifier>> parentList = new ArrayList<ArrayList<Classifier>>();
        switch (type){
            case "BEST":
                for(String action : actionSet){
                    parentList.add(genericAlgorithm.findBest_Parents(action, this.getSet()));
                }
                break;
            case "ROULET":
                break;
        }

        return parentList;
    }

    private void doGA_Type(String ga_type,ArrayList<ArrayList<Classifier>> parentList ){
        switch (ga_type){
            case "ONE_POINT_CROSSOVER":

                break;
        }
    }

    private void ga_OnePoint_Crossover(){

        ArrayList<ArrayList<Classifier>> parentList = new ArrayList<ArrayList<Classifier>>();

        for(String action : actionSet){
            parentList.add(genericAlgorithm.findBest_Parents(action, this.getSet()));
        }

        for(ArrayList<Classifier> parents : parentList){
            Classifier father = parents.get(0);
            Classifier mother = parents.get(1);

            String[] child_con_Array = genericAlgorithm.onePoint_Crossover(4,father.getCondition(),mother.getCondition());

            for(String child_Con : child_con_Array){
                String cName = "GA_Classifier" + "_" + Integer.toString(idCounter++);
                String action = father.getAction(); // mother got the same
                double newPred = (father.getPrediction() + mother.getPrediction()) / 2;
                double newPreErr = (father.getPredictionError() + mother.getPredictionError()) / 2;
                double newFit = (father.getFitness() + mother.getFitness()) / 2;

                Classifier classifier_Child = new Classifier(
                        cName,
                        newPred,
                        newPreErr,
                        newFit,
                        child_Con,
                        action);

                this.addNewClassifier(classifier_Child);
            }
        }


    }



}
