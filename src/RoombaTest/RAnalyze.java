package RoombaTest;

import Tsuchida.Analyze;
import Tsuchida.ControlLoop;

public class RAnalyze extends Analyze{

	public RAnalyze(ControlLoop cm, String name) {
		super(cm, name);
	}

	@Override
	public Object analysis(Object o) {
		return o;
	}


}
