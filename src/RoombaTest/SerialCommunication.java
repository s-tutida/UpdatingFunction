
package RoombaTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialCommunication {

                InputStream in = null;
                public OutputStream out = null;
		// OutputStream out = null;
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
	                
	                this.in = serialPort.getInputStream();
	                this.out = serialPort.getOutputStream();
	                
	                serialReaderThread = new Thread(new SerialReader(in, this));
	                serialReaderThread.start();
	            } else {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }
	    }
	    
	   public SerialCommunication(){}
	    
	   public static class SerialReader implements Runnable 
	    {
	        InputStream in;
	        SerialCommunication sc;
	        
	        public SerialReader ( InputStream in_in , SerialCommunication in_sc)
	        {
	            this.in = in_in;
	            this.sc = in_sc;
	        }
	        
	        public void run ()
	        {
	            byte[] buffer = new byte[1024];
	            int len = -1;
	            try
	            {
	                while ( ( len = this.in.read(buffer)) > -1 )
	                {
           			 
	                		if(((buffer[0]&0xFF) != 0) && (sc.getButtonEvent()==-1)) {
	                		
	                			 
	                			int input = buffer[0]&0xFF;
	                			
	                         if((input == 1) || (input == 2)){
		            	            this.sc.send_command_original(2);
		            	            this.sc.send_command_original(1);
		            	            this.sc.button_event = input;
	                         }
	                         
	                		}
	                		
	                }
	            }
	            catch ( IOException e )
	            {
	                e.printStackTrace();
	            }            
	        }
	    }
	    
	   public int getButtonEvent() {
	    		return this.button_event;
	   }
	    
	   public void resetButtonEvent() {
	    		this.button_event = -1;
	   }
	    
       public void send_command_original(int inputValue) {
            try {
            	    switch(inputValue) {
            	    		case 0: write(out, 142, 18);
            	    			break;
                	    	case 1: startup(out);
                	    	    break;
                	    	case 2:	stop(out);
                	    		 break;
                	    	case 3 : fullMode(out);
                	    		 break;
                	    	case 4 : clean_normal(out);//clean
                	    	         break;
                	    	case 5 : clean_spot(out);//Spot
                	    			break;
                	    	default : break;
		                	    	
	                	    
	            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    
       public void send_command(int inputValue) {
            try {

            	    
            	    switch(inputValue) {
            	    		case 0: write(out, 140, 0, 9, 57, 30, 57, 30, 57, 30, 53, 20, 60, 10, 57, 30, 53, 20, 60, 10, 57, 45);
					        write(out, 141, 0);
                	    	        break;
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
                	    	case 6 : 
                	    		drive(out, -160, 0);//forward_l, mm/s
                	    	    break;
                	    	case 7 : 
                	    		drive(out, -500, 0);//forward_l, mm/s
                	    	    break;
                	    	case 141: // counter-clockwise_low 
                	    		driveDirect(out, 11, -11);
    	         		 	break;
                   	case 142: // counter-clockwise_high
                   		driveDirect(out, 20, -20);
	         		 	break;
                   	case 231: // clockwise_low
                   		driveDirect(out, -5, 5);//clockwise_l
                   		break;
                   	case 232:// clockwise_high
                   		driveDirect(out, -15, 15);//clockwise_l
                   		break;
                	    	case 8 : break;//sleep
                	    	default : break;
		                	    	
	                	    
	            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

	    public static void write(OutputStream out, int... data) throws IOException {
	    // private static void write(OutputStream out, int... data) throws IOException {
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
	        write(out, OPC_CLEAN);				
	    }

	    public static void drive(OutputStream out, int velocity, int radius) throws IllegalArgumentException, IOException {

	        if (velocity < -500 || velocity > 500)
	            throw new IllegalArgumentException("Velocity should be between -500 and 500");
	        if ((radius < -2000 || radius > 2000) && (radius != 32768 && radius != 32767))
	            throw new IllegalArgumentException("Radius should be between -2000 and 2000 or 32767-32768");

//	        System.out.println("Sending 'drive' command (velocity:" + velocity + ", radius:" + radius + ") to roomba.");
	        byte[] cmd = { (byte)OPC_DRIVE, (byte)(velocity >>> 8), (byte)velocity,
	                        (byte)(radius >>> 8), (byte)radius
	        };
	        drivePWM(out, 25, 25);
	        write(out, cmd);
	    }

	    public static void driveDirect(OutputStream out, int rightVelocity, int leftVelocity) throws IllegalArgumentException, IOException {

	        // Validate argument values
	        if (rightVelocity < -500 || rightVelocity > 500 || leftVelocity < -500 || leftVelocity > 500)
	            throw new IllegalArgumentException("Velocity should be between -500 and 500");

//	        System.out.println("Sending 'driveDirect' command (velocity right: " + rightVelocity + ", " +
//	                "velocity left: " + leftVelocity + ") to roomba.");
	        byte[] cmd = { (byte)OPC_DRIVE_WHEELS, (byte)(rightVelocity >>> 8), (byte)rightVelocity,
	                        (byte)(leftVelocity >>> 8), (byte)leftVelocity
	        };
	        write(out, cmd);
	    }

	    public static void drivePWM(OutputStream out, int rightPWM, int leftPWM) throws IllegalArgumentException, IOException {

	        // Validate argument values
	        if (rightPWM < -100 || rightPWM > 100 || leftPWM < -100 || leftPWM > 100)
	            throw new IllegalArgumentException("PWM should be between -100% and 100%");

//	        System.out.println("Sending 'drivePWM' command (right PWM: " + rightPWM + "%, left PWM: " + leftPWM + "%) to roomba.");
	        int relRightPWM = DRIVE_WHEEL_MAX_POWER * rightPWM / 100;
	        int relLeftPWM = DRIVE_WHEEL_MAX_POWER * leftPWM / 100;
	        byte[] cmd = { (byte)OPC_DRIVE_PWM, (byte)(relRightPWM >>> 8), (byte)relRightPWM,
	                        (byte)(relLeftPWM >>> 8), (byte)relLeftPWM
	        };
	        write(out, cmd);
	    }

	    public static void motors(OutputStream out, boolean sideBrush, boolean vacuum, boolean mainBrush,
	                       boolean sideBrushClockwise, boolean mainBrushOutward) throws IOException {
//	        System.out.println("Sending 'motors' command (sideBrush: " + sideBrush + "(clockwise: " + sideBrushClockwise + "), " +
//	                "vacuum: " + vacuum + ", mainBrush: " + mainBrush + "(outward: " + mainBrushOutward + ")) to roomba.");

	        // Create motor byte
	        byte motors = (byte)((sideBrush?MOTORS_SIDE_BRUSH_MASK:0) | (vacuum?MOTORS_VACUUM_MASK:0) |
	                            (mainBrush?MOTORS_MAIN_BRUSH_MASK:0) | (sideBrushClockwise?MOTORS_SIDE_BRUSH_CW_MASK:0) |
	                            (mainBrushOutward?MOTORS_MAIN_BRUSH_OW_MASK:0));
	        byte[] cmd = { (byte)OPC_MOTORS, motors };
	        write(out, cmd);
	    }

	    public static void motorsPWM(OutputStream out, int mainBrushPWM, int sideBrushPWM, int vacuumPWM) throws IllegalArgumentException, IOException {

	        // Validate argument values
	        if (mainBrushPWM < -100 || mainBrushPWM > 100 || sideBrushPWM < -100 || sideBrushPWM > 100)
	            throw new IllegalArgumentException("Main- and side- brush PWM should be between -100% and 100%");
	        if (vacuumPWM < 0 || vacuumPWM > 100)
	            throw new IllegalArgumentException("Vacuum PWM should be between 0% and 100%");

//	        System.out.println("Sending 'motorsPWM' command (mainBrushPWM: " + mainBrushPWM + "%, sideBrushPWM: " + sideBrushPWM +
//	                "%, vacuumPWM: " + vacuumPWM + "%) to roomba.");
	        int relMainBrushPWM = MOTORS_MAX_POWER * mainBrushPWM / 100;
	        int relSideBrushPWM = MOTORS_MAX_POWER * sideBrushPWM / 100;
	        int relVacuumPWM    = MOTORS_MAX_POWER * vacuumPWM / 100;
	        byte[] cmd = { (byte)OPC_PWM_MOTORS, (byte)relMainBrushPWM, (byte)relSideBrushPWM, (byte)relVacuumPWM };
	        write(out, cmd);
	    }

	    
	    // roomba Open interface commands Opcodes
	    private static final int OPC_START              = 128;
	    private static final int OPC_SAFE               = 131;
	    private static final int OPC_FULL               = 132;
	    private static final int OPC_CLEAN_SPOT         = 134;
	    private static final int OPC_CLEAN              = 135;
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
