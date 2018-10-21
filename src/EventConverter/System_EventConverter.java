package EventConverter;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import Tsuchida.*;  


public class System_EventConverter extends ComponentManager{
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		
		System_EventConverter se = new System_EventConverter();
			
		// xml parser
		Parser ps = new Parser();
		
		// Serial Controller
	    SerialCommunication sc = null;
        try {
	    	    sc = new SerialCommunication();
	        sc.connect("/dev/ttyUSB0");
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		//add MAPE-K components to system instance
		se.addKnowledge(new KnowledgeState(se, "Knowledge", ps))
		  .addMonitor(new MonitorEvent(se, "Monitor"))
		  .addAnalysis(new AnalyzeState(se, "Analyze"))
		  .addPlan(new PlanEvent(se, "Plan"))
		  .addExecute(new ExecuteEvent(se, "Eexecute", sc))
          .build()
          .start();
	}

}
