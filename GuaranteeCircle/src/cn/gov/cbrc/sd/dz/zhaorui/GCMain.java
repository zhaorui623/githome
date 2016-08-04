package cn.gov.cbrc.sd.dz.zhaorui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import cn.gov.cbrc.sd.dz.zhaorui.exception.ErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GCLogger;

public class GCMain {
	/**
	 * the constructor of the class
	 * 
	 * @param args
	 * 
	 */

	public static final String TITLE = "担保圈自动识别程序v16.7.28";

	Logger logger = GCLogger.getInstance(this.getClass());

	public GCMain(String[] args) {
		try {
			// creating the GC object
			final GC gc = new GC(args);

			// creating the parent frame
			JFrame mainFrame = new JFrame();
			mainFrame.setTitle(TITLE);

			// handling the close case
			mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			mainFrame.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent evt) {

					gc.exit();
				}
			});

			// intializing the galileo
			gc.init(mainFrame, true, true, true, true, null);
		} catch (Throwable e) {
			logger.error(e.toString());
			e.printStackTrace();
		}

	}

	/**
	 * the main method
	 */
	public static void main(String[] args) {
		try {
			new GCMain(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
