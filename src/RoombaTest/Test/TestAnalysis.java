package Test;

import Tsuchida.Analysis;
import Tsuchida.ComponentManager;
import Tsuchida.Configuration;

public class TestAnalysis extends Analysis {
	
	private int referenceValue = 0;

	public TestAnalysis(ComponentManager cm, String name, Configuration conf) {
		super(cm, name, conf);
		referenceValue =  Integer.parseInt(conf.getValue(name).trim());
	}

	@Override
	public Object analysis(Object o) {
		if(Integer.parseInt( (String) o ) > referenceValue) {
			return 1;
		}else {
			return 0;
		}
		
	}
}



