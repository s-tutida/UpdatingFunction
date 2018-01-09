package Test;

import java.io.IOException;

import Tsuchida.ComponentManager;
import Tsuchida.Configuration;

public class PictureManager extends ComponentManager {

	public static void main(String[] args) throws IOException{
		
		PictureManager pm = new PictureManager();
		Configuration conf = new Configuration("test.properties");
		
		pm.addMonitor(new TestMonitor(pm, "Monitor"))
		   .addAnalysis(new TestAnalysis(pm, "Analysis", conf))
		   .addPlan(new TestPlan(pm, "P-Quality", conf))
		   .addExecute(new TestExecute(pm, "Eexecute"))
		   .addSubPlan(new TestPlan(pm, "P-Format", conf))
           .build()
           .start();
	}
}
