package service.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ApacheHttpClientPost {

	// http://localhost:8080/RESTfulExample/json/product/post
	
	public static void main(String[] args) throws JSONException
	{

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			HttpPost postRequest = new HttpPost("http://localhost:8085/WebservicesAutomationFramework/json/product/");
			
			StringEntity input=new StringEntity("<?xml version=\"1.0\" encoding=\"UTF-8\"?><array><name>iPad 3</name><qty>999</qty></array>");
			
			input.setContentType("application/xml");
			
			postRequest.setEntity(input);
			
//	        TransformerFactory tFactory=TransformerFactory.newInstance();
	        
	        

			HttpResponse response = httpClient.execute(postRequest);
			
			if (response.getStatusLine().getStatusCode() != 201)
			{
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		
			String output;

			while ((output = br.readLine()) != null)
			{
//				JSONArray jsonResponse = new JSONArray("["+output+"]");
				
//				String xmlResponse = XML.toString(jsonResponse);
	
				
//				System.out.println("JSON Response : "+jsonResponse);

//				System.out.println("XML Response : "+xmlResponse);
//				Hashtable strHT= new Hashtable();
//				strHT=convertXMLResponseToHTML(xmlResponse);
//				String htmlresponse=(String)strHT.get(0);
//				htmlresponse="<html><head><title>WebService Response</title></head><body><center><h3>"+htmlresponse+"</h3></center></body></html>";
//				String plainTextResponse=(String)strHT.get(1);
				
//				System.out.println("HTML Response : "+htmlresponse);
//				System.out.println("PLAIN_TEXT Response : "+plainTextResponse);
				
//				System.out.println("Output from Server .... \n");
				System.out.println(output);
				
				
			}
			
			
			httpClient.getConnectionManager().shutdown();

		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{

			e.printStackTrace();
		}

	}

	
public static Hashtable convertXMLResponseToHTML(String xmlRes)
	{
		Hashtable strHT= new Hashtable();
		String htmlString="";
		try{
			Document xmlDoc=convertStringtoXMLDoc(xmlRes);
			NodeList rootNL=xmlDoc.getElementsByTagName("array");
			Node arrayNode=rootNL.item(0);
			
			String htmlstring="";
			String plainstring="";
			htmlstring=recurNode(arrayNode,htmlstring);
			plainstring=htmlstring.replaceAll("<br/>", "\n");
//			System.out.println(htmlstring);
//			System.out.println(plainstring);
			strHT.put(0,htmlstring);
			strHT.put(1,plainstring);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return strHT;
	}


public static String recurNode(Node arrNode,String htmlstring)
{
	try{

		String nodeValue="";
		String nodename="";
	
			NodeList childNL=arrNode.getChildNodes();
			for(int i=0;i<childNL.getLength();i++)
			{
				Node childN=childNL.item(i);
				if(childN.getNodeType()==Node.ELEMENT_NODE)
				{
					nodename=childN.getNodeName();
					nodeValue=childN.getTextContent();
					htmlstring=htmlstring+nodename+" : "+nodeValue+"<br/>";
					
				if(childN.hasChildNodes())
				{
					htmlstring=recurNode(childN,htmlstring);
				}
				
				}
			}
			
	
			
		
		
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	return htmlstring;
}
	
	
	 public static Document convertStringtoXMLDoc(String xmlSource) 
	            throws SAXException, ParserConfigurationException, IOException {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        return builder.parse(new InputSource(new StringReader(xmlSource)));
	    }

}
