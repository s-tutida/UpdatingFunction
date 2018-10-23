package RoombaTest;

import EventConverter.KnowledgeState;
import Tsuchida.ControlLoop;

//Roomba.javaのnew functions版
//MAPE-K loopから呼び出せるようにした. 本機能を主として動かす場合はRoomba.javaを使用する.
public class MoveToStartPoint extends ControlLoop{
	
	public MoveToStartPoint rm = null;
	private SerialCommunication sc = null;
	
	public MoveToStartPoint(SerialCommunication in_sc, KnowledgeState knowledge) {
		this.sc = in_sc;
		super.setKnowledge(this.knowledge);
		
		runNewfunctions();
	}
	
	public void runNewfunctions(){
		
//		rm = new MoveToStartPoint(this.sc, this.knowledge);
        
		USBcamera uc = new USBcamera();
		
		this.addMonitor(new RMonitor(this, "Monitor", uc))
		  .addAnalysis(new RAnalyze(this, "Analyze"))
		  .addPlan(new RPlan(this, "Plan"))
		  .addExecute(new RExecute(this, "Execute", this.sc))
          .build()
          .start();

	}
	
//	public Boolean getEndEvent() {
//		if(this.rm==null) return false;
//		return this.rm.getStatus();
//	}
//	
//	public Boolean getStatus() {
//		return super.getEndEvent();
//	}
//	
	


}
