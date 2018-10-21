package Tsuda;

import java.util.ArrayList;

public class Port<obj> extends ArrayList<obj>{

	public enum PortType{
		PROVIDED, REQUIRED
	}

	//data store mode
	static final int _EXCLUSIVE_MODE_ = 1;           //store only the latest one *default*
	public static final int _QUEUE_MODE_ = 2;	    //queue type (FIFO)
	public static final int _STACK_MODE_ = 3;	    //stack type

	String name;
	PortType portType;
	Component myComponent;
	int portMode = _EXCLUSIVE_MODE_;
	public Port<obj> pairPort;

	public Port(String name, PortType type, Component c){
		this.name = name;
		myComponent = c;
	}

	public void setMode(int mode){
		portMode = mode;
	}

	//接続先のポートを設定するメソッド
	public void setPairPort(Port<?> p){
		this.pairPort = (Port<obj>)p;
	}

	//あるポートの削除を命じられた時,そのポートとつながっているポートも一緒に削除してやる
	public void removePairPort(){
		this.pairPort.pairPort = null;//相手のpairport(自分)を消しておく
		this.pairPort = null;
	}

	//pairportにメッセージを送信する
	public void put(obj o) throws Exception{
		if(pairPort == null){
			throw new Exception("PiarPort is not connected");
		}else{
			pairPort.add(o);
		}
	}

	//portに何かを受け取る
	public obj get() throws Exception{

		if(this.isEmpty()){
			throw new Exception("Port is Empty");//ここは最終的に必要
		}else{
			switch(portMode){
			case _EXCLUSIVE_MODE_:
				obj data = this.get(this.size()-1);
				this.clear();
				return data;
			case _QUEUE_MODE_:
				return (obj) this.remove(0);
			case _STACK_MODE_:
				return (obj) this.remove(this.size()-1);
			}
		}
		return null;
	}

}
