package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;

import mainUI.ApplicationGUIMain;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.XML;

public class ServiceController {

	/**
	 * @param args
	 */
	String serviceURL="";
	String methodType="";
	String requestString="";
	String reqMediaType="";
	String resMediaType="application/json";
	
	public String getResMediaType() {
		return resMediaType;
	}
	public void setResMediaType(String resMediaType) {
		this.resMediaType = resMediaType;
	}
	public String getReqMediaType() {
		return reqMediaType;
	}
	public void setReqMediaType(String reqMediaType) {
		this.reqMediaType = reqMediaType;
	}
	public String getServiceURL() {
	return serviceURL;
	}
	public String getRequestString() {
		return requestString;
	}
	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}
	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}





	
	
	public ServiceController(String url,String methodType,String reqMtype,String reqString)
	{
		this.serviceURL=url;
		this.methodType=methodType;
		this.requestString=reqString;
		this.reqMediaType=reqMtype;
		
	}
	public Hashtable sendRequest()
	{
		Hashtable allResponseHT=new Hashtable<>();
		
		String htmlResponse="";
		String xmlResponse="";
		String plainTextResponse="";
		String jsonResponse="";
		StringEntity input=null;
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = null;
			
			switch(methodType)
			{
			
			case "GET":
				HttpGet getRequest = new HttpGet(serviceURL);
				getRequest.addHeader("accept", resMediaType);
				response = httpClient.execute(getRequest);
				break;
				
			case "POST":
				HttpPost postRequest = new HttpPost(serviceURL);
							
				input=new StringEntity(requestString);
				input.setContentType(reqMediaType);
				postRequest.setEntity(input);
				
				response= httpClient.execute(postRequest);
				break;
				
			case "PUT":
				HttpPut putRequest= new HttpPut(serviceURL);
				putRequest.addHeader("accept", resMediaType);
				
				input=new StringEntity(requestString);
				input.setContentType(reqMediaType);
				putRequest.setEntity(input);
				
				response = httpClient.execute(putRequest);
				break;
				
			case "DELETE":
				HttpDelete deleteRequest= new HttpDelete(serviceURL);
				deleteRequest.addHeader("accept", resMediaType);
				
				response = httpClient.execute(deleteRequest);
				break;
			
			}
			
			
			
			
			if (response.getStatusLine().getStatusCode() != 201&&response.getStatusLine().getStatusCode() != 200)
			{
				ApplicationGUIMain.statusLabel.setText("Failed : HTTP error code : "+response.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatusLine().getStatusCode());
			}else{
				ApplicationGUIMain.statusLabel.setText("Success : HTTP Status - "+response.getStatusLine().getStatusCode());
			}
			
			BufferedReader responseReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			
			String output;
			
			while ((output = responseReader.readLine()) != null)
			{
//				if(methodType.equalsIgnoreCase("GET"))
//				{
					output="["+output+"]";
//				}
				
				JSONArray jsonResponseArray = new JSONArray(output);
				
				xmlResponse=Global.Common.JSONtoXMLConverter(output);
				jsonResponse=jsonResponseArray.toString();
				
											
				Hashtable strHT= new Hashtable();
				xmlResponse="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response xmlns=\""+serviceURL+"\"><result>"+xmlResponse+"</result></Response>";
				strHT=Global.Common.convertXMLResponseToHTML(xmlResponse);
//				xmlResponse=xmlResponse.replaceFirst("<array>", "<Response xmlns=\""+serviceURL+"\">");
//				xmlResponse=Global.Common.replaceLast(xmlResponse, "</array>", "</Response>");
				htmlResponse=(String)strHT.get(0);
				htmlResponse="<html><head><title>WebService Response</title></head><body><center><h3>"+htmlResponse+"</h3></center></body></html>";
				plainTextResponse=(String)strHT.get(1);
				
			}
			
			System.out.println("JSON Response : "+jsonResponse);
			System.out.println("XML Response : "+xmlResponse);
			System.out.println("HTML Response : "+htmlResponse);
			System.out.println("PLAIN_TEXT Response : "+plainTextResponse);
			
			allResponseHT.put("JSON", jsonResponse);
			allResponseHT.put("XML", xmlResponse);
			allResponseHT.put("HTML", htmlResponse);
			allResponseHT.put("PLAIN", plainTextResponse);

			httpClient.getConnectionManager().shutdown();
			
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return allResponseHT;
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

