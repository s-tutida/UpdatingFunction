package EventConverter;

import RoombaTest.SerialCommunication;
import Tsuchida.*;  

public class MonitorEvent extends Monitor{

	SerialCommunication sc = null;
	public MonitorEvent(ComponentManager cm, String name, SerialCommunication in_sc) {
		super(cm, name);
		this.sc = in_sc;
	}

	@Override
	public Object getData() {
		
		String event = new String();

		//internal eventの設定
		KnowledgeState knowledge = (KnowledgeState)super.knowledge;
		if(knowledge.getEvent()!=null) {
			event = knowledge.getEvent();
			knowledge.setEvent(null);
			return event;
		}
		
		//internal eventがない場合, ボタン押待ち状態でボタンが押されるのを待つ
	
		while(true) {
			this.sc.send_command_original(0);//受信モード
			int button_event = -1;
			if((button_event = sc.getButtonEvent()) != -1) {
				sc.resetButtonEvent();
	        		//Clean, Spot, EndSpotの3つのみ.
	        	    switch(button_event) {
	        	    		case 1: return "Clean";
	            	    	case 2: return "Spot";
	            	    	default : break;
	        	    }
			}
		}
		
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
