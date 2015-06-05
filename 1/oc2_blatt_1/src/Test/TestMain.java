package Test;

import General_XCS.Classifier;
import General_XCS.PopulationSet;

import StarCraftBW_XCS.StarCraftBW_FileThread;



public class TestMain {
	
//    final private StarCraftBW_XCS xcs = new StarCraftBW_XCS();
//
////    public static byte[] toByteArray(double value) {
////        byte[] bytes = new byte[8];
////        ByteBuffer.wrap(bytes).putDouble(value);
////        return bytes;
////    }
////
////    public static double toDouble(byte[] bytes) {
////        return ByteBuffer.wrap(bytes).getDouble();
////    }
//
//    @Deprecated
//    private void testXCS(){
//    	double distance = 291.4648418;
//        System.out.println("distance: " + distance);
//
//        xcs.getDetector().setDistance(distance);
//        xcs.run();
//    }
//
//
    public static void main(String[] args) {
        StarCraftBW_FileThread fileThread = new StarCraftBW_FileThread();
        PopulationSet pSet = fileThread.getSavedPopulationSet();

        for(Classifier c: pSet.getSet()){
            System.out.println("ID: " + c.getId() + " Con: " + c.getCondition());
        }



    }
}
