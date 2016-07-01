package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

@Path("/json/product")
public class JSONService 
{	
	@GET
	@Path("/{tableName}")
	@Produces("application/json")
	public String getAllEmployeeDetails(@PathParam("tableName") String tableName)
	{
		
		Connection conn=controller.DBController.createDBConnection("msaccess", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\webservicesImplementation\\SampleDB.accdb;", "", "");
		JSONArray jarray= controller.DBController.executeSelect(conn, "Select * from "+tableName);
		
		
		return jarray.toString(); 
	}
	
	@GET
	@Path("/{tableName}/query")
	@Produces("application/json")
	public String getProductInJSON(@PathParam("tableName") String tableName,@QueryParam("empID") String eid,@QueryParam("empName") String eName,@QueryParam("organization") String organization)
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
	
	
	@POST
// @Path("/post")
	@Consumes("application/json")
    @Produces("application/json")
	public Response createProductInJSON(Product product) 
	{
		//String result = "Product created : " + product;
		return Response.status(201).entity(product).build();
	}
	
	@POST
//	@Path("/post")
	@Consumes("application/xml")
	@Produces("application/json")
	public Response createProductInXML(Document product) throws JSONException, ParserConfigurationException, TransformerException 
	{
		//String result = "Product created : " + product;
		String jsonPrettyPrintString=Global.Common.XMLtoJSONConverter(product);
		return Response.status(201).entity(jsonPrettyPrintString).build();
	}
	
	@PUT
    @Consumes(MediaType.TEXT_XML)
	@Produces("application/json")
    public void putIntoDAO(InputStream xml) 
	{
        String line = "";
        StringBuilder sb = new  StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(xml));
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
    }

}


