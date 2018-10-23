package Tsuchida;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import Tsuda.Component;

public abstract class ControlLoop extends Thread{
	
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
    	public volatile Knowledge knowledge = null;
    	
	private volatile boolean loopStatus = false;//MAPE loopの状態を保持するフラグ
	
	private volatile ExecutorService threadManager;//マルチスレッド使用時のComponentManagerのオブジェクト格納場所
	
	private BufferedReader in = null;
	
	private Boolean status = false;

	//Monitorをextendsして作成されたコンポーネントを本クラスのメンバーに追加する
	public ControlLoop addMonitor(Component c) {
		monitor = c;
		addComponent(c);
		return this;
	}
	
	//Analysisをextendsして作成されたコンポーネントを本クラスのメンバーに追加する
	public ControlLoop addAnalysis(Component c) {
		analysis = c;
		addComponent(c);
		return this;
	}
	
	//Planをextendsして作成されたコンポーネントを本クラスのメンバーに追加する
	public ControlLoop addPlan(Component c) {
		plan = c;
		addComponent(c);
		return this;
	}
	
	//Executeをextendsして作成されたクラスのコンポーネントを本クラスのメンバーに追加する
	public ControlLoop addExecute(Component c) {
		execute = c;
		addComponent(c);
		return this;
	}
	
	//Monitorをextendsして作成されたコンポーネントを本クラスのサブメンバーに追加する
	public ControlLoop addSubMonitor(Component c) {
		monitorSub = c;
		addSubComponent(c);
		return this;
	}
	
	//Analysisをextendsして作成されたコンポーネントを本クラスのサブメンバーに追加する
	public ControlLoop addSubAnalysis(Component c) {
		analysisSub = c;
		addSubComponent(c);
		return this;
	}
	
	//Planをextendsして作成されたコンポーネントを本クラスのサブメンバーに追加する
	public ControlLoop addSubPlan(Component c) {
		planSub = c;
		addSubComponent(c);
		return this;
	}
	
	//Executeをextendsして作成されたクラスのコンポーネントを本クラスのサブメンバーに追加する
	public ControlLoop addSubExecute(Component c) {
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
	public ControlLoop build() {
		
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
	
	public ControlLoop addKnowledge(Knowledge c) {
		knowledge = c;
		return this;
	}
	
	public Knowledge getKnowledge() {
		return this.knowledge;
	}
	
	//標準入力から文字列を受け付ける
	public void run() {
		startLoop();
	}

	
    //[start] startコマンドの処理
	private void startLoop() {
		
		if(getLoopCondition()) {
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

			System.out.println("***  Start to run new functions. ***"); 
			ExecutorService executorService = null;
			String time = null;
			
			//TODO repeat.
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
					Thread.sleep(3000);//ミリ秒単位
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		}
	
	//[stop] stopコマンドの処理
	private int stopLoop() {
		
		if(!getLoopCondition()) {
			return 0;
		}
		OffLoopCondition();//loopを止める
		
		return 1;
	}
	
	//[exit] exitコマンドの処理
	public void exit() {
		
		//stopコマンドを呼び出す
		stopLoop();
		
		//標準入力を閉じる
		try {
			if(in!=null) in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("*** Stop new functions  ***"); 
		this.status = true;
	}
	
	public Boolean getEndEvent() {
		System.out.println(this.status);
		return this.status;
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
