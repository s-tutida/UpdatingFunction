package Tsuchida;

import Tsuda.Component;
import Tsuda.Port;

public abstract class Analyze extends Component{

	public volatile Knowledge knowledge = null;
	
	//configurationを使用しない場合
	public Analyze(ComponentManager cm, String name) {
		super(cm, name);
		this.knowledge = getKnowledge(cm);
		makePort();
	}
	
	//configurationを使用する場合
	public Analyze(ComponentManager cm, String name, Configuration conf) {
		super(cm, name, conf);
		makePort();
	}
	
	//make port
	public void makePort() {
		super.addPort(new Port<Object>("analysisP", Port.PortType.PROVIDED, this));
		super.addPort(new Port<Object>("analysisR", Port.PortType.REQUIRED, this));
	}
	
	//get knowledge
	public Knowledge getKnowledge(ComponentManager cm) {
		return cm.getKnowledge();
	}

	@Override
	public void action() {
		
		Object o = null;
		Object obj = null;
		
		//1.Monitorからデータを取ってくる
		try {
			o = getPort("analysisR").get();//この返り値を作成して！
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2. Analysisの本体の実行
		obj = analysis(o);
		
		//3. Analysisの結果をPlanクラスに渡す
		try {
			getPort("analysisP").put(obj);//ここのnullにデータを入れて
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void finalize() {
//		getPort("analysisR").clear();//entityの掃除
	}
	
	public abstract Object analysis(Object o);

}


