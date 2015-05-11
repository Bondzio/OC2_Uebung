package RollesKleineEcke;

import java.util.ArrayList;

/**
 * Created by Rolle on 11.05.2015.
 */
public class ClassifierSet {

    private ArrayList<Classifier> myColletction = new ArrayList<Classifier>();
    
    public ArrayList<Classifier> getSet() {
        return new ArrayList<Classifier>(myColletction);
    }

    
    public boolean addNewClassifier(Classifier newClassifier){
        return myColletction.add(newClassifier);
    }
    

}
