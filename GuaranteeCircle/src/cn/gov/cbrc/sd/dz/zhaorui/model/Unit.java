package cn.gov.cbrc.sd.dz.zhaorui.model;

public enum Unit {
	亿元,万元;
	
	public int getMultiple(Unit u) throws Exception{
		if(亿元.equals(u))
			return 10000;
		else if(万元.equals(u))
			return 1;
		else
			throw new Exception("无效的金额单位:"+String.valueOf(u));
	}
}
