package RoombaTest;

import Tsuchida.ComponentManager;
import Tsuchida.Monitor;  
import org.opencv.core.Mat;

public class RMonitor extends Monitor{

	public RMonitor(ComponentManager cm, String name) {
		super(cm, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getData() {
		
		USBcamera uc = new USBcamera();
	
		return uc.run();
	}

	@Override
	public Object prepareData(Object o) {
		
		System.out.println("DEBUG : Above if statement in prepareData");
		System.out.println("DEBUG : Object parameter in prepareData "  + o);
		
		if( !(Mat)o.empty() ) {
			
			System.out.println("DEBUG : Enter if statement in prepareData");
			//Detect target color in a pic
			ColorDetector dc = new ColorDetector();
			Mat webcam_image2 = dc.detect_blue((Mat)o);
	
			//Detect target corn
			ObjectDetector od = new ObjectDetector();
			return  od.detect_object(webcam_image2);
		}
		return null;
	}

}
