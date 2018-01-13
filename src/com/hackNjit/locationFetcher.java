package com.hackNjit;


import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class locationFetcher {


	/*
	 * Method to get User's Address and City information using Alexa's API
	 */
	public static String[] predictions(String diviceId, String consentToken) {

		String baseUrl = "https://api.amazonalexa.com/v1/devices/"+diviceId+"/settings/address";
		StringBuilder requestUrl = new StringBuilder(baseUrl);
		GetMethod method = new GetMethod(requestUrl.toString());
		method.addRequestHeader("Accept", "application/json");
		method.addRequestHeader("Authorization", "Bearer "+consentToken);

		String response = "";
		HttpClient client = new HttpClient();

		try {
			
			Integer statusCode = client.executeMethod(method);

			if(statusCode == 200){
				response = method.getResponseBodyAsString();
				return cov2(response);
			}else{
				return new String[] {"Error! Please try again ", ""+statusCode};
			}

		} catch (Exception e) {
			response = null;
			return new String[] {"Error! Please try again ", e.getMessage()};
		}


	}

	/*
	 *  Convert String data to User model
	 */
	public static User convertStringToMember(String listString) {
		
		User user = new User();
		ObjectMapper objectmapper = new ObjectMapper();

		try {
			user = objectmapper.readValue(listString, new TypeReference<User>() {});
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return user;
	}

	/*
	 * Extract address and city from given Json Object.
	 */
	public static String[] cov2(String response) {
		JSONObject obj = new JSONObject(response);
		String[] Names = new String[2];
		Names[0] = obj.getString("addressLine1");
		Names[1] = obj.getString("city");
		return Names;
	}


	/*
	 * Method to get Latitude and Longitude using Google Map API based
	 * on the provided address and city
	 */
	public static String getLatLong(String[] args,float[] result) {
		
		String addrs = args[0].replaceAll(" ", "+");
		String city= args[1].replaceAll(" ", "+");
		String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		StringBuilder requestUrl = new StringBuilder(baseUrl);
		String location = addrs+","+city+"&key=";
		String key= ""; //google map api key, generated from Google Console

		requestUrl.append(location);
		requestUrl.append(key);
		GetMethod method = new GetMethod(requestUrl.toString());
		method.addRequestHeader("Accept", "application/json");
		String response = "";
		HttpClient client = new HttpClient();

		try {

			Integer statusCode = client.executeMethod(method);

			if(statusCode == 200){

				response = method.getResponseBodyAsString();
				JSONObject obj = new JSONObject(response);
				JSONObject res = obj.getJSONArray("results").getJSONObject(0);
				result[0]=Float.parseFloat(res.getJSONObject("geometry").getJSONObject("location").get("lat").toString());				
				result[1]=Float.parseFloat(res.getJSONObject("geometry").getJSONObject("location").get("lng").toString());
				return "Success";

			}else{
				return "Error";
			}
		} catch (Exception e) {
			response = null;
			return "Error!";
		}


	}
}

