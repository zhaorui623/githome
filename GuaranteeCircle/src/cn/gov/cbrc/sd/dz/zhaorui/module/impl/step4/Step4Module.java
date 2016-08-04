package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.component.ProgressBar;
import cn.gov.cbrc.sd.dz.zhaorui.exception.ErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;

public class Step4Module extends Module {

	ResultPanel resultPanel;
	public static boolean shouldRefreshResult = false;
	private int progress = 0;

	public Step4Module(String id, GC gc, String name, String iconName) {
		super(id, gc, name, iconName);
	}

	@Override
	protected List<Component> getTabs() {
		if (tabs == null) {
			tabs = new ArrayList<Component>();
			// "第四步：查看结果"选项卡
			resultPanel = new ResultPanel(this);
			tabs.add(resultPanel);
		}
		try {
			if (shouldRefreshResult == true) {
				Thread generatTableThread = new Thread() {
					public void run() {
						try {
							
							resultPanel.refreshResult(((Step3Module) gc.getModule("3")).getResultGraphics());

						} catch (Exception e) {
							new ErrorDialog("生成报表时遇到异常", e);
							InfoPane.getInstance().error(e.toString());
							e.printStackTrace();
						}
						shouldRefreshResult = false;
					};
				};
				final ProgressBar bar = new ProgressBar((JFrame) GC.getParent(), "报表生成进程");
				bar.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				bar.setEnabled(false);
				bar.setAlwaysOnTop(true);
				bar.setVisible(true);
				Thread refreshProgressThread = new Thread() {
					public void run() {
						int max=12;
						Step4Module.this.setProgress(0);
						while (true) {
							int currentProgress=Step4Module.this.getProgress();
							bar.setProgressBarValue(currentProgress, 0, max);
							if(currentProgress==max){
								bar.dispose();
								return;
							}
							try {
								sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
				};
				refreshProgressThread.start();
				generatTableThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tabs;
	}

	protected int getProgress() {

		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	protected void initMenuItems() {
		menuitems.add(new JMenuItem(this.getName()));
	}

	public GC getGC() {
		return this.gc;
	}

}
