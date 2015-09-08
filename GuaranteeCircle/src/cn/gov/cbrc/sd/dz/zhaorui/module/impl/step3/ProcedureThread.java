package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcedureThread extends Thread {

	Step3Module obj;

	public void start(Step3Module object) {
		this.obj = object;
		start();
	}

	public void run() {
		try {
			for (int i = 1; i <= Step3Module.PROCEDURE_COUNT; i++){
				obj.getProcedure().setIndex(i);
				obj.getProcedure().setPercent(0);
				Step3Module.class.getMethod("procedure" + i).invoke(obj);
				obj.getProcedure().setPercent(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Procedure getCurrentProcedure() {
		return obj.getProcedure();
	}
}
