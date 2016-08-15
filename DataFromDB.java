import java.sql.*;

public class DataFromDB {
   
   public String[] DB(String returnType, String stockName, int indexCount) {
   Connection conn = null;
   Statement stmt = null;
   
   int arraySize = indexCount ;
   String getMethod = null;
   String[] Symbols = new String[arraySize];
   
//------------------------------------------------------------------------   
   
   //Get Methods are set according to the Return Type
   if(returnType == "name")
	   getMethod = "TABLE_NAME";
   if(returnType == "date" || returnType == "tellIndex" || returnType == "getX")
	   getMethod = "date";
   if(returnType == "getY")
	   getMethod = "close";
   
//------------------------------------------------------------------------   
   
   //Connection with JDBC is created
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/stanalysis", "root", "");      
      stmt = conn.createStatement();
      
//-------------------------------------------------------------------------
      
      //Purpose Selection for DB Connection 
      String sql = null;
 	  
      if(returnType == "name")
    	  sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='Stanalysis'";

      if(returnType == "date" || returnType == "getX")
    	  sql = "SELECT date FROM " + stockName;
      
      if(returnType == "getY")
    	  sql = "SELECT close FROM " + stockName;
      
      ResultSet rs = stmt.executeQuery(sql);

//--------------------------------------------------------------------------
      
      //Extracting data from Result Set
      int stringCounter = 0;
      
      while(rs.next()){        
          String value = rs.getString(getMethod);
             
          Symbols[stringCounter] = value;
          stringCounter++;
      }
      rs.close();

//------------------------------------------------------------      
      
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            conn.close();
      }catch(SQLException se){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
   }
   return Symbols;
   }
}