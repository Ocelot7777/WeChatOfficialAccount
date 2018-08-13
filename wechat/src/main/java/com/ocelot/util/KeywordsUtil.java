package com.ocelot.util;

public class KeywordsUtil {
	public static String[] getKeys(String str) {
		String[] keys = str.split("\\s+");

		if(keys.length>1)
			return keys;
		else {
			String[] res = {"中国",keys[0]};
			return res;
		}
	}
}
