package Tsuchida;

import Tsuda.*;

public abstract class Monitor extends Component{
	
	public volatile Knowledge knowledge = null;

	//configurationを使用しない場合
	public Monitor(Thread cm, String name) {
		super(cm, name);
		this.knowledge = getKnowledge(cm);
		makePort();//Monitorクラスのインスタンスが作成された時に、Portをつなぐ
	}
	
	//configurationを使用する場合
	public Monitor(ComponentManager cm, String name, Configuration conf) {
		super(cm, name, conf);
		makePort();
	}
	
	//make port
	public void makePort() {
		super.addPort(new Port<Object>("monitorP", Port.PortType.PROVIDED, this));
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
		
		//1. dataを取ってくる.
		o = getData();
		
		//2. Monitorの本体の実行
		obj = prepareData(o);
		
		//3. Monitorの結果をAnalysisに渡す
		try {
			getPort("monitorP").put(obj);//ここのnullにデータを入れて
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void finalize() {}
	
	public abstract Object getData();
	
	public abstract Object prepareData(Object o);

}
