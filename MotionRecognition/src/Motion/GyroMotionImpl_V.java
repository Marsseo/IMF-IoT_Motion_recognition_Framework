
package Motion;

import Motion.*;
import java.util.List;
import java.util.Map;


public class GyroMotionImpl_V implements GyroMotionInterface{

	@Override
	public void gyroMotion(List<List> differenceResultList, Map<String, Integer> motionMap) {
		int nCount=0;
		boolean step1=true;
		boolean step2=false;
		boolean step3=false;
		boolean rightCheck = false;
		List<double[]> factorsInRange = differenceResultList.get(6);
		for (int j = 0; j < factorsInRange.size(); j++) {

			double[] count = factorsInRange.get(j);
			if(step1==true){
			if (count[1] > 0&&count[2]>0) {
						
						rightCheck = false;
						nCount++;
					}else if (count[1] > 0&&count[2]<0) {
					if (rightCheck) {
						step1 = false;
						step2 = true;
						System.out.println("V - Step1");
						nCount++;
						nCount++;
						
					}else{
					rightCheck = true;
					}
				} else {
					rightCheck = false;
				}
			}
			else if(step2==true){
				if (count[1] > 0&&count[2]<0) {
					step2=false;	
					step3=true;    
					System.out.println("Step2");
					nCount++;
					}
				
			}
			
		}
		
		
		if(step3==true){
		motionMap.put("v", nCount);
		}else{
			motionMap.put("v", 0);
		}
	}

}
