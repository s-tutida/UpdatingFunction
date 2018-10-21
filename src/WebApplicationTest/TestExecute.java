package WebApplicationTest;

import java.io.IOException;

import Tsuchida.ComponentManager;
import Tsuchida.Execute;
import Tsuchida.Configuration;

public class TestExecute extends Execute{

	public TestExecute(ComponentManager cm, String name) {
		super(cm, name);
	}

	@Override
	public Object execute(Object o) {

		Runtime runtime = Runtime.getRuntime();
		Process p = null;
		
		try {
			p = runtime.exec((String) o);
			p.waitFor();
			
	    } catch (InterruptedException e) {
			e.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
	    }
		
		return null;
	}
}
