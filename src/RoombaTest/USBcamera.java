package RoombaTest;


import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class USBcamera {

	public USBcamera(){
	}

	public Mat run(){

		// Native Libraryのロード
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//宣言
		Mat webcam_image=new Mat();
		VideoCapture capture =new VideoCapture(1);

		if( capture.isOpened() ){
			return capture;
		}else {
			System.out.println("Cannot open video Capture");
		}

		return null;
	}
}


