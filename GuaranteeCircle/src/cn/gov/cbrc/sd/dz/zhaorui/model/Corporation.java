package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Corporation {

	public static final String NAME_COL = "客户名称";
	public static final String LOAN_BALANCE_COL = "贷款余额";
	public static final String REGION_CODE_COL = "行政区划代码";
	public static final String GUANZHU_LOAN_BALANCE_COL="关注类贷款余额";
	public static final String BULIANG_LOAN_BALANCE_COL = "不良贷款";
	public static final String YUQI30YINEI_LOAN_BALANCE_COL = "逾期30天以内";
	public static final String YUQI31_90_LOAN_BALANCE_COL = "逾期31_90天";
	public static final String OFF_BALANCE_ZQ_COL = "债券";
	public static final String OFF_BALANCE_GQ_COL = "股权";
	public static final String OFF_BALANCE_CD_COL = "银行承兑汇票";
	public static final String OFF_BALANCE_XYZ_COL = "信用证";
	public static final String OFF_BALANCE_BH_COL = "保函";
	public static final String OFF_BALANCE_WTDK_COL = "委托贷款";
	public static final String OFF_BALANCE_WTTZ_COL = "委托投资";
	public static final String OFF_BALANCE_CN_COL = "承诺";
	public static final String OFF_BALANCE_XYFXRZYH_COL = "信用风险仍在银行的销售与购买协议";
	public static final String OFF_BALANCE_JRYSP_COL = "金融衍生品";
	

	// 字段名->字段值
	private LinkedHashMap<String, Object> datas;

//	private static Map<String, Corporation> corps;
	
	//摘取法中，用来标记该企业是否为核心企业
	private boolean isCore=false;
	private int level;

	public Corporation(LinkedHashMap<String, Object> datas) {
		this.datas = datas;
	}

//	public static void addCorp(Corporation corp) {
//		if (corps == null)
//			corps = new HashMap<String, Corporation>();
//		corps.put(corp.getStringValue(NAME_COL), corp);
//	}
//
//	public static void initCorps() {
//		if (corps != null)
//			corps.clear();
//	}

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

//	public static void printCorps() {
//		if (corps == null) {
//			System.out.println("Corporation.corps is null");
//		} else {
//			System.out.println("Corporation.corps={");
//			Set<String> keys = corps.keySet();
//			for (String key : keys)
//				System.out.println(key + "->" + corps.get(key));
//			System.out.println("}");
//		}
//	}

	public String toString() {
		StringBuffer str = new StringBuffer("[");
		Set<String> keys = datas.keySet();
		for (String key : keys) {
			str.append(key + "=" + datas.get(key) + "\t");
		}
		str.append("]");
		return str.toString();
	}

//	public static Collection<Corporation> getAllCorps() {
//		return corps.values();
//	}

//	public static Corporation getCorpByName(String name) {
//		if (corps != null)
//			return corps.get(name);
//		else
//			return null;
//	}

	public static Corporation createDefaultCorp(String corpName) {

		LinkedHashMap<String, Object> datas = new LinkedHashMap<String, Object>();
		datas.put(NAME_COL, corpName);
		Corporation corp = new Corporation(datas);
		return corp;
	}

	
	
	public boolean isCore() {
		return isCore;
	}

	public void setCore(boolean isCore) {
		this.isCore = isCore;
	}

	public String getName() {
		return getStringValue(NAME_COL);
	}

	public void setLevel(int level) {
		this.level=level;		
	}
	public int getLevel(){
		return level;
	}
	public Corporation clone(){
		Corporation corpClone;
		LinkedHashMap<String, Object> datasClone = new LinkedHashMap<String, Object>();
		
		Set<String> keys=this.datas.keySet();
		for(String key:keys){
			Object value=this.datas.get(key);
			Object valueClone=new String(String.valueOf(value));
			datasClone.put(new String(key), valueClone);
		}
		
		corpClone = new Corporation(datasClone);
		
		return corpClone;
	}
}
