package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.json.JSONArray;
import org.json.JSONObject;

import model.DBTable;

public class DBController {

	/**
	 * @param args
	 */
	public final static Connection createDBConnection(final String databaseType,
			 final String url, final String username,final String password) {
		Connection con = null;
		
			try {
				if ("mysql".equalsIgnoreCase(databaseType)) {
					String dbClass = "com.mysql.jdbc.Driver";
					Class.forName(dbClass).newInstance();
					con = DriverManager.getConnection(url, username, password);
				} else if ("oracle".equalsIgnoreCase(databaseType)) {
					DriverManager
							.registerDriver(new oracle.jdbc.driver.OracleDriver());
					con = DriverManager.getConnection(url, username, password);
				} else if ("mssql".equalsIgnoreCase(databaseType)) {
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					con = DriverManager.getConnection(url, username, password);
				}else if("msaccess".equalsIgnoreCase(databaseType)||"Access".equalsIgnoreCase(databaseType)){
					 Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					 con = DriverManager.getConnection(url, username, password);
				}
				else if (databaseType.isEmpty()) {
					
				}
				 
			} catch (SQLException e) {

				e.printStackTrace();
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			
			return con;


	}
	
	public final void executeSQLQuery(Connection conn, final String query) {
		try {
		java.sql.Statement queryStmt = conn.createStatement();
		boolean result =queryStmt.execute(query);
						
		} catch (SQLException e) {

		e.printStackTrace();
		
		
	} catch (Exception e) {
		e.printStackTrace();
		
	}
	}
		
	public final static JSONArray executeSelect(Connection conn, final String query) 
	{
		JSONArray jArray=null;
			try {
			java.sql.Statement queryStmt = conn.createStatement();
			ResultSet queryRs=queryStmt.executeQuery(query);
			
//			Hashtable columnDetHT=getColumnDetails(queryRs);
//			Hashtable rowValueHT=getAllRowValues(columnDetHT, queryRs);
			
			jArray=convertRSToJSON(queryRs);
			
			System.out.println(jArray);
			
							
			} catch (SQLException e) {

			e.printStackTrace();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
			return jArray;

}
	
	public static Hashtable getAllRowValues(Hashtable columnDetHT,ResultSet queryRs){
		Hashtable rowValueHT=new Hashtable<>();
		int rowindex=0;
		try{

			while(queryRs.next())
			{
				Object[] row=null;
				for(int i=0;i<columnDetHT.size();i++)
				{
				row=new Object[columnDetHT.size()];
				DBTable dbtable=(DBTable)columnDetHT.get(i);
				String columnName=dbtable.getColumnName();
				String columnDtype=dbtable.getColumnDataType();
				Object colValue=null;
				switch(columnDtype)
				{
				case "COUNTER":
					colValue=queryRs.getInt(columnName);
					break;
				case "VARCHAR":
					colValue=queryRs.getString(columnName);
					break;
				case "LONGCHAR":
					colValue=queryRs.getString(columnName);
					break;
				default:
					colValue=queryRs.getString(columnName);
					break;
				
				}
				row[i]=colValue;
				}
				rowValueHT.put(rowindex,row);
				rowindex++;
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return rowValueHT;
	}
	
	
	public static Hashtable getColumnDetails(ResultSet queryRs)
	{
		Hashtable columnDetHT=new Hashtable<>();
		try{
			ResultSetMetaData  rsmtdata=queryRs.getMetaData();
			int rsColCount=rsmtdata.getColumnCount();
			for(int i=0;i<rsColCount;i++)
			{
				String colName=rsmtdata.getColumnName(i+1);
				String colDtype=rsmtdata.getColumnTypeName(i+1);
				DBTable colDetOBj=new DBTable(colName, colDtype);
				columnDetHT.put(i, colDetOBj);
				System.out.println("Column Name : "+colName);
				System.out.println("Data Type : "+colDtype);
				
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return columnDetHT;
	}
	
	public static JSONArray convertRSToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
                
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }
    
    public static String convertRSToXML(ResultSet resultSet)
            throws Exception {
        StringBuffer xmlArray = new StringBuffer("<results>");
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            xmlArray.append("<row>");
            for (int i = 0; i < total_rows; i++) {
//                xmlArray.append(" " + resultSet.getMetaData().getColumnLabel(i + 1)
//                        .toLowerCase() + "='" + resultSet.getObject(i + 1) + "'");
            	 xmlArray.append("<"+resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase()+">");
            	 xmlArray.append(resultSet.getObject(i + 1));
            	 xmlArray.append("</"+resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase()+">");
            	}
            xmlArray.append("</row>");
        }
        xmlArray.append("</results>");
        return xmlArray.toString();
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con=createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		executeSelect(con,"Select * from EmployeeDetails where empID='8018551'");
	}

}
