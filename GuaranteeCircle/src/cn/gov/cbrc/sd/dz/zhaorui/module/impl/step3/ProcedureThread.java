package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;

public class ProcedureThread extends Thread {

	Step3Module step3Module;

	public void start(Step3Module object) {
		this.step3Module = object;
		start();
	}

	public void run() {
		try {
			for (int i = 1; i <= Step3Module.PROCEDURE_COUNT; i++) {
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
