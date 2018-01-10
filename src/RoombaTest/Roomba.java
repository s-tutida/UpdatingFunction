package RoombaTest;

import Test.TestMonitor;
import Tsuchida.ComponentManager;
 

public class Roomba extends ComponentManager{
	
	public static void main(String[] args) {
		Roomba rm = new Roomba();
		
		rm.addMonitor(new TestMonitor(rm, "Monitor"))
		   .addAnalysis(new RAnalyze(rm, "Analyze"))
		   .addPlan(new RPlan(rm, "Plan"))
		   .addExecute(new RExecute(rm, "Eexecute"))
           .build()
           .start();

	}


}