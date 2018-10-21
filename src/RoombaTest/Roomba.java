package RoombaTest;

import Tsuchida.ComponentManager;
import WebApplicationTest.TestMonitor;
 

public class Roomba extends ComponentManager{
	
	public static void main(String[] args) {
		Roomba rm = new Roomba();
		
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

	}


}
