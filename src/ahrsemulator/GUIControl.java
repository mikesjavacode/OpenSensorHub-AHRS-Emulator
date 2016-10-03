
package ahrsemulator;

import gnu.io.SerialPort;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Loads the GUI objects and implements the listeners.
 * 
 * @author Mike Fouche
 */
public class GUIControl 
{
    // Data object - to exchange data between other objects.
    private final DataObject dataObject;
    // Top JPanel which contains the GUI controls.
    private final JPanel topPanel;
    // JPanel which is used as serial port connection indicator light.
    private final JPanel connectLight;
    // JPanels which act as the AHRS status indicator lights.
    private final JPanel[] lightPanel;
    // Class which builds the serial port object.
    private final ConnectSerialPort connSerialPort;
    // Serial port object.
    private SerialPort sP;
    // Comm port number - e.g. COM1, COM2, etc.
    private String commNum;
    // "Click" number.  0 = going from off to on, 1 = going from on to off.
    private int ctr;
    // True = serial port is connected, False = serial port is NOT connected.
    private boolean serialOn;
    
    /**
     * Constructor 
     * 
     * @param dObject Data object for exchanging data between other objects.
     * @param tPanel Top JPane which holds the GUI controls.
     * @param lPanel The JPanel which holds the AHRS indicator lights.
     */
    public GUIControl(DataObject dObject, JPanel tPanel, JPanel[] lPanel)
    {
        this.dataObject = dObject;
        this.topPanel = tPanel;
        this.lightPanel = lPanel;

        int[] dim = new int[2];
        
        // Initialize the serial port object - does NOT perform the connection.
        connSerialPort = new ConnectSerialPort(dataObject);
        // Initialize "click" value.
        ctr = 0;
        // Set serial port status boolean - default is always "off".
        serialOn = false;
        // The default value is COM3 if the user doesn't select a Comm port number.
        commNum = "COM3";
        // Create a "Connect" button.
        JButton serialConnect = new JButton("Connect");
        // Create the inner class with listener.
        SerialConnect sc = new SerialConnect();
        // Add the listener to the "Connect" button.  If the "Connect" button
        // is depressed, then the serial port will either be connected or 
        // disconnected.
        serialConnect.addActionListener(sc);
        // Add the "Connect" button to the top JPanel.
        topPanel.add(serialConnect);
        
        // Set the color value.
        int color = 192;
        // Set the value so that the JPanel will be raised.
        int beveled = 1;
        // Set the dimensions for the serial port light indicator JPanel.
        dim[0] = 20;
        dim[1] = 20;
        // Build the serial port status indicator light  JPanel.
        connectLight = new BuildJPanel(dim,color,beveled);
        // Set the color to red.
        connectLight.setBackground(Color.red);
        // Add the serial port status indicator light JPanel to the top JPanel.
        topPanel.add(connectLight);
        
        // Build a JMenuBar to add selectable options.
        JMenuBar bar = new JMenuBar();
        
        // Build a JMenu to allow COM port selection.
        JMenu commPort = new JMenu("Select COM Port");
        // Add it to the JMenuBar.
        bar.add(commPort);
        // Add the JMenuBar to the top JPanel.
        topPanel.add(bar);
        // Create the inner class which acts as the listener for Comm port selection.
        CommSelect cS = new CommSelect();
        // Create the JMenuItem for COM1
        JMenuItem com1 = new JMenuItem("COM1");
        // Add the listener.
        com1.addActionListener(cS);
        // Add it to the Comm port JMenu
        commPort.add(com1);
        
        JMenuItem com2 = new JMenuItem("COM2");
        com2.addActionListener(cS);
        commPort.add(com2);
        
        JMenuItem com3 = new JMenuItem("COM3");
        com3.addActionListener(cS);
        commPort.add(com3);
        
        JMenuItem com4 = new JMenuItem("COM4");
        com4.addActionListener(cS);
        commPort.add(com4);
        
        JMenuItem com5 = new JMenuItem("COM5");
        com5.addActionListener(cS);
        commPort.add(com5);
    }
    
    //--------------------------------------------------------------------------
    // Inner class for serial connection button action
    private class SerialConnect implements ActionListener
    {   
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            // If "first click" then turn on serial port.
            if( ctr == 0 )
            {    
                // Clear out serial port object from previous connections.
                sP = null;
                // Open serial port (attempt a serial port connection with the
                // selected Comm number).
                sP = connSerialPort.connect(commNum);
                // Pass the serial port object to dataObject.
                dataObject.loadSerialPortObject(sP);
                // If the serial port connection attempt was successful 
                // (object exists - which means it's connected), then turn on 
                // the serial port indicator light.
                if( sP != null )
                {
                    // Serial port indicator light.
                    connectLight.setBackground(Color.green);
                    // Update serial port status boolean.
                    serialOn = true;
                    // Update the "click" number.
                    ctr = 1;
                    // Update display window with serial port status.
                    dataObject.setSentData("Serial port opened ...");
                }
            }
            else
            // If "second" click then turn off serial port.
            {
                // If the serial port objects exists (which means it's connected),
                // then set the serial port indicator light to red, and close
                // the serial port.
                if( sP != null )
                {    
                    // Serial port indicator light.
                    connectLight.setBackground(Color.red);
                    // Update serial port status boolean.
                    serialOn = false;
                    // Update display window with serial port status.
                    dataObject.setSentData("Closed serial port ...");
                    // Close the serial port object.
                    sP.close();
                    // Make sure that all four of the AHRS status indicator 
                    // lights are turned off (set to red).
                    for(int i = 0; i < 4; i++)
                    {
                        lightPanel[i].setBackground(Color.red);
                    }  
                    
                    // Reset the "click" number.
                    ctr = 0;
                }
            }
            
            // Update the serial port status in dataObject.
            dataObject.setSerialConnect(serialOn);
        }
        
    } // end of inner class SerialConnect

    //--------------------------------------------------------------------------
    // Inner class for comm port selection options
    private class CommSelect implements ActionListener
    {   
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            String eAction = ae.getActionCommand();
            if( null != eAction )
            switch (eAction) 
            {
                // Whichever selection is selected then send the String to 
                // DataObject.  This will be send to the display window to 
                // notify the user.
                case "COM1":
                    commNum = "COM1";
                    dataObject.setSentData("COM1 selected");
                    break;
                case "COM2":
                    commNum = "COM2";
                    dataObject.setSentData("COM2 selected");
                    break;
                case "COM3":
                    commNum = "COM3";
                    dataObject.setSentData("COM3 selected");
                    break;
                case "COM4":
                    commNum = "COM4";
                    dataObject.setSentData("COM4 selected");
                    break;
                case "COM5":
                    commNum = "COM5";
                    dataObject.setSentData("COM5 selected");
                    break;
            }        
        }
        
    } // end of inner class CommSelect

} // end of class GUIControl
