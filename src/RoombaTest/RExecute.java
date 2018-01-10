package RoombaTest;


import java.io.IOException;

import Tsuchida.ComponentManager;
import Tsuchida.Execute;
import gnu.io.SerialPort;

public class RExecute extends Execute{

    SerialCommunication sc = null;
    SerialPort serialPort = null;
	
	public RExecute(ComponentManager cm, String name, SerialCommunication in_sc) {
		super(cm, name);
		sc = in_sc;
	}
	
	@Override
	public Object execute(Object o) {
		return o;
	}

	@Override
	public void setData(Object o) {
        
		String str = String.valueOf(o);
		String[] commands = str.split(",", 0);
	
		if(sc!=null){
			for(String command: commands){
				if(! (command==null || command.isEmpty())) {
					System.out.println(command);
					sc.send_command(Integer.parseInt(command));
				}
			}
		}
		
//		try {
//            serialPort.removeEventListener();
//            serialPort.close();            
//            sc.out.close();
//        } catch (IOException ex) {
//            // don't care
//        }
//        // Close the port.
//        serialPort.close();
//        
//    		Runtime runtime = Runtime.getRuntime();
//		Process p = null;
//		
//		try {
//			p = runtime.exec((String) "rm -R /var/lock/LCK..ttyUSB0");
//			p.waitFor();
//			
//	    } catch (InterruptedException e) {
//			e.printStackTrace();
//	    } catch (IOException e) {
//			e.printStackTrace();
//	    }
    }

}
