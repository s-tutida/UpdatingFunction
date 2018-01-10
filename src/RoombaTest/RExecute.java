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
		String str = String.valueOf(o);
		String[] commands = str.split(",", 0);
	
		if(sc != null) {
			for(String command: commands){
				if(command!=null) {
				sc.send_command(Integer.parseInt(command));
				}
			}
		}
		
	}

}
