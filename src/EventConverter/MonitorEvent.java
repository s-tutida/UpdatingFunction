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
		event = "Clean";
		
		//internal eventの設定
		KnowledgeState knowledge = (KnowledgeState)super.knowledge;
		if(knowledge.getEvent()!=null) {
			event = knowledge.getEvent();
			System.out.println("Internal event : " + event);
			knowledge.setEvent(null);
		}
//		event = "Spot";
//		event = "arriveSpot";
//		event = "endSpot";
		System.out.println(event);
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
