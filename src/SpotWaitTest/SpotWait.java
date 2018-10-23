package SpotWaitTest;

public class SpotWait extends Thread{
	
	public Boolean status = false;
	public SpotWait() {
		this.start();	
	}
	
	public void run(){
//		System.out.println("***  Start to run new functions. ***"); 
		try {
			Thread.sleep(3000);
			this.status=true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.println("***  Send tm(2s) event to EventConverter. ***"); 
//		System.out.println("***  Stop new functions  ***"); 
//		this.status=true;
	}
	
	public Boolean getEndEvent() {
		return this.status;
	}
}
