package RoombaTest;

public class CameraTest {

	public static void main(String[] args) {
		
		USBcamera uc = new USBcamera();
		uc.run();
		((VideoCapture)o).read(webcam_image);
		if( !(webcam_image.empty()) ) {
		//用意
		Mat webcam_image=new Mat();// 入力画像の取得
		Mat tmp = Imgcodecs.imread("tmp.jpg");//テンプレートの用意	
	
		Mat result = new Mat();

		Imgproc.matchTemplate(im, tmp, result, Imgproc.TM_CCOEFF_NORMED);	//テンプレートマッチング
		Imgproc.threshold(result, result, 0.8, 1.0, Imgproc.THRESH_TOZERO); 	// 検出結果から相関係数がしきい値以下の部分を削除
		
		// テンプレート画像の部分を元画像に赤色の矩形で囲む
		for (int i=0;i<result.rows();i++) {
			for (int j=0;j<result.cols();j++) {
				if (result.get(i, j)[0] > 0) {
					Imgproc.rectangle(im, new Point(j, i), new Point(j + tmp.cols(), i + tmp.rows()), new Scalar(0, 0, 255));
					System.out.println(i + " "+j);
				}
			}
		}

		Imgcodecs.imwrite("result.jpg", im);// 画像の出力
	}
}
