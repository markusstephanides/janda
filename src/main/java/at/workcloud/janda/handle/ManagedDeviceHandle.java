package at.workcloud.janda.handle;

import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class ManagedDeviceHandle implements Closeable {
    
    private DeviceHandle handle;
    private boolean detach;
    
    public ManagedDeviceHandle( Device device ) {
        this.open( device );
    }
    
    public void close() throws IOException {
        // Attach the kernel driver again if needed
        if ( this.detach ) {
            int result = LibUsb.attachKernelDriver( this.handle, 0 );
            if ( result != LibUsb.SUCCESS )
                throw new LibUsbException( "Unable to re-attach kernel driver", result );
        }

        int result = LibUsb.releaseInterface(this.handle, 0);
        if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to release interface", result);

        LibUsb.close( this.handle );

        System.out.println("closed");
        
    }
    
    public DeviceHandle getHandle() {
        return handle;
    }
    
    private void open( Device device ) {
        this.handle = new DeviceHandle();
        int openResult = LibUsb.open( device, this.handle );
        if ( openResult != LibUsb.SUCCESS ) throw new LibUsbException( "Unable to open USB device", openResult );

        int claimResult = LibUsb.claimInterface(this.handle, 0);
        if (claimResult != LibUsb.SUCCESS) throw new LibUsbException("Unable to claim interface", claimResult);


        // Check if kernel driver must be detached
        this.detach = LibUsb.hasCapability( LibUsb.CAP_SUPPORTS_DETACH_KERNEL_DRIVER )
                && LibUsb.kernelDriverActive( this.handle, 0 ) == 1;
        
        // Detach the kernel driver
        if ( this.detach ) {
            int result = LibUsb.detachKernelDriver( this.handle, 0 );
            if ( result != LibUsb.SUCCESS ) throw new LibUsbException( "Unable to detach kernel driver", result );
        }
        System.out.println("opened");
    }
    
}
