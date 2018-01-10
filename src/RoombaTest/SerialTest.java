package RoombaTest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialTest {

		InputStream in = null;
		OutputStream out = null;

		//シリアルポートとの接続を確立する関数
	    void connect(String portName) throws Exception {
	        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	        if (portIdentifier.isCurrentlyOwned()) {
	            System.out.println("Error: Port is currently in use");
	        } else {
	            int timeout = 2000;
	            CommPort commPort = portIdentifier.open(this.getClass().getName(),timeout);

	            if (commPort instanceof SerialPort) {
	                SerialPort serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

	                in = serialPort.getInputStream();
	                out = serialPort.getOutputStream();

	                (new Thread(new SerialReader(in))).start();//インプット用のスレッド
	                (new Thread(new SerialWriter(out))).start();//アウトプット用のスレッド

	            } else {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }
	    }

	    //シリアルポートからのインプットを扱うスレッド
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
	                	    int inputValue = Integer.parseInt(new String(buffer));
		            	    System.out.println("DEBUG : SerialReader Len" + len);
		            	    System.out.println("DEBUG : SerialReader InputValue" + (byte)(inputValue&0xFF));
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    //シリアルポートからのアウトプットを扱うスレッド
	    public static class SerialWriter implements Runnable {

	        OutputStream out;

	        public SerialWriter(OutputStream out) {
	            this.out = out;
	        }

	        //TODO 入力により, 用意する
	        public void run() {
	            try {

	                String line = null;
	     			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//標準入力から受け付ける
	     			
		    			while ((line = br.readLine()) != null){
	
		                	    //debug
		                	    int inputValue = Integer.parseInt(new String(line));
		                	    System.out.println("DEBUG : SerialWriter input cmd is " + inputValue);
		                	    
		                	    switch(inputValue) {
			                	    	case 1: write(out, 140, 0, 9, 57, 30, 57, 30, 57, 30, 53, 20, 60, 10, 57, 30, 53, 20, 60, 10, 57, 45);
			                	    	        write(out, 141, 0);
			                	    	case 2:	 out.write(motor(64,64));//forward
			                	    	case 3:	 out.write(motor(64,-64));//right
			                	    	case 4:	 out.write(motor(-64,64));//left
			                	    	case 5:	 out.write(motor(-64,-64));//back
			                	    	case 6:  write(out, 128, 135);
			                	    	case 7:  write(out, 128, 137, 146, 6, 64, -64, -6);
			                	    	default:	 out.write(motor(0,0));//stop
		                	    
		                	    }
	            	    }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	   
	    
	    static byte[] motor(int l, int r) {
	    		byte buffer[] = {
	    				(byte)(128&0xFF),//start
	    				(byte)(132&0xFF),//FULL
	    				(byte)(137&0xFF),//Drive PWM
	    				(byte)((r>>8)&0xFF),
	    				(byte)(r&0xFF),
	    				(byte)((l>>8)&0xFF),
	    				(byte)(l&0xFF)
	    		};
	    		
	    		return buffer;
	    }

	    private static void write(OutputStream out, int... data) throws IOException {
	        // Sigh, unsigned Java:
	        byte[] output = new byte[data.length];
	        for(int i = 0; i < data.length; i++) {
	            output[i] = (byte)(data[i]&0xFF);
	        }
	        out.write(output);
	    }

		public static void main(String arg[]) throws Exception{
			    SerialTest rs = new SerialTest();
	            rs.connect("/dev/ttyUSB1");
		}

		public SerialTest(){

		}

	}
