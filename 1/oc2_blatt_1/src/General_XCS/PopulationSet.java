package General_XCS;



import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PopulationSet extends ClassifierSet{

    private String[] actionSet;
    private int idCounter = 0;



    public PopulationSet(ClassifierSet population,String[] actionSet) {
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
            for(Classifier c: getSet()){
                String cConditon = c.getCondition();
                conAsArray = cConditon.split("");

                if(this.isEqual(aMatcher,conAsArray)) {
                    newSet.addNewClassifier(c);
                    foundSomething = true;
                }
            }
            if (!foundSomething)
                covering(matcher);
            else break;
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
            String cName = XCS_Constants.DEFAULT_CLASSIFIER_NAME + "_" + Integer.toString(idCounter);

            Classifier newC = new Classifier(
                    cName,
                    XCS_Constants.DEFAULT_CLASSIFIER_PREDICTION,
                    XCS_Constants.DEFAULT_CLASSIFIER_PREDICTION_ERR,
                    XCS_Constants.DEFAULT_CLASSIFIER_FITNESS,
                    matcher,
                    action);

            this.addNewClassifier(newC);
            idCounter++;
        }
    }
}
