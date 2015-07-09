package Hydralisk_XCS;

import FileHandler.SimpleFileHandler;
import General_XCS.PopulationSet;
import General_XCS.XCS_Constants;
import Units.Hatchery;
import Units.Hydralisk;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;

import java.util.ArrayList;

public class AllHydralisk_XCS_Manager {

    private final JNIBWAPI bwapi;
    private SimpleFileHandler simpleFileHandler = new SimpleFileHandler();
    private ArrayList<Managed_Hydralisk>  managedHyd = new ArrayList<>();
    private String[] actionSet = {"attackMoveToClosestEnemy","attackMoveToClosestFlyingEnemy","moveToHatchery", "supportFriend" , "protectHatchery", "burrow"};


    //GA type
    /*
        Scroll down to inner class
     */


    public AllHydralisk_XCS_Manager(JNIBWAPI bwapi) {
        this.bwapi = bwapi;

    }

    public Hydralisk createHydralisk(Unit hydralisk){
        Hydralisk hyd = new Hydralisk(hydralisk,this.bwapi,this);
        this.managedHyd.add(new Managed_Hydralisk(hydralisk.getID(),actionSet));
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




//
//    public void saveProgress(){
//        try {
//            fileThread.putClassifierSetToSave(xcs.getPopulationSet());
//            fileThread.putMatchStatsToSave(this.mStats);
//
//        } catch (InterruptedException e) {
//            System.err.println("XCS_Manager: SAVING INTERUPTED");
//        }
//    }
//
//    public void loadOldProgress(){
//        loadOldPopulationSet();
//        loadOldMatchStats();
//    }
//
//    private void loadOldPopulationSet(){
//        PopulationSet newPSet = fileThread.getSavedPopulationSet();
//
//        if (newPSet == null)
//            return;
//
//        else if(newPSet.getSet().size() > 0)
//            xcs.setPopulationSet(newPSet);
//    }
//
//    private void loadOldMatchStats(){
//        StarCraftBW_MatchStats newMStats = fileThread.getSavedMatchStats();
//
//        if (newMStats == null)
//            return;
//
//        else if(newMStats.getMatchStats().size() > 0)
//            this.mStats = newMStats;
//    }
//
//    private void saveOnlyOnce(){
//        saveProgress();
//        fileThread.stopMe();
//    }
//
//    public void cleanUp(){
//        saveOnlyOnce();
//    }

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

        private void setGA_Type(){
            PopulationSet pSet = hydralisk_xcs.getPopulationSet();
            pSet.setParent_select_method_type(XCS_Constants.GEN_ALGO_PARENT_ROULET);
            pSet.setCrossover_method_type(XCS_Constants.GEN_ALGO_CROSSOVER_RANDOM_ONE_POINT);
            pSet.setMutation_method_type(XCS_Constants.GEN_ALGO_MUTATION_RANDOM_ONE_POS);
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