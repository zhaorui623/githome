package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.awt.Component;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;

public class Corporation {

	public static final String NAME_COL = "客户名称";
	public static final String ORG_CODE_COL = "组织机构代码";
	public static final String LOAN_BANK_COUNT_COL = "贷款银行家数";
	public static final String ADDRESS_COL = "注册地址";
	public static final String REGION_CODE_COL = "行政区划代码";	
	public static final String LOAN_BALANCE_COL = "贷款余额";
	public static final String BULIANG_LOAN_BALANCE_COL = "不良贷款";
	public static final String BULIANG_LV_BALANCE_COL = "不良贷款率";
	public static final String ZHENGCHANG_LOAN_BALANCE_COL = "正常类贷款余额";
	public static final String GUANZHU_LOAN_BALANCE_COL = "关注类贷款余额";
	public static final String CIJI_LOAN_BALANCE_COL = "次级类贷款余额";
	public static final String KEYI_LOAN_BALANCE_COL = "可疑类贷款余额";
	public static final String SUNSHI_LOAN_BALANCE_COL = "损失类贷款余额";
	public static final String YUQI_LOAN_BALANCE_COL = "逾期贷款";
	public static final String YUQI_30DAY_YINEI_LOAN_BALANCE_COL = "逾期30天以内";
	public static final String YUQI_31DAY_90DAY_LOAN_BALANCE_COL = "逾期31_90天";
	public static final String YUQI_91DAY_180DAY_LOAN_BALANCE_COL = "逾期91_180天";
	public static final String YUQI_181DAY_365DAY_LOAN_BALANCE_COL = "逾期181_365天";
	public static final String YUQI_1YEAR_3YEAR_LOAN_BALANCE_COL = "逾期1年_3年";
	public static final String YUQI_3YEAR_YISHANG_LOAN_BALANCE_COL = "逾期3年以上";
	public static final String ZHAIQUAN_COL = "债券";
	public static final String GUQUAN_COL = "股权";
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

	// private static Map<String, Corporation> corps;

	// 摘取法中，用来标记该企业是否为核心企业
	private boolean isCore = false;
	private int level;

	public Corporation(LinkedHashMap<String, Object> datas) {
		this.datas = datas;
	}

	// public static void addCorp(Corporation corp) {
	// if (corps == null)
	// corps = new HashMap<String, Corporation>();
	// corps.put(corp.getStringValue(NAME_COL), corp);
	// }
	//
	// public static void initCorps() {
	// if (corps != null)
	// corps.clear();
	// }

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

	// public static void printCorps() {
	// if (corps == null) {
	// System.out.println("Corporation.corps is null");
	// } else {
	// System.out.println("Corporation.corps={");
	// Set<String> keys = corps.keySet();
	// for (String key : keys)
	// System.out.println(key + "->" + corps.get(key));
	// System.out.println("}");
	// }
	// }

	public String toString() {
		StringBuffer str = new StringBuffer("[");
		Set<String> keys = datas.keySet();
		for (String key : keys) {
			str.append(key + "=" + datas.get(key) + "\t");
		}
		str.append("]");
		return str.toString();
	}

	// public static Collection<Corporation> getAllCorps() {
	// return corps.values();
	// }

	// public static Corporation getCorpByName(String name) {
	// if (corps != null)
	// return corps.get(name);
	// else
	// return null;
	// }

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
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public Corporation clone() {
		Corporation corpClone;
		LinkedHashMap<String, Object> datasClone = new LinkedHashMap<String, Object>();

		Set<String> keys = this.datas.keySet();
		for (String key : keys) {
			Object value = this.datas.get(key);
			Object valueClone = new String(String.valueOf(value));
			datasClone.put(new String(key), valueClone);
		}

		corpClone = new Corporation(datasClone);

		return corpClone;
	}

	public Region getRegion() throws Exception {
		Region region = Config.getRegionByCode(this.getStringValue(REGION_CODE_COL));
		return region;
	}

	public double getLoanBalance() {
		return this.getDoubleValue(LOAN_BALANCE_COL);
	}

	public double getGuanZhuLoanBalance() {
		return this.getDoubleValue(GUANZHU_LOAN_BALANCE_COL);
	}

