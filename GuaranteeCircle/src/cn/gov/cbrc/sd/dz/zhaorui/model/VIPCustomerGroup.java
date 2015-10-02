package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.util.ArrayList;
import java.util.List;

public class VIPCustomerGroup {

	private String name;

	List<Corporation> corps;

	public VIPCustomerGroup(String groupName) {
		this.name=groupName;
	}

	public void addCorporation(Corporation corporation) {
		if (corps == null)
			corps = new ArrayList<Corporation>();
		corps.add(corporation);
	}
	public List<Corporation> getCorps(){
		return corps;
	}

	public String getName() {
		return this.name;
	}
}
