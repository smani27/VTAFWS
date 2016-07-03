package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import Decoder.BASE64Decoder;
import controller.DBController;

@Path("/Demo")
public class JSONService 
{	
	
	// Added authentication to this method to verify the process
	@GET
	@Path("/{tableName}")
	@Produces("application/json")
	public String getAllEmployeeDetails(@PathParam("tableName") String tableName, @HeaderParam("authorization") String authString)
	{
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray= controller.DBController.executeSelect(conn, "Select * from "+tableName);
		
		if(!isUserAuthenticated(authString)){
            return "{\"error\":\"User not authenticated\"}";
        }else {
        	return jarray.toString(); 
        }
		
		
	}
	

	@GET
	@Path("/{tableName}/query")
	@Produces("application/json")
	public String queryEmployee(@PathParam("tableName") String tableName,@QueryParam("empID") String eid,@QueryParam("empName") String eName,@QueryParam("organization") String organization)
	{
		
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray=null;
		String query="";
		if(eid!=null && eName!=null && organization!=null)
		{
			query=" where empID="+eid+" and empName="+eName+" and organization="+organization;
		}else if(eid!=null && eName!=null){
			query=" where empID="+eid+" and empName="+eName;
			
		}else if(eid!=null)
		{
			query=" where empID="+eid;
			
		}else{
			query="";
		}
				
		jarray= controller.DBController.executeSelect(conn, "Select * from "+tableName+query);	
		return jarray.toString(); 
	}
	
	
	@PUT
	@Path("/{tableName}")
	@Consumes("application/json")
    @Produces("application/json")
	public String addEmployeeJSON(@PathParam("tableName") String tableName,Object jsonInput) 
	{
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray=Global.Common.objectToJSONArray(jsonInput);
		String jsonStr=jarray.toString();
		
		try {
			String xmlString=Global.Common.JSONtoXMLConverter(jsonStr);
			xmlString="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Request><result>"+xmlString+"</result></Request>";
			Document xmlDoc=Global.Common.convertStringtoXMLDoc(xmlString);
			ArrayList allColandValAL=Global.Common.splitValuesfromXML(xmlDoc);
			
			
			for(int i=0;i<allColandValAL.size();i++)
			{
				String insStmt="INSERT INTO "+tableName+" (";
				String consColumn="",consValues="";
				TreeMap rowTM=(TreeMap)allColandValAL.get(i);
				 Set<String> keys = rowTM.keySet();
			        for(String key: keys){
			            System.out.println(key+" "+rowTM.get(key));
			            
			            consColumn=consColumn+key+",";
			            String colDtype=DBController.getColumnDataType(tableName,key);
			            if(colDtype.contains("CHAR"))
			            {
			            	consValues=consValues+"'"+rowTM.get(key)+"'"+",";
			            }else{
			            	consValues=consValues+rowTM.get(key)+",";
			            }
			        }
			        consColumn= Global.Common.replaceLast(consColumn, ",", "");
			        consValues= Global.Common.replaceLast(consValues, ",", "");
			        insStmt=insStmt+consColumn+") VALUES ("+consValues+")";
			        
			        System.out.println("Insert Statement : "+insStmt);
			        DBController.executeSQLQuery(conn, insStmt);
			        
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return jarray.toString();
	}
	
	
	@PUT
	@Path("/{tableName}")
	@Consumes("application/xml")
    @Produces("application/json")
	public String addEmployeeXML(@PathParam("tableName") String tableName,Document xmlDoc) 
	{
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		String jsonStr=Global.Common.XMLtoJSONConverter(xmlDoc);

		try {
			ArrayList allColandValAL=Global.Common.splitValuesfromXML(xmlDoc);

			for(int i=0;i<allColandValAL.size();i++)
			{
				String insStmt="INSERT INTO "+tableName+" (";
				String consColumn="",consValues="";
				TreeMap rowTM=(TreeMap)allColandValAL.get(i);
				 Set<String> keys = rowTM.keySet();
			        for(String key: keys){
			            System.out.println(key+" "+rowTM.get(key));
			            
			            consColumn=consColumn+key+",";
			            String colDtype=DBController.getColumnDataType(tableName,key);
			            if(colDtype.contains("CHAR"))
			            {
			            	consValues=consValues+"'"+rowTM.get(key)+"'"+",";
			            }else{
			            	consValues=consValues+rowTM.get(key)+",";
			            }
			        }
			        consColumn= Global.Common.replaceLast(consColumn, ",", "");
			        consValues= Global.Common.replaceLast(consValues, ",", "");
			        insStmt=insStmt+consColumn+") VALUES ("+consValues+")";
			        
			        System.out.println("Insert Statement : "+insStmt);
			        DBController.executeSQLQuery(conn, insStmt);
			        
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return jsonStr;
	}
	
	@POST
	@Path("/{tableName}")
	@Consumes("application/json")
    @Produces("application/json")
	public String updateEmployeeJSON(@PathParam("tableName") String tableName,Object jsonInput) 
	{
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray=Global.Common.objectToJSONArray(jsonInput);
		String jsonStr=jarray.toString();
		String condition="";
		try {
			String xmlString=Global.Common.JSONtoXMLConverter(jsonStr);
			xmlString="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Request><result>"+xmlString+"</result></Request>";
			Document xmlDoc=Global.Common.convertStringtoXMLDoc(xmlString);
			ArrayList allColandValAL=Global.Common.splitValuesfromXML(xmlDoc);
			
			for(int i=0;i<allColandValAL.size();i++)
			{
				String updateStmt="UPDATE "+tableName+" SET ";
				String updateCols="";
				String pKey=DBController.getPrimaryKeyColumn(tableName);
				condition="";
				TreeMap rowTM=(TreeMap)allColandValAL.get(i);
				 Set<String> keys = rowTM.keySet();
			        for(String key: keys){
			        	if(!pKey.equalsIgnoreCase(key))
			        	{
				            System.out.println(key+" "+rowTM.get(key));
				            String colDtype=DBController.getColumnDataType(tableName,key);
				            if(colDtype.contains("CHAR"))
				            {
				            	updateCols=updateCols+key+"='"+rowTM.get(key)+"'"+",";
				            }else{
				            	updateCols=updateCols+key+"="+rowTM.get(key)+",";
				            }
			        	}else{
			        		String colDtype=DBController.getColumnDataType(tableName,key);
				            if(colDtype.contains("CHAR"))
				            {
				            	condition=key+"='"+rowTM.get(key)+"'";
				            }else{
				            	condition=key+"="+rowTM.get(key)+"";
				            }
			        	}
			        }
			        updateCols= Global.Common.replaceLast(updateCols, ",", "");
			        if(pKey!=null&&!pKey.equalsIgnoreCase(""))
			        {
			        updateStmt=updateStmt+updateCols+" WHERE "+condition;
			        }else{
			        	condition=updateCols.replaceAll(",", " OR ");
			        	updateStmt=updateStmt+updateCols+" WHERE "+condition;	
			        }
			        System.out.println("Update Statement : "+updateStmt);
			        DBController.executeSQLQuery(conn, updateStmt);
			        
			}
			
			jarray= controller.DBController.executeSelect(conn, "Select * from "+tableName+" WHERE "+condition);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return jarray.toString();
	}
	
	@POST
	@Path("/{tableName}")
	@Consumes("application/xml")
    @Produces("application/json")
	public String updateEmployeeXML(@PathParam("tableName") String tableName,Document xmlDoc) 
	{
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray=null;
		String condition="";
		try {

			ArrayList allColandValAL=Global.Common.splitValuesfromXML(xmlDoc);
			
			for(int i=0;i<allColandValAL.size();i++)
			{
				String updateStmt="UPDATE "+tableName+" SET ";
				String updateCols="";
				String pKey=DBController.getPrimaryKeyColumn(tableName);
				condition="";
				TreeMap rowTM=(TreeMap)allColandValAL.get(i);
				 Set<String> keys = rowTM.keySet();
			        for(String key: keys){
			        	if(!pKey.equalsIgnoreCase(key))
			        	{
				            System.out.println(key+" "+rowTM.get(key));
				            String colDtype=DBController.getColumnDataType(tableName,key);
				            if(colDtype.contains("CHAR"))
				            {
				            	updateCols=updateCols+key+"='"+rowTM.get(key)+"'"+",";
				            }else{
				            	updateCols=updateCols+key+"="+rowTM.get(key)+",";
				            }
			        	}else{
			        		String colDtype=DBController.getColumnDataType(tableName,key);
				            if(colDtype.contains("CHAR"))
				            {
				            	condition=key+"='"+rowTM.get(key)+"'";
				            }else{
				            	condition=key+"="+rowTM.get(key)+"";
				            }
			        	}
			        }
			        updateCols= Global.Common.replaceLast(updateCols, ",", "");
			        if(pKey!=null&&!pKey.equalsIgnoreCase("")&&!condition.equalsIgnoreCase(""))
			        {
			        updateStmt=updateStmt+updateCols+" WHERE "+condition;
			        }else{
			        	condition=updateCols.replaceAll(",", " OR ");
			        	updateStmt=updateStmt+updateCols+" WHERE "+condition;	
			        }
			        System.out.println("Update Statement : "+updateStmt);
			        DBController.executeSQLQuery(conn, updateStmt);
			        
			}
			
			jarray= controller.DBController.executeSelect(conn, "Select * from "+tableName+" WHERE "+condition);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return jarray.toString();
	}
	
	@DELETE
	@Path("/{tableName}/{id}")
	@Produces("application/json")
	public String deleteEmployee(@PathParam("tableName") String tableName,@PathParam("id") String id) 
	{
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray=null;
		try {

				String deleteStmt="DELETE FROM "+tableName+" WHERE id="+id;

		        System.out.println("Delete Statement : "+deleteStmt);
		        jarray= controller.DBController.executeSelect(conn, "Select * from "+tableName+" WHERE id="+id);
		        DBController.executeSQLQuery(conn, deleteStmt);
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return jarray.toString();
	}
	
// Method to validate the entered credentials is matching with the expected credentials	
private boolean isUserAuthenticated(String authString){
        
        
		String decodedAuth = "";
      
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
      
        byte[] bytes = null;
        try {
        	bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        // decoded given credentials
        System.out.println(decodedAuth);
        
        String [] val=decodedAuth.split(":");
        if(val[0].equalsIgnoreCase("Admin") && val[1].equalsIgnoreCase("User")){
        	
        	System.out.println("VAlid credentials");
        	return true;
        	
        }else
	        {
	        	return false;
	        }
      }

}


