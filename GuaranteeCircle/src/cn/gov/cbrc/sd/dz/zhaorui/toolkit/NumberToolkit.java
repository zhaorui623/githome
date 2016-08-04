package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.text.DecimalFormat;

public class NumberToolkit {

	public final static DecimalFormat    df   = new DecimalFormat("######0.00");  

	//保留兩位小數
	public static double format(double d){
		return Double.parseDouble(df.format(d));
	}
	//保留兩位小數
	public static String formatStr(double d){
		return df.format(d);
	}
}
