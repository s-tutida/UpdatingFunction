package Tsuchida;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import Tsuda.Component;

public abstract class ComponentManager extends Thread{
	
	public volatile List<Component> componentList = new ArrayList<Component>();//実行中のコンポーネントのリスト 
	public volatile List<Component> componentSubList  = new ArrayList<Component>();;//サブのコンポーエントのリスト

	static Boolean LIST = true;//componentListを指定する時に使用
	static Boolean SUBLIST = false;//componentSubListを指定する時に使用
	
	//各コンポーネントをここに格納する.
    	public volatile Component monitor = null;
    	public volatile Component analysis = null;
    	public volatile Component plan = null;
    	public volatile Component execute = null;
    	public volatile Component monitorSub = null;
    	public volatile Component analysisSub = null;
    	public volatile Component planSub = null;
    	public volatile Component executeSub = null;
    	
	private volatile boolean loopStatus = false;//MAPE loopの状態を保持するフラグ
	
	private volatile ExecutorService threadManager;//マルチスレッド使用時のComponentManagerのオブジェクト格納場所
	
	private BufferedReader in = null;

	//Monitorをextendsして作成されたコンポーネントを本クラスのメンバーに追加する
	public ComponentManager addMonitor(Component c) {
		monitor = c;
		addComponent(c);
		return this;
	}
	
	//Analysisをextendsして作成されたコンポーネントを本クラスのメンバーに追加する
	public ComponentManager addAnalysis(Component c) {
		analysis = c;
		addComponent(c);
		return this;
	}
	
	//Planをextendsして作成されたコンポーネントを本クラスのメンバーに追加する
	public ComponentManager addPlan(Component c) {
		plan = c;
		addComponent(c);
		return this;
	}
	
	//Executeをextendsして作成されたクラスのコンポーネントを本クラスのメンバーに追加する
	public ComponentManager addExecute(Component c) {
		execute = c;
		addComponent(c);
		return this;
	}
	
	//Monitorをextendsして作成されたコンポーネントを本クラスのサブメンバーに追加する
	public ComponentManager addSubMonitor(Component c) {
		monitorSub = c;
		addSubComponent(c);
		return this;
	}
	
	//Analysisをextendsして作成されたコンポーネントを本クラスのサブメンバーに追加する
	public ComponentManager addSubAnalysis(Component c) {
		analysisSub = c;
		addSubComponent(c);
		return this;
	}
	
	//Planをextendsして作成されたコンポーネントを本クラスのサブメンバーに追加する
	public ComponentManager addSubPlan(Component c) {
		planSub = c;
		addSubComponent(c);
		return this;
	}
	
	//Executeをextendsして作成されたクラスのコンポーネントを本クラスのサブメンバーに追加する
	public ComponentManager addSubExecute(Component c) {
		executeSub = c;
		addSubComponent(c);
		return this;
	}
	
	//実行するコンポーネントをメンバーに格納する
	private void addComponent(Component c) {
		componentList.add(c);
	}
	
	//予備コンポーネントをサブメンバーに追加
	public void addSubComponent(Component c){
		componentSubList.add(c);
	}
	
	//M,A,P,Eの各コンポーネントが用意されたかをチェックする. Dataのフローを設定する.
	public ComponentManager build() {
		
		//各componentが正しく設定されているかを確認します
        if (monitor == null || analysis == null || plan == null || execute == null) {
            throw new NullPointerException();
        }

        //data交換を行うため,コンポーネントの接続をします。    
        monitor.connectPort(monitor.getPort("monitorP"), analysis.getPort("analysisR"));
        analysis.connectPort(analysis.getPort("analysisP"), plan.getPort("planR"));
        plan.connectPort(plan.getPort("planP"), execute.getPort("executeR"));
        
        return this;
    }
	
