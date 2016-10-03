
package ahrsemulator;

/**
 * This application emulates the response of four Microstrain AHRS models:
 * 1. 3DM-GX2
 * 2. 3DM-GX3-25-OEM
 * 3. 3DM-GX3-35
 * 4. 3DM-GX4-25
 * 
 * At the current time, it will only emulate the Euler angle data packet which
 * consists of stabilized roll, pitch, and heading.
 * 
 * @author Mike Fouche
 */
public class AHRSEmulator 
{
    /**
     *
     * @param args None.
     */
    public static void main(String[] args) 
    {
        BuildFrame bf = new BuildFrame();
    }
    
} // end of class AHRSEmulator
