package RoombaTest;

import Tsuchida.Analysis;
import Tsuchida.ComponentManager;

public class RAnalyze extends Analysis{

	public RAnalyze(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object analysis(Object o) {
		
		String nums[] = (String[]) o;
		String plan[] = null;
		
		int direct = Integer.parseInt(nums[0]);
		int distance = Integer.parseInt(nums[1]);
		if(distance < REFERENCE_distance ) {
			plan[0] = String.valueOf(direct);
			plan[1] = String.valueOf(CONTINUE_SEARCH);
		}else {
			plan[0] = String.valueOf(-1);
			plan[1] = String.valueOf(START_CLEAN);
		}
		return plan;
	}
	
	private static final int REFERENCE_distance     = 5000;
	private static final int CONTINUE_SEARCH        = 0;
    private static final int START_CLEAN            = 1;

}
