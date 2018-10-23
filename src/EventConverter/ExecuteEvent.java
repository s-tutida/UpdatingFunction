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
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(Object o) {
		
		// TODO 実際に命令を出す
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
            
            // 既存の機能を動かす.
            System.out.println("      Operate existing functions for " + knowledge.getNewCurrentStateName());
            
            if(knowledge.getNewCurrentStateName().equals("MoveToStartPoint")) {
            		MoveToStartPoint mtsp = new MoveToStartPoint(this.sc);
            		mtsp.runNewfunctions();
            		while(!mtsp.getEndEvent()) {
            		}
        			//arriveSpot event
        			knowledge.setEvent("arriveSpot");//knowledgeにinternal eventを配置
        			System.out.println("      arriveSpot event");
        			mtsp = null;
            		
            }else if(knowledge.getNewCurrentStateName().equals("SpotWait")){
            		SpotWait sw = new SpotWait();
            		while(!sw.getEndEvent()) {
            		}
        			//tm(2s) event
        			knowledge.setEvent("tm%282s&29");//knowledgeにinternal eventを配置
        			System.out.println("      tm(2s) event");
        			sw = null;
        			
            }else {
            		//none
            }
            break;
        case 2://既存機能を動かす
        	
	    		// state machine を進める
	    		String name = knowledge.getNewNextStateName(event);
	        knowledge.setOriginalCurrentState(knowledge.get_original_state_list().get(name));// originalを進める
	        knowledge.moveNewNextState(event);// currentを進める
        	
        		// TODO eventの送信
	    		while(event_list.peekLast()!=null) {
	    			System.out.println("      MMAPE-K loop will send this event : "+ event_list.pollLast());
	    		}
    			sc.send_command_original(1);
			//実行後, 少し時間をとる. MAPEと次のMAPEの間の時間を調整.
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    			sc.send_command_original(4);
			//実行後, 少し時間をとる. MAPEと次のMAPEの間の時間を調整.
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    			sc.send_command_original(5);
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
