package at.workcloud.janda;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class CANMessage {
    
    private long address;
    private long b2;
    private byte[] b3;
    private long b4;
    
    public CANMessage( long address, long b2, byte[] b3, long b4 ) {
        this.address = address;
        this.b2 = b2;
        this.b3 = b3;
        this.b4 = b4;
    }
    
    public long getAddress() {
        return address;
    }
    
    public long getB2() {
        return b2;
    }
    
    public byte[] getB3() {
        return b3;
    }
    
    public long getB4() {
        return b4;
    }
}
