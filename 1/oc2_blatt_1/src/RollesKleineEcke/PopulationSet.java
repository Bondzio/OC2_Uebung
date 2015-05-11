package RollesKleineEcke;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class PopulationSet {

    private ClassifierSet population;


    public PopulationSet(ClassifierSet population) {
        this.population = population;
    }
    
    
    public MatchSet findMatchingClassifier(String matcher){
        ArrayList<Classifier> cSet = population.getSet();
        String[] aMatcher = matcher.split("");

        ClassifierSet newSet = new ClassifierSet();


        String[] conAsArray;
        for(Classifier c: cSet){
            String cConditon = c.getCondition();
            conAsArray = cConditon.split("");

            if(this.isEqual(aMatcher,conAsArray))
                newSet.addNewClassifier(c);

        }

        return new MatchSet(newSet);
        
    }

    private boolean isEqual(String[] aMatcher, String[] conAsArray){
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
}
