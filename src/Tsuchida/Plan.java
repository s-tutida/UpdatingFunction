package Tsuchida;

import Tsuda.*;

public abstract class Plan extends Component{
	
	public volatile Knowledge knowledge = null;

	//configurationを使用しない場合
	public Plan(Thread cm, String name) {
		super(cm, name);
		this.knowledge = getKnowledge(cm);
		makePort();
	}
	
	//configurationを使用する場合
	public Plan(Thread cm, String name, Configuration conf) {
		super(cm, name,conf);
		makePort();
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
	
	//make port
	public void makePort() {
		super.addPort(new Port<Object>("planP", Port.PortType.PROVIDED, this));
		super.addPort(new Port<Object>("planR", Port.PortType.REQUIRED, this));
	}

	@Override
	public void action() {
		
		Object o = null;
		Object obj = null;
		
		//1. Analysisからデータを持ってくる
		try {
			o = getPort("planR").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2. Planの本体の実行
		obj = plan(o);
		
		//3. Planの結果をExecuteクラスに渡す
		try {
			getPort("planP").put(obj);//nullに値を入れて
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void finalize() {
//		getPort("planR").clear();//entityの掃除
	}
	
	public abstract Object plan(Object o);

}

