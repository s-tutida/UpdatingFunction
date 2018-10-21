package EventConverter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import Tsuchida.*;  

public class PlanEvent extends Plan{

	public PlanEvent(ComponentManager cm, String name) {
		super(cm, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object plan(Object o) {
		
		
		Object[] result_of_analyzing = (Object[]) o;
		Integer mode = (Integer) result_of_analyzing[0];//mode
		String event =  (String) result_of_analyzing[1];//event
		KnowledgeState knowledge = (KnowledgeState)super.knowledge;
		Deque<String> event_list = null;
		Object[] result_of_planning = new Object[3];
		
		System.out.println("Plan");
		
		switch (mode) {
        case 1://追加機能を動かす
			System.out.println("      MAPE-K loop does not send events. ");
            break;
        case 2://既存機能を動かす
        	
	    	    Integer original_current_state  = knowledge.getOriginalCurrentState();//original current state
	    		Integer new_current_state  = knowledge.getNewNextState(event);//new next current state
	    		
	    		event_list = get_path(original_current_state, new_current_state);
	    		if(event_list.peekLast()==null) {
	    			System.out.println("      Error. There are no choice to move ");
	    		}
	    		while(event_list.peekLast()!=null) {
	    			System.out.println("      MAPE-K loop will send this event : "+ event_list.pollLast());
	    		}
	        break;
        case 3://何もしない
			System.out.println("      Invalid input in now state. ");
        	    break;
        	default:
            break;
        }
		
		result_of_planning[0] = mode;
		result_of_planning[1] = event;
		if(mode == 2) {
			result_of_planning[2] = event_list;		
		}else {
			result_of_planning[2] = null;
		}
		
		return result_of_planning;
	}
	
	//幅優先探索. 必ず, 見つかる設定.
	private Deque<String> get_path(Integer start, Integer goal) {
		
		Integer from = start;
		KnowledgeState knowledge = (KnowledgeState)super.knowledge;
		int size = knowledge.get_original_state_list().size();
		int[][] transition_map = new int[size][size];
		Map<String, Integer[]> original_event_list = knowledge.get_original_event_list();
		Map<String, String> original_event_refid_list = knowledge.get_original_event_refid_list();
		
		//経路マップ 初期化
		for(int i = 0; i < size; i++) {
			for(int k = 0; k < size; k++) {
				transition_map[i][k] = -1;
			}
		}
		
		//経路マップ 設定
		for(String event_refid:original_event_list.keySet()) {
			Integer[] transition = original_event_list.get(event_refid);
			transition_map[transition[0]][transition[1]] = 1;
		}
		
		//以下, 経路の幅優先探索
		Deque<Integer> stack = new ArrayDeque<Integer>();
		Deque<Integer> path = new ArrayDeque<Integer>();

		stack.offer(from);//queueに開始位置を入れる.
		while(!stack.isEmpty()) {

			path.offer(stack.peek());
			from = stack.pollLast();

			if(transition_map[from][goal] == 1) {
				path.offer(goal);
				break;
			}else {
				for(int k = 0; k < size; k++) {
					if(transition_map[from][k] == 1) {
						stack.offer(k);
					}
				}
			}
		}
		
		//経路 解答用意
		Deque<Integer> path_tmp = new ArrayDeque<Integer>();
		while(path.peekLast()!=start) {
			path_tmp.offerFirst(path.pollLast());
		}
		path_tmp.offerFirst(start);
		
		//change to event name from state
		Deque<String> answer = new ArrayDeque<String>();
		int event_s = -1; 
		int event_e = path_tmp.pollFirst();
		while(path_tmp.peek()!=null) {
			
			event_s = event_e; 
			event_e = path_tmp.pollFirst();
			
			for(String event_refid:original_event_list.keySet()) {
				Integer[] transition = original_event_list.get(event_refid);
				if((transition[0]==event_s)&&(transition[1]==event_e)){
					answer.offerFirst(original_event_refid_list.get(event_refid));
				}
			}
			
		}
		
		
		return answer;
	}
}
