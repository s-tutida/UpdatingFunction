package RoombaTest;

import Test.TestMonitor;
import Tsuchida.ComponentManager;
 

public class Roomba extends ComponentManager{
	
	public static void main(String[] args) {
		Roomba rm = new Roomba();
		
	    SerialCommunication sc = null;
        try {
	    	    sc = new SerialCommunication();
	        sc.connect("/dev/ttyUSB0");
			sc.send_command(1);//initial setup for roomba
			sc.send_command(4);//initial setup for roomba
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