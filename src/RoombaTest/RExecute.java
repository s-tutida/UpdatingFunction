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
					sc.send_command(Integer.parseInt(command));
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	
    }

}
