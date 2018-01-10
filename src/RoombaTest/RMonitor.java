package RoombaTest;

import Tsuchida.ComponentManager;
import Tsuchida.Monitor;  
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class RMonitor extends Monitor{

	USBcamera uc = null;
	
	public RMonitor(ComponentManager cm, String name, USBcamera in_uc) {
		super(cm, name);
		uc = in_uc;
	}

	@Override
	public Object getData() {
		return uc.run();
	}

	@Override
	public Object prepareData(Object o) {
		
		Mat webcam_image=new Mat();
//		System.out.println("DEBUG : Above if statement in prepareData");
//		System.out.println("DEBUG : Object parameter in prepareData "  + o);
		
		
		((VideoCapture)o).read(webcam_image);
		if( !(webcam_image.empty()) ) {
			
//			System.out.println("DEBUG : Enter if statement in prepareData");
			//Detect target color in a pic
			ColorDetector dc = new ColorDetector();
			Mat webcam_image2 = dc.detect_blue(webcam_image);
	
			//Detect target corn
			ObjectDetector od = new ObjectDetector();
			return  od.detect_object(webcam_image2);
		}
		return null;
	}

}
