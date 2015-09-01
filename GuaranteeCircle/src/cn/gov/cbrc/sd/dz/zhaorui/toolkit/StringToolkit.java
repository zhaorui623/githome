package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;

public class StringToolkit {

	private final static String illegalCharacters[] = { ";", "'", "\"", ">",
			"<", ",", ":", "[", "]", "{", "}", "\\", "|", "+", "=", ")", "(",
			"_", "*", "&", "^", "%", "$", "#", "!", "~", "`", "/", "?" };
	public final static String ERROR = "ERROR";

	private static StringBuilder illegalCharactersStr = null;

	private static java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.00");

	public static boolean illegalCharacterCheck(String str) {
		if (str == null || str.length() == 0)
			return true;
		for (String illegal : illegalCharacters)
			if (str.contains(illegal))
				return false;
		return true;
	}

	public static String getAllIllegalCharacterStr() {
		if (illegalCharactersStr == null) {
			illegalCharactersStr = new StringBuilder();
			for (String illegal : illegalCharacters)
				illegalCharactersStr.append(illegal);
			illegalCharactersStr.append(" ");
			illegalCharactersStr
					.deleteCharAt(illegalCharactersStr.length() - 1);
		}
		return illegalCharactersStr.toString();
	}

	// �?��字符串形式的属�?值是否合�?
	// 判断依据：无非法字符，长度不�?,长度超过50上线时要在infoPane输出截断信息
	// 若出现非法字符或长度�?，则返回false
	public static String checkPropertyValueIllegal(String propertyName,
			String propertyValue) {

		if (StringToolkit.illegalCharacterCheck(propertyValue) == false) {
			JOptionPane.showMessageDialog(GC.getParent(), propertyName
					+ "中含有非法字符" + propertyName + "中不得出现下列字符"
					+ StringToolkit.getAllIllegalCharacterStr() + ")");
			return ERROR;
		}
		if (propertyValue == null || propertyValue.length() == 0) {
			JOptionPane.showMessageDialog(GC.getParent(), "请输入"
					+ propertyName + "");
			return ERROR;
		}
		if (propertyValue.length() > 50) {
			InfoPane.getInstance().info(propertyName + "的长度超过了上限(50),超出部分将被截去");
			propertyValue = propertyValue.substring(0, 50);
		}

		return propertyValue;
	}

	public static String checkIP(String ip) {
		return checkIP(ip, false);
	}

	public static String checkIP(String ip, boolean isMask) {
		StringBuilder newIP = new StringBuilder("");
		String values[] = ip.split("\\.");
		if (values.length != 4)
			return ERROR;
		for (int i = 0; i < 4; i++) {
			String value = values[i];
			int var;
			try {
				var = Integer.parseInt(value);
				if (i == 0 && var == 0)
					return ERROR;
			} catch (NumberFormatException e) {
				return ERROR;
			}
			if (!(var >= 0 && var <= 255))
				return ERROR;
			newIP.append("." + var);
		}
		newIP.delete(0, 1);

		if (isMask == true) {
			int ipvalue = IPToolkit.getValue(newIP.toString());
			String binStr = Integer.toBinaryString(ipvalue);
			if (binStr.contains("01"))
				return ERROR;
		}

		return newIP.toString();
	}

	public static void main(String[] args) {
		String sql="select ratesum,count(*) count,logicnbindex from (select sum(rate) ratesum,l.type(NOLock),logicnbindex from iubsigatmlink l(NOLock),logicnb ln(NOLock) where ln.indexx=l.logicnbindex and ln.rncindex=l.rncindex and l.userid='teacher' group by logicnbindex ,l.type) a group by logicnbindex,ratesum";
		System.out.println(StringToolkit.count(sql, "from"));
		System.out.println(sql);
		
	}

	public static boolean checkMAC(String mac, int length) {

		int count = length / 4 / 2;
		String values[] = mac.split("-");
		if (values.length != count)
			return false;
		for (String value : values)
			if (value.length() != 2)
				return false;

		mac = mac.toUpperCase();

		for (int i = 0; i < mac.length(); i++) {
			char ch = mac.charAt(i);
			if (!((ch >= '0' && ch <= '9') || ch == '-' || (ch >= 'A' && ch <= 'F')))
				return false;
		}

		return true;
	}

	public static double formatDouble(double number) {
		return 	Double.parseDouble(df.format(number));		
	}

	//随即生成length位十六进制数，以-两两分隔
	public static String getRandomMac(int length) {
		StringBuilder sb=new StringBuilder();
		char[] chars={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		for(int i=1;i<=length;i++){
			int random=(int)(Math.random()*chars.length);
			sb.append(chars[random]);
			if(i%2==0&&i!=length)
				sb.append("-");
		}
		return sb.toString();
	}

	//数数str里有几个s
	public static int count(String str, String s) {
		int count=0;
		while(str.contains(s)){
			count++;
			str=str.replaceFirst(s, "");
		}
		return count;
	}
}
