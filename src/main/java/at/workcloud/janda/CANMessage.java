package at.workcloud.janda;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class CANMessage {
    
    private long address;
    private long b2;
    private byte[] data;
    private long bus;
    
    public CANMessage( long address, long b2, byte[] data, long bus ) {
        this.address = address;
        this.b2 = b2;
        this.data = data;
        this.bus = bus;
    }
    
    public long getAddress() {
        return address;
    }
    
    public long getB2() {
        return b2;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public long getBus() {
        return bus;
    }
}
