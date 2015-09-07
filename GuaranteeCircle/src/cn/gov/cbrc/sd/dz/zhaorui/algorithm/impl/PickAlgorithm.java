package cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl;

import org.w3c.dom.Document;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.model.Unit;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;

public class PickAlgorithm extends HugeCircleSplitAlgorithm {
	private int guarantor_floor_value;
	private boolean guarantor_floor_selected;
	private int mutually_guaranteed_floor_value;
	private boolean mutually_guaranteed_floor_selected;
	private int guaranteed_loan_balance_floor_value;
	private Unit guaranteed_loan_balance_floor_unit;
	private boolean guaranteed_loan_balance_floor_selected;
	private int loan_balance_floor_value;
	private Unit loan_balance_floor_unit;
	private boolean loan_balance_floor_selected;
	private boolean one_hand_vertex_all;
	private boolean one_hand_vertex_in_only;
	private boolean one_hand_vertex_none;
	private boolean two_hand_vertex_all;
	private boolean two_hand_vertex_in_only;
	private boolean two_hand_vertex_none;
	private boolean three_hand_vertex_all;
	private boolean three_hand_vertex_in_only;
	private boolean three_hand_vertex_none;
	private boolean unpick_corecorp_son;
	private boolean pick_mutually_guaranteed_corp;
	private boolean pick_corecorp_loop;

