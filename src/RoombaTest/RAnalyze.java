package RoombaTest;

import Tsuchida.Analysis;
import Tsuchida.ComponentManager;

public class RAnalyze extends Analysis{

	public RAnalyze(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object analysis(Object o) {
		
		int nums[] = (int[]) o;
		int plan[] = null;
		
		int direct = nums[0];
		int distance = nums[1];
		if(distance < REFERENCE_distance ) {
			plan[0] = direct;
			plan[1] = CONTINUE_SEARCH;
		}else {
			plan[0] = -1;
			plan[1] = START_CLEAN;
		}
		return plan;
	}
	
	private static final int REFERENCE_distance     = 5000;
	private static final int CONTINUE_SEARCH        = 0;
    private static final int START_CLEAN            = 1;

}
