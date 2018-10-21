package WebApplicationTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import Tsuchida.ComponentManager;
import Tsuchida.Configuration;
import Tsuchida.Monitor;

public class TestMonitor extends Monitor{

	public TestMonitor(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object getData() {

  	  	String line = null;
		
		Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
	    String command = "grep " + format.format(date) + " /var/log/httpd/MAPE-access_log | awk '{print $4}' | cut -b 2-21 | sort | uniq -c";
	    
	    try {
	    	
	    	    Process p = Runtime.getRuntime().exec(command);
	        InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
		        	break;
	  	  	}
	        is.close();
	    } catch (IOException ex) {
	    }
		return line;
	}


	public Object prepareData(Object o) {
		if(o == null) {
			return "0" ;
		}
		return String.valueOf(o).substring(String.valueOf(o).indexOf(":") + 1);
	}
}

