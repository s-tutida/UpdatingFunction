package SpotWaitTest;

public class SpotWait extends Thread{
	
	public Boolean status = false;
	public SpotWait() {
	}
	
	public void run(){
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.status = true;
	}
	
	public Boolean getEndEvent() {
		return this.status;
	}
}
