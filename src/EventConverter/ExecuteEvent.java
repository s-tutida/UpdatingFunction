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
        	
        		// TODO eventの送信
	    		while(event_list.peekLast()!=null) {
	    			System.out.println("      MAPE-K loop will send this event : "+ event_list.pollLast());
	    		}
	    		
			System.out.println("start up");
    			this.sc.send_command_original(1);//start up 

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("full mode");
			this.sc.send_command_original(3);//full modeへ以降

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("clean normal");
    			this.sc.send_command_original(4);//clean_normal
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("clean spot");
    			this.sc.send_command_original(5);//clean spot
    			
    			try {
    				Thread.sleep(10000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			
    			System.out.println("stop");
    			this.sc.send_command_original(2);//stop
    			
    			try {
    				Thread.sleep(10000);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			
    			System.out.println("sensor commands");
    			this.sc.send_command_original(0);//sensor commands
    			
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
