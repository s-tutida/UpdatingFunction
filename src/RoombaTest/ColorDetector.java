package RoombaTest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ColorDetector {

	public ColorDetector(){
	}

	public Mat detect_blue(Mat img){

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat im = img;// 入力画像の取得
		Mat hsv = new Mat();
		
		Imgproc.cvtColor(im, hsv, Imgproc.COLOR_RGBA2BGR);//From RGBA to BGR
		Imgproc.cvtColor(im, hsv, Imgproc.COLOR_BGR2HSV);//From BGR to HSV. HSVは色抽出に向いている
	    Imgproc.Canny(im2,im2,400,500,5,true);//Edge検出
		Core.inRange(hsv, new Scalar(0, 100, 30), new Scalar(5, 255, 255), im2);//指定した画素値の範囲内にある領域をマスク画像で取得する. ノイズ除去後の画像を保存する.
	    Imgproc.Canny(im2,im2,400,800,5,true);//Edge検出
//		Highgui.imwrite("output.jpg", im2);//debug用 画像ファイルとして出力する.

		return im2;
	}

}
