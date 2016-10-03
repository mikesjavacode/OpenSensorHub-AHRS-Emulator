
package ahrsemulator;

import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Processes data received from the serial port - if the value corresponds to a
 * Euler angle data request, 
 * <p>
 * it creates simulated roll / pitch / heading data, assembles it into a data 
 * packet (the protocol depends on which AHRS model is selected), and sends it
 * to the serial port.
 * 
 * @author Mike Fouche
 */
public class AHRSOutput extends Thread
{
    // Serial port inputstream.
    private InputStream in;
    // Serial port outputstream.
    private OutputStream out;
    // Object to hold and arrange the data packet.
    private ByteBuffer byteBuffer;
    // Generated stabilized roll value.
    private float roll;
    // Generated stabilized pitch value.
    private float pitch;
    // Generated stabilized heading value.
    private float heading;
    // AHRS model number.
    private int ahrsNum;
    // This object allows thread-safe communications (synchronized methods) 
    // between this thread and other objects.
    private ThreadQueue threadQueue;
    // This object is used to exchanged data between different objects - it
    // is loaded into the constructors of any objects that are part of the data
    // exchange.
    private DataObject dataObject;
    
    /**
     * Constructor
     * 
     * @param sPort The serial port object.
     * @param tQ The thread object which allows thread-safe communication 
     * between this thread and several other objects.
     * @param ahrsNum This number represents the AHRS model selected.
     * @param dObj The data object which is used to exchange data between 
     * different objects.
     */
    public AHRSOutput(SerialPort sPort, ThreadQueue tQ, int ahrsNum, DataObject dObj)
    {
        this.ahrsNum = ahrsNum;
        this.threadQueue = tQ;
        this.dataObject = dObj;
        
        // Set up input and output stream objects.
        try
        {
            // Build the serial port inputstream.
            in = sPort.getInputStream();
            // Build the serial port outputstream.
            out = sPort.getOutputStream();
        } 
        catch ( IOException e )
        {
            dataObject.setSentData("Exception: "+e+", in AHRSOutput");
        }
    }

    @Override
    public void run()
    {     
        // Set the thread status to "active".
        threadQueue.setThreadStatus(true);
        
        switch (ahrsNum) 
        {
            case 1:
                // 3DM-GX2
                ahrs_Type_1();
                break;
            case 2:
                // 3DM-GX4-25
                ahrs_Type_2();
                break;
            case 3:
                // 3DM-GX3-35
                ahrs_Type_3();
                break;
            case 4:
                // 3DM-GX3-25-OEM
                ahrs_Type_4();
                break;
            default:
                break;
        }
        
        // Notify the thread that it must terminate.
        threadQueue.setThreadStatus(false);

    } // end of method run

    //--------------------------------------------------------------------------
    /**
     * 3DM-GX2 emulator.
     * <p>
     * Receives the data packet request - if it's for Euler angles then it 
     * generates stabilized roll, pitch, and heading values based on sinusoidal
     * waves - each with different amplitude, phase, and frequency - and outputs
     * the data to the serial port.  
     */
    public void ahrs_Type_1()
    {
        // Number of bytes currently in the serial port buffer.
        int len;
        // Integer time step.
        int i = 0;

        // Byte array which is loaded with data coming into the serial port.
        byte[] buffer = new byte[30]; 
        // Temporary - tack on the last 6 bytes.
        byte[] buffPad = new byte[6];
        // Fill with some data (that's meaningless) - will fix this later.
        buffPad[0] = 0x01;
        buffPad[1] = 0x02;
        buffPad[2] = 0x03;
        buffPad[3] = 0x04;
        buffPad[4] = 0x05;
        buffPad[5] = 0x06;

        // Long which is used to mask the first byte received.
        long buff2;
        // Byte array, which contains the Euler angles, that is sent to the 
        // serial port.
        byte[] buffSend = new byte[19];

        try
        {    
            // Check to see if data is in UART buffer
            len = in.available();
            
            // While there is data in the serial port.
            while( len > -1)
            {
                // Read the data in the serial port buffer.
                len = in.read(buffer);

                if( len == 1)
                {
                    // buff2 is a Long, mask the first 8 bits
                    buff2 = ( (int)buffer[0] ) & 0xFF;
                    // If the request for Euler angles then proceed.
                    if( buff2 == 206)
                    {
                        // Compute roll, pitch, and heading.
                        compEulerAngles(i,20.0f,10.0f,50.0f);
                        
                        // Allocate byte buffer size.
                        byteBuffer = ByteBuffer.allocate(19);
                        // Set up byte order.
                        byteBuffer.order(ByteOrder.BIG_ENDIAN);
                        // Echo Euler command word back at front of data pack
                        byte comWord = (byte)buff2;
                        // Load command word.
                        byteBuffer.put(comWord);
                        // Load roll, pitch, and heading.
                        byteBuffer.putFloat(roll);
                        byteBuffer.putFloat(pitch);
                        byteBuffer.putFloat(heading);
                        // Load trailing stuff which doesn't matter for now.
                        byteBuffer.put(buffPad);
                        // Load data packet into byte array.
                        buffSend = byteBuffer.array();
                        // Send the data packet to the serial port.
                        out.write(buffSend);
                        
                        // Increment the integer time counter.
                        i++;
                    }
                }
                
                // If the serial status has been set to false by object AHRSSelect
                // then exit the thread.
                if( !threadQueue.getSerialStatus() )
                {
                    len = -1;
                }    
            }

            // Notify the user that the thread is terminating.
            dataObject.setSentData("Exiting 3DM-GX2 serial communications thread ...");
        }
        catch(Exception e)
        {
            dataObject.setSentData("Exception in module 3DM-GX2: "+e);
        }    
    }

