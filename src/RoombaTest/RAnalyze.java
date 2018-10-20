package RoombaTest;

import Tsuchida.Analyze;
import Tsuchida.ComponentManager;

public class RAnalyze extends Analyze{

	public RAnalyze(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object analysis(Object o) {
		return o;
	}


}
