package edu.pku.test.expression.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DataCache {
	private static List<BigDecimal> bigDecimalCache = new ArrayList<BigDecimal>();

	private static List<String> stringCache = new ArrayList<String>();

	private static List<Character> charCache = new ArrayList<Character>();

	private static List<Calendar> dateCache = new ArrayList<Calendar>();

	public static int getBigDecimalIndex(BigDecimal val) {
		int index = bigDecimalCache.indexOf(val);
		if (index < 0) {
			bigDecimalCache.add(val);
			return bigDecimalCache.size() - 1;
		}
		return index;
	}

	public static BigDecimal getBigDecimalValue(int index) {
		if (index < 0 || index >= bigDecimalCache.size())
			return null;
		return bigDecimalCache.get(index);
	}

	public static int getStringIndex(String val) {
		int index = stringCache.indexOf(val);
		if (index < 0) {
			stringCache.add(val);
			return stringCache.size() - 1;
		}
		return index;
	}

	public static String getStringValue(int index) {
		if (index < 0 || index >= stringCache.size())
			return null;
		return stringCache.get(index);
	}

	public static int getCharIndex(Character val) {
		int index = charCache.indexOf(val);
		if (index < 0) {
			charCache.add(val);
			return charCache.size() - 1;
		}
		return index;
	}

	public static Character getCharValue(int index) {
		if (index < 0 || index >= charCache.size())
			return null;
		return charCache.get(index);
	}

	public static int getDateIndex(Calendar val) {
		int index = dateCache.indexOf(val);
		if (index < 0) {
			dateCache.add(val);
			return dateCache.size() - 1;
		}
		return index;
	}

	public static Calendar getDateValue(int index) {
		if (index < 0 || index >= dateCache.size())
			return null;
		return dateCache.get(index);
	}

	public static int getBooleanIndex(Boolean val) {
		return val ? 1 : 0;
	}

	public static Boolean getBooleanValue(int index) {
		if (index < 0)
			return null;
		return index == 0 ? false : true;
	}

}