    //--------------------------------------------------------------------------
    /**
     * 3DM-GX4-25 emulator.
     * <p>
     * Receives the data packet request - if it's for Euler angles then it 
     * generates stabilized roll, pitch, and heading values based on sinusoidal
     * waves - each with different amplitude, phase, and frequency - and outputs
     * the data to the serial port.  
     */
    public void ahrs_Type_2()
    {
        // Number of bytes currently in the serial port buffer.
        int len;
        // Integer time step.
        // Integer time step.
        int i = 20;
        
        // Byte array which is loaded with data coming into the serial port.
        byte[] buffer = new byte[30]; 
        // Byte array, which contains the Euler angles, that is sent to the 
        // serial port.
        byte[] buffSend = new byte[20];
        // The processed data request bytes.
        byte[] synchBuff = new byte[2];
        // Long which is used to mask the incoming command bytes.
        long buff2;
        // Number of bytes loaded into the final "received byte array".
        int buffCtr = 0;
        // Lead data request byte.
        int leadIn = 0;
        // Lagging data request byte.
        int lagIn  = 0;
        
        try
        {
            // Check to see if data is in UART buffer.  Note that 
            len = in.available();
            
            // While there is data in the serial port.
            while( len > -1 )
            {
                // Read the data in the serial port buffer.
                len = in.read(buffer);
                
                for(int j = 0; j < len; j++)
                {
                    synchBuff[buffCtr] = buffer[0];
                    // Increment the counter for message ID bytes.
                    buffCtr++;
                    // If two bytes have been loaded then decode the values.
                    if( buffCtr == 2 )
                    {
                        // Decode the 1st byte.
                        buff2 = ( (int)synchBuff[0] ) & 0xFF;
                        leadIn = (int)buff2;
                        // Decode the 2nd byte.
                        buff2 = ( (int)synchBuff[1] ) & 0xFF;
                        lagIn = (int)buff2;
                        // Reset the counter.
                        buffCtr = 0;
                        
                        // If the message byte values are correct then assemble
                        // the Euler angle data packet.
                        if( leadIn == 117 && lagIn == 101 )  // 75 & 65
                        {
                            // Compute roll, pitch, and heading.
                            compEulerAngles(i,50.0f,50.0f,50.0f);

                            // Synch

                            // |0x75| - leading synch
                            // |0x65| - lagging synch

                            // Command and field length descriptors

                            // |0x80| - IMU dataset command
                            // |0x0E| - data set field length (number of bytes), 14 in this case

                            // NOTE that there can be multiple fields.  If there is only one
                            // field then this value will agree with the field length value below.

                            // Start of data packet

                            // |0x0E| - field length (14) for this field 
                            // |0x0C| - data type - Euler angles
                            // 4 bytes of roll
                            // 4 bytes of pitch
                            // 4 bytes of heading

                            // Checksum

                            // |0x60| MSB check sum
                            // |0x65| LSB check sum

                            byteBuffer = ByteBuffer.allocate(20);
                            // Set up byte order.
                            byteBuffer.order(ByteOrder.BIG_ENDIAN);

                            // Load synch bytes at front of data pack
                            byte leadSynch = 0x75;
                            byteBuffer.put(leadSynch);
                            byte lagSynch  = 0x65;
                            byteBuffer.put(lagSynch);

                            // Load IMU dataset command
                            byte commType = (byte)0x80;
                            byteBuffer.put(commType);
                            // data set field length
                            byte fieldLength = 0x0E;
                            byteBuffer.put(fieldLength);

                            // Start of data packet
                            // Load field length of this one field
                            byteBuffer.put(fieldLength);
                            // Load data type - Euler Angles
                            byte dataType = 0x0C;
                            byteBuffer.put(dataType);

                            // Load roll - 4 bytes
                            byteBuffer.putFloat(roll);
                            // Load pitch - 4 bytes
                            byteBuffer.putFloat(pitch);
                            // Load heading - 4 bytes
                            byteBuffer.putFloat(heading);

                            // Load trailing checksum bytes
                            byte msbChecksum = 0x60;
                            byteBuffer.put(msbChecksum);
                            byte lsbChecksum = 0x65;
                            byteBuffer.put(lsbChecksum);

                            // Load data packet into byte array.
                            buffSend = byteBuffer.array();

                            // Send data packet out to the serial port.
                            out.write(buffSend);

                            i++;
                        }
                    }    
                } 
                
                // If the serial status has been set to false by object AHRSSelect
                // then exit the thread.
                if( !threadQueue.getSerialStatus() )
                {
                    len = -1;
                }    
            } 
            
            // Notify the user that the thread is terminating.
            dataObject.setSentData("Exiting 3DM-GX4-25 serial communications thread ...");
        }
        catch(Exception e)
        {
            dataObject.setSentData("Exception in module 3DM-GX4-25: "+e);
            
        }    
    }

