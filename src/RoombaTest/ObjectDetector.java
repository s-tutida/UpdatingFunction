package RoombaTest;


import org.opencv.core.Mat;
import org.opencv.core.Size;


public class ObjectDetector {

	public ObjectDetector(){
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
		System.out.println(sum);
		if(sum > 2500) {
			answer = -1;//終了
		}
		return String.valueOf(answer);
	}

}

