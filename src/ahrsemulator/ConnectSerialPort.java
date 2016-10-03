
package ahrsemulator;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Initializes the serial communications and launches two threads – 
 * AttCom and AttRecv.
 *
 * @author Mike Fouche
 */
public class ConnectSerialPort 
{
    private final DataObject dataObject;
    private SerialPort sP;
    
    /**
     *
     * @param dO This is used to exchanged data between different objects - it
     * is loaded into the constructors of any objects that are part of the data
     * exchange.
     */
    public ConnectSerialPort(DataObject dO)
    {
        this.dataObject = dO;
    }
    
    /**
    * This is the main serial communications method, the serial port name (e.g. 
    * COM3) is pass through the argument list.
    * <p>
    * 1.  checks to see if the serial port is available (the string value is 
    *     passed in the method argument.
    * <p>
    * 2.  creates a CommPort object and verifies that the serial port is a valid
    *     RS-232 port (not an RS-485 port, etc.)
    * <p>
    * 3.  verifies that the commPort object is the same type as the SerialPort object
    * <p>
    * 4.  creates an instance of SerialPort and sets the communications parameters
     *
     * @param portName The String value of the serial port number (e.g. "COM3").
     * 
     * @return The serial port object.
     */
    public SerialPort connect ( String portName)  
    {
        
        try
        {
            sP = null;
            
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            // If the serial port is not locked by another process then proceed
            // on building the serial port object.
            if ( portIdentifier.isCurrentlyOwned() )
            {
                System.out.println("Error: Port is currently in use");
            }
            else
            {
                CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
                
                // Verify that commPort is of the same type as SerialPort
                if ( commPort instanceof SerialPort ) 
                {
                    SerialPort serialPort = (SerialPort)commPort;
                    
                    // Set the serial port connection parameters.
                    serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,
                                     SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                    
                    sP = serialPort;
                }
                else
                {
                    dataObject.setSentData("Error: Only serial ports are handled by this code.");
                }
            } 
        }
        catch(PortInUseException | UnsupportedCommOperationException | NoSuchPortException e)
        {
            dataObject.setSentData("Serial port exception: "+e);
            return sP;
        }   
        
        return sP;
    
    } // end of method connect
    
} // end of class ConnectSerialPort
