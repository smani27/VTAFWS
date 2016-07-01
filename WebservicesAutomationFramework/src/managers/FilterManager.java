package managers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controller.ServiceController;

import mainUI.ApplicationGUIMain;
import managers.*;
import model.CheckListManager;
public class FilterManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String Xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\"http://localhost:8085/WebservicesAutomationFramework/Demo/EmployeeDetails\">" +
				"<result>" +
				"<array><id>3</id><empid>8018556</empid><organization>Chennai</organization><empname>Anand</empname></array>" +
				"</result></Response>";
//		applyFilter(Xml,new Hashtable<>());
	}
	
	public void applyFilter(String xmlString,Hashtable filterHT)
	{
		Document xmlDoc = null;
		try{
				xmlDoc = Global.Common.convertStringtoXMLDoc(xmlString);
				Node resultNode=xmlDoc.getFirstChild().getFirstChild();
				NodeList arrayNL=xmlDoc.getElementsByTagName("array");
				for(int i=0;i<arrayNL.getLength();i++)
				{
					TreeMap colandValHT=new TreeMap();
					Node arrayNode=arrayNL.item(i);
					if(arrayNode.getNodeType()==Node.ELEMENT_NODE)
					{
						NodeList oneRowNL=arrayNode.getChildNodes();
						for(int j=0;j<oneRowNL.getLength();)
						{
							
							
							Node oneVal=oneRowNL.item(j);
							System.out.println(j+" = "+oneRowNL.getLength()+" "+oneVal.getNodeName());
							if(oneVal.getNodeType()==Node.ELEMENT_NODE)
							{
								String nodeName=oneVal.getNodeName();
								String nodeTxt=oneVal.getTextContent();
								if(nodeName==null){nodeName="";}
								if(nodeTxt==null){nodeTxt="";}
								String filterColumn=(String) filterHT.get(nodeName);
									if(filterColumn==null){filterColumn="";}
					
									if(!filterColumn.equalsIgnoreCase(nodeName))
									{
									System.out.println(j+" "+nodeName +" removed");
									arrayNode.removeChild(oneVal);
									j=0;
									}else{
										j++;
									}
								
							}
							
						}
						
					}
					
				}
				ApplicationGUIMain.fillResponse(xmlDoc);
			System.out.println("Filter Applied");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}

	
}