    //--------------------------------------------------------------------------
    /**
     * 3DM-GX3-35 emulator.  This module is not complete.
     * <p>
     * Receives the data packet request - if it's for Euler angles then it 
     * generates stabilized roll, pitch, and heading values based on sinusoidal
     * waves - each with different amplitude, phase, and frequency - and outputs
     * the data to the serial port.  
     */
    public void ahrs_Type_3()
    {
        int i = 0;
        
        // Byte array which is loaded with data coming into the serial port.
        byte[] buffer = new byte[30]; // nominal 1024;
        byte[] buffSend = new byte[19];
        // Long which is used to mask the incoming command bytes.
        long buff2 = 0; // temporary until code is implemented.

        if( buff2 == 206)
        {
            compEulerAngles(i,20.0f,10.0f,50.0f);

            try
            {

            }
            catch(Exception e)
            {
                System.out.println("Exception: "+e);
            }    
        }    
    }

    //--------------------------------------------------------------------------
    /**
     * 3DM-GX3-25-OEM emulator.  This module is not complete.
     * <p>
     * Receives the data packet request - if it's for Euler angles then it 
     * generates stabilized roll, pitch, and heading values based on sinusoidal
     * waves - each with different amplitude, phase, and frequency - and outputs
     * the data to the serial port.  
     */
    public void ahrs_Type_4()
    {
        int i = 0;
        
         // Byte array which is loaded with data coming into the serial port.
        byte[] buffer = new byte[30]; // nominal 1024;
        byte[] buffSend = new byte[19];
        // Long which is used to mask the incoming command bytes.
        long buff2 = 0; // temporary until code is implemented.

        if( buff2 == 206)
        {
            compEulerAngles(i,20.0f,10.0f,50.0f);

            try
            {

            }
            catch(Exception e)
            {
                System.out.println("Exception: "+e);
            }    
        }    
    }
    
    //--------------------------------------------------------------------------
    /**
     * Generates the roll, pitch, and heading values for all of the AHRS models.
     * 
     * @param i Integer time step.
     * @param rA Amplitude for Roll sinusoidal equation.
     * @param pA Amplitude for Pitch sinusoidal equation.
     * @param hA Amplitude for Heading sinusoidal equation.
     */
    public void compEulerAngles(int i, float rA, float pA, float hA)
    {
        // Create sinusoidal roll motion
        roll = rA * (float)Math.sin(((double)i)/50.0);
        // Convert to radians
        roll = roll * (float)Math.PI / 180.0f;
        // Create sinusoidal pitch motion
        pitch = pA * (float)Math.sin(((double)i)/40.0);
        // Convert to radians
        pitch = pitch * (float)Math.PI / 180.0f;
        // Create sinusoidal heading motion
        heading = hA * (float)Math.sin(((double)i)/30.0);
        // Convert to radians
        heading = heading * (float)Math.PI / 180.0f;
    }
    
} // end of class AHRSOutput
