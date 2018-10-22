package SpotWaitTest;

public class SpotWait extends Thread{
	
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
		System.exit(0);
		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
