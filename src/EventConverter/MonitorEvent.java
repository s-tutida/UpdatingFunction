package EventConverter;

import Tsuchida.*;  

public class MonitorEvent extends Monitor{

	public MonitorEvent(ComponentManager cm, String name) {
		super(cm, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getData() {
		
		// TODO 実際に, Eventを監視.
		String event = new String();
//		event = "Clean";
//		event = "Spot";
//		event = "arriveSpot";
//		event = "endSpot";
		return event;
	}

	@Override
	public Object prepareData(Object o) {
		String event = o.toString();
		System.out.println("");
		System.out.println("Monitor");
		System.out.println("      inputs " + event + " event.");
		return event;
	}

}
