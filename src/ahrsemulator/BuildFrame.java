
package ahrsemulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * Builds the JFrame and corresponding JPanel (which has all of the other JPanels
 * and other objects loaded - including the GUI).
 * 
 * @author Mike Fouche
 */
public class BuildFrame 
{
    private final JFrame jf;

    /**
     * Constructor
     */
    public BuildFrame()
    {
        jf = new JFrame("SENSOR EMULATOR");
        int hSize = 1170;
        int vSize = 595;
        jf.setSize(hSize,vSize);
        jf.setResizable(false);
        jf.setLayout(new BorderLayout());
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
        BuildMainPanel bmp = new BuildMainPanel(hSize,vSize);
        jf.add(bmp);
        
        jf.setVisible(true);
    }        

} // end of class BuildFrame
