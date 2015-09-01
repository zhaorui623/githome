package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.io.File;
import java.io.IOException;

public class Windows {

	public static void open(File file) throws IOException, InterruptedException{
		String cmd="rundll32 url.dll FileProtocolHandler file://" +
				file.getAbsolutePath(); 
		Process p = Runtime.getRuntime().exec(cmd); 
	}
}
