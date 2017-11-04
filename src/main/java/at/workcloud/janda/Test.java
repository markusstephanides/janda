package at.workcloud.janda;

/**
 * @author MaSte
 * @created 04.11.2017
 */
public class Test {
    
    public static void main(String[] args) throws Exception {
        Panda panda = new Panda( );
        panda.setSafetyMode( Panda.SAFETY_ELM327 );
        CANMessage[] msgs = panda.canRecv();
        System.out.println("Received " + msgs.length);
        for ( CANMessage msg : msgs ) {
            System.out.println(msg.getAddress() + ", " + msg.getB2() + ", " + msg.getData().length + " + bytes, " + msg.getBus());
        }
        
        panda.close();
    }
    
}
