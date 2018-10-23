package EventConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class SerialPortTest {

		OutputStream out = null;
//		BufferedReader in = null;
		SerialPort sp = null;
		
	    public SerialPortTest()
	    {
	        super();
	    }

		//シリアルポートとの接続を確立する関数
	    public void connect(String portName) throws Exception {
	    	
	    	    //ポートを取得
	        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	        
	        if (portIdentifier.isCurrentlyOwned()) {
	            System.out.println("Error: Port is currently in use");
	        } else {
	        	
	        	    //ポートを開く, timeoutを2s秒に設定
	            int timeout = 2000;
	            CommPort commPort = portIdentifier.open(this.getClass().getName(),timeout);

	            if (commPort instanceof SerialPort) {
	            	
	            	    //シリアルポートのインスタンスを作成
	                SerialPort serialPort = (SerialPort) commPort;
	                
	                //ボーレート, データビット数, ストップビット数, パリティを設定
	                serialPort.setSerialPortParams(
	                		115200, 
	                		SerialPort.DATABITS_8,
	                		SerialPort.STOPBITS_1, 
	                		SerialPort.PARITY_NONE
                		);
	                //フロー制御はしない
	                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	                
	                InputStream in = serialPort.getInputStream();
	                OutputStream out = serialPort.getOutputStream();
	                
	                (new Thread(new SerialReader(in))).start();
	                (new Thread(new SerialWriter(out))).start();

	            } else {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }
	    }
	    
	    /** */
	    public static class SerialReader implements Runnable 
	    {
	        InputStream in;
	        
	        public SerialReader ( InputStream in )
	        {
	            this.in = in;
	        }
	        
	        public void run ()
	        {
	            byte[] buffer = new byte[1024];
	            int len = -1;
	            try
	            {
	                while ( ( len = this.in.read(buffer)) > -1 )
	                {
                	        System.out.print("read" + len);
	                    System.out.print(new String(buffer,0,len));
	                }
	            }
	            catch ( IOException e )
	            {
	                e.printStackTrace();
	            }            
	        }
	    }

	    /** */
	    public static class SerialWriter implements Runnable 
	    {
	        OutputStream out;
	        
	        public SerialWriter ( OutputStream out )
	        {
	            this.out = out;
	        }
	        
	        public void run ()
	        {
	            try
	            {                
	                int c = 0;
	                while ( ( c = System.in.read()) > -1 )
	                {
	                	    System.out.println("write: " + c);
	                	    System.out.println("");
	                    this.out.write((byte)c);
	                }                
	            }
	            catch ( IOException e )
	            {
	                e.printStackTrace();
	            }            
	        }
	    }
	    
	    public static void main ( String[] args )
	    {
	        try
	        {
	            (new SerialPortTest()).connect("/dev/ttyUSB0");
	        }
	        catch ( Exception e )
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	}