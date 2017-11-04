package at.workcloud.janda;

import at.workcloud.janda.handle.Handle;
import at.workcloud.janda.handle.USBHandle;
import at.workcloud.janda.util.Utils;
import org.usb4java.LibUsb;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class Panda {
    
    public static int SAFETY_NOOUTPUT = 0;
    public static int SAFETY_HONDA = 1;
    public static int SAFETY_TOYOTA = 2;
    public static int SAFETY_TOYOTA_NOLIMITS = 0x1336;
    public static int SAFETY_ALLOUTPUT = 0x1337;
    public static int SAFETY_ELM327 = 0xE327;
    
    private final byte REQUEST_IN = LibUsb.ENDPOINT_IN | LibUsb.REQUEST_TYPE_VENDOR | LibUsb.RECIPIENT_DEVICE;
    private final byte REQUEST_OUT = LibUsb.ENDPOINT_OUT | LibUsb.REQUEST_TYPE_VENDOR | LibUsb.RECIPIENT_DEVICE;
    
    private Handle handle;
    
    public Panda( String handle ) throws Exception {
        if ( handle != null && handle.equals( "WIFI" ) ) {
            throw new UnsupportedOperationException( "Not implemented yet" );
        } else {
            this.handle = new USBHandle();
        }
    }
    
    public Panda() throws Exception {
        this( null );
    }
    
    public CANMessage[] canRecv() throws Exception {
        while ( true ) {
            try {
                byte[] data = this.handle.bulkRead( ( byte ) 0x1, 0x10 * 0xFF );
                System.out.println( "Can recv read " + data.length );
                return Utils.parseCanBuffer( data );
            } catch ( Exception a ) {
                System.out.println( "BAD CAN RECV, RETRYING!" );
            }
            
            Thread.sleep( 10 );
        }
    }
    
    public void setSafetyMode( int mode ) throws Exception {
        this.handle.controlWrite( REQUEST_OUT, 0xdc, ( short ) mode, ( short ) 0 );
        System.out.println( "Safety mode has been set to " + mode );
    }
    
    public void canClear( short bus ) throws Exception {
        this.handle.controlWrite( REQUEST_OUT, 0xf1, bus, ( short ) 0 );
    }
    
    public void close() {
        this.handle.close();
    }
    
}
