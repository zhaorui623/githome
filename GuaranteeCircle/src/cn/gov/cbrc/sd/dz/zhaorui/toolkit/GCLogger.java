package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;

public class GCLogger {

	private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cn.gov.cbrc.sd.dz.zhaorui");

	static{
		BasicConfigurator.configure();
		
//		logger.setLevel(Level.INFO);
//		logger.setLevel(Level.DEBUG);
		logger.setLevel(Level.ERROR);
//		logger.setLevel(Level.OFF);
	}
	
	@SuppressWarnings("unchecked")
	public static org.apache.log4j.Logger getInstance(Class className){
		org.apache.log4j.Logger l = org.apache.log4j.Logger
		.getLogger(className);
		if (l.getAppender("log") == null) {
			org.apache.log4j.DailyRollingFileAppender appender = new DailyRollingFileAppender();
			appender.setName("log");
			appender.setLayout(new SimpleLayout());
			appender.setAppend(true);
			appender.setDatePattern("'.'yyyy-MM-dd");
			String log=System.getProperty("user.dir")+"\\log.log";
			appender.setFile(log);
			appender.activateOptions();
			l.addAppender(appender);
		}
		
		return l;
	}
}
