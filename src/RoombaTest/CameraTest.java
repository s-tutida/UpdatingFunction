package RoombaTest;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class CameraTest {

	public static void main(String[] args) {

		CameraTest c = new CameraTest();	
		while(true) {
			c.get_image();
		}
		
	}
	
	
	public Object get_image() {
		
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
	}
	
	public String detect_object(Mat img){

		Mat A = img;
		Size sizeA = A.size();
		int count1=0,count2=0,count3=0,count4=0,count5=0;
		for (int i = 0; i < sizeA.height; i++){
		    for (int j = 0; j < sizeA.width; j++) {
		    	double[] data = new double[3];
		        data = A.get(i, j);
		        if(data[0]!= 0){
					if(i <  (sizeA.height)/2 && j <  (sizeA.width)/2){
						count1++;
					}else if(i <  (sizeA.height)/2 && j >=  (sizeA.width)/2){
						count2++;
					}else if(i >=  (sizeA.height)/2 && j >=  (sizeA.width)/2){
						count3++;
					}else if(i >=  (sizeA.height)/2 && j <  (sizeA.width)/2){
						count4++;
					}
		        }
		    }
		}
		
		
		int values[] = {count1, count2, count3, count4};
        int max = values[0];
 
        for (int index = 1; index < values.length; index ++) {
            max = Math.max(max, values[index]);
        }

		int answer = 0;
		if(max==count1){
			answer = 1;
		}else if (max==count2){
			answer = 2;
		}else if (max==count3){
			answer = 3;
		}else{
			answer = 4;
		}
		
		int sum = count1 + count2 + count3 + count4;
		System.out.println("count1 : " + count1);
		System.out.println("count2 : " + count2);
		System.out.println("count3 : " + count3);
		System.out.println("count4 : " + count4);
		System.out.println("sum : " + sum);
		if(sum > 2650) {
			answer = -1;//終了
		}
		return String.valueOf(answer);
	}

}