	public PickAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected) throws Exception {

		super(algorithm_name, algorithm_enable, algorithm_selected);

		Document doc = Config.getDoc();

		guarantor_floor_value = Integer.parseInt(XMLToolkit.getElementById(doc, "6").getAttribute("value"));
		guarantor_floor_selected = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "6").getAttribute("selected"));

		mutually_guaranteed_floor_value = Integer.parseInt(XMLToolkit.getElementById(doc, "7").getAttribute("value"));
		mutually_guaranteed_floor_selected = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "7").getAttribute("selected"));

		guaranteed_loan_balance_floor_value = Integer
				.parseInt(XMLToolkit.getElementById(doc, "8").getAttribute("value"));
		guaranteed_loan_balance_floor_unit = Unit.valueOf(XMLToolkit.getElementById(doc, "8").getAttribute("unit"));
		guaranteed_loan_balance_floor_selected = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "8").getAttribute("selected"));

		loan_balance_floor_value = Integer.parseInt(XMLToolkit.getElementById(doc, "9").getAttribute("value"));
		loan_balance_floor_unit = Unit.valueOf(XMLToolkit.getElementById(doc, "9").getAttribute("unit"));
		loan_balance_floor_selected = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "9").getAttribute("selected"));

		one_hand_vertex_all = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "12").getAttribute("selected"));
		one_hand_vertex_in_only = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "13").getAttribute("selected"));
		one_hand_vertex_none = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "14").getAttribute("selected"));

		two_hand_vertex_all = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "16").getAttribute("selected"));
		two_hand_vertex_in_only = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "17").getAttribute("selected"));
		two_hand_vertex_none = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "18").getAttribute("selected"));

		three_hand_vertex_all = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "20").getAttribute("selected"));
		three_hand_vertex_in_only = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "21").getAttribute("selected"));
		three_hand_vertex_none = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "22").getAttribute("selected"));

		unpick_corecorp_son = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "23").getAttribute("selected"));
		pick_mutually_guaranteed_corp = Boolean
				.parseBoolean(XMLToolkit.getElementById(doc, "24").getAttribute("selected"));
		pick_corecorp_loop = Boolean.parseBoolean(XMLToolkit.getElementById(doc, "25").getAttribute("selected"));
	}

	public int getGuarantor_floor_value() {
		return guarantor_floor_value;
	}

	public void setGuarantor_floor_value(int guarantor_floor_value) {
		this.guarantor_floor_value = guarantor_floor_value;
	}

	public boolean isGuarantor_floor_selected() {
		return guarantor_floor_selected;
	}

	public void setGuarantor_floor_selected(boolean guarantor_floor_selected) {
		this.guarantor_floor_selected = guarantor_floor_selected;
	}

	public int getMutually_guaranteed_floor_value() {
		return mutually_guaranteed_floor_value;
	}

	public void setMutually_guaranteed_floor_value(int mutually_guaranteed_floor_value) {
		this.mutually_guaranteed_floor_value = mutually_guaranteed_floor_value;
	}

	public boolean isMutually_guaranteed_floor_selected() {
		return mutually_guaranteed_floor_selected;
	}

	public void setMutually_guaranteed_floor_selected(boolean mutually_guaranteed_floor_selected) {
		this.mutually_guaranteed_floor_selected = mutually_guaranteed_floor_selected;
	}

	public int getGuaranteed_loan_balance_floor_value() {
		return guaranteed_loan_balance_floor_value;
	}

	public void setGuaranteed_loan_balance_floor_value(int guaranteed_loan_balance_floor_value) {
		this.guaranteed_loan_balance_floor_value = guaranteed_loan_balance_floor_value;
	}

	public Unit getGuaranteed_loan_balance_floor_unit() {
		return guaranteed_loan_balance_floor_unit;
	}

	public void setGuaranteed_loan_balance_floor_unit(Unit guaranteed_loan_balance_floor_unit) {
		this.guaranteed_loan_balance_floor_unit = guaranteed_loan_balance_floor_unit;
	}

	public boolean isGuaranteed_loan_balance_floor_selected() {
		return guaranteed_loan_balance_floor_selected;
	}

	public void setGuaranteed_loan_balance_floor_selected(boolean guaranteed_loan_balance_floor_selected) {
		this.guaranteed_loan_balance_floor_selected = guaranteed_loan_balance_floor_selected;
	}

	public int getLoan_balance_floor_value() {
		return loan_balance_floor_value;
	}

	public void setLoan_balance_floor_value(int loan_balance_floor_value) {
		this.loan_balance_floor_value = loan_balance_floor_value;
	}

	public Unit getLoan_balance_floor_unit() {
		return loan_balance_floor_unit;
	}

	public void setLoan_balance_floor_unit(Unit loan_balance_floor_unit) {
		this.loan_balance_floor_unit = loan_balance_floor_unit;
	}

	public boolean isLoan_balance_floor_selected() {
		return loan_balance_floor_selected;
	}

	public void setLoan_balance_floor_selected(boolean loan_balance_floor_selected) {
		this.loan_balance_floor_selected = loan_balance_floor_selected;
	}

	public boolean isOne_hand_vertex_all() {
		return one_hand_vertex_all;
	}

	public void setOne_hand_vertex_all(boolean one_hand_vertex_all) {
		this.one_hand_vertex_all = one_hand_vertex_all;
	}

	public boolean isOne_hand_vertex_in_only() {
		return one_hand_vertex_in_only;
	}

	public void setOne_hand_vertex_in_only(boolean one_hand_vertex_in_only) {
		this.one_hand_vertex_in_only = one_hand_vertex_in_only;
	}

	public boolean isOne_hand_vertex_none() {
		return one_hand_vertex_none;
	}

	public void setOne_hand_vertex_none(boolean one_hand_vertex_none) {
		this.one_hand_vertex_none = one_hand_vertex_none;
	}

	public boolean isTwo_hand_vertex_all() {
		return two_hand_vertex_all;
	}

	public void setTwo_hand_vertex_all(boolean two_hand_vertex_all) {
		this.two_hand_vertex_all = two_hand_vertex_all;
	}

	public boolean isTwo_hand_vertex_in_only() {
		return two_hand_vertex_in_only;
	}

	public void setTwo_hand_vertex_in_only(boolean two_hand_vertex_in_only) {
		this.two_hand_vertex_in_only = two_hand_vertex_in_only;
	}

	public boolean isTwo_hand_vertex_none() {
		return two_hand_vertex_none;
	}

	public void setTwo_hand_vertex_none(boolean two_hand_vertex_none) {
		this.two_hand_vertex_none = two_hand_vertex_none;
	}

	public boolean isThree_hand_vertex_all() {
		return three_hand_vertex_all;
	}

	public void setThree_hand_vertex_all(boolean three_hand_vertex_all) {
		this.three_hand_vertex_all = three_hand_vertex_all;
	}

	public boolean isThree_hand_vertex_in_only() {
		return three_hand_vertex_in_only;
	}

	public void setThree_hand_vertex_in_only(boolean three_hand_vertex_in_only) {
		this.three_hand_vertex_in_only = three_hand_vertex_in_only;
	}

	public boolean isThree_hand_vertex_none() {
		return three_hand_vertex_none;
	}

	public void setThree_hand_vertex_none(boolean three_hand_vertex_none) {
		this.three_hand_vertex_none = three_hand_vertex_none;
	}

	public boolean isUnpick_corecorp_son() {
		return unpick_corecorp_son;
	}

	public void setUnpick_corecorp_son(boolean unpick_corecorp_son) {
		this.unpick_corecorp_son = unpick_corecorp_son;
	}

	public boolean isPick_mutually_guaranteed_corp() {
		return pick_mutually_guaranteed_corp;
	}

	public void setPick_mutually_guaranteed_corp(boolean pick_mutually_guaranteed_corp) {
		this.pick_mutually_guaranteed_corp = pick_mutually_guaranteed_corp;
	}

	public boolean isPick_corecorp_loop() {
		return pick_corecorp_loop;
	}

	public void setPick_corecorp_loop(boolean pick_corecorp_loop) {
		this.pick_corecorp_loop = pick_corecorp_loop;
	}

	public boolean updateConfigCache() throws Exception {

		Document doc = Config.getDoc();

		XMLToolkit.getElementById(doc, "6").setAttribute("value", String.valueOf(guarantor_floor_value));
		XMLToolkit.getElementById(doc, "6").setAttribute("selected", String.valueOf(guarantor_floor_selected));

		XMLToolkit.getElementById(doc, "7").setAttribute("value", String.valueOf(mutually_guaranteed_floor_value));
		XMLToolkit.getElementById(doc, "7").setAttribute("selected",
				String.valueOf(mutually_guaranteed_floor_selected));

		XMLToolkit.getElementById(doc, "8").setAttribute("value", String.valueOf(guaranteed_loan_balance_floor_value));
		XMLToolkit.getElementById(doc, "8").setAttribute("unit", String.valueOf(guaranteed_loan_balance_floor_unit));
		XMLToolkit.getElementById(doc, "8").setAttribute("selected",
				String.valueOf(guaranteed_loan_balance_floor_selected));

		XMLToolkit.getElementById(doc, "9").setAttribute("value", String.valueOf(loan_balance_floor_value));
		XMLToolkit.getElementById(doc, "9").setAttribute("unit", String.valueOf(loan_balance_floor_unit));
		XMLToolkit.getElementById(doc, "9").setAttribute("selected", String.valueOf(loan_balance_floor_selected));

		XMLToolkit.getElementById(doc, "12").setAttribute("selected", String.valueOf(one_hand_vertex_all));
		XMLToolkit.getElementById(doc, "13").setAttribute("selected", String.valueOf(one_hand_vertex_in_only));
		XMLToolkit.getElementById(doc, "14").setAttribute("selected", String.valueOf(one_hand_vertex_none));

		XMLToolkit.getElementById(doc, "16").setAttribute("selected", String.valueOf(two_hand_vertex_all));
		XMLToolkit.getElementById(doc, "17").setAttribute("selected", String.valueOf(two_hand_vertex_in_only));
		XMLToolkit.getElementById(doc, "18").setAttribute("selected", String.valueOf(two_hand_vertex_none));

		XMLToolkit.getElementById(doc, "20").setAttribute("selected", String.valueOf(three_hand_vertex_all));
		XMLToolkit.getElementById(doc, "21").setAttribute("selected", String.valueOf(three_hand_vertex_in_only));
		XMLToolkit.getElementById(doc, "22").setAttribute("selected", String.valueOf(three_hand_vertex_none));

		XMLToolkit.getElementById(doc, "23").setAttribute("selected", String.valueOf(unpick_corecorp_son));
		XMLToolkit.getElementById(doc, "24").setAttribute("selected", String.valueOf(pick_mutually_guaranteed_corp));
		XMLToolkit.getElementById(doc, "25").setAttribute("selected", String.valueOf(pick_corecorp_loop));

		return true;
	}

}
