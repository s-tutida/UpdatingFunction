package RoombaTest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.sql.rowset.serial.SerialException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialTest {

		InputStream in = null;
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

	                in = serialPort.getInputStream();
	                out = serialPort.getOutputStream();
	                sp = serialPort;

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
			                	    	case 1: startup(out);
			                	    	        break;
			                	    	case 2:	stop(out);
			                	    			 break;
			                	    	case 3 : safeMode(out);
			                	    			 break;
			                	    	case 4 : fullMode(out);
			                	    		     break;
			                	    	case 5 : clean(out);
			                	    	         break;
			                	    	case 6 : break;
			                	    	case 7 : break;
			                	    	case 8 : break;
			                	    	default : break;
			                	    	
//			                	    	case 3:	 out.write(motor(64,-64));//right
//	                	    			         break;
//			                	    	case 4:	 out.write(motor(-64,64));//left
//			                	    	         break;
//			                	    	case 5:	 out.write(motor(-64,-64));//back
//			                	    	         break;
//			                	    	case 6:  write(out, 128, 132, 137, 255, 56, 1, 244);
//			                	    	         break;
//			                	    	case 7:  write(out, 128, 132, 137, 6, 64, -6, -64);
//			                	    	         break;
//			                	    	case 8:  write(out, 135);
//			                	    	default:	 out.write(motor(0,0));//stop
		                	    
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
	    				(byte)((l&0x0000FF00)>>8),
	    				(byte)(l&0x000000FF),
	    				(byte)((r&0x0000FF00)>>8),
	    				(byte)(r&0x000000FF)
	    		};
	    		System.out.println("In motor method :" + buffer[0]+ ","+ buffer[1]+ ","+ buffer[2]+ ","+ buffer[3]+ ","+ buffer[4]+ ","+ buffer[5]+ ","+ buffer[6]); 
	    		return buffer;
	    }

	    private static void write(OutputStream out, int... data) throws IOException {
	        // Sigh, unsigned Java:
	        byte[] output = new byte[data.length];
	        for(int i = 0; i < data.length; i++) {
	            output[i] = (byte)(data[i]);
//	            output[i] = (byte)(data[i]&0xFF);
	        }
	        out.write(output);
	    }

		public static void main(String arg[]) throws Exception{
			    SerialTest rs = new SerialTest();
	            rs.connect("/dev/ttyUSB1");
		}

		public SerialTest(){

		}
		
	    public static void startup(OutputStream out) throws IOException {
	        System.out.println("Sending 'startup' and 'safeMode' command to roomba.");
	        int cmd[] = { OPC_START, OPC_SAFE };
	        write(out, cmd);
	    }

	    public static void stop(OutputStream out) throws IOException {
	        System.out.println("Sending 'stop' command to roomba.");
	        write(out, OPC_STOP);
	    }

	    public static void safeMode(OutputStream out) throws IOException {
	        System.out.println("Sending 'safe' command to roomba.");
	        write(out, OPC_SAFE);
	    }

	    
	    public static void fullMode(OutputStream out) throws IOException {
	        System.out.println("Sending 'full' command to roomba.");
	        write(out, OPC_FULL);
	    }


	    public static void clean(OutputStream out) throws IOException {
	        System.out.println("Sending 'clean' command to roomba.");
	        write(out, OPC_CLEAN);
	    }


