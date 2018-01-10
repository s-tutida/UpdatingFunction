package RoombaTest;


import java.io.IOException;

import Tsuchida.ComponentManager;
import Tsuchida.Execute;
import gnu.io.SerialPort;

public class RExecute extends Execute{

    SerialCommunication sc = null;
    SerialPort serialPort = null;
	
	public RExecute(ComponentManager cm, String name) {
		super(cm, name);
	}
	
	@Override
	public Object execute(Object o) {
		return o;
	}

	@Override
	public void setData(Object o) {
		
        try {
	    	    sc = new SerialCommunication();
	        sc.connect("/dev/ttyUSB0");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        
		String str = String.valueOf(o);
		String[] commands = str.split(",", 0);
	
		if(sc != null) {
			for(String command: commands){
				if(command!=null) {
				sc.send_command(Integer.parseInt(command));
				}
			}
		}
		
		try {

            serialPort.removeEventListener();
            serialPort.close();            
            sc.out.close();
        } catch (IOException ex) {
            // don't care
        }
        // Close the port.
        serialPort.close();
    }

}
