package RoombaTest;

import Tsuchida.ComponentManager;
import Tsuchida.Plan;

public class RPlan extends Plan{

	public RPlan(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object plan(Object o) {
		
		int plan[] = (int[]) o;
		int direct = plan[0];
		
		if(plan[1] == START_CLEAN) {
			int commands[] = {1,0,4,5};
			return commands;
		}else if(plan[1] == CONTINUE_SEARCH){
			if(direct == 1) {
				int commands[] = {1,0,4,5};
				return commands;
			}else if(direct == 2){
				int commands[] = {1,4,8};
				return commands;
			}else if(direct == 3) {
				int commands[] = {1,0,4,5};
				return commands;
			}else if(direct == 4) {
				int commands[] = {1,0,4,5};
				return commands;
			}
		}
		return plan;
	}

	private static final int CONTINUE_SEARCH        = 0;
    private static final int START_CLEAN            = 1;
}
