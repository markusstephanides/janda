package at.workcloud.janda.handle;

import javax.usb.UsbException;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public interface Handle {
    void controlWrite(byte requestType, int request, short value, short index) throws Exception;
    byte[] bulkRead(byte interface_, int length) throws Exception;
    void close();
}
