package at.workcloud.janda.handle;

import org.apache.commons.lang3.ArrayUtils;
import org.usb4java.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class USBHandle implements Handle {
    
    private Device device;
    
    public USBHandle() throws Exception {
        this.initialize();
    }
    

    
    @Override
    public void controlWrite(byte requestType, int request, short value, short index, long timeout) throws Exception {
        try ( ManagedDeviceHandle handle = new ManagedDeviceHandle( this.device ) ) {
            requestType = ( byte ) ( ( requestType & ~ LibUsb.ENDPOINT_DIR_MASK ) );
            LibUsb.controlTransfer( handle.getHandle(), requestType, ( byte ) 0xdc, value, ( short ) 0, ByteBuffer.allocateDirect( 0 ), timeout );
        }
    }
    
    @Override
    public byte[] bulkRead( byte interface_, int length, long timeout ) throws Exception {
        try ( ManagedDeviceHandle handle = new ManagedDeviceHandle( this.device ) ) {
            byte endpoint = ( byte ) ( ( interface_ & ~ LibUsb.ENDPOINT_DIR_MASK ) | LibUsb.ENDPOINT_IN );

            ByteBuffer buffer = BufferUtils.allocateByteBuffer(length).order(ByteOrder.LITTLE_ENDIAN);
            IntBuffer transferred = BufferUtils.allocateIntBuffer();
            System.out.println("Sending " + length + " bytes");
            int res = LibUsb.bulkTransfer( handle.getHandle(), endpoint, buffer, transferred, timeout );
            System.out.println("res " + res);
            if(res != LibUsb.SUCCESS) {
                throw new Exception("Error code: " + res);
            }
            System.out.println(transferred.get() + " bytes read from device");
           //
           // System.out.println(ArrayUtils.toString(data));
            return new byte[length];
        }
    }
    
    @Override
    public void bulkWrite( byte endpoint, ByteBuffer data, long timeout ) throws Exception {
        try ( ManagedDeviceHandle handle = new ManagedDeviceHandle( this.device ) ) {
            endpoint = ( byte ) ( ( endpoint & ~ LibUsb.ENDPOINT_DIR_MASK ) | LibUsb.ENDPOINT_OUT );
            LibUsb.bulkTransfer( handle.getHandle(), endpoint, data, null, timeout );
        }
    }
    
    @Override
    public void close() {
        LibUsb.exit( null );
    }
    
    private void initialize() throws Exception {
        int initResult = LibUsb.init( null );
        if ( initResult != LibUsb.SUCCESS ) throw new LibUsbException( "Unable to initialize libusb.", initResult );
        
        this.device = this.findPanda();
        
        if ( this.device == null ) {
            throw new Exception( "Couldn't find a panda!" );
        }
        
        System.out.println( "Panda has been found on bus " + LibUsb.getBusNumber( this.device ) );
    }
    
    private Device findPanda() {
        // Read the USB device list
        DeviceList list = new DeviceList();
        int result = LibUsb.getDeviceList( null, list );
        if ( result < 0 ) throw new LibUsbException( "Unable to get device list", result );
        
        try {
            // Iterate over all devices and scan for the right one
            for ( Device device : list ) {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor( device, descriptor );
                if ( result != LibUsb.SUCCESS ) throw new LibUsbException( "Unable to read device descriptor", result );
                if ( descriptor.idVendor() == (short)0xbbaa && ( descriptor.idProduct() == (short)0xddcc || descriptor.idProduct() == (short)0xddee ) )
                    return device;
            }
        } finally {
            // Ensure the allocated device list is freed
            LibUsb.freeDeviceList( list, true );
        }
        
        // Device not found
        return null;
    }
    
  
}
