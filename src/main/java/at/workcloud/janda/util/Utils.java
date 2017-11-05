package at.workcloud.janda.util;

import at.workcloud.janda.CANMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class Utils {
    
    public static CANMessage[] parseCanBuffer( byte[] dat) {
        List<CANMessage> canMessages = new ArrayList<>();
        
        for(int i = 0; i < dat.length; i+=0x10) {
            byte[] ddat = new byte[0x10];
            System.arraycopy( dat, i, ddat, 0, 0x10);
            long f1 = ByteBuffer.wrap( ddat, 0, 4 ).getInt();
            long f2 = ByteBuffer.wrap( ddat, 4, 4 ).getInt();
            byte extended = 4;
            long address;
            
            if ((f1 & extended) == 1) {
                address = f1 >> 3;
            }
            else {
                address = f1 >> 21;
            }
            
            int len = Math.toIntExact( f2 & 0xF );
            byte[] tmp = new byte[ len ];
            
            System.arraycopy( ddat, 8, tmp, 0, len );
            canMessages.add( new CANMessage( address, f2 >> 16, tmp , (f2>>4) & 0xFF ) );
        }
        
        return canMessages.toArray( new CANMessage[canMessages.size()] );
    }
    
}
