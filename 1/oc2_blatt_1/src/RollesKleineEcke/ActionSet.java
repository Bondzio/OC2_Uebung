package RollesKleineEcke;

import java.util.Random;

/**
 * Created by Rolle on 11.05.2015.
 */
public class ActionSet extends ClassifierSet{

    public ActionSet(ClassifierSet population){
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

    public Classifier rouletteActionWinner(){
        double bidSum=0.;
        int i;
        for(Classifier c: getSet())
            bidSum+= c.getPrediction();

        Random generator = new Random();
        bidSum*=generator.nextDouble();

        double bidC=0.0;
        for(i=0; bidC<bidSum; i++){
            bidC+= getSet().get(i).getPrediction();
        }

        return getSet().get(i-1);
    }
}
