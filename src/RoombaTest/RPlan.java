package RoombaTest;

import Tsuchida.ControlLoop;
import Tsuchida.Plan;

public class RPlan extends Plan{

	public RPlan(ControlLoop cm, String name) {
		super(cm, name);
	}

	@Override
	public Object plan(Object o) {
		
		int mode = Integer.parseInt(String.valueOf(o));
		
		if(mode == -1) {
			String commands= "1,0,4,8,5";//Start cleaning
			return commands;
		}else if(mode == 1) {
			String commands= "1,4,141,6";
		    return commands;
		}else if(mode == 2){
			String commands= "1,4,231,6";
			return commands;
		}else if(mode == 3) {
			String commands= "1,4,232,6";
			return commands;
		}else if(mode == 4) {
			String commands= "1,4,142,6";
			return commands;
		}else{
            String commands= "8";
	        return commands;
		}
		
	}

}
