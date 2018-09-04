package com.gmail.srthex7.oitc.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {

	public static boolean isEvenNumber(int number) {
		if(number%2 == 0) return true;
		return false;
	}
	
	 public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
	        list.sort(Entry.comparingByValue());

	        Map<K, V> result = new LinkedHashMap<>();
	        for (Entry<K, V> entry : list) {
	            result.put(entry.getKey(), entry.getValue());
	        }

	        return result;
	    }
}