	//標準入力から文字列を受け付ける
	public void run() {
		
		in = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		
		System.out.print("\n>>> ");
		try {
			while ((line = in.readLine()) != null ) {
				
			    if( callFunction(new String(line)) == 3 ) {//stopコマンドが押され, loopが終わって本スレッド(本ターミナル)が不必要になった時
			    		break;
			    }
			    
			    System.out.print("\n>>> ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	//標準入力から入力された文字列を判別して,個々の処理を呼び出します
	private int callFunction(String inputCommand) {
	    
	    switch(inputCommand){
	        case "start":
	            startLoop();
	            return 1;
	        case "stop":
	            if(stopLoop() == 0) return 2;
	            return 3;
	        case "exchange":
	            exchangeComponent(); 
		        return 4;
	        case "exit":
	        	    exit();
	    	        return 5;
	        case "status":
	        		status();
	    	        return 6;
	        case "list":
	        		list();
	        		return 7;
	        default:
	            System.out.println("Command is wrong"); 
		        return 0;

	    }
		
	}
	
    //[start] startコマンドの処理
	private void startLoop() {
		
		if(getLoopCondition()) {
			System.out.println("*** MAPE loop is alreaday running. ***");
			return;
		}
		
		OnLoopCondition();
		
		threadManager = Executors.newFixedThreadPool(1);
		threadManager.execute(this);
		
		controlLoopOrder();//Loop開始
		
		threadManager.shutdown();//今実行しているスレッドが,終了したら破棄するよー！というだけ.強制終了ではない.そのため,runメソッドをできればすぐに終了させたい.
	}

	//[start] M,A,P,Eのループ処理
	private void controlLoopOrder(){

			System.out.println("*** MAPE loop is running now. ***"); 
			ExecutorService executorService = null;
			String time = null;
			
			//TODO repear.
//			if(plan.conf.getValue("time") != null) time = plan.conf.getValue("time");
			
			while(getLoopCondition()){
				
				executorService = Executors.newSingleThreadExecutor();
				try {
					executorService.submit(monitor).get();
					executorService.submit(analysis).get();
					executorService.submit(plan).get();
					executorService.submit(execute).get();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ExecutionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				executorService.shutdown();
				
				try {  
					executorService.shutdown(); 

					if(!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)){ //1000のところにループ時間などを入れて調整
					    // タイムアウトした場合、全てのスレッドを中断(interrupted)してスレッドプールを破棄する。 
						executorService.shutdownNow(); 
					} 
				} catch (InterruptedException e) { 
						// awaitTerminationスレッドがinterruptedした場合も、全てのスレッドを中断する 
						executorService.shutdownNow(); 
				} 
				
				//TIMERの実装
				try {
//					if(time != null) Thread.sleep(Long.parseLong(time, 1000));//ミリ秒単位
					Thread.sleep(2000);//ミリ秒単位
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		}
	
	//[stop] stopコマンドの処理
	private int stopLoop() {
		
		if(!getLoopCondition()) {
			System.out.println("*** MAPE loop is alreaday in pause. ***");
			return 0;
		}
		OffLoopCondition();//loopを止める
		System.out.println("*** MAPE loop is in pause now. ***"); 
		
		return 1;
	}
	
	//[exchange] exchangeコマンドの処理
	private void exchangeComponent() {
		
		in = new BufferedReader(new InputStreamReader(System.in));
		String targetName = null, subName = null;
		Component target = null, sub = null;
		
		//このtry-catchは入力内容の確認です.
		try {
			
			//****** target component *******
			System.out.print("input target component name >>> ");
			if((targetName = in.readLine()) != null ) {
				
				if(!checkComponentName(targetName, LIST)) {
					System.out.println("*** " + targetName + " don't exisit ***");
					return;
				}
				
				target = getComponent(targetName, LIST);
			}
			
			//****** sub component *******
			System.out.print("input sub component name >>> ");
			if ((subName = in.readLine()) != null ) {
				
				if(!checkComponentName(subName, SUBLIST)) {
					System.out.println("*** " + subName + " exisit, but type is invalid. ***");
					return;
				}

				sub = getComponent(subName, SUBLIST);
				
				if(checkType(target.getClass(), LIST) != checkType(sub.getClass(), SUBLIST)) {
					System.out.println("*** " + subName + " exisit, but class type is invalid. ***");
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		exchange(target, sub);
		System.out.println("*** Component "  + targetName + " is exchanged to " + subName + " ***"); 
	}
	
	//[exchange] componentの交換処理
	//TODO 既存の接続を切ってから,新しい接続を確立するのではなく,既存の接続を残して新しい接続をつないで.接続が確立したら切り替える仕様にしたい. 
	private void exchange(Component from, Component to) {
		
		//portの接続を解除します
		from.disConnectAllPort();//Portの接続を切る
		
		//targetをsubComponentListに移します
		removeComponent(from, LIST);
		addSubComponent(from);
		
		//subをComponentListに移します
		removeComponent(to, SUBLIST);
		addComponent(to);
		
		//portをつなぎます.
		switch (checkType(from, LIST)){
		  case 1:
			monitor = to;
			monitorSub = from;
			monitor.connectPort(monitor.getPort("monitorP"), analysis.getPort("analysisR"));
		    break;
		  case 2:
		    analysis = to;
		    analysisSub = from;
			monitor.connectPort(monitor.getPort("monitorP"), analysis.getPort("analysisR"));
		    analysis.connectPort(analysis.getPort("analysisP"), plan.getPort("planR"));
		    break;
		  case 3:
			plan = to;
			planSub = from;
		    analysis.connectPort(analysis.getPort("analysisP"), plan.getPort("planR"));
		    plan.connectPort(plan.getPort("planP"), execute.getPort("executeR"));	  
			break;
		  case 4:
			execute = to;
			executeSub = from;
	        plan.connectPort(plan.getPort("planP"), execute.getPort("executeR"));
			break;
		}		
	}
	
	//[exchange] コンポーネントがM,A,P,Eのどのタイプなのかをintで返す
	private int checkType(Object c, Boolean mode) {
		
		if( mode ) {
			if(c == monitor ) {
				return 1;
			}else if(c == analysis ){
				return 2;
			}else if(c == plan){
				return 3;
			}else if(c == execute){
				return 4;
			}
		}else{
			if(c == monitorSub ) {
				return 1;
			}else if(c == analysisSub ){
				return 2;
			}else if(c == planSub){
				return 3;
			}else if(c == executeSub){
				return 4;
			}
		}
		
		return 0;
		
	}

	//[exchange] 引数名のComponentがあるかどうかを確認する
	public boolean checkComponentName(String name, Boolean mode){

		Iterator<Component> i = null;
		 if( mode ) {
			 i = componentList.iterator();
		 } else {
			 i = componentSubList.iterator(); 
		 }
		 
		while(i.hasNext()){
			if(i.next().name.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	//[exchange] 引数名のComponentオブジェクトを取得する
	public Component getComponent(String name, Boolean mode){

		Component tempC = null;
		Iterator<Component> i = null;
		
		if( mode ) {
			i = componentList.iterator();
		} else {
			i = componentSubList.iterator();		
		}
		
		while(i.hasNext()){
			tempC = i.next();
			if(tempC.name.equals(name)){
				return tempC;
			}
		}
		
		return null;

	}
	
	//[exchange] 引数名のComponentをリストから削除する
	private void removeComponent(Component o, Boolean mode){ 

			Iterator<Component> i = null;
			if( mode ) {
				i = componentList.iterator();
			} else {
				i = componentSubList.iterator();
			}
			
			while(i.hasNext()){
				Component c = i.next();
				if(c == o){
					i.remove();
					break;
				}
			}

		}
		
	//[list] listコマンドの処理
	private void list() {
		
		System.out.println("Main components");
		System.out.println("Monitor  : " + monitor.name);
		System.out.println("Analysis : " + analysis.name);
		System.out.println("Plan     : " + plan.name);
		System.out.println("Execute  : " + execute.name);
		
		System.out.println();
		
		System.out.println("Sub components");
		if(monitorSub != null) System.out.println("Monitor  : " + monitorSub.name);
		if(analysisSub != null) System.out.println("Analysis : " + analysisSub.name);
		if(planSub != null) System.out.println("Plan     : " + planSub.name);
		if(executeSub != null) System.out.println("Execute  : " + executeSub.name);
	}
	
	//[exit] exitコマンドの処理
	private void exit() {
		
		//stopコマンドを呼び出す
		stopLoop();
		
		//標準入力を閉じる
		try {
			if(in!=null) in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("*** See you ***"); 
		System.exit(0);
	}
	
	//[status] statusコマンドの処理
	private void status() {
		if(loopStatus) {
			System.out.println("MAPE loop is runnning now.");
		}else {
			System.out.println("MAPE loop is in pause now.");
		}
	}
	
	//Loopの実行を許可します
	private void OnLoopCondition(){
		loopStatus = true ;
	}
	
	//Loopの実行を中断します
	private void OffLoopCondition(){
		loopStatus = false ;
	}
	
	//Loopの実行状況を取得します
	private boolean getLoopCondition() {
		return loopStatus;
	}

}
