package RoombaTest;

import Tsuchida.ComponentManager;
import Tsuchida.Monitor;
import org.opencv.core.Core;                                                                                            
import org.opencv.core.CvType;                                                                                          
import org.opencv.core.Mat;   

public class RMonitor extends Monitor{

	public RMonitor(ComponentManager cm, String name) {
		super(cm, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getData() {
		
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat im = Imgcodecs.imread("test.jpg");// 入力画像の取得
		Mat hsv = new Mat();
		Mat mask = new Mat();
		Mat im2 = new Mat();
		Imgproc.cvtColor(tempMat, tempMat, Imgproc.COLOR_RGBA2BGR);
		Imgproc.cvtColor(tempMat, tempMat, Imgproc.COLOR_BGR2HSV);
		Core.inRange(tempMat, new Scalar(0, 100, 30), new Scalar(5, 255, 255), tempMat);//指定した画素値の範囲内にある領域をマスク画像で取得する. ノイズ除去後の画像を保存する.
		Imgproc.cvtColor(tempMat, tempMat, Imgproc.COLOR_GRAY2BGRA);
	
		return null;
	}

	@Override
	public Object prepareData(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
