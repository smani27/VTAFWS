package Global;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.DBTable;

import org.json.JSONArray;
import org.json.JSONObject;
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
			strHT.put(0, htmlstring);
			strHT.put(1, plainstring);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strHT;
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
	
	public static Hashtable parseXMLtoValues(Document doc)
	{
		try{
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
    


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
	}

}
