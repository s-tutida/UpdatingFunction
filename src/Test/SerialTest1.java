package Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
public class SerialTest1 {
	
	public void SerialTest1() {
	}

	public void start() {
		// TODO Auto-generated method stub
		// On the Raspberry Pi the Serial communication pins are connected to /dev/ttyAMA0
	    CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");
	    
	    if( portIdentifier.isCurrentlyOwned() ) {
	        System.out.println( "Error: Port is currently in use" );
	    } else {
	        CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
	
	        if( commPort instanceof SerialPort) {
	            SerialPort serialPort = ( SerialPort )commPort;
	            serialPort.setSerialPortParams(
	                    115200,
	                    SerialPort.DATABITS_8,
	                    SerialPort.STOPBITS_1,
	                    SerialPort.PARITY_NONE );
	
	            InputStream in = serialPort.getInputStream();
	            OutputStream out = serialPort.getOutputStream();
	

                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
                
	    	    		// Write imperial march as song '0':
	    	    		write(out, 140, 0, 9, 57, 30, 57, 30, 57, 30, 53, 20, 60, 10, 57, 30, 53, 20, 60, 10, 57, 45);
	    	    
	    	    		// Play song '0':
	    	    		write(out, 141, 0);
	    	    		
	        }
	    }
	}
   

private static void write(OutputStream out, int... data) throws IOException {
    
		// Sigh, unsigned Java:
	byte[] output = new byte[data.length];
	
	for(int i = 0; i < data.length; i++) {
			output[i] = (byte)(data[i]&0xFF);
	}
	out.write(output); 
}

}