	public double getBuLiangLoanBalance() {
		return this.getDoubleValue(BULIANG_LOAN_BALANCE_COL);
	}

	public double getYuQi90YiNeiLoanBalance() {
		return this.getDoubleValue(YUQI_30DAY_YINEI_LOAN_BALANCE_COL)
				+ this.getDoubleValue(YUQI_31DAY_90DAY_LOAN_BALANCE_COL);
	}

	public double getOffBalance() {
		double value1=this.getDoubleValue(Corporation.OFF_BALANCE_CD_COL);
		double value2=this.getDoubleValue(Corporation.OFF_BALANCE_XYZ_COL);
		double value3=this.getDoubleValue(Corporation.OFF_BALANCE_BH_COL);
		double value4=this.getDoubleValue(Corporation.OFF_BALANCE_WTDK_COL);
		double value5=this.getDoubleValue(Corporation.OFF_BALANCE_WTTZ_COL);
		double value6=this.getDoubleValue(Corporation.OFF_BALANCE_CN_COL);
		double value7=this.getDoubleValue(Corporation.OFF_BALANCE_XYFXRZYH_COL);
		double value8=this.getDoubleValue(Corporation.OFF_BALANCE_JRYSP_COL);
		double result=value1+value2+value3+value4+value5+value6+value7+value8;
		return result;
	}

	public String getOrgCode() {
		String orgCode=this.getStringValue(ORG_CODE_COL);
		if("null".equals(orgCode)||orgCode==null)
			return "未知";
		else
			return orgCode;
	}

	public int getLoanBankCount() {
		return this.getIntegerValue(LOAN_BANK_COUNT_COL);
	}

	public double getBuLiangLv() {
		return this.getDoubleValue(BULIANG_LV_BALANCE_COL);
	}

	public double getZhengChangLoanBalance() {
		return this.getDoubleValue(ZHENGCHANG_LOAN_BALANCE_COL);
	}

	public double getKeYiLoanBalance() {
		return this.getDoubleValue(KEYI_LOAN_BALANCE_COL);
	}

	public double getCiJiLoanBalance() {
		return this.getDoubleValue(CIJI_LOAN_BALANCE_COL);
	}

	public double getSunShiLoanBalance() {
		return this.getDoubleValue(SUNSHI_LOAN_BALANCE_COL);
	}

	public double getYuQiLoanBalance() {
		return this.getDoubleValue(YUQI_LOAN_BALANCE_COL);
	}

	public double getYuQi30YiNeiLoanBalance() {
		return this.getDoubleValue(YUQI_30DAY_YINEI_LOAN_BALANCE_COL);
	}

	public double getYuQi31_90YiNeiLoanBalance() {
		return this.getDoubleValue(YUQI_31DAY_90DAY_LOAN_BALANCE_COL);
	}

	public double getYuQi91_180YiNeiLoanBalance() {
		return this.getDoubleValue(YUQI_91DAY_180DAY_LOAN_BALANCE_COL);
	}

	public double getYuQi181_365YiNeiLoanBalance() {
		return this.getDoubleValue(YUQI_181DAY_365DAY_LOAN_BALANCE_COL);
	}

	public double getYuQi1Year_3YearYiNeiLoanBalance() {
		return this.getDoubleValue(YUQI_1YEAR_3YEAR_LOAN_BALANCE_COL);
	}

	public double getYuQi3YearYiShangLoanBalance() {
		return this.getDoubleValue(YUQI_3YEAR_YISHANG_LOAN_BALANCE_COL);
	}

	public double getZhaiQuanBalance() {
		return this.getDoubleValue(ZHAIQUAN_COL);
	}

	public double getGuQuanBalance() {
		return this.getDoubleValue(GUQUAN_COL);
	}
	public int hashCode(){
		return String.valueOf(getName()).hashCode();
	}
	public boolean equals(Object o){
		if(o instanceof Corporation&&o!=null){
			Corporation corp=(Corporation)o;
			if(String.valueOf(corp.getName()).equals(String.valueOf(this.getName())))
				return true;
		}
		return false;
	}
}
