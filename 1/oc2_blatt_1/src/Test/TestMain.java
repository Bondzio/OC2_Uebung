package Test;

import jnibwapi.types.UnitType;
import jnibwapi.types.WeaponType;
import jnibwapi.JNIBWAPI;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import StarCraftBW_XCS.StarCraftBW_XCS;



public class TestMain {
	
    final private StarCraftBW_XCS xcs = new StarCraftBW_XCS();

//    public static byte[] toByteArray(double value) {
//        byte[] bytes = new byte[8];
//        ByteBuffer.wrap(bytes).putDouble(value);
//        return bytes;
//    }
//
//    public static double toDouble(byte[] bytes) {
//        return ByteBuffer.wrap(bytes).getDouble();
//    }
    
    private void testXCS(){
    	double distance = 291.4648418;
        System.out.println("distance: " + distance);

        xcs.getDetector().setDistance(distance);
        String action = xcs.run();
    }
    
    
    public static void main(String[] args) {
    	TestMain test = new TestMain();
    	test.testXCS();
    	
    	
//        double blub = 161;
//        int inti = (int)blub;
//
//        System.out.println(Long.toBinaryString(Double.doubleToRawLongBits(blub)));
//        System.out.println(Integer.toBinaryString(inti));
//
//        System.out.println("START");
//        int testo = 150;
//        for (int i=0; i<= 20; i++){
//            int cur = testo + i;
//            System.out.println(cur + " : " + Integer.toBinaryString(cur));
//        }

    }
}
