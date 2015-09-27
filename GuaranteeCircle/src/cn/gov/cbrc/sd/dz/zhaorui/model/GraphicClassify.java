package cn.gov.cbrc.sd.dz.zhaorui.model;

public class GraphicClassify {

	private Scale scale;
	private Risk risk;

	public GraphicClassify(Scale scale, Risk risk) {
		this.scale = scale;
		this.risk = risk;
	}

	public String getName() {
		return String.valueOf(scale) + String.valueOf(risk);
	}

	public boolean equals(Object o) {
		if (o instanceof GraphicClassify) {
			String nameThis=String.valueOf(this.getName());
			String nameO=String.valueOf(((GraphicClassify) o).getName());
			if (nameThis.equals(nameO))
				return true;
		}
		return false;
	}
	public String toString(){
		return getName();
	}

    public int hashCode(){
		return String.valueOf(this.toString()).hashCode();
	}
}
