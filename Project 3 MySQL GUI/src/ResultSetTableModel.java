import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import java.util.Properties;

import javax.print.attribute.standard.JobHoldUntil;
import javax.sql.DataSource;



public class ResultSetTableModel extends AbstractTableModel 
{
   private Connection connection;
   private ResultSetMetaData metaData;
   private ResultSetMetaData operationMetaData;
   private int numRows;
   private int operatioNumRows;
   private Statement statement;
   private Statement operationStatement;
   private ResultSet resultOfSet;
   private ResultSet resultOfSet2;
   private ResultSet operationResultOfSet;
   private Connection connectOperations;
   private boolean connectionSuccess = false;
   private int numQueries = 0;
   private int numUpdates = 0;

   public ResultSetTableModel( String sqlQuery , Connection connection) throws SQLException, ClassNotFoundException, IOException{


           try {
                
                this.connection = connection;
                if(!this.connection.isClosed())
                {
                        connectionSuccess = true;
        
            statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );            
            
                if(sqlQuery.contains("select") || sqlQuery.contains("SELECT")){
                                try 
                                {
                                    setQuery(sqlQuery);
                                        
                                } catch (IllegalStateException | SQLException e){
                                    JOptionPane.showMessageDialog( null, 
                                    e.getMessage(), "Database error", 
                                    JOptionPane.ERROR_MESSAGE );
                                    e.printStackTrace();
                                }
                     }
                  else{
                     try {
                           setUpdate(sqlQuery);
         
                        } catch (IllegalStateException | SQLException e){
                                 JOptionPane.showMessageDialog( null, 
                                 e.getMessage(), "Database error", 
                                 JOptionPane.ERROR_MESSAGE );
                                 e.printStackTrace();
                           }
                     }

                }
                        
          }
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
         System.exit( 1 );
      }
   }

   public void setOperationQuery(String query) throws SQLException, IllegalStateException {
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "No connection to database" );
         int res2;

      res2 = statement.executeUpdate(query);    
      
      fireTableStructureChanged();
   }

   public void setOperationUpdate( String query ) throws SQLException, IllegalStateException {
         int res;
      if ( !connectionSuccess ) 
      throw new IllegalStateException( "Not Connected to Database" );

      res = statement.executeUpdate(query);
      fireTableStructureChanged();
   }


   public Class getColumnClass( int column ) throws IllegalStateException
   {
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "Not Connected to Database" );

      try 
      {
         String className = metaData.getColumnClassName( column + 1 );
         
         return Class.forName( className );
      }
      catch ( Exception exception ) 
      {
         exception.printStackTrace();
      }
      
      return Object.class; 
   } 

   public int getColumnCount() throws IllegalStateException
   {   
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "No connection to database" );

      try 
      {
         return metaData.getColumnCount(); 
      }
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      }
      
      return 0;
   }

   public String getColumnName( int column ) throws IllegalStateException
   {    
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "No connection to database" );

      try 
      {
         return metaData.getColumnName( column + 1 );  
      }
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      }
      
      return "";
   }

   public int getRowCount() throws IllegalStateException
   {      
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "No connection to database" );
 
      return numRows;
   }

   public Object getValueAt( int row, int column ) 
      throws IllegalStateException
   {
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "No connection to database" );

      try 
      {
         resultOfSet.next();

         resultOfSet.absolute( row + 1 );
         return resultOfSet.getObject( column + 1 );
      } 
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } 
      
      return "";
   }
   
   public void setQuery( String query ) 
      throws SQLException, IllegalStateException {
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "No connection to database" );

      resultOfSet = statement.executeQuery( query );

      metaData = resultOfSet.getMetaData();

      resultOfSet.last();
      numRows = resultOfSet.getRow();      
      
      fireTableStructureChanged();
   }


   public void setUpdate( String query ) 
      throws SQLException, IllegalStateException {
            int res;
      if ( !connectionSuccess ) 
         throw new IllegalStateException( "Not Connected to Database" );

      res = statement.executeUpdate( query );

      //metaData = resultOfSet.getMetaData();
//      resultOfSet.last();
//      numRows = resultOfSet.getRow();

      fireTableStructureChanged();
   }
         
}