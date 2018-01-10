package RoombaTest;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;


public class USBcamera {

	VideoCapture capture = null;
	public USBcamera(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		capture =new VideoCapture(1);
	}

	public VideoCapture run(){

		if( capture.isOpened() ){
			return capture;
		}else {
			System.out.println("Cannot open video Capture");
		}

		return null;
	}
}


