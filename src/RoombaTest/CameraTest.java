package RoombaTest;

import org.opencv.imgcodecs.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.videoio.VideoCapture;

public class CameraTest {

	public static void main(String[] args) {
		
                VideoCapture o = new VideoCapture(0);
                Mat img = new Mat();
	        
		if(o.isOpened()){
              		 while(true){
                                o.read(img);
	       			if( !(img.empty()) ) {

					//用意
					Mat tmp = Imgcodecs.imread("tmp.jpg");//テンプレートの用意	
					Mat result = new Mat(img.rows() - tmp.rows() + 1, img.cols() - tmp.cols() + 1,CvType.CV_32FC1);//結果格納用

					Imgproc.matchTemplate(img, tmp, result, Imgproc.TM_CCOEFF_NORMED);	//テンプレートマッチング
					Imgproc.threshold(result, result, 0.8, 1.0, Imgproc.THRESH_TOZERO); 	// 検出結果から相関係数がしきい値以下の部分を削除
		
					// テンプレート画像の部分を元画像に赤色の矩形で囲む
					for (int i=0;i<result.rows();i++) {
						for (int j=0;j<result.cols();j++) {
							if (result.get(i, j)[0] > 0) {
								Imgproc.rectangle(img, new Point(j, i), new Point(j + tmp.cols(), i + tmp.rows()), new Scalar(0, 0, 255));
								System.out.println(i + " "+j);
							}
						}
					}	

					Imgcodecs.imwrite("result.jpg", img);// 画像の出力

               			}
	        	}
		}
	}
}
