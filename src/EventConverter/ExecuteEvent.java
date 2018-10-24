package EventConverter;

import java.util.Deque;

import Tsuchida.*;  
import SpotWaitTest.*;
import RoombaTest.*;


public class ExecuteEvent extends Execute{

	private SerialCommunication sc = null;
	public ExecuteEvent(ComponentManager cm, String name, SerialCommunication in_sc) {
		super(cm, name);
		sc = in_sc;
	}

	@Override
	public Object execute(Object o) {
		
		System.out.println("Execute");
		
		Object[] result_of_planning = (Object[]) o;
		Integer mode = (Integer) result_of_planning[0];//mode
		String event =  (String) result_of_planning[1];//event
		Deque<String> event_list =  (Deque<String>) result_of_planning[2];//event
		KnowledgeState knowledge = (KnowledgeState)super.knowledge;
		
		switch (mode) {
        case 1://追加機能を動かす
        	
        		// state machine を進める
            knowledge.moveNewNextState(event);
            
            // 追加機能を動かす
            System.out.println("      Operate existing functions for " + knowledge.getNewCurrentStateName() + " in the another thread.");
            
            if(knowledge.getNewCurrentStateName().equals("MoveToStartPoint")) {
            		
            		MoveToStartPoint mtsp = new MoveToStartPoint(this.sc, knowledge);
            		mtsp.startControlLoop();
            		
            }else if(knowledge.getNewCurrentStateName().equals("SpotWait")){
            		
            		SpotWait sw = new SpotWait(knowledge);
            		sw.start();
        			
            }
            
            break;
            
        case 2://既存機能を動かす
        	
	    		// state machine を進める
	    		String name = knowledge.getNewNextStateName(event);
	        knowledge.setOriginalCurrentState(knowledge.get_original_state_list().get(name));// originalを進める
	        knowledge.moveNewNextState(event);// currentを進める
        	
        		// eventの送信
			this.sc.send_command_original(1);//start up 
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.sc.send_command_original(3);//full mode
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
	    		while(event_list.peekLast()!=null) {
	    			String new_event = event_list.pollLast();
	    			System.out.println("      MAPE-K loop will send this event : "+ new_event);
	    			if(new_event.equals("Clean")) {
	    				if(knowledge.getNewCurrentStateName().equals("Clean")) {
	    					this.sc.send_command_original(4);//clean_normal
	    				}
	    			}else if(new_event.equals("Spot")) {
	    				System.out.println("      Spot command");
	    				this.sc.send_command_original(5);
	    				try {
	    					Thread.sleep(70000);
	    				} catch (InterruptedException e) {
	    					e.printStackTrace();
	    				}
	    				knowledge.setEvent("endSpot");
	    			}
	    		}

	        break;
        case 3:
        	    break;
        	default:
            break;
        }
		
		System.out.println("      original_current_state: "  + knowledge.getOriginalCurrentState());
		System.out.println("      new_current_state: "  + knowledge.getNewCurrentState());
		
		return null;
	}

}
