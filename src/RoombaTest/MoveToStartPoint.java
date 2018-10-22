package RoombaTest;

import Tsuchida.ControlLoop;


//Roomba.javaのnew functions版
//MAPE-K loopから呼び出せるようにした. 本機能を主として動かす場合はRoomba.javaを使用する.
public class MoveToStartPoint extends ControlLoop{
	
	
	private MoveToStartPoint rm = null;
	private SerialCommunication sc = null;
	public MoveToStartPoint(SerialCommunication in_sc) {
		
		this.sc = in_sc;
		runNewfunctions();
	}
	
	public void runNewfunctions(){
		
		this.rm = new MoveToStartPoint(this.sc);
        
		USBcamera uc = new USBcamera();
		
		rm.addMonitor(new RoombaTest.RMonitor(rm, "Monitor", uc))
		  .addAnalysis(new RoombaTest.RAnalyze(rm, "Analyze"))
		  .addPlan(new RoombaTest.RPlan(rm, "Plan"))
		  .addExecute(new RoombaTest.RExecute(rm, "Execute",this.sc))
          .build()
          .start();

	}
	
	public Boolean getEndEvent() {
		return rm.getEndEvent();
	}
	
	


}
