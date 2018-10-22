package EventConverter;

import java.io.IOException;
import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialCommunication {

		OutputStream out = null;
		SerialPort sp = null;

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
	                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	                
	                out = serialPort.getOutputStream();
	                sp = serialPort;

	            } else {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }
	    }
	    
        public void send_command(int inputValue) {
            try {

            		//Clean, Spot, EndSpotの3つのみ.
            	    switch(inputValue) {
                	    	case 1: startup(out);
                	    	        break;
                	    	case 2:	stop(out);
                	    		 break;
                	    	case 3 : safeMode(out);
                	    		 break;
                	    	case 4 : fullMode(out);
                	    		 break;
                	    	case 5 : clean_spot(out);//Spot
                	    	         break;
                	    	case 6 : clean_normal(out);
                	    			break;
                	    	default : break;
		                	    	
	                	    
	            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


	    private static void write(OutputStream out, int... data) throws IOException {
	        // Sigh, unsigned Java:
	        byte[] output = new byte[data.length];
	        for(int i = 0; i < data.length; i++) {
	            output[i] = (byte)(data[i]);
	        }
	        out.write(output);
	    }
	    
	    private static void write(OutputStream out, byte... data) throws IOException {
	        byte[] output = new byte[data.length];
	        for(int i = 0; i < data.length; i++) {
	            output[i] = data[i];
	        }
	        out.write(output);
	    }

		public SerialCommunication(){

		}
		
	    public static void startup(OutputStream out) throws IOException {
	        int cmd[] = { OPC_START, OPC_SAFE };
	        write(out, cmd);
	    }

	    public static void stop(OutputStream out) throws IOException {
	        write(out, OPC_STOP);
	    }

	    public static void safeMode(OutputStream out) throws IOException {
	        write(out, OPC_SAFE);
	    }

	    
	    public static void fullMode(OutputStream out) throws IOException {
	        write(out, OPC_FULL);
	    }


	    public static void clean_spot(OutputStream out) throws IOException {
	        write(out, OPC_CLEAN_SPOT);				
	    }

	    
	    public static void clean_normal(OutputStream out) throws IOException {
	        write(out, OPC_CLEAN_NORMAL);				
	    }
	    
	    // roomba Open interface commands Opcodes
	    private static final int OPC_START              = 128;
	    private static final int OPC_SAFE               = 131;
	    private static final int OPC_FULL               = 132;
	    private static final int OPC_CLEAN_SPOT         = 134;
	    private static final int OPC_CLEAN_NORMAL       = 135;
	    private static final int OPC_DRIVE              = 137;
	    private static final int OPC_MOTORS             = 138;
	    private static final int OPC_PWM_MOTORS         = 144;
	    private static final int OPC_DRIVE_WHEELS       = 145;
	    private static final int OPC_DRIVE_PWM          = 146;
	    private static final int OPC_STOP               = 173;

	    // Sensor packets Group packet ID
	    static final int SENSOR_PACKET_ALL_SIZE         = 80;

	    // Motors bitmask
	    private static final int MOTORS_SIDE_BRUSH_MASK     = 0x1;
	    private static final int MOTORS_VACUUM_MASK         = 0x2;
	    private static final int MOTORS_MAIN_BRUSH_MASK     = 0x4;
	    private static final int MOTORS_SIDE_BRUSH_CW_MASK  = 0x8;
	    private static final int MOTORS_MAIN_BRUSH_OW_MASK  = 0x10;

	    // Drive constants
	    private static final int DRIVE_WHEEL_MAX_POWER  = 0xFF;

	    // Motors constants
	    private static final int MOTORS_MAX_POWER       = 0x7F;

	}
