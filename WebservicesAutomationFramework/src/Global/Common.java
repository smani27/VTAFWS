package Global;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.DBTable;

import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Common {

	/**
	 * @param args
	 */

	public static Hashtable convertXMLResponseToHTML(String xmlRes) {
		Hashtable strHT = new Hashtable();
		String htmlString = "";
		try {
			Document xmlDoc = convertStringtoXMLDoc(xmlRes);
			NodeList rootNL = xmlDoc.getElementsByTagName("result");
			Node arrayNode = rootNL.item(0);

			String htmlstring = "";
			String plainstring = "";
			htmlstring = recurNode(arrayNode, htmlstring);
			plainstring = htmlstring.replaceAll("<br/>", "\n");
			// System.out.println(htmlstring);
			// System.out.println(plainstring);
			htmlString ="<html><head><title>WebService Response</title></head><body><center><h3>"+htmlString+"</h3></center></body></html>";
			strHT.put(0, htmlstring);
			strHT.put(1, plainstring);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strHT;
	}

	public static ArrayList splitValuesfromXML(Document doc){
		ArrayList allValuesAL=new ArrayList<>();
	try{
		
		NodeList arrayNL=doc.getElementsByTagName("array");
		for(int i=0;i<arrayNL.getLength();i++)
		{
			TreeMap colandValHT=new TreeMap();
			Node arrayNode=arrayNL.item(i);
			if(arrayNode.getNodeType()==Node.ELEMENT_NODE)
			{
				NodeList oneRowNL=arrayNode.getChildNodes();
				for(int j=0;j<oneRowNL.getLength();j++)
				{
					Node oneVal=oneRowNL.item(j);
					if(oneVal.getNodeType()==Node.ELEMENT_NODE)
					{
						String nodeName=oneVal.getNodeName();
						String nodeTxt=oneVal.getTextContent();
						if(nodeName==null){nodeName="";}
						if(nodeTxt==null){nodeTxt="";}
						colandValHT.put(nodeName,nodeTxt);
					}
				}
			}
			allValuesAL.add(colandValHT);
		}
		
		
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	return allValuesAL;
		
	}
	public static String recurNode(Node arrNode, String htmlstring) {
		try {

			String nodeValue = "";
			String nodename = "";
			boolean hasChildElementNode=false;

			NodeList childNL = arrNode.getChildNodes();
			
		
			
			
			for (int i = 0; i < childNL.getLength(); i++) {
				Node childN = childNL.item(i);
				if (childN.getNodeType() == Node.ELEMENT_NODE) {
					nodename = childN.getNodeName();
					
					NodeList cOfChildNL = childN.getChildNodes();
					for (int j = 0; j < cOfChildNL.getLength(); j++) {
						Node singleNode=cOfChildNL.item(j);
						if(singleNode.getNodeType()==Node.TEXT_NODE)
						{
							hasChildElementNode=true;
						}
					}
					if(hasChildElementNode)
					{
						nodeValue = childN.getTextContent();	
					}
					
					htmlstring = htmlstring + nodename + " : " + nodeValue
							+ "<br/>";

					if (childN.hasChildNodes()) {
						htmlstring = recurNode(childN, htmlstring);
					}

				}
			}

		} catch (Exception e) {
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
	
	public static String replaceLast(String string, String toReplace, String replacement) {
	    int pos = string.lastIndexOf(toReplace);
	    if (pos > -1) {
	        return string.substring(0, pos)
	             + replacement
	             + string.substring(pos + toReplace.length(), string.length());
	    } else {
	        return string;
	    }
	}
	
	
	
	public static String XMLtoJSONConverter(Document doc)
	{
		String jsonPrettyPrintString="";
		try{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			
			JSONObject xmlJSONObj = XML.toJSONObject(writer.toString());
			jsonPrettyPrintString= "["+xmlJSONObj.toString(4).replaceAll("\n", "")+"]";
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return jsonPrettyPrintString;
	}
	
	public static String JSONtoXMLConverter(String jsonInputString)
	{
		String xmlString="";
		
		try{
			
			JSONArray jsonResponseArray = new JSONArray(jsonInputString);
			xmlString = XML.toString(jsonResponseArray);
					
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return xmlString;
	}
	
	public static Document createXMLdoc()
	{
		Document doc=null;
		
		File file = new File("C:\\Users\\gokuls\\Desktop\\11111.xml");
		try{
				DocumentBuilderFactory dbFactory =
		         DocumentBuilderFactory.newInstance();
		         DocumentBuilder dBuilder = 
		            dbFactory.newDocumentBuilder();
		         doc = dBuilder.parse(file);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	 return doc;
	}
	
	public static void writeXML(Document doc,String XMLpath)
	{
		try{
			 TransformerFactory transformerFactory =
			         TransformerFactory.newInstance();
			         Transformer transformer =
			         transformerFactory.newTransformer();
			         DOMSource source = new DOMSource(doc);
			         StreamResult result =
			         new StreamResult(new File(XMLpath));
			         transformer.transform(source, result);
			         // Output to console for testing
			         StreamResult consoleResult =
			         new StreamResult(System.out);
			         transformer.transform(source, consoleResult);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Hashtable parseXMLtoValues(Document doc)
	{
		try{
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static JSONArray objectToJSONArray(Object object){
	    Object json = null;
	    JSONArray jsonArray = null;
	    try {
	        json = new JSONTokener(object.toString()).nextValue();
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	    if (json instanceof JSONArray) {
	        jsonArray = (JSONArray) json;
	    }
	    return jsonArray;
	}
    


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document doc=createXMLdoc();
		ArrayList allVAL=splitValuesfromXML(doc);
		for(int i=0;i<allVAL.size();i++)
		{
			TreeMap rowTM=(TreeMap)allVAL.get(i);
			 Set<String> keys = rowTM.keySet();
		        for(String key: keys){
		            System.out.println(key+" "+rowTM.get(key));
		        }
		
			
		}
	
	}
	
	
	
	

}
