package Tsuda;

import java.util.ArrayList;
import java.util.Iterator;
import Tsuchida.Configuration;

public abstract class Component extends Thread{

	public String name;
	public Thread cm;
	public Configuration conf;
	public ArrayList<Port<?>> portList;

	//construct
	public Component(Thread cm, String name){

		portList = new ArrayList<Port<?>>();
		this.cm = cm;
		this.name = name;

	}
	
	//construct
	public Component(Thread cm, String name, Configuration conf){

		portList = new ArrayList<Port<?>>();
		this.cm = cm;
		this.name = name;
		this.conf = conf;

	}

	//このコンポーネントに,portを登録する
	public void addPort(Port<?> p){
		portList.add(p);
	}

	//あるポートを削除する
	public void removePort(String name){
		Iterator<Port<?>> i = portList.iterator();
		while(i.hasNext()){
			Port<?> p = i.next();
			if(p.name.equals(name)){
				i.remove();
			}
		}
	}
	
	//あるポートを返す
	public Port getPort(String name){
		Iterator<Port<?>> i = portList.iterator();
		while(i.hasNext()){
			Port<?> p = i.next();
			if(p.name.equals(name)){
				return p;
			}
		}
		
		return null;
	}


	
	//p1,p2を接続させるメソッド
	public void connectPort(Port<?> p1, Port<?>p2){
		p1.setPairPort(p2);
		p2.setPairPort(p1);
	}
	
	public void disConnectAllPort() {
		Iterator<Port<?>> i = portList.iterator();
		while(i.hasNext()){
			Port<?> p = i.next();
				disConnectPort(p);
		}
	}

	//あるポートの接続を削除する時に呼び出す命令
	public void disConnectPort(Port<?> p){
		p.removePairPort();
	}

   public void run(){
		action();//Thread処理
		finalize();//Thread終了処理
   }

	public abstract void action();

	public abstract void finalize();

}
