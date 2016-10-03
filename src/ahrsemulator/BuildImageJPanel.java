
package ahrsemulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * Loads the AHRS image into a JPanel for display.
 * 
 * @author Mike Fouche
 */
public final class BuildImageJPanel extends JPanel
{
    // Dimension of the JPanel.
    private final int[] dim;
    // Name of the image (duh - of course).
    private final String imageName;
    // Holds the image loaded from the image file.
    private BufferedImage image;
    
    /**
     * Constructor
     * 
     * @param dim Dimension (size) of the JPanel.
     * @param imageName Name of the image.
     */
    public BuildImageJPanel(int[] dim, String imageName)
    {
        this.dim = dim;
        this.imageName = imageName;
        
        getPanel();
    }
    
    /**
     * Return the assembled JPanel.
     */
    public void getPanel()
    {
        // Set the JPanel size (width and height).
        this.setPreferredSize(new Dimension(dim[0],dim[1]));
        // Set the JPanel layout.
        this.setLayout(new BorderLayout());
        // Set JPanel "bevel" appearance to be recessed.
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));            
        
        try
        {
            // Read the image data from the file.
            File ahrsFile = new File(System.getProperty("user.dir").concat("\\sensorimages\\"+imageName));
            image = ImageIO.read(ahrsFile); 
        }
        catch(Exception e)
        {
            System.out.println("Read AHRS image exception: "+e);
        }
        
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        // Draw the image which will be loaded into the JPanel.
        super.paintComponent(g);
        if( image != null )
        {    
            int x = this.getWidth();
            int y = this.getHeight();
            g.drawImage(image,0,0,x,y,null);
        }    
    }        
    
} // end of BuildImageJPanel
