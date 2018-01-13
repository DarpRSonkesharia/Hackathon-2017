package com.hackNjit;

import java.util.ArrayList;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONObject;


public class HospitalFetcher {


	/*
	 * Method to get nearby hospital list using Google Map API.
	 * Arguments : 1) Distance range from the given latitude and longitude.
	 *             2) Integer Array containing latitude and longitude.
	 */
	public static ArrayList<String> predictions(int i,float[] loc) {
		
		//Google map api
		String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
		StringBuilder requestUrl = new StringBuilder(baseUrl);
		float lat = loc[0];
		float lng = loc[1];
		String location1 = lat+","+lng+"&radius=";
		int radius=500*i;
		
		String location2 ="&type=hospital&key=";

		// Key can be generated in Google Console
		String key= "";
		requestUrl.append(location1);
		requestUrl.append(radius);
		requestUrl.append(location2);
		requestUrl.append(key);
		GetMethod method = new GetMethod(requestUrl.toString());
		method.addRequestHeader("Accept", "application/json");

		String response = "";
		HttpClient client = new HttpClient();
		try {
			Integer statusCode = client.executeMethod(method);

			if(statusCode == 200){
				response = method.getResponseBodyAsString();
				ArrayList<String> result = convString(response);
				if(result.size() == 0) {
					return predictions(i+1,loc);
				}
				else {
					return result;
				}
			}
			else{
				return null;
			}
		} catch (Exception e) {
			response = null;
			return  null;
		}


	}


	/*
	 * Method to convert Json object to String Array List
	 */
	public static ArrayList<String> convString(String source) {
		JSONObject obj = new JSONObject(source);
		ArrayList<String> Names = new ArrayList<String>();
		JSONArray res = obj.getJSONArray("results");

		for(int i=0;i<res.length()-1;i++) {
			JSONObject temp =res.getJSONObject(i);
			Names.add(temp.getString("name"));
		}

		return Names;
	}


}