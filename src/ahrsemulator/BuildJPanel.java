
package ahrsemulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * This is for the construction of a JPanel.  There are two constructors: 
 * 1) for JPanels with a FlowLayout, and 2) for JPanels with no FlowLayout, just
 * a BorderLayout which is mainly for mounting images, setting indicator lights,
 * etc.
 * <p>
 * This keeps the code clean - when a JPanel has to be assembled, it is done in 
 * this class (the assembly parameters are passed through the constructor.
 * 
 * @author Mike Fouche
 */
public final class BuildJPanel extends JPanel
{
    // Dimensions of the JPanel
    private final int[] dim;
    // Layout parameters
    private final int[] fLayout;
    // JPanel color value
    private final int color;
    // -1 = lowered bevel border, appearance of sunken panel 
    //  0 = no bevel, 
    //  1 = raised bevel border, appearance of raised panel
    private final int beveled;

    /**
     * Constructor #1
     * 
     * @param dim Dimension (size) of the JPanel - height and width.
     * @param fLayout FlowLayout parameters - alignment, vertical spacing, 
     * // horizontal spacing.
     * @param color JPanel color.
     * @param beveled Bevel type - recessed, raised, or none at all.
     */
    public BuildJPanel(int[] dim, int[] fLayout, int color, int beveled)
    {
        this.dim = dim;
        this.fLayout = fLayout;
        this.color = color;
        this.beveled = beveled;
        
        getPanel();
    }

    /**
     * Constructor #2
     * 
     * @param dim Dimension (size) of the JPanel - height and width.
     * @param color JPanel color.
     * @param beveled Bevel type - recessed, raised, or none at all.
     */
    public BuildJPanel(int[] dim, int color, int beveled)
    {
        this.dim = dim;
        this.color = color;
        this.beveled = beveled;
        this.fLayout = null;
        
        getPanel();
    }

    /**
     * Sets and builds the JPanel.
     */
    public void getPanel()
    {
        // Set size of the JPanel.
        this.setPreferredSize(new Dimension(dim[0],dim[1]));
        // Set the color of the JPanel.
        this.setBackground(new Color(color,color,color));
        
        // If there is a FlowLayout object, then set the parameters.  Otherwise
        // set a BorderLayout.
        if ( fLayout != null )
        {    
            this.setLayout(new FlowLayout(fLayout[0],fLayout[1],fLayout[2]));
        }
        else
        {
            this.setLayout(new BorderLayout());
        }
        
        // Set a raised bevel border.
        if( beveled == 1 )
        {
            this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));            
        }
        // Set a lowered bevel border.
        else if ( beveled == -1 )
        {
            this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));            
        }
    }
    
} // end of class BuildJPanel
