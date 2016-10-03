
package ahrsemulator;

/**
 * This is the thread-safe communications link between AHRSSelect and AHRSOutput.
 * 
 * @author Mike Fouche
 */
public class ThreadQueue 
{
    private boolean serialStatus;
    private boolean threadStatus;

    /**
     * Constructor
     */
    public ThreadQueue()
    {
        serialStatus = false;
        threadStatus = false;
    }

    //--------------------------------------------------------------------------
    /**
     * Set the serial status.
     * 
     * @param sStatus Serial status.
     */
    public synchronized void setSerialStatus(boolean sStatus)
    {
        serialStatus = sStatus;
    }
    
    /**
     * Get the serial status.
     *
     * @return Serial status.
     */
    public synchronized boolean getSerialStatus()
    {
        return serialStatus;
    }

    //--------------------------------------------------------------------------
    /**
     * Sets the status of the thread - this is used in AHRSSelect to wait until
     * a thread is terminated before beginning another one.
     * 
     * @param tStatus Thread status.
     */
    public synchronized void setThreadStatus(boolean tStatus)
    {
        threadStatus = tStatus;
    }
    
    //--------------------------------------------------------------------------
    /**
     * Retrieves the status of the thread.
     * 
     * @return The thread status.
     */
    public synchronized boolean getThreadStatus()
    {
        return threadStatus;
    }

} // end of class ThreadQueue
