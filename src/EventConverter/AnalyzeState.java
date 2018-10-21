package EventConverter;

import java.util.Map;

import Tsuchida.*;  

public class AnalyzeState extends Analyze{

	
	public AnalyzeState(ComponentManager cm, String name) {
		super(cm, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object analysis(Object o) {

		System.out.println("Analyze");
		
		String event = o.toString();//event
		KnowledgeState knowledge = (KnowledgeState)super.knowledge;
		String new_next_state = knowledge.getNewNextStateName(event);// next state on new state machine
		Map<String, Integer> original_state_list = knowledge.get_original_state_list();
		
		int mode = 1;// 追加機能を動かす.
		if(original_state_list.containsKey(new_next_state)) {//既存のシステムで, 同じ遷移先が存在するか.
			mode = 2;// 既存機能を動かす.
		}else if(new_next_state == null) {
			mode = 3;// 何もしない. 現在の状態で、このイベントが入力されるのはおかしい。
		}
		
		System.out.println("      original_current_state: "  + knowledge.getOriginalCurrentState());
		System.out.println("      new_current_state: "  + knowledge.getNewCurrentState());
		System.out.println("      mode:" + mode);
		
		Object[] result_of_analyzing = new Object[2];
		result_of_analyzing[0] = mode;
		result_of_analyzing[1] = event;
		
		return result_of_analyzing;
	}

}
