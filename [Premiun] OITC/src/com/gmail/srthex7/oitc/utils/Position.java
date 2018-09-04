package com.gmail.srthex7.oitc.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Position {

	public static String getPositon(int position, HashMap<String,Integer> hash) {
		List<String> keys = new ArrayList<>();
		HashMap<String,Integer> ss = new HashMap<>();
		
		boolean order = true;
		while (order) {
			order = false;
			int max = 0;
			String p = "";
			for (String key : hash.keySet()) {
				if (hash.get(key) > max) {
					p = key;
					max = hash.get(key);
				}
			} 
		}
		
		
		return "";
	}
	
	public static String getPositionFormat(String playername, int score) {
		return "<playername> : <score>".replaceAll("<playername>", playername).replaceAll("<score>", ""+score);
	}
	
	public static ArrayList<String> orderMapToList(HashMap<String, Integer> map) {
		ArrayList<String> result = new ArrayList<String>();
		
		
		return result;
	}
	
	
}
