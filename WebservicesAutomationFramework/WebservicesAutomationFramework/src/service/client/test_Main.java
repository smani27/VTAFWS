package service.client;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class test_Main {

	/**
	 * @param args
	 */
	
	 public static Document convertStringtoXMLDoc(String xmlSource) 
	            throws SAXException, ParserConfigurationException, IOException {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        return builder.parse(new InputSource(new StringReader(xmlSource)));
	    }
	 
	public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException {
		// TODO Auto-generated method stub
		
		Document xmlDoc=convertStringtoXMLDoc("<array><name><fname><sname>HHHH</sname></fname></name><qty>100</qty></array>");
		NodeList rootNL=xmlDoc.getElementsByTagName("array");
		Node arrNode=rootNL.item(0);
		String htmlstring="";
		String plainstring="";
		htmlstring=recurNode(arrNode,htmlstring);
		plainstring=htmlstring.replaceAll("<br/>", "\n");
		System.out.println(htmlstring);
		System.out.println(plainstring);

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

}
