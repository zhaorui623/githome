package cn.gov.cbrc.sd.dz.zhaorui.model;

public class Scale {

	private String level;// A/B/C
	private double floor;
	private double ceilling;
	
	

	public Scale(String level, double floor, double ceilling) {
		super();
		this.level = level;
		this.floor = floor;
		this.ceilling = ceilling;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public double getFloor() {
		return floor;
	}

	public void setFloor(double floor) {
		this.floor = floor;
	}

	public double getCeilling() {
		return ceilling;
	}

	public void setCeilling(double ceilling) {
		this.ceilling = ceilling;
	}
	
	public String toString(){
		return String.valueOf(level);
	}
}