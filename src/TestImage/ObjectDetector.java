package TestImage;

import org.opencv.core.Mat;
import org.opencv.core.Size;


public class ObjectDetector {

	public ObjectDetector(){

	}


	public Mat detect_object(Mat img){

		Mat A = img;
		Mat C = A.clone();
		Size sizeA = A.size();
		int count1=0,count2=0,count3=0,count4=0;
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


		int answer = 0;
		int max = 0;
		if(Math.max(count4,Math.max(count3,Math.max(count1,count2)))==count1){
			answer = 1;
			max = count1;
		}else if (Math.max(count4,Math.max(count3,Math.max(count1,count2)))==count2){
			answer = 2;
			max = count2;
		}else if (Math.max(count4,Math.max(count3,Math.max(count1,count2)))==count3){
			answer = 3;
			max = count3;
		}else if (Math.max(count4,Math.max(count3,Math.max(count1,count2)))==count4){
			answer = 4;
			max = count4;
		}

		System.out.println(answer + ","  +max);

		//debug用　実際のAnalyzeはanswerと数値を返せばよい.
		for (int i = 0; i < sizeA.height; i++){
		    for (int j = 0; j < sizeA.width; j++) {

		    	if(answer == 1 && (i <  (sizeA.height)/2 && j <  (sizeA.width)/2)){
		    		C.put(i, j, 255.0);
		    	}else if(answer == 2 && (i <  (sizeA.height)/2 && j >=  (sizeA.width)/2)){
		    		C.put(i, j, 255.0);
		    	}else if(answer == 3 && (i >=  (sizeA.height)/2 && j >=  (sizeA.width)/2)){
		    		C.put(i, j, 255.0);
		    	}else if(answer == 4 && (i >=  (sizeA.height)/2 && j <  (sizeA.width)/2)){
		    		C.put(i, j, 255.0);
		    	}else{
		    		C.put(i, j, 0);
		    	}

		    }
		}

		return C;
	}

}

