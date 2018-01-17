package RoombaTest;

public class CameraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	Mat tmpl = Highgui.imread(tmplFile.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);
	
	Mat result = new Mat(img.rows() - tmpl.rows() + 1, img.cols() - tmpl.cols() + 1, CvType.CV_32FC1);
	Imgproc.threshold(result, result, 0.8, 1.0, Imgproc.THRESH_TOZERO);
	
	for (int i=0;i<result.rows();i++) {
		  for (int j=0;j<result.cols();j++) {
		    if (result.get(i, j)[0] > 0) {
		      Core.rectangle(img, new Point(j, i), new Point(j + tmpl.cols(), i + tmpl.rows()), new Scalar(0, 0, 255));
		    }
		  }
	}
}
