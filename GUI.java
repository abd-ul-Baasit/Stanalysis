import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;

import net.miginfocom.swing.MigLayout;

public class GUI {

	Graph chart = new Graph();							//Graph Object Created
	DataFromDB Data = new DataFromDB();					//Database Object Created
	
    JComboBox<String> Symbols; 
    JComboBox<String> Dates;
    String StockSelected = "AABS";						//By Default, the first StockName in the List is set
    
    int yearLimit = 2;						//Default Date is set for the First(Default) Value on the Drop-Down Menu Dates					
    int monthLimit = 6;
    int dayLimit = 4;
    
    int windowSize = 1;						//Default Window Size set to 1
    
    
    //Basic GUI Method
    public void BasicGUI() {					

    JFrame frame = new JFrame("Stanalysis Chart");
    JPanel panel = new JPanel(new MigLayout());
      
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize( 1200 , 600 );
    frame.setLocation( 50 , 50 );
    frame.add(panel);
    
    //Separate Panels are made for each Couple of Components
      JLabel generalGraph = new JLabel("SELCTION PANEL");
      panel.add(generalGraph, "wrap");
    
	  JPanel NamePanel = new JPanel(new FlowLayout());
	  panel.add(NamePanel, "wrap");
	  
	  JPanel DatePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 11, 0));
	  panel.add(DatePanel, "wrap");
	  
	  JPanel WindowPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 9));
	  panel.add(WindowPanel, "wrap");
	  
	  JPanel ButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
	  panel.add(ButtonPanel, "wrap");
    
//----------------------------------------------------------------------------


    //Drop-Down Menu for Stocks' Names
    JLabel labelName = new JLabel("Stock Name");
    
    String callName = "name";
    String formality = null;
    
    String[] StockName = Data.DB(callName, formality, IndexCounter(StockSelected, callName));

    for(int i = 0; i < StockName.length; i++)
    	StockName[i] = StockName[i].toUpperCase();

    Symbols = new JComboBox<String>(StockName);
    Symbols.setVisible(true);
    
//-------------------------------------------------------------------------------------------    

    //Item Listener added in Symbols
    Symbols.addItemListener(
    		new ItemListener(){
		    	public void itemStateChanged(ItemEvent e){
		    		if(e.getStateChange() == ItemEvent.SELECTED)
		    			StockSelected =  (String) Symbols.getSelectedItem();		//Universal Variable "StockSelected" is updated
					  

				  	//DATES' Drop-Down Menu is updated ( The First one is created lower in the code )
		    		Dates.setVisible(false);
		    		
				  	String callDate = "date";

				    String[] allDates = Data.DB(callDate, StockSelected, IndexCounter(StockSelected, callDate));		//This call is made to set the Drop-Down Size
				    Dates = new JComboBox<String>(allDates);
				    DatePanel.add(Dates);

				    //Date is converted from String to Integer and Divided respectively
		    		dayLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(0))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(1));
		    		monthLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(3))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(4));
		    		yearLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(6))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(7));

//-------------------------------------------------------------------------------------------    

		    		//Item Listener added in Dates
		    	    Dates.addItemListener( new ItemListener(){
		    	    	public void itemStateChanged( ItemEvent e ){
		    	    		
		    	    		dayLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(0))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(1));
		    	    		monthLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(3))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(4));
		    	    		yearLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(6))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(7));		    	    		
		    	    	}
		    	    });

//------------------------------------------------------------------------------------------- //ItemListener of Dates is finished   
		    	  }
		    	});
    
 //-------------------------------------------------------------------------------------------//ItemListener of Symbols is finished 


    //First Appearance of DATES Drop-Down Menu
    JLabel labelDate = new JLabel("Date From");
    labelDate.setVisible(true);
    
    
    String callMethod = "date";
    String[] allDates = Data.DB(callMethod, StockSelected, IndexCounter(StockSelected, callMethod));
    Dates = new JComboBox<String>(allDates);
    Dates.setVisible(true);
    
//-------------------------------------------------------------------------------------------    

    //Item Listener for First Appearance of DATES Drop-Down Menu is added
    Dates.addItemListener( new ItemListener(){
    	public void itemStateChanged( ItemEvent e ){
    		
    		dayLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(0))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(1));
    		monthLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(3))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(4));
    		yearLimit = Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(6))*10 + Character.getNumericValue(((String) Dates.getSelectedItem()).charAt(7));
    	}
    });
    
//-------------------------------------------------------------------------------------------    

    
    //MOVING AVERAGE INPUT COMPONENTS ARE CREATED
    
		  JLabel MALabel = new JLabel("Window Size");
		  MALabel.setVisible(true);
		  
//	  NumberFormat format = NumberFormat.getInstance();
//	  NumberFormatter formatter = new NumberFormatter(format);
//	  formatter.setValueClass(Integer.class);
//	  formatter.setMinimum(0);
//	  formatter.setMaximum(2);
	  JFormattedTextField MAWindow = new JFormattedTextField();
	  MAWindow.setColumns(5);
	  
	  MAWindow.setVisible(true);    


//-------------------------------------------------------------------------------------------    
	  
	final ChartPanel chartPanel = new ChartPanel(null);
	  
	//"GENERATE" BUTTON IS CREATED
	  JButton Select = new JButton("GENERATE");
	  
	  Select.addActionListener(new ActionListener(){	  
		  public void actionPerformed(ActionEvent e){
			  
			  try {
				    windowSize = Integer.parseInt(MAWindow.getText());
				}
				catch(NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(frame, "Kindly Enter a Number for the Moving Average Window");
				}

			  try {
				chartPanel.setChart(new Graph().runGraph(StockSelected, "DAYS (From Selected Date to Last Available Date)", "PRICE", StockSelected, dayLimit, monthLimit, yearLimit, windowSize));
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(frame, "The Selected Window Size is Greater that the Number of Dates Available");
			}
			    panel.add(chartPanel);    

		  }
	  }  
			  );
	  
//-------------------------------------------------------------------------------------------    
	  
	  // ALL THE COMPONENTS ARE ADDED AT ONE PLACE TO MAINTAIN SEQUENCE
	    NamePanel.add(labelName);
	    NamePanel.add(Symbols);
	    DatePanel.add(labelDate);
	    DatePanel.add(Dates);
	    WindowPanel.add(MALabel);
	    WindowPanel.add(MAWindow);
	    ButtonPanel.add(Select);

	    panel.add(chartPanel);    

//-------------------------------------------------------------------------------------------    

 
    frame.pack();
    frame.setVisible(true);

	}
    
//-------------------------------------------------------------------------------------------    
   
    //IndexCounter Method is used to get Row Count from SQL
    
    public int IndexCounter(String stockName, String callMethod){
        
	    int indexValue = 10000;

	    String[] indexCount = Data.DB(callMethod, stockName, indexValue);
	    
	    int counter = 0;
	    for (int i = 0; i < indexCount.length; i ++){
	        if (indexCount[i] != null)
	            counter ++;
	    }
	    return counter;
    }
    
//-------------------------------------------------------------------------------------------    

}
