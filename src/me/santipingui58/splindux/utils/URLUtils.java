package me.santipingui58.splindux.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;


public class URLUtils {

	private static URLUtils manager;	
	 public static URLUtils getURLUtils() {
	        if (manager == null)
	        	manager = new URLUtils();
	        return manager;
	    }
	
	
	//Here we can get the country of the player based on their IP adress, using the ip-api.com API. 
	public String getCountry (String ip) {
		 String jsonS = "";
	        URL url = null;
			try {
				 ip = ip.replace("/", "");
				if (ip.equalsIgnoreCase("127.0.0.1") || ip.contains("192.168.0")) {
					return "AR";
				}
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
			return json.get("countryCode").toString();

	}

	
public boolean onNameMCVote(SpleefPlayer sp) {	
		UUID uuid = sp.getUUID();
		 boolean ret = false;
		try { 
			URL url = new URL("https://api.namemc.com/server/mc.splindux.com/likes?profile="+uuid.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	        String str = null;
	        while ((str = in.readLine()) != null) {
	            str = str.toLowerCase();
	            if (str.contains("true")) {
	                ret = true;
	                break;
	            }
	        }
	        in.close();
	} catch (Exception e) {}
		return ret;

}



	
	
	
}
