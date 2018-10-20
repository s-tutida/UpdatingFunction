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
		String str = String.valueOf(o);
		String[] commands = str.split(",", 0);
        
		if(sc!=null){
			for(String command: commands){
				if(! (command==null || command.isEmpty())) {
					sc.send_command(Integer.parseInt(command));
					
					//MAPEの終了
					if(Integer.parseInt(command)==5) {
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
					
				    System.out.println("DEBUG : this command is send " + Integer.parseInt(command));
						try {
							Thread.sleep(700);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		}
		return null;
	}

}
