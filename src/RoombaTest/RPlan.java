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
		
	    System.out.println("DEBUG : mode in RPlan instance is " + mode);
		
		if(mode == -1) {
			String commands= "1,0,4,8,5";//Start cleaning
			return commands;
		}else if(mode == 1) {
			String commands= "1,4,81,6";
		    return commands;
		}else if(mode == 2){
			String commands= "1,4,71,6";
			return commands;
		}else if(mode == 3) {
			String commands= "1,4,72,6";
			return commands;
		}else if(mode == 4) {
			String commands= "1,4,82,6";
			return commands;
		}else{
            String commands= "8";
	        return commands;
		}
		
	}

}
