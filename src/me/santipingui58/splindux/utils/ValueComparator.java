package me.santipingui58.splindux.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class ValueComparator implements Comparator<String>{
	 
	HashMap<String, Integer> map = new HashMap<String, Integer>();
 
	public ValueComparator(TreeMap<String, Integer> map2){
		this.map.putAll(map2);
	}
 
	@Override
	public int compare(String s1, String s2) {
		if(map.get(s1) >= map.get(s2)){
			return -1;
		}else{
			return 1;
		}	
	}
	
	

}