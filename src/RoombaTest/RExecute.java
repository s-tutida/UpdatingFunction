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
		String commands[] = (String[]) o;
		
		if(sc != null) {
			for(String command: commands){
				sc.send_command(Integer.parseInt(command));
			}
		}
		
	}

}
