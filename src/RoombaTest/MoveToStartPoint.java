package RoombaTest;

import Tsuchida.ControlLoop;


//Roomba.javaのnew functions版
//MAPE-K loopから呼び出せるようにした. 本機能を主として動かす場合はRoomba.javaを使用する.
public class MoveToStartPoint extends ControlLoop{
	
	public MoveToStartPoint() {
	}
	
	public Boolean runNewfunctions(){
		MoveToStartPoint rm = new MoveToStartPoint();
		
	    SerialCommunication sc = null;
        try {
	    	    sc = new SerialCommunication();
	        sc.connect("/dev/ttyUSB0");
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        
		USBcamera uc = new USBcamera();
		
		rm.addMonitor(new RMonitor(rm, "Monitor", uc))
		  .addAnalysis(new RAnalyze(rm, "Analyze"))
		  .addPlan(new RPlan(rm, "Plan"))
		  .addExecute(new RExecute(rm, "Eexecute",sc))
          .build()
          .start();
		
		while(rm!=null) {
		}
		
		return true;

	}


}
