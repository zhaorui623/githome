package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Corporation {

	public static final String NAME_COL = "客户名称";

	// 字段名->字段值
	private LinkedHashMap<String, Object> datas;

	private static Map<String, Corporation> corps;

	public Corporation(LinkedHashMap<String, Object> datas) {
		this.datas = datas;
	}

	public static void addCorp(Corporation corp) {
		if (corps == null)
			corps = new HashMap<String, Corporation>();
		corps.put(corp.getStringValue(NAME_COL), corp);
	}

	public static void initCorps() {
		if (corps != null)
			corps.clear();
	}

	public String getStringValue(String key) {
		return String.valueOf(datas.get(key));
	}

	public int getIntegerValue(String key) {
		int value = 0;
		try {
			value = Integer.parseInt(getStringValue(key));
		} catch (NumberFormatException e) {
		}
		return value;
	}

	public double getDoubleValue(String key) {
		double value = 0;
		try {
			value = Double.parseDouble(getStringValue(key));
		} catch (NumberFormatException e) {
		}
		return value;
	}

	public static void printCorps() {
		if (corps == null) {
			System.out.println("Corporation.corps is null");
		} else {
			System.out.println("Corporation.corps={");
			Set<String> keys = corps.keySet();
			for (String key : keys)
				System.out.println(key + "->" + corps.get(key));
			System.out.println("}");
		}
	}

	public String toString() {
		StringBuffer str = new StringBuffer("[");
		Set<String> keys = datas.keySet();
		for (String key : keys) {
			str.append(key + "=" + datas.get(key) + "\t");
		}
		str.append("]");
		return str.toString();
	}

	public static Collection<Corporation> getAllCorps() {
		return corps.values();
	}

	public static Corporation getCorpByName(String name) {
		if (corps != null)
			return corps.get(name);
		else
			return null;
	}

	public static Corporation createDefaultCorp(String corpName) {

		LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
		datas.put(NAME_COL, corpName);
		Corporation corp = new Corporation(datas);
		return corp;
	}

	public String getName() {
		return getStringValue(NAME_COL);
	}
}
