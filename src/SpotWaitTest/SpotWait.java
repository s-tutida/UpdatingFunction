package SpotWaitTest;

import EventConverter.KnowledgeState;

public class SpotWait extends Thread{
	
	public Boolean status = false;
	public KnowledgeState knowledge = null;
	
	public SpotWait(KnowledgeState knowledge) {
		this.knowledge = knowledge;
	}
	
	public void run(){
		
		try {
			Thread.sleep(4000);//2秒待機
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		knowledge.setEvent("tm(2s)");
	}
}
