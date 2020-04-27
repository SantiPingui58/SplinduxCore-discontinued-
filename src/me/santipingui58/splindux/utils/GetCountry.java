package me.santipingui58.splindux.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import me.santipingui58.splindux.game.SpleefPlayer;


public class GetCountry {


	//Here we can get the country of the player based on their IP adress, using the ip-api.com API. 
	public GetCountry (SpleefPlayer sp) {
		 String jsonS = "";
	        URL url = null;
			try {

				String ip = sp.getIP().replace("/", "");
				
				url = new URL("http://ip-api.com/json/" + ip+ "?fields=message,countryCode");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
	        URLConnection conn = null;
			try {
				conn = url.openConnection();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        try {
				conn.connect();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        String inputLine;

	        try {
				while((inputLine = in.readLine()) != null) {
				    jsonS+=inputLine;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
			JSONObject json = new Gson().fromJson(jsonS, JSONObject.class);
			 sp.setCountry(json.get("countryCode").toString());

	        
	        try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	
}
