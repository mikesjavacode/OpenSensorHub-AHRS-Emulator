
package ahrsemulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Builds the main JPanel - that gets loaded into the JFrame - and all components
 * that are associated with the main JPanel (sub JPanels, mouse listeners, etc.).
 * 
 * @author Mike Fouche
 */
public final class BuildMainPanel extends JPanel
{
    // Top panel - contains the GUI controls
    private JPanel subPanel1;
    // 2nd down panel - contains text windows
    private JPanel subPanel2;
    // 3rd down panel - contains the AHRS light indicators
    private JPanel subPanel3;
    // 4th down panel - contains the AHRS image panels
    private JPanel subPanel4;
    // Each panel is allocated to an AHRS - an AHRS image is mounted to this panel.
    private JPanel[] ahrsSubPanel;
    // Model names of each of the AHRS
    private final String[] ahrsNames;
    // JPanels that contain the AHRS indicator lights and are inserted
    // into the 3rd (from top) JPanel.
    private final JPanel[] lightPanel;
    private AHRSSelect as;
    
    // Each panel contains a JScrollPanel (which contains a JTextArea panel) and
    // is inserted in the 2nd panel down.
    private final JPanel[] textPanel;
    private final JTextArea[] textArea;
    private final JScrollPane[] windArea;
    // This object is used to exchanged data between different objects - it
    // is loaded into the constructors of any objects that are part of the data
    // exchange.
    private final DataObject dataObject;
    // Horizontal dimension of the panel.
    private final int hSize;
    // Vertical dimension of the panel.
    private final int vSize;
    
    /**
     *
     * @param hS
     * @param vS
     */
    public BuildMainPanel(int hS, int vS)
    {
        this.hSize = hS;
        this.vSize = vS;
        
        ahrsNames = new String[4];
        ahrsNames[0] = "3dmGx2.jpg";
        ahrsNames[1] = "3dmGx4-25.jpg";
        ahrsNames[2] = "3dmGx3-35.jpg";
        ahrsNames[3] = "3dmGx3-25.jpg";
        
        lightPanel = new JPanel[4];
        
        textPanel = new JPanel[2];
        textArea  = new JTextArea[2];
        windArea  = new JScrollPane[2];
        
        dataObject = new DataObject();
        
        getMainPanel();
    }

