
package ahrsemulator;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * Loads the mouse listeners into each AHRS panel.  When the user clicks a
 * particular AHRS panel then that AHRS model will be selected for emulation.
 * 
 * @author Mike Fouche
 */
public final class AHRSSelect 
{
    private final int[] clicked;
    // JPanels that contain the AHRs images and are inserted into the 4th 
    // (from top) JPanel.
    private final JPanel[] ahrsPanel;
    // JPanels that contain the AHRS indicator lights and are inserted
    // into the 3rd (from top) JPanel.
    private final JPanel[] lightPanel;
    // This object is used to exchanged data between different objects - it
    // is loaded into the constructors of any objects that are part of the data
    // exchange.
    private final DataObject dataObject;
    // Creates the HashMap which maps the model number (0 through 3) to
    // the AHRS model name (a String - e.g. "3DM-GX2").  The value 
    // corresponds to the AHRS panel that is clicked. 0 = 3DM-GX2, 
    // 1 = 3DM-GX4-25, etc.  
    private final EmulatorMain eM;
    // This object allows thread-safe communications (synchronized methods) 
    // between this thread and other objects.
    private final ThreadQueue tQue;
    
    /**
     *
     * @param aP Bottom JPanel - the user clicks on one of the AHRS panels,
     * contained in this JPanel, to select the AHRS to emulate.
     * @param lP JPanel above the AHRS panels - contains the indicator lights 
     * (show which AHRS is active).
     * @param dO Data object which is used to exchange data between different
     * objects.
     */
    public AHRSSelect(JPanel[] aP, JPanel[] lP, DataObject dO)
    {
        this.ahrsPanel  = aP;
        this.lightPanel = lP;
        this.dataObject = dO;
        
        eM = new EmulatorMain(dataObject);
        tQue = new ThreadQueue();

        // The integer array holds the value of which AHRS has been selected.
        // e.g. {0, 1, 0, 0} means that the 2nd AHRS has been selected.
        clicked = new int[4];
        
        // Inner class contained in this class which contains the mouse listener
        // - for determining which AHRS model was selected (which AHRS panel
        // was "clicked").
        PanelSelect ps;
        
        // Load each AHRS panel with the PanelSelect mouse listener object.
        for(int i = 0; i < 4; i++)
        {    
            // Create a new instance of the PanelSelect inner class which
            // contains the mouse listener instructions.
            ps = new PanelSelect(i);
            // Add this inner classs to the AHRS panel listener.
            ahrsPanel[i].addMouseListener(ps);
        }    
    }
    
    //--------------------------------------------------------------------------
    private class PanelSelect implements MouseListener
    {
        private final int ahrsNum;
        
        // Constructor
        public PanelSelect(int aNum)
        {
            // The AHRS model number (0 = 3DM-GX2, etc.) that corresponds to 
            // this listener.
            this.ahrsNum = aNum;
        }        
        
        @Override
        public void mouseClicked(MouseEvent e) 
        {
            // If serial port is connected then proceed forward.
            if( dataObject.getSerialConnect() )
            {   
                // If another thread is operating then tell it to stop.
                tQue.setSerialStatus(false);
                // Wait until the other thread has stopped.  "threadStatus"
                // will be true until the thread has terminated.
                while( tQue.getThreadStatus() )
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch(Exception ex)
                    {
                        System.out.println("Exception: "+ex);
                    }    
                }    
                
                // If the AHRS model previous state is "unclicked" (the indicator
                // light is off) - the user is switching from another AHRS model 
                // to this one.
                if( clicked[ahrsNum] == 0 )
                {    
                    // Turn on the selected AHRS indicator light.
                    lightPanel[ahrsNum].setBackground(Color.green);
                    // Set to "on" the model number selected in the array.
                    clicked[ahrsNum] = 1;
                    // Set the String name and load to DataObject with a message
                    // the lets the user know that it's being activated.
                    eM.launchEmulator(ahrsNum+1);
                    // Set the thread status to "active".
                    tQue.setSerialStatus(true);
                    // Launch the AHRS thread.
                    new AHRSOutput(dataObject.getSerialPortObject(),
                                            tQue,ahrsNum+1,dataObject).start();
                    // Set the other AHRS indicator lights to red since they
                    // are not active.
                    for(int i = 0; i < 4; i++)
                    {
                        if( i != ahrsNum )
                        {
                            lightPanel[i].setBackground(Color.red);
                            clicked[i] = 0;
                        }    
                    }    
                }
                else
                // The AHRS model previous state is "clicked" (active) which 
                // means that another one is being selected so this one's indicator
                // light is changed to red.
                {
                    lightPanel[ahrsNum].setBackground(Color.red);
                    clicked[ahrsNum] = 0;
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {

        }

        @Override
        public void mouseReleased(MouseEvent e) 
        {
       
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {

        }

        @Override
        public void mouseExited(MouseEvent e) 
        {

        }
    }        
        
} // end of class AHRSSelect
