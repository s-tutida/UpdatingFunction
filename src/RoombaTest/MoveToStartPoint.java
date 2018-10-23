package RoombaTest;

import EventConverter.KnowledgeState;
import Tsuchida.ControlLoop;

//Roomba.javaのnew functions版
//MAPE-K loopから呼び出せるようにした. 本機能を主として動かす場合はRoomba.javaを使用する.
public class MoveToStartPoint extends ControlLoop{
	
	private SerialCommunication sc = null;
	
	public MoveToStartPoint(SerialCommunication in_sc, KnowledgeState knowledge) {
		this.sc = in_sc;
		super.addKnowledge(knowledge);
	}
	
	public void start(){
        
		USBcamera uc = new USBcamera();
		
		this.addMonitor(new RMonitor(this, "Monitor", uc))
		  .addAnalysis(new RAnalyze(this, "Analyze"))
		  .addPlan(new RPlan(this, "Plan"))
		  .addExecute(new RExecute(this, "Execute", this.sc))
          .build()
          .start();

	}

}
