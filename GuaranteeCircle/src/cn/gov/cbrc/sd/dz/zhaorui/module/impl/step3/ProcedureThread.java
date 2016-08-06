package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;

public class ProcedureThread extends Thread {
	ProcessPanel processPanel;
	Step3Module step3Module;

	public void start(ProcessPanel object) {
		this.processPanel = object;
		this.step3Module = object.getStep3Module();
		start();
	}

	public void run() {
		try {
			for (int i = 1; i <= processPanel.getProcedureCount(); i++) {
				if(processPanel.isOnlyDoVIP()==true&&(i==1||i==2)){
					InfoPane.getInstance().info("用户选择只做重点客户担保圈分析，跳过" + step3Module.getpPanel().getLabeltexts()[i - 1]);
					continue;					
				}
				if (i == 8 && processPanel.isSkipGraphicGenerate() == true){ // 是否需要跳过担保图生成的步骤
					InfoPane.getInstance().info("根据用户选择，跳过" + step3Module.getpPanel().getLabeltexts()[i - 1]);
					continue;
				}
				if (i == 4 && processPanel.isSkipGraphicMerge() == true) {// 是否需要跳过担保圈合并的步骤
					InfoPane.getInstance().info("根据用户选择，跳过" + step3Module.getpPanel().getLabeltexts()[i - 1]);
					continue;
				}
				if (i == 9 && processPanel.isSkipHugeGraphicGenerate() == true) {// 是否需要跳过超大担保圈图像生成的步骤
					InfoPane.getInstance().info("根据用户选择，跳过" + step3Module.getpPanel().getLabeltexts()[i - 1]);
					continue;
				}
				InfoPane.getInstance().info("开始执行" + step3Module.getpPanel().getLabeltexts()[i - 1]);
				step3Module.getProcedure().setIndex(i);
				step3Module.getProcedure().setPercent(0);
				Step3Module.class.getMethod("procedure" + i).invoke(step3Module);
				step3Module.getProcedure().setPercent(100);
				Thread.sleep(500);
			}
			step3Module.setSucessMark(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "识别过程中遇到异常！", "错误", JOptionPane.ERROR_MESSAGE);
			InfoPane.getInstance().info(e.toString());
			if (e.getCause() != null)
				InfoPane.getInstance().info(e.getCause().toString());
			e.printStackTrace();
		}
	}

	public Procedure getCurrentProcedure() {
		return step3Module.getProcedure();
	}
}
