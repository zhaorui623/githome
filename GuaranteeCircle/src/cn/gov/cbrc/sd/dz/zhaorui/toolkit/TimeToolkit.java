package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import cn.gov.cbrc.sd.dz.zhaorui.GC;

public class TimeToolkit {

	public static final SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat formater2=new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static String getRandomTime(){
		Calendar calendar=Calendar.getInstance();
		int random=(int)(Math.random()*1000000);
		calendar.setTimeInMillis(calendar.getTimeInMillis()-random);
		return calendar.getTime().toLocaleString();
	}
	
	public static String getTime(){
		Calendar calendar=Calendar.getInstance();
		return calendar.getTime().toLocaleString();
	}

	public static String getLeftTimeStr(Date time1, Date time2) {	
		
		int time=(int) ((time2.getTime()-time1.getTime())/1000);
		int hour=time/3600;
		time=time-hour*3600;
		int mi=time/60;
		int ss=time-mi*60;		
		ss=ss<0?0:ss;
		
		return (hour<10?("0"+hour):hour)+":"+(mi<10?("0"+mi):mi)+":"+(ss<10?("0"+ss):ss);
	}

	public static Date parse(String time, String errorMessage) {
		Date date=null;
		try {
			date = TimeToolkit.formater.parse(time);
		} catch (ParseException e) {
			try {
				date = TimeToolkit.formater2.parse(time);
			} catch (ParseException e1) {
//				JOptionPane.showMessageDialog(Galileo.getParent(),
//						errorMessage);
				e1.printStackTrace();
			}
		}
		return date;
	}	
	public static String format(Date date){
		String str="";
		if(date!=null)
			str=formater.format(date);
		return str;
	}
}
