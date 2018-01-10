package RoombaTest;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;


public class USBcamera {

	public USBcamera(){
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


