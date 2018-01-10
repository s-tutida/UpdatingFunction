package RoombaTest;

import Tsuchida.ComponentManager;
import Tsuchida.Plan;

public class RPlan extends Plan{

	public RPlan(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object plan(Object o) {
		
		int mode = Integer.parseInt(String.valueOf(o));
		
		if(mode == -1) {
			String commands= "1,0,4,5";
			return commands;
		}else if(mode == 1) {
			String commands= "1,0,4,5";
		    return commands;
		}else if(mode == 2){
			String commands= "1,0,4,5";
			return commands;
		}else if(mode == 3) {
			String commands= "1,0,4,5";
			return commands;
		}else if(mode == 4) {
			String commands= "1,0,4,5";
			return commands;
		}else{
                        String commands= "0";
	                return commands;
		}
	}

}
