package service.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientPut {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	
	{
		// TODO Auto-generated method stub
		 try {

	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPut putRequest = new HttpPut("http://localhost:8080/RESTfulExample/employee/result");

	            StringEntity input = new StringEntity("Hello, this is a message from your put client!");
	            input.setContentType("text/xml");
	            putRequest.setEntity(input);

	            httpClient.execute(putRequest);
	            
	            httpClient.getConnectionManager().shutdown();


	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	}

}