    /**
     *
     */
    public void getMainPanel()
    {
        ahrsSubPanel = new JPanel[4];
        
        // Build main JPanel and 4 sub JPanels:
        // 1 - GUI controls
        // 2 - text window
        // 3 - light displays
        // 4 - AHRS image panels
        // 
        // JPanel build constructor inputs:
        // JPanel Dimensions
        int[] dim = new int[2];
        // JPanel color
        int color;
        // JPanel FlowLayout parameters
        int[] fLayout = new int[3];
        // JPanel bevel border value
        int beveled;
        
        //----------------------------------------------------------------------
        // Build main JPanel
        dim[0] = hSize;
        dim[1] = vSize;
        color = 200;
        fLayout[0] = 1;   // centered 
        fLayout[1] = 10;  // horizontal spacing
        fLayout[2] = 5;   // vertical spacing
        this.setPreferredSize(new Dimension(dim[0],dim[1]));
        this.setLayout(new FlowLayout(fLayout[0],fLayout[1],fLayout[2]));
        this.setBackground(new Color(color,color,color));

        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        // 1st sub JPanel which contains the GUI controls
        dim[0] = 1150;
        dim[1] = 40;
        color = 160;
        fLayout[0] = 0;   // left-aligned 
        fLayout[1] = 10;  // horizontal spacing
        fLayout[2] = 5;  // vertical spacing
        beveled = 1;      // raised
        subPanel1 = new BuildJPanel(dim,fLayout,color,beveled);
        this.add(subPanel1);
        
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        // 2nd (from top) sub JPanel which contains the display windows
        dim[0] = 1150;
        dim[1] = 280;
        fLayout[0] = 1;   // centered 
        fLayout[1] = 40;  // horizontal spacing
        fLayout[2] = 10;  // vertical spacing
        subPanel2 = new BuildJPanel(dim,fLayout,color,beveled);

        //----------------------------------------------------------------------
        // Make two labels and load into subPanel
        dim[0] = 1100;
        dim[1] = 20;
        fLayout[0] = 1;   // centered 
        fLayout[1] = 480; // horizontal spacing
        fLayout[2] = 5;   // vertical spacing
        beveled = 0;      // no bevel effect
        JPanel labelPanel = new BuildJPanel(dim,fLayout,color,beveled);
        
        JLabel sendText = new JLabel("Data Sent");
        JLabel receiveText = new JLabel("Data Received");
        labelPanel.add(receiveText);
        labelPanel.add(sendText);
        
        subPanel2.add(labelPanel);
        
        //----------------------------------------------------------------------
        // Build 2 sub JPanels to act as text windows and load into subPanel3
        dim[0] = 530;
        dim[1] = 220;
        beveled = -1;     // sunken panel
        for(int i = 0; i < 2; i++)
        {   
            // Build the text display JPanel.
            textPanel[i] = new BuildJPanel(dim,color,beveled);
            // Build the JTextArea object which will contain displayed text.
            textArea[i] = new JTextArea();
            // Build the JScrollPane object, insert the JTextArea object, 
            // and set the dimensions.
            windArea[i] = new JScrollPane(textArea[i]);
            windArea[i].setPreferredSize(new Dimension(dim[0],dim[1]));
            // Add the JScrollPane object to the text display JPanel.
            textPanel[i].add(windArea[i]);
            // Add the text display JPanel to the 2nd (from top) JPanel.
            subPanel2.add(textPanel[i]);
        }

        // Add the 2nd (from top) JPanel to the main JPanel.
        this.add(subPanel2);

        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        // Build the 3rd (from top) sub JPanel
        dim[0] = 1150;
        dim[1] = 50;
        fLayout[0] = 1;   // centered 
        fLayout[1] = 250; // horizontal spacing
        fLayout[2] = 7;   // vertical spacing
        beveled = 1;      // raised
        subPanel3 = new BuildJPanel(dim,fLayout,color,beveled);

        //----------------------------------------------------------------------
        // Build 4 sub JPanels to act as lights and load into subPanel3
        dim[0] = 30;
        dim[1] = 30;
        fLayout[0] = 1;   // centered 
        fLayout[1] = 5;   // horizontal spacing
        fLayout[2] = 5;   // vertical spacing
        beveled = -1;     // sunken
        for(int i = 0; i < 4; i++)
        { 
            // Build the AHRS indicator light panel.
            lightPanel[i] = new BuildJPanel(dim,fLayout,color,beveled);
            lightPanel[i].setBackground(Color.red);
            // Add the AHRS indicator light panel to the sub JPanel.
            subPanel3.add(lightPanel[i]);
        }
        
        // Add the 3rd (from top) JPanel to the main JPanel.
        this.add(subPanel3);
        
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        // 4th sub JPanel
        dim[0] = 1150;
        dim[1] = 170;
        fLayout[0] = 1;   // centered 
        fLayout[1] = 130; // horizontal spacing
        fLayout[2] = 8;   // vertical spacing
        beveled = 1;      // raised
        subPanel4 = new BuildJPanel(
                dim,fLayout,color,beveled);

        //----------------------------------------------------------------------
        // Build 4 AHRS sub JPanels and load into subPanel4
        dim[0] = 150;
        dim[1] = 150;
        for(int i = 0; i < 4; i++)
        {   
            // Build the AHRS JPanel (loads the AHRS image as well).
            ahrsSubPanel[i] = new BuildImageJPanel(dim,ahrsNames[i]);
            // Add the AHRS JPanel to the sub JPanel.
            subPanel4.add(ahrsSubPanel[i]);
        }
        
        // Add the 4th (from top) JPanel to the main JPanel.
        this.add(subPanel4);

        // Load the two JTextArea objects (which will contain the display 
        // text) to dataObject.
        dataObject.loadReceivedData(textArea[0]);
        dataObject.loadSentData(textArea[1]);
        // Load the GUI controls.
        GUIControl gc = new GUIControl(dataObject,subPanel1,lightPanel);
        // Load the mouse listeners for each of the AHRS panels.
        as = new AHRSSelect(ahrsSubPanel,lightPanel,dataObject);
    }
    
} // end of class BuildMainPanel
