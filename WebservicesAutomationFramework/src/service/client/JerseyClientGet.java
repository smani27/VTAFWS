package service.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import Decoder.BASE64Encoder;

public class JerseyClientGet {

	
	// using jersey client basic authentication implementation
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {

			Client client = Client.create();

			WebResource webResource = client
			   .resource("http://localhost:8080/WSDemo/test/get");
			
			String name = "Admin";
	        String password = "User1";
	        String authString = name + ":" + password;
	        String authStringEnc = new BASE64Encoder().encode(authString.getBytes());
	        System.out.println("Base64 encoded auth string: " + authStringEnc);

			ClientResponse response = webResource.accept("application/json").header("Authorization", "Basic " + authStringEnc)
	                   .get(ClientResponse.class);

			if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

		  } catch (Exception e) {

			e.printStackTrace();

		  }

	}

}
