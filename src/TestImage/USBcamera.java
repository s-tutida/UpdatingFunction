package TestImage;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class USBcamera extends JPanel{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	// Create a constructor method
	public USBcamera(){
		super();
	}

	private BufferedImage getimage(){
		return image;
	}

	private void setimage(BufferedImage newimage){
		image=newimage;
		return;
	}

	/**
	 * Converts/writes a Mat into a BufferedImage.
	 *
	 * @param matrix Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	public static BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int)matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// bgr to rgb
			byte b;
			for(int i=0; i<data.length; i=i+3) {
				b = data[i];
				data[i] = data[i+2];
				data[i+2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}

	public void paintComponent(Graphics g){
		BufferedImage temp=getimage();
		if(temp!=null){
			g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);
		}
	}

	public static void main(String arg[]){

		// Native Libraryのロード
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

//		//debug用フレーム
//		JFrame frame = new JFrame("USBカメラ画像");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(400,400);
//		USBcamera panel = new USBcamera();
//		frame.setContentPane(panel);
//		frame.setVisible(true);
//
//		JFrame frame2 = new JFrame("画像抽出後");
//		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame2.setSize(400,400);
//		USBcamera panel2 = new USBcamera();
//		frame2.setContentPane(panel2);
//		frame2.setVisible(true);
//
//		JFrame frame3 = new JFrame("物体抽出後");
//		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame3.setSize(400,400);
//		USBcamera panel3 = new USBcamera();
//		frame3.setContentPane(panel3);
//		frame3.setVisible(true);


		//宣言
		Mat webcam_image=new Mat();
//		BufferedImage temp,temp2,temp3;
		VideoCapture capture =new VideoCapture(1);

		if( capture.isOpened())
		{

			while( true )
			{

				capture.read(webcam_image);
				if( !webcam_image.empty() )
				{

					//画像から色を抽出する.
					ColorDetector dc = new ColorDetector();
					Mat webcam_image2 = dc.detect_blue(webcam_image);

					//画像から物体の座標を抽出する.
					ObjectDetector od = new ObjectDetector();
					Mat webcam_image3 = od.detect_object(webcam_image2);



//					//debug用にbufferに変換
//					Imgproc.resize(webcam_image, webcam_image, new Size(webcam_image.size().width*0.3,webcam_image.size().height*0.3));
//					frame.setSize(webcam_image.width()+40,webcam_image.height()+60);
//					temp = matToBufferedImage(webcam_image);
//					Imgproc.resize(webcam_image2, webcam_image2, new Size(webcam_image2.size().width*0.3,webcam_image2.size().height*0.3));
//					frame2.setSize(webcam_image2.width()+40,webcam_image2.height()+60);
//					temp2 = matToBufferedImage(webcam_image2);
//					Imgproc.resize(webcam_image3, webcam_image3, new Size(webcam_image3.size().width*0.3,webcam_image3.size().height*0.3));
//					frame3.setSize(webcam_image3.width()+40,webcam_image3.height()+60);
//					temp3 = matToBufferedImage(webcam_image3);
//
//					//debug用 画像をpanelに設定
//					panel.setimage(temp);
//					panel.repaint();
//					panel2.setimage(temp2);
//					panel2.repaint();
//					panel3.setimage(temp3);
//					panel3.repaint();

				}
				else
				{
					System.out.println(" --(!) No captured frame -- ");
				}
			}
		}
		return;
	}
}


