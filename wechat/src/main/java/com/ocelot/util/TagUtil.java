package com.ocelot.util;

public class TagUtil {
	public static String customTag(String tag) {
		String[] result = tag.split(",", 5);
		
		StringBuffer sb = new StringBuffer(result[0]);
		for(int i=1;i<result.length;i++) {
			sb.append("," + result[i]);
		}
		
		return sb.toString();
	}
}
