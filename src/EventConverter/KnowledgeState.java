package EventConverter;

import java.util.HashMap;
import java.util.Map;

import Tsuchida.*;

public class KnowledgeState extends Knowledge{

	// 状態マシン, 保存先, 宣言
	
	// 状態のリスト 例:[[状態名], [番号]]
	private Map<String, Integer> original_state_list = new HashMap();
	private Map<String, Integer> new_state_list = new HashMap();
	
	// reference id, event_name のセット
	private Map<String, String> original_event_refid_list = new HashMap();
	private Map<String, String> new_event_refid_list = new HashMap();
	
	// 遷移のリスト 例:["event_refid"][[遷移元番号],[遷移先番号]]
	private Map<String, Integer[]> original_event_list = new HashMap<String, Integer[]>();
	private Map<String, Integer[]> new_event_list = new HashMap<String, Integer[]>();
	
	// value for handle current state
	private Integer original_state = 1;
	private Integer new_state = 1;
	
	// internal event 
	public String event = null;

	public KnowledgeState(ComponentManager cm, String name, Parser ps) {
		super(cm, name);
		
		this.original_state_list = ps.get_original_state_list();
		this.new_state_list = ps.get_new_state_list();
		this.original_event_refid_list = ps.get_original_event_refid_list();
		this.new_event_refid_list = ps.get_new_event_refid_list();
		this.original_event_list = ps.get_original_event_list();
		this.new_event_list = ps.get_new_event_list();
		
		//開始擬似状態から, 初期状態に移行させる.
//		moveOriginalNextState("Initial_event");
//		moveNewNextState("Initial_event");
	}

	// Override
	@Override
	public Object setData(Object o) {
		return null;
	}

	@Override
	public Object getData(Object o) {
		return null;
	}

	// Set internal event
	public void setEvent(String s) {
		this.event = s;
	}
	
	// Get internal event
	public String getEvent() {
		return this.event;
	}
	
	// 次の状態を確認するメソッド.
	public Integer getOriginalNextState(String event) {
		for(String key : original_event_refid_list.keySet()) {
			if(original_event_refid_list.get(key).equals(event)) {//eventのref_id取得
				if(original_state == original_event_list.get(key)[0]) {//同じevent名で, 異なる箇所のeventか判定
					return original_event_list.get(key)[1];
				}
			}
		}
		return -1;
	}
	
	public Integer getNewNextState(String event) {
		for(String key : this.new_event_refid_list.keySet()) {
			if(this.new_event_refid_list.get(key).equals(event)) {
				if(new_state == new_event_list.get(key)[0]) {
					return this.new_event_list.get(key)[1];
				}
			}
		}
		return -1;
	}
	
	public String getNewCurrentStateName() {
		for(String key : new_state_list.keySet()) {
			if(new_state_list.get(key) == new_state) return key;
		}
		return null;
	}
	
	public String getOriginalNextStateName(String event) {
		Integer state_id = getOriginalNextState(event);
		for(String key : original_state_list.keySet()) {
			if(original_state_list.get(key) == state_id) return key;
		}
		return null;
	}
	
	public String getNewNextStateName(String event) {
		Integer state_id = getNewNextState(event);
		
		for(String key : new_state_list.keySet()) {
			if(new_state_list.get(key) == state_id) return key;
		}
		return null;
	}
		
	// 次の状態に移動させるメソッド.
	public Boolean moveOriginalNextState(String event) {
		for(String key : original_event_refid_list.keySet()) {
			if(original_event_refid_list.get(key).equals(event)) {
				if(getOriginalCurrentState() == original_event_list.get(key)[0]) {
					setOriginalCurrentState(original_event_list.get(key)[1]);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Boolean moveNewNextState(String event) {
		
		for(String key : this.new_event_refid_list.keySet()) {
			if(this.new_event_refid_list.get(key).equals(event)) {
				if(getNewCurrentState() == new_event_list.get(key)[0]) {
					setNewCurrentState(this.new_event_list.get(key)[1]);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void setOriginalCurrentState(Integer i) {
		this.original_state = i;
	}
	
	public void setNewCurrentState(Integer i) {
		this.new_state = i;		
	}
	
	// MAPEからKnowledgeを参照する用のメソッド.
	public Integer getOriginalCurrentState() {
		return this.original_state;
	}
	
	public Integer getNewCurrentState() {
		return this.new_state;
	}
	
	public Map<String, Integer> get_original_state_list(){
		return this.original_state_list;
	}
	
	public Map<String, Integer> get_new_state_list(){
		return this.new_state_list;
	}
	
	public Map<String, String> get_original_event_refid_list(){
		return this.original_event_refid_list;
	}
	
	public Map<String, String> get_new_event_refid_list(){
		return this.new_event_refid_list;
	}

	public Map<String, Integer[]> get_original_event_list(){
		return this.original_event_list;
	}
	
	public Map<String, Integer[]> get_new_event_list(){
		return this.new_event_list;
	}
	
	
}
