package SpotWaitTest;

public class SpotWait extends Thread{
	
	public static void main(String[] args) {
		
		SpotWait sw = new SpotWait();
		sw.start();
	}
	
	public void run(){
		System.out.println("***  Start to run new functions. ***"); 
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("   Send tm(3s) event to EventConverter. ***"); 
		System.out.println("*** Stop new functions  ***"); 
	}
}
