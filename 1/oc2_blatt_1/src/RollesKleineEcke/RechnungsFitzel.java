package RollesKleineEcke;

/**
 * Created by Rolle on 15.05.2015.
 */
public class RechnungsFitzel {

    /**
     * The error threshold under which the accuracy of a classifier is set to one.
     */
    final public static double epsilon_0=10;

    /**
     * Returns the accuracy of the classifier.
     * The accuracy is determined from the prediction error of the classifier using Wilson's
     * power function as published in 'Get Real! XCS with continuous-valued inputs' (1999)
     *
     * @see XCSConstants#epsilon_0
     * @see XCSConstants#alpha
     * @see XCSConstants#nu
     */
   /* public double getAccuracy()
    {
        double accuracy;

        if(predictionError <= (double)epsilon_0){
            accuracy = 1.;
        }else{
            accuracy = cons.alpha * Math.pow( predictionError / cons.epsilon_0 , -cons.nu);
        }
        return accuracy;
    }*/



}
