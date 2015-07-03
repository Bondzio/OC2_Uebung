package General_XCS;

/**
 * Created by Rolle on 11.05.2015.
 */
public class MatchSet extends ClassifierSet{

    public MatchSet(ClassifierSet population){
        for(Classifier c : population.getSet())
            this.addNewClassifier(c);
    }

    public String toString(){
        String s = "";
        for(Classifier c: this.getSet()){
            s += c.toString();
        }
        return s;
    }
}
