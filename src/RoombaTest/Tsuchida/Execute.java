package Tsuchida;

import Tsuda.Component;
import Tsuda.Port;

public abstract class Execute extends Component{

	//configurationを使用しない場合
	public Execute(System cm, String name) {
		super(cm, name);
		makePort();
	}
	
	//configurationを使用する場合
	public Execute(System cm, String name, Configuration conf) {
		super(cm, name, conf);
		makePort();
	}
	
	//make port
	public void makePort() {
		super.addPort(new Port<Object>("executeR", Port.PortType.REQUIRED, this));
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
		
		//3.setDataメソッドで呼び出す
		setData(obj);
		
	}

	@Override
	public void finalize() {
	}
	
	public abstract Object execute(Object o);
	public abstract void setData(Object o);
}
