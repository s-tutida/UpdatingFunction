package SpotWaitTest;

public class SpotWait extends Thread{
	
	public Boolean end = false;
	public SpotWait() {
		this.start();	
	}
	
	public void run(){
		System.out.println("***  Start to run new functions. ***"); 
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("   Send tm(2s) event to EventConverter. ***"); 
		System.out.println("*** Stop new functions  ***"); 
		this.end=true;
	}
	
	public Boolean getEndEvent() {
		return this.end;
	}
}
