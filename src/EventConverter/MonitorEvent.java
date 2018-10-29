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
		
		
		//internal eventがない場合, ボタン押待ち状態でボタンが押されるのを待つ
		sc.resetButtonEvent();// reset button
		while(true) {
			//受信モード on
			this.sc.send_command_original(0);
			int button_event = -1;
			if((button_event = sc.getButtonEvent()) != -1) {
	        		//Clean, Spot, EndSpotの3つのみ.
	        	    switch(button_event) {
	        	    		case 1: return "Clean";
	            	    	case 2: return "Spot";
	            	    	default : 
		            	    	break;
	        	    }
			}
			
			if(knowledge.getEvent()!=null) {
				event = knowledge.getEvent();
				knowledge.setEvent(null);
				return event;
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
