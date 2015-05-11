package RollesKleineEcke;

import jnibwapi.types.UnitType;
import jnibwapi.types.WeaponType;
import jnibwapi.JNIBWAPI;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;



/**
 * Created by Rolle on 11.05.2015.
 */
public class TestMain {

    public static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
    
    
    public static void main(String[] args) {
        double blub = 161;
        int inti = (int)blub;

        System.out.println(Long.toBinaryString(Double.doubleToRawLongBits(blub)));
        System.out.println(Integer.toBinaryString(inti));

        System.out.println("START");
        int testo = 150;
        for (int i=0; i<= 20; i++){
            int cur = testo + i;
            System.out.println(cur + " : " + Integer.toBinaryString(cur));
        }

    }
}
