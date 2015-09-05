package cn.gov.cbrc.sd.dz.zhaorui.algorithm;

import javax.swing.JPanel;

public class HugeCircleSplitAlgorithm {
	
	protected static int huge_circle_vertex_floor;

	protected String algorithm_name;

	protected boolean algorithm_enable;

	protected boolean algorithm_selected;
	
	protected JPanel algorithmConfigPanel;

	public HugeCircleSplitAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected) {
		super();
		this.algorithm_name = algorithm_name;
		this.algorithm_enable = algorithm_enable;
		this.algorithm_selected = algorithm_selected;
	}

	public String getAlgorithm_name() {
		return algorithm_name;
	}

	public void setAlgorithm_name(String algorithm_name) {
		this.algorithm_name = algorithm_name;
	}

	public boolean isAlgorithm_enable() {
		return algorithm_enable;
	}

	public void setAlgorithm_enable(boolean algorithm_enable) {
		this.algorithm_enable = algorithm_enable;
	}

	public boolean isAlgorithm_selected() {
		return algorithm_selected;
	}

	public void setAlgorithm_selected(boolean algorithm_selected) {
		this.algorithm_selected = algorithm_selected;
	}

	public JPanel getAlgorithmConfigPanel() {
		return algorithmConfigPanel;
	}

	public void setAlgorithmConfigPanel(JPanel algorithmConfigPanel) {
		this.algorithmConfigPanel = algorithmConfigPanel;
	}
	
	
	
	
}
