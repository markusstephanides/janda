package at.workcloud.janda.util;

import org.usb4java.DeviceHandle;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public interface HandleRunable {
    void run(DeviceHandle handle);
}
