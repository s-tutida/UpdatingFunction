package RoombaTest;


import Tsuchida.ComponentManager;
import Tsuchida.Execute;

public class RExecute extends Execute{

    SerialCommunication sc = null;
	
	public RExecute(ComponentManager cm, String name) {
		super(cm, name);
	}
	
	@Override
	public Object execute(Object o) {
        try {
        	    sc = new SerialCommunication();
            sc.connect("/dev/ttyUSB0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return o;
	}

	@Override
	public void setData(Object o) {
		int commands[] = (int[]) o;
		
		if(sc != null) {
			for(int command: commands){
				sc.send_command(command);
			}
		}
		
	}

}
