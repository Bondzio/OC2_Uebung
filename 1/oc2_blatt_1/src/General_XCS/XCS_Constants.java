package General_XCS;

/**
 * Created by Rolle on 16.05.2015.
 */
public class XCS_Constants {

    public static final double DEFAULT_CLASSIFIER_PREDICTION = 1;
    public static final double DEFAULT_CLASSIFIER_PREDICTION_ERR = 0.5;
    public static final double DEFAULT_CLASSIFIER_FITNESS = 0.5;
    public static final String DEFAULT_CLASSIFIER_NAME = "DefClassifier";

    /**
     * The learning rate for updating fitness, prediction, prediction error,
     * and action set size estimate in XCS's classifiers.
     */
    public static final double BETA = 0.2;

    /**
     * The fall of rate in the fitness evaluation.
     */
    final public static double ALPHA=0.1;

    /**
     * The error threshold under which the accuracy of a classifier is set to one.
     */
    final public static double EPSILON_ZERO=10;

    /**
     * Specifies the exponent in the power function for the fitness evaluation.
     */
    final public static double NU=5.;

    /**
     * The discount rate in multi-step problems.
     */
    final public static double GAMMA=0.95;

}
