package JavaRoombaTest;

public class Main{

	public static void main(String args[]){
           
            SampleSerialCommunication sc = null;
            try {
	        sc = new SampleSerialCommunication();
	        sc.connect("/dev/ttyUSB0");
		
             	sc.write(140, 0, 9, 57, 30, 57, 30, 57, 30, 53, 20, 60, 10, 57, 30, 53, 20, 60, 10, 57, 45);
                sc.write(141, 0);;		
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        
	}

}
