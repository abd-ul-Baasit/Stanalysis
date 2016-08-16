import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

@SuppressWarnings("serial")
public class Graph extends JFrame 
{  
	
   // DataSet Method 
   private static XYDataset createDataset(String stockName, int dayLimit, int monthLimit, int yearLimit, int windowSize)
   {
	   
	   DataFromDB DataSet = new DataFromDB();					//DB Object Created
	   						
	    int indexValue = 10000;
	    
	    String callDate = "getX";
	    String[] indexCountDate = DataSet.DB(callDate, stockName, indexValue);
	    
	    int counterRow = 0;
	    for (int i = 0; i < indexCountDate.length; i++){
	        if (indexCountDate[i] != null)
	            counterRow ++;
	    }
	    
	    String[] x_cordinates = DataSet.DB(callDate, stockName, counterRow);
	    
	    int[] days = new int[x_cordinates.length];
	    int[] months = new int[x_cordinates.length];
	    int[] years = new int[x_cordinates.length];
	   
	    for(int i = 0; i < x_cordinates.length; i++){

	    	days[i] = Character.getNumericValue(x_cordinates[i].charAt(0))*10 + Character.getNumericValue(x_cordinates[i].charAt(1));
	    	
	    	months[i] = Character.getNumericValue(x_cordinates[i].charAt(3))*10 + Character.getNumericValue(x_cordinates[i].charAt(4));
	    	
	    	years[i] = Character.getNumericValue(x_cordinates[i].charAt(6))*10 + Character.getNumericValue(x_cordinates[i].charAt(7)); 
	    	
	    }
	    
	    
	    //GET THE STOCK PRICE VALUES

	    String callPrice = "getY";						//DB Query Selection Method
	    
	    String[] y_cordinates = DataSet.DB(callPrice, stockName, counterRow);						//Stock Prices are stored
	  
//----------------------------------------------------------------------------
	    
	    double[] price = new double[y_cordinates.length];					
	    
	    for(int i = 0; i < y_cordinates.length; i++){						//Array is converted into Double for drawing Chart
	    	price[i] = Double.parseDouble(y_cordinates[i]);
	    }

//----------------------------------------------------------------------------
	    
	    //LIMITING FUNCTIONS FOR DATE
	    
	    int yearCounter = 0;					
	    for(int i = 0; i < years.length; i++){
	    	if(years[i] == yearLimit)
	    		break;
	    	else
	    		yearCounter++;
	    }
	    	
	    int monthCounter = 0;
	    for(int i = yearCounter; i < months.length; i++){
	    	if(months[i] == monthLimit)
	    		break;
	    	else
	    		monthCounter++;
	    }	
	    
	    int dayCounter = 0;
	    for(int i = (yearCounter + monthCounter); i < days.length; i++){
	    	if( days[i] == dayLimit)
	    		break;
	    	else
	    		dayCounter++;	    		
	    }

//-----------------------------------------------------------------------------
	    
	    int indexNumber = x_cordinates.length - (yearCounter + monthCounter + dayCounter);					//Row of Selected Date in the Database is calculated
	    
	    double[] indexNumberArray = new double[y_cordinates.length];
	    	for(int i = 0; i < indexNumber; i++)							//Number of Days to be plotted at X-Axis
	    		indexNumberArray[i] = i;

	    
	    double[] movingAverageArray = new double[(price.length - windowSize) + 1];
	    movingAverageArray = MovingAverage.movingAverage(price, windowSize);
	    

	  //To Accompany the Moving Average Graph till the end of Regular Graph ( otherwise it would be only plotted along the center of regular graph)	    
	    double[] aheadMovingAverage = new double[price.length];
	    
	    for(int i = windowSize; i < (price.length - windowSize); i++)
	    	aheadMovingAverage[i] = movingAverageArray[i - windowSize];
	    
//-------------------------------------------------------------------------------------	    
	    

	  //Regular Graph Plot  
      final XYSeries regular = new XYSeries( "Regular" );          
 
      for(int i = 1; i < indexNumber; i++){
      	  regular.add(indexNumberArray[i] , price[i]);  
    	}

      //Moving Graph Average Plot
      final XYSeries movingAverage = new XYSeries( "Moving Average" );          
      	
      for(int i = windowSize; i < indexNumber; i++){
    	  movingAverage.add(indexNumberArray[i], aheadMovingAverage[i]);
    	  if(aheadMovingAverage[i + 1] == 0){
    		  break;}
      }
      
      
      //Information of both Graphs is added to the JFreeChart method "dataset" 
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( regular );          
      dataset.addSeries( movingAverage );          

      return dataset;
   }

//-------------------------------------------------------------------------
   
   // Run Graph method to create both Charts
   public JFreeChart runGraph(String chartTitle, String xLabel, String yLabel, String stockName, int yearLimit, int monthLimit, int dayLimit, int windowSize) {
	   
	      JFreeChart xylineChart = ChartFactory.createXYLineChart(
	    	         chartTitle ,
	    	         xLabel ,
	    	         yLabel ,
	    	         createDataset(stockName, yearLimit, monthLimit, dayLimit, windowSize) ,
	    	         PlotOrientation.VERTICAL ,
	    	         true , true , false);
	      
	      ChartPanel chartPanel = new ChartPanel( xylineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 200,200 ) );
	      final XYPlot plot = xylineChart.getXYPlot( );
	      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	      renderer.setSeriesPaint( 0 , Color.DARK_GRAY );
	      renderer.setSeriesPaint( 1 , Color.RED );

	      renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
	      renderer.setSeriesStroke( 1 , new BasicStroke( 1.0f ) );
	      renderer.setBaseShapesVisible(false);
	      
	      plot.setRenderer( renderer ); 
	      
	      return xylineChart;
	  }
}
