
package ahrsemulator;

import java.util.HashMap;

/**
 * Creates the HashMap which maps the model number (0 through 3) to
 * the AHRS model name (a String - e.g. "3DM-GX2").  The value 
 * corresponds to the AHRS panel that is clicked. 0 = 3DM-GX2, 
 * 1 = 3DM-GX4-25, etc.  
 *
 * @author Mike Fouche
 */
public class EmulatorMain 
{
    // This object is used to exchanged data between different objects - it
    // is loaded into the constructors of any objects that are part of the data
    // exchange.
    private final DataObject dataObject;
    // The String of AHRS model names.  This will be used in the HashMap to map
    // AHRS number to the name.
    private final String[] ahrsModelNames;
    // HashMap to map the AHRS number to the AHRS model name (map an Integer
    // number to a String name).
    private final HashMap<Integer,String> modelType;
    
    /**
     * Constructor 
     * 
     * @param dO DataObject for exchanging data between different objects.
     */
    public EmulatorMain(DataObject dO)
    {
        this.dataObject = dO;
        
        ahrsModelNames = new String[4];
        
        ahrsModelNames[0] = "3DM-GX2";
        ahrsModelNames[1] = "3DM-GX4-25";
        ahrsModelNames[2] = "3DM-GX3-35";
        ahrsModelNames[3] = "3DM-GX3-25-OEM";
        
        modelType = new HashMap<>();
        
        int[] modelNum = new int[4];
        // Load the HashMap.
        for(int i = 0; i < 4; i++)
        {
            modelNum[i] = i+1;
            modelType.put(modelNum[i], ahrsModelNames[i]);
        }    
    }
    
    /**
     * Notifies, the user, by sending a message to the DataObject, which AHRS
     * is being activated.
     * 
     * @param ahrsNum The AHRS number - e.g. 0 = 3DM-GX2, etc.
     */
    public void launchEmulator(int ahrsNum)
    {
        String initAHRS = "Initializing ".concat(modelType.get(ahrsNum)+"\n");
        dataObject.setSentData(initAHRS);
    }
    
} // end of class EmulatorMain
