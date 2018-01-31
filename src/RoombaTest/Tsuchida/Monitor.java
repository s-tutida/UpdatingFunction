package Tsuchida;

import Tsuda.Component;
import Tsuda.Port;

public abstract class Monitor extends Component{

	//configurationを使用しない場合
	public Monitor(System cm, String name) {
		super(cm, name);
		makePort();//Monitorクラスのインスタンスが作成された時に、Portをつなぐ
	}
	
	//configurationを使用する場合
	public Monitor(System cm, String name, Configuration conf) {
		super(cm, name, conf);
		makePort();
	}
	
	//make port
	public void makePort() {
		super.addPort(new Port<Object>("monitorP", Port.PortType.PROVIDED, this));
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
