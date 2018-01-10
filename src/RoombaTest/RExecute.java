package RoombaTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import Tsuchida.ComponentManager;
import Tsuchida.Execute;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class RExecute extends Execute{

	InputStream in = null;
	OutputStream out = null;
	
	public RExecute(ComponentManager cm, String name) {
		super(cm, name);
		// TODO Auto-generated constructor stub
	}

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier
                .getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            int timeout = 2000;
            CommPort commPort = portIdentifier.open(this.getClass().getName(),
                    timeout);
 
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
 
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
 
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
 
            } else {
                System.out
                        .println("Error: Only serial ports are handled by this example.");
            }
        }
    }
 
    public static class SerialReader implements Runnable {
 
        InputStream in;
 
        public SerialReader(InputStream in) {
            this.in = in;
        }
 
        public void run() {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ((len = this.in.read(buffer)) > -1) {
                	    //TODO Read byteに変更する.
//                	    int inputValue = Integer.parseInt(new String(buffer));
	            	    //debug
//	            	    System.out.println("DEBUG : SerialReader " + len);	
////                	    System.out.println("DEBUG : SerialWriter " + String.valueOf(len));	
//	            	    System.out.println("DEBUG : Byte " + (byte)(inputValue&0xFF));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static class SerialWriter implements Runnable {
 
        OutputStream out;
 
        public SerialWriter(OutputStream out) {
            this.out = out;
        }
 
        //TODO 入力により, 用意する
        public void run() {
            try {
 
                String line = null;
     			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    			    while ((line = br.readLine()) != null){    			    	
                	
                	    //debug
//                	    System.out.println("DEBUG : SerialWriter " + c);	
                	    System.out.println("DEBUG : SerialWriter " + line);	
                	    int inputValue = Integer.parseInt(new String(line));
                	    
                	    
//                    System.out.println("DEBUG : Byte " + (byte)(c&0xFF));
                	
//                	    if(c==1) {
                	    		// Write imperial march as song '0':
//                	    		write(out, 140, 0, 9, 57, 30, 57, 30, 57, 30, 53, 20, 60, 10, 57, 30, 53, 20, 60, 10, 57, 45);
//                    		write(out, 141, 0);
//                	    }
                	    out.write((byte)(inputValue&0xFF));
                }
            } catch (IOException e) {
                e.printStackTrace();
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
    
	@Override
	public Object execute(Object o) {
        try {
            connect("/dev/ttyUSB0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return null;
	}

	@Override
	public void setData(Object o) {
		// TODO Auto-generated method stub
		
	}

}