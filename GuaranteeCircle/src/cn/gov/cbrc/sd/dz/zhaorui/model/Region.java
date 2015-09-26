package cn.gov.cbrc.sd.dz.zhaorui.model;

import java.util.ArrayList;
import java.util.List;

public class Region {
	private String name;
	private List<String> codes;

	public Region(String name, String... codes) {
		this.name = name;
		this.codes = new ArrayList<String>();
		for (String code : codes)
			this.codes.add(code);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object o) {
		if (o instanceof Region) {
			if (String.valueOf(((Region) o).getName()).equals(String.valueOf(this.getName())))
				return true;
		}
		return false;
	}
}
