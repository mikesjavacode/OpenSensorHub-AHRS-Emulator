
package ahrsemulator;

import gnu.io.SerialPort;

import javax.swing.JTextArea;

/**
 * This class provides a means to exchange data between different classes.  It
 * is loaded into the constructors of those particular classes.
 *
 * @author Mike Fouche
 */
public class DataObject 
{
    private JTextArea sentData;
    
    private JTextArea receivedData;
    // Serial port object.
    private SerialPort sP;
    private boolean serialConnect;

    /**
     * Constructor
     */
    public DataObject()
    {
        serialConnect = false;
    }
    
    /**
     * Loads the JTextArea object that is used in the right display window.
     * 
     * @param sD JText area object for the right display window.
     */
    public void loadSentData(JTextArea sD)
    {
        this.sentData = sD;
    }
    
    /**
     * Takes the text String object and displays it in the right display window.
     * 
     * @param text
     */
    public void setSentData(String text)
    {
        this.sentData.append(text+"\n");
        this.sentData.setCaretPosition(sentData.getDocument().getLength());
    } 
    
    /**
     * Loads the JTextArea object that is used in the left display window.
     *
     * @param rD JText area object for the left display window.
     */
    public void loadReceivedData(JTextArea rD)
    {
        this.receivedData = rD;
    }        

    /**
     * Takes the text String object and displays it in the left display window.
     * 
     * @param text Text to be written in the left display window.
     */
    public void setReceivedData(String text)
    {
        this.receivedData.append(text+"\n");
        this.receivedData.setCaretPosition(receivedData.getDocument().getLength());
    }
    
    /**
     * Sets the status of the serial port connection.
     * 
     * @param sC True if a serial connection has been made.
     */
    public void setSerialConnect(boolean sC)
    {
        this.serialConnect = sC;
    }        
    
    /**
     * Retrieves the status of the serial port object connection.
     * 
     * @return The status of the serial connection (true or false).
     */
    public boolean getSerialConnect()
    {
        return serialConnect;
    }

    /**
     * Loads the serial port object.
     * 
     * @param sP The serial port object.
     */
    public void loadSerialPortObject(SerialPort sP)
    {
        this.sP = sP;
    } 
    
    /**
     * Retrieves the serial port object.
     * 
     * @return The serial port object.
     */
    public SerialPort getSerialPortObject()
    {
        return sP;
    }
    
} // end of class DataObject
