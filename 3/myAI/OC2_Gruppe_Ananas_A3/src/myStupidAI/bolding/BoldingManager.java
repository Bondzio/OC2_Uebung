package myStupidAI.bolding;

import java.util.Random;

/**
 * Created by Rolle on 27.06.2015.
 */
public class BoldingManager {

    private final SimpleFileHandler fileHandler;
    private ParamSetCollection pSetColl;
    private final Flock_Ga flock_ga= new Flock_Ga();
    private RuleMachine ruleMachine;


    public BoldingManager() {
        this.fileHandler = new SimpleFileHandler();
        this.pSetColl = fileHandler.loadParamSetCollection();
        if(this.pSetColl == null){
            this.pSetColl = new ParamSetCollection();
        }
        createRuleMachineWithRouletParamSet();
    }

    public RuleMachine getRuleMachine() {
        return ruleMachine;
    }

    private void createRuleMachineWithRouletParamSet(){
        this.ruleMachine = new RuleMachine(this.getRouletParamSet());
    }

    private ParamSet getRouletParamSet(){
        execGa();
        int rewardSum = 0;
        for (ParamSet pS : pSetColl.getParams()){
            rewardSum += pS.getReward();
        }

        Random r = new Random();
        int randomRew = r.nextInt(rewardSum + 1);

        rewardSum = 0;
        int pos = 0;
        for(ParamSet pS : pSetColl.getParams()){
            rewardSum += pS.getReward();
            if(rewardSum < randomRew){
                pos++;
            }
            else
                break;
        }
       return pSetColl.getParams().get(pos);
    }

    private void execGa(){
        if(pSetColl.getParams().size() < 2){
            addRndParamSet();
            addRndParamSet();
        }

        flock_ga.doGa(pSetColl);
    }

    private void addRndParamSet(){
        Random r = new Random();

        double w1 = r.nextDouble();
        double w3 = r.nextDouble();
        double w4 = r.nextDouble();

        int rangeOfN = r.nextInt(100);
        int lines = r.nextInt(5);
        int columns = r.nextInt(5);

        ParamSet pSet = new ParamSet(
                w1,
                w3,
                w4,
                rangeOfN,
                lines,
                columns,
                1
        );
        pSetColl.addNewParamSet(pSet);
    }

    public void gameFin(int hp_from_UnistLeft){
        rewardParamSet(hp_from_UnistLeft);
        pSetColl.cleanUp();
        fileHandler.saveParamSetCollection(pSetColl);
    }

    private void rewardParamSet(int hp_from_UnistLeft){
        this.ruleMachine.getpSet().setReward(hp_from_UnistLeft);
    }



}
