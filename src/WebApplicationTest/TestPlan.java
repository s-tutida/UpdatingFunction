package WebApplicationTest;

import Tsuchida.ComponentManager;
import Tsuchida.Plan;
import Tsuchida.Configuration;

public class TestPlan extends Plan{

	private Configuration conf = null;
	
	public TestPlan(ComponentManager cm, String name, Configuration conf) {
		super(cm, name, conf);
		this.conf = conf;
	}

	@Override
	public Object plan(Object o) {
		if((Integer) o == 1) {
			return conf.getValue(name + "-On");
		}else {
			return conf.getValue(name + "-Off");
		}
	}
}








