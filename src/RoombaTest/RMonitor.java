package RoombaTest;

import Tsuchida.ControlLoop;
import Tsuchida.Monitor;  
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class RMonitor extends Monitor{

	USBcamera uc = null;
	
	public RMonitor(ControlLoop cm, String name, USBcamera in_uc) {
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
		
		((VideoCapture)o).read(webcam_image);
		if( !(webcam_image.empty()) ) {
			
			//Detect red color in a picture
			ColorDetector dc = new ColorDetector();
			Mat webcam_image2 = dc.detect_blue(webcam_image);
	
			//Detect target's place
			ObjectDetector od = new ObjectDetector();
			return  od.detect_object(webcam_image2);
		}
		return null;
	}

}
