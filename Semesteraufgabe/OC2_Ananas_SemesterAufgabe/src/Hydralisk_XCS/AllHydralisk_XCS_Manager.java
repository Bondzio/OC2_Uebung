package Hydralisk_XCS;

import FileHandler.SimpleFileHandler;
import General_XCS.Classifier;
import General_XCS.PopulationSet;
import General_XCS.XCS_Constants;
import Units.Hatchery;
import Units.Hydralisk;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AllHydralisk_XCS_Manager {

    private final JNIBWAPI bwapi;
    private SimpleFileHandler simpleFileHandler = new SimpleFileHandler(null,null,"Hydralisk_XCS_PopulationSet");
    private ArrayList<Managed_Hydralisk>  managedHyd = new ArrayList<>();
    private String[] actionSet = {"attackMoveToClosestEnemy","attackMoveToClosestFlyingEnemy","moveToDefPoint", "supportFriend" , "protectHatchery", "burrow"};
    private PopulationSet loadedPopSetForAllHydras = null;

    private int maxPopulationSize = 10000;


    //GA type
    /*
        Scroll down to inner class
     */


    public AllHydralisk_XCS_Manager(JNIBWAPI bwapi) {
        this.bwapi = bwapi;

    }

    public Hydralisk createHydralisk(Unit hydralisk){
        Hydralisk hyd = new Hydralisk(hydralisk,this.bwapi,this);
        Managed_Hydralisk mH;
        if(loadedPopSetForAllHydras != null)
            mH = new Managed_Hydralisk(hydralisk.getID(),actionSet,loadedPopSetForAllHydras);
        else
            mH = new Managed_Hydralisk(hydralisk.getID(),actionSet);

        this.managedHyd.add(mH);

        return hyd;
    }

    public void giveDetectorSomethingToDetected(int hydralisk_id, double distanceToHatcheryToDefend){
        Managed_Hydralisk mh = getSpecificManagedHydraliks(hydralisk_id);
        mh.getdDetector().setDistance(distanceToHatcheryToDefend);
    }

    public String getNextPredictedAction(int hydralisk_id){
        Managed_Hydralisk mh = getSpecificManagedHydraliks(hydralisk_id);
        mh.getHydralisk_xcs().cleanCurrentAction();
        mh.getHydralisk_xcs().execPredictAction(true);
        return mh.getTheEffector().getCurrentActionToExecute();
    }

    public void actionExecutionFin(Hydralisk myHydra, Unit target, Double distanceToDefPoint, Hatchery hatcheryToDefend){
        Managed_Hydralisk mh = getSpecificManagedHydraliks(myHydra.getUnit().getID());
        mh.getTheEffector().setStats(myHydra,target,distanceToDefPoint,hatcheryToDefend);
        mh.getHydralisk_xcs().rewardCurrentAction();
    }


    public void saveProgress(){
        PopulationSet pSet = null;
        for(Managed_Hydralisk mH : managedHyd){
            if(pSet == null)
                pSet = mH.getHydralisk_xcs().getPopulationSet();
            else {
                PopulationSet tmp = mH.getHydralisk_xcs().getPopulationSet();
                for(Classifier classifier: tmp.getSet())
                    pSet.addClassifierToPopulationSet(classifier);
            }
        }


        if(pSet.getSet().size() >= maxPopulationSize){
            ArrayList<Classifier> pSetAsArrayList = pSet.getSet();

            Collections.sort(pSetAsArrayList, new Comparator<Classifier>() {
                @Override
                public int compare(Classifier o1, Classifier o2) {
                    double diff = o1.getFitness() - o2.getFitness();
                    if (diff < 0.0)
                        return -1;
                    else if (diff == 0.0)
                        return 0;
                    else
                        return +1;

                }
            });

            double elementsToDelete = pSetAsArrayList.size() - maxPopulationSize; // delete 20% so we got room for new ones
            double sizeAfter = pSetAsArrayList.size() - elementsToDelete;
            elementsToDelete += sizeAfter * 0.1;

            for(int i = 0; i < elementsToDelete; i++)
                pSetAsArrayList.remove(0);

            System.out.println("ALL HADRA MANG: removed some elements old: " + pSet.getSet().size()+" new: " + pSetAsArrayList.size());
            PopulationSet pSetToSave = new PopulationSet(actionSet);
            for(Classifier classifier: pSetAsArrayList)
                pSetToSave.addClassifierToPopulationSet(classifier);

            pSet = pSetToSave;
        }

        System.out.println("ALL HADRA MANG: pSet to Save Size " + pSet.getSet().size());
        simpleFileHandler.savePopulationSet(pSet);
    }


    public void loadProcess(){
        loadedPopSetForAllHydras = simpleFileHandler.laodPopulationSet();
    }



    private Managed_Hydralisk getSpecificManagedHydraliks(int hydralisk_id){
        Managed_Hydralisk retMH = null;
        for (Managed_Hydralisk mH : managedHyd){
            if(mH.getManagedUnitID() == hydralisk_id) {
                retMH = mH;
            }
        }
        return retMH;
    }

    private class Managed_Hydralisk {

        private final Hydralisk_DistanceDetector dDetector;
        private final Hydralisk_Effector theEffector;
        private final The_Hydralisk_XCS hydralisk_xcs;
        private final int managedUnitID;

        public Managed_Hydralisk(int unitId, String[] actionSet) {
            this.dDetector = new Hydralisk_DistanceDetector();
            this.theEffector = new Hydralisk_Effector();
            this.hydralisk_xcs = new The_Hydralisk_XCS(actionSet,theEffector,dDetector);

            this.managedUnitID = unitId;

            setGA_Type();
        }

        public Managed_Hydralisk(int unitId, String[] actionSet, PopulationSet pSet) {
            this.dDetector = new Hydralisk_DistanceDetector();
            this.theEffector = new Hydralisk_Effector();
            this.hydralisk_xcs = new The_Hydralisk_XCS(actionSet,theEffector,dDetector);
            PopulationSet myPset = getHydralisk_xcs().getPopulationSet();
            for(Classifier c : pSet.getSet())
                myPset.addClassifierToPopulationSet(c);

            this.managedUnitID = unitId;

            setGA_Type();
        }

        private void setGA_Type(){
            PopulationSet pSet = hydralisk_xcs.getPopulationSet();
            pSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
            pSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_RANDOM_ONE_POINT);
            pSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
            pSet.setGa_classifier_creation_threshold(maxPopulationSize);
            pSet.setGa_cooldown_time(15);
        }

        public Hydralisk_DistanceDetector getdDetector() {
            return dDetector;
        }

        public Hydralisk_Effector getTheEffector() {
            return theEffector;
        }

        public The_Hydralisk_XCS getHydralisk_xcs() {
            return hydralisk_xcs;
        }

        public int getManagedUnitID() {
            return managedUnitID;
        }
    }



}