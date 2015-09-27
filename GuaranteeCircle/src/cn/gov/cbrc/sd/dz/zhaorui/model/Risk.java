package cn.gov.cbrc.sd.dz.zhaorui.model;

public class Risk {
	private String level;// 1/2/3

	public Risk(String level) {
		super();
		this.level = level;
	}

	public String toString(){
		return String.valueOf(level);
	}
}