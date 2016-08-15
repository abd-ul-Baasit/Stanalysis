public class MovingAverage{

//Moving Average Calculating Function
	public static double[] movingAverage(double[] Integer, int WindowSize){
		
		double[] SubArray = new double[WindowSize];			//SubArray is used for Averaging a Single Window
		double[] FinalArray = new double[(Integer.length - WindowSize) + 1];			//Final Array is the one which will be returned
		
		for(int i = 0; i < FinalArray.length; i++){
				for(int j = 0; j < WindowSize; j++){
					SubArray[j] = Integer[i+j];
				}				
			
			FinalArray[i] = (Average(SubArray, WindowSize));			//A single Window is averaged out and stored into the Final Array
		}
		return FinalArray;
	}
	
	
	//Average Calculating Function
	public static double Average(double[] Array, int Total){
		
		double sum = 0;
		  
		for (int i=0; i < Array.length; i++)
	            sum = sum + Array[i];
	   
		return sum / Total;
	}
}