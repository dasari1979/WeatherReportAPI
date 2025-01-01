package com.weather.api.report.weatherreport.countrycodes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weather.api.report.weatherreport.util.UserDefinedVariables;

public class CountryCodes {

	private Map<String, String> countryCodes;
	
	public CountryCodes() {
		
		this.countryCodes = new LinkedHashMap<>();
	    for (String iso : Locale.getISOCountries()) {
	        Locale l = new Locale("", iso);
	        countryCodes.put(l.getDisplayCountry(), iso);
	    }

	}
	
	public String getCountyName(String zipCode) throws IOException, JSONException {

		// Creating an object of URL class
        URL u = new URL("https://api.postalpincode.in/pincode/"+zipCode);
        // communicate between application and URL
        URLConnection urlconnect = u.openConnection();
        // for our application streams to be read
        BufferedReader br = new BufferedReader(new InputStreamReader(urlconnect.getInputStream()));
        // Declaring an integer variable
        String readAPIResponse = "";
        StringBuilder builder = new StringBuilder();
        // Till the time URL is being read
        while ((readAPIResponse = br.readLine()) != null) {
        	builder.append(readAPIResponse);
        }
        JSONArray jsonArray = new JSONArray(builder.toString().trim());
        String cuntryName = "";
        for (int i =0;i<jsonArray.length();i++) {
        	if(jsonArray.getJSONObject(i).isNull(UserDefinedVariables.POSTOFFICE))
            throw new NullPointerException("OR Server down");
        	
        	JSONArray jsonObj = jsonArray.getJSONObject(i).getJSONArray(UserDefinedVariables.POSTOFFICE);	
        	if(jsonObj.getJSONObject(i) instanceof JSONObject) {
	        	JSONObject arrayOfJson = jsonObj.getJSONObject(i);
	        	if(arrayOfJson.get(UserDefinedVariables.COUNTRY) != null)
	        	cuntryName = arrayOfJson.get(UserDefinedVariables.COUNTRY).toString();
	        	break;
              }
        }
		return cuntryName;
	}
	public String getCountryCode(String countryName) {
		
	String country = countryCodes.entrySet().stream().filter(name->name.getKey().equalsIgnoreCase(countryName)).findAny().get().getValue();
	if(country.isEmpty() || country == null)
        throw new NullPointerException("OR server down");
	return country;
		
	}
}
