package RoombaTest;

import Tsuchida.ComponentManager;
import Tsuchida.Plan;

public class RPlan extends Plan{

	public RPlan(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object plan(Object o) {
		
		String plan[] = (String[]) o;
		int direct = Integer.parseInt(plan[0]);
		int mode = Integer.parseInt(plan[1]);
		
		if(mode == START_CLEAN) {
			int commands[] = {1,0,4,5};
			return commands;
		}else if(mode == CONTINUE_SEARCH){
			if(direct == 1) {
				String commands[] = {"1","0","4", "5"};
				return commands;
			}else if(direct == 2){
				String commands[] = {"1" ,"0" ,"4","8"};
				return commands;
			}else if(direct == 3) {
				String commands[] = {"1","0","4","5"};
				return commands;
			}else if(direct == 4) {
				String commands[] = {"1","0","4","5"};
				return commands;
			}
		}
		return plan;
	}

	private static final int CONTINUE_SEARCH        = 0;
    private static final int START_CLEAN            = 1;
}