//	    /**
//	     * This command controls Roomba’s drive wheels.
//	     * @param velocity The average velocity of the drive wheels in millimeters per second (mm/s)
//	     *                 A positive velocity makes the roomba drive forward, a negative velocity
//	     *                 makes it drive backwards.
//	     *                 min: -500mm/s max: 500mm/s
//	     * @param radius The radius in millimeters at which Roomba will turn
//	     *               The longer radii make Roomba drive straighter, while the shorter radii make Roomba turn more.
//	     *               The radius is measured from the center of the turning circle to the center of Roomba.
//	     *               <p>min: -2000mm max: 2000mm</p>
//	     *               Special cases:
//	     *               <ul>
//	     *                  <li>Straight = 32767 or 32768</li>
//	     *                  <li>Turn in place clockwise = -1</li>
//	     *                  <li>Turn in place counter-clockwise = 1</li>
//	     *               </ul>
//	     * @throws IllegalArgumentException One of the arguments is out of bounds.
//	     */
//	    public void drive(OutputStream out, int velocity, int radius) throws IllegalArgumentException {
//
//	        // Validate argument values
//	        if (velocity < -500 || velocity > 500)
//	            throw new IllegalArgumentException("Velocity should be between -500 and 500");
//	        if ((radius < -2000 || radius > 2000) && (radius != 32768 && radius != 32767))
//	            throw new IllegalArgumentException("Radius should be between -2000 and 2000 or 32767-32768");
//
//	        System.out.println("Sending 'drive' command (velocity:" + velocity + ", radius:" + radius + ") to roomba.");
//	        byte[] cmd = { (byte)OPC_DRIVE, (byte)(velocity >>> 8), (byte)velocity,
//	                        (byte)(radius >>> 8), (byte)radius
//	        };
//	        write(out, cmd);
//	    }
//
//	    /**
//	     * This command lets you control the forward and backward motion of Roomba’s drive wheels independently.
//	     * A positive velocity makes that wheel drive forward, while a negative velocity makes it drive backward.
//	     * @param rightVelocity Right wheel velocity min: -500 mm/s, max: 500 mm/s
//	     * @param leftVelocity Left wheel velocity min: -500 mm/s, max: 500 mm/s
//	     * @throws IllegalArgumentException One of the arguments is out of bounds.
//	     */
//	    public void driveDirect(int rightVelocity, int leftVelocity) throws IllegalArgumentException {
//
//	        // Validate argument values
//	        if (rightVelocity < -500 || rightVelocity > 500 || leftVelocity < -500 || leftVelocity > 500)
//	            throw new IllegalArgumentException("Velocity should be between -500 and 500");
//
//	        System.out.println("Sending 'driveDirect' command (velocity right: " + rightVelocity + ", " +
//	                "velocity left: " + leftVelocity + ") to roomba.");
//	        byte[] cmd = { (byte)OPC_DRIVE_WHEELS, (byte)(rightVelocity >>> 8), (byte)rightVelocity,
//	                        (byte)(leftVelocity >>> 8), (byte)leftVelocity
//	        };
//	        send(cmd);
//	    }
//
//	    /**
//	     * This command lets you control the raw forward and backward motion of Roomba’s drive wheels independently.
//	     * A positive PWM makes that wheel drive forward, while a negative PWM makes it drive backward.
//	     * The PWM values are percentages: 100% is full power forward, -100% is full power reverse.
//	     * @param rightPWM Right wheel PWM (min: -100%, max: 100%)
//	     * @param leftPWM Left wheel PWM (min: -100%, max: 100%)
//	     * @throws IllegalArgumentException One of the arguments is out of bounds.
//	     */
//	    public void drivePWM(int rightPWM, int leftPWM) throws IllegalArgumentException {
//
//	        // Validate argument values
//	        if (rightPWM < -100 || rightPWM > 100 || leftPWM < -100 || leftPWM > 100)
//	            throw new IllegalArgumentException("PWM should be between -100% and 100%");
//
//	        System.out.println("Sending 'drivePWM' command (right PWM: " + rightPWM + "%, left PWM: " + leftPWM + "%) to roomba.");
//	        int relRightPWM = DRIVE_WHEEL_MAX_POWER * rightPWM / 100;
//	        int relLeftPWM = DRIVE_WHEEL_MAX_POWER * leftPWM / 100;
//	        byte[] cmd = { (byte)OPC_DRIVE_PWM, (byte)(relRightPWM >>> 8), (byte)relRightPWM,
//	                        (byte)(relLeftPWM >>> 8), (byte)relLeftPWM
//	        };
//	        send(cmd);
//	    }
//
//	    /**
//	     * This command lets you control the forward and backward motion of Roomba’s main brush, side brush,
//	     * and vacuum independently. Motor velocity cannot be controlled with this command, all motors will run at
//	     * maximum speed when enabled. The main brush and side brush can be run in either direction.
//	     * The vacuum only runs forward.
//	     * @param sideBrush Turns on side brush
//	     * @param vacuum Turns on vacuum
//	     * @param mainBrush Turns on main brush
//	     * @param sideBrushClockwise if true the side brush will turn clockwise (default: counterclockwise)
//	     * @param mainBrushOutward if true the side brush will turn outward (default: inward)
//	     */
//	    public void motors(boolean sideBrush, boolean vacuum, boolean mainBrush,
//	                       boolean sideBrushClockwise, boolean mainBrushOutward) {
//	        System.out.println("Sending 'motors' command (sideBrush: " + sideBrush + "(clockwise: " + sideBrushClockwise + "), " +
//	                "vacuum: " + vacuum + ", mainBrush: " + mainBrush + "(outward: " + mainBrushOutward + ")) to roomba.");
//
//	        // Create motor byte
//	        byte motors = (byte)((sideBrush?MOTORS_SIDE_BRUSH_MASK:0) | (vacuum?MOTORS_VACUUM_MASK:0) |
//	                            (mainBrush?MOTORS_MAIN_BRUSH_MASK:0) | (sideBrushClockwise?MOTORS_SIDE_BRUSH_CW_MASK:0) |
//	                            (mainBrushOutward?MOTORS_MAIN_BRUSH_OW_MASK:0));
//	        byte[] cmd = { (byte)OPC_MOTORS, motors };
//	        send(cmd);
//	    }
//
//	    /**
//	     * This command lets you control the speed of Roomba’s main brush, side brush, and vacuum independently.
//	     * The main brush and side brush can be run in either direction. The vacuum only runs forward.
//	     * Positive speeds turn the motor in its default (cleaning) direction. Default direction for the side brush is
//	     * counterclockwise. Default direction for the main brush/flapper is inward.
//	     * The PWM values are percentages: 100% is full power forward, -100% is full power reverse.
//	     * @param mainBrushPWM Main brush PWM (min: -100%, max: 100%)
//	     * @param sideBrushPWM Side brush PWM (min: -100%, max: 100%)
//	     * @param vacuumPWM Vacuum PWM (min: 0%, max: 100%)
//	     * @throws IllegalArgumentException One of the arguments is out of bounds.
//	     */
//	    public void motorsPWM(int mainBrushPWM, int sideBrushPWM, int vacuumPWM) throws IllegalArgumentException {
//
//	        // Validate argument values
//	        if (mainBrushPWM < -100 || mainBrushPWM > 100 || sideBrushPWM < -100 || sideBrushPWM > 100)
//	            throw new IllegalArgumentException("Main- and side- brush PWM should be between -100% and 100%");
//	        if (vacuumPWM < 0 || vacuumPWM > 100)
//	            throw new IllegalArgumentException("Vacuum PWM should be between 0% and 100%");
//
//	        System.out.println("Sending 'motorsPWM' command (mainBrushPWM: " + mainBrushPWM + "%, sideBrushPWM: " + sideBrushPWM +
//	                "%, vacuumPWM: " + vacuumPWM + "%) to roomba.");
//	        int relMainBrushPWM = MOTORS_MAX_POWER * mainBrushPWM / 100;
//	        int relSideBrushPWM = MOTORS_MAX_POWER * sideBrushPWM / 100;
//	        int relVacuumPWM    = MOTORS_MAX_POWER * vacuumPWM / 100;
//	        byte[] cmd = { (byte)OPC_PWM_MOTORS, (byte)relMainBrushPWM, (byte)relSideBrushPWM, (byte)relVacuumPWM };
//	        send(cmd);
//	    }
//
//	    
	    // roomba Open interface commands Opcodes
	    private static final int OPC_RESET              =   7;
	    private static final int OPC_START              = 128;
	    private static final int OPC_SAFE               = 131;
	    private static final int OPC_FULL               = 132;
	    private static final int OPC_POWER              = 133;
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
