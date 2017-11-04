package at.workcloud.janda.handle;

import javax.usb.UsbException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public interface Handle {
    void controlWrite(byte requestType, int request, short value, short index, long timeout) throws Exception;
    byte[] bulkRead(byte interface_, int length, long timeout) throws Exception;
    void bulkWrite( byte endpoint, ByteBuffer data, long timeout) throws Exception;
    void close();
}
