package cn.gov.cbrc.sd.dz.zhaorui.model;

public enum RegionLevel {
	省,市,县;
	
	public int getLength() throws Exception{
		if(省.equals(this))
			return 2;
		else if(市.equals(this))
			return 4;
		else if(县.equals(this))
			return 6;
		else
			throw new Exception("无效的地区级别:"+String.valueOf(this));
	}
}
