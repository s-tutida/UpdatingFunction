
package JavaRoombaTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SampleSerialCommunication {

      OutputStream out = null;
      SerialPort sp = null;
      public Integer button_event = -1;
      public Thread serialReaderThread = null;

      public void connect(String portName) throws Exception {
	    	
	        	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	        
	        	if (portIdentifier.isCurrentlyOwned()) {
	            		System.out.println("Error: Port is currently in use");
	        	} else {
	        	
	            		int timeout = 2000;
	            		CommPort commPort = portIdentifier.open(this.getClass().getName(),timeout);

	            		if (commPort instanceof SerialPort) {
	            	
	                		 SerialPort serialPort = (SerialPort) commPort;
	                
	               			 serialPort.setSerialPortParams(
	                			115200, 
	                			SerialPort.DATABITS_8,
	                			SerialPort.STOPBITS_1, 
	                			SerialPort.PARITY_NONE
                		 	 );
	                		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	               
	                 		this.out = serialPort.getOutputStream();
	                
	            		} else {
	                		System.out.println("Error: Only serial ports are handled by this example.");
	        	    	}
		}
       }

	    

       public void write(int... data) throws IOException {
	        // private static void write(OutputStream out, int... data) throws IOException {
	        byte[] output = new byte[data.length];
	        for(int i = 0; i < data.length; i++) {
	            output[i] = (byte)(data[i]);
	        }
	        this.out.write(output);
       }
	   
} 
