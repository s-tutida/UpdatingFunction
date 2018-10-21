package Tsuchida;

import Tsuda.Component;
import Tsuda.Port;

public abstract class Execute extends Component{

	public volatile Knowledge knowledge = null;
	
	//configurationを使用しない場合
	public Execute(Thread cm, String name) {
		super(cm, name);
		this.knowledge = getKnowledge(cm);
		makePort();
	}
	
	//configurationを使用する場合
	public Execute(ComponentManager cm, String name, Configuration conf) {
		super(cm, name, conf);
		makePort();
	}
	
	//make port
	public void makePort() {
		super.addPort(new Port<Object>("executeR", Port.PortType.REQUIRED, this));
	}
	
	//get knowledge
	public Knowledge getKnowledge(Thread cm) {
		try {
			return ((ComponentManager)cm).getKnowledge();
        } catch (ClassCastException exception) {
    			return ((ControlLoop)cm).getKnowledge();
        } catch (Exception exception) {

        }
        return null;
	}

	@Override
	public void action() {
		
		Object o = null;
		Object obj = null;
		
		//1. Planクラスの結果を持ってくる
		try {
			o = getPort("executeR").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2. Executeクラスの本体の実行
		obj = execute(o);
		
	}

	@Override
	public void finalize() {
	}
	
	public abstract Object execute(Object o);
}
