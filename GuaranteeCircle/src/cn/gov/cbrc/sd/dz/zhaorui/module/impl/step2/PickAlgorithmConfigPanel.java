package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl.PickAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.component.NumberSpinner;
import cn.gov.cbrc.sd.dz.zhaorui.component.UnitComboBox;
import cn.gov.cbrc.sd.dz.zhaorui.model.Unit;

public class PickAlgorithmConfigPanel extends JPanel {

	private PickAlgorithm algorithm;
	private JCheckBox guarantorFloorCheck,outGuarantorFloorCheck, mutuallyGuarantorFloorCheck, /*guaranteedLoanBalanceFloorCheck,*/
			loanBalanceFloorCheck, unpickCorecorpSonCheck, pickMutuallyGuaranteedCorp, pickCorecorpLoop;
	private JComboBox conditionNumberCombo, guarantorFloorCombo,outGuarantorFloorCombo, mutuallyGuarantorFloorCombo;
	private NumberSpinner guaranteedLoanBalanceFloorSpinner,outGuaranteedLoanBalanceFloorSpinner, loanBalanceFloorSpinner;
	private UnitComboBox guaranteedLoanBalanceFloorUnitCombo,outGuaranteedLoanBalanceFloorUnitCombo, loanBalanceFloorUnitCombo;
	private JRadioButton radio1_1, radio1_2, radio1_3, radio2_1, radio2_2, radio2_3, radio3_1, radio3_2, radio3_3;

	public PickAlgorithmConfigPanel() {
		this.setLayout(new GridLayout(1, 2));
	}

	public void init() {
		addPanel1();
		addPanel2();

	}

	private void addPanel1() {
		JPanel p1 = new JPanel(new GridLayout(5, 1));
		p1.setBorder(BorderFactory.createTitledBorder("拆分规则-核心企业的认定"));
		// =====================================================
		JPanel p1_0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel instruction1 = new JLabel("以下条件满足其中");
		Integer[] conditionNumberValues = new Integer[4];
		for (int i = 0; i < conditionNumberValues.length; i++)
			conditionNumberValues[i] = new Integer(i + 1);
		conditionNumberCombo = new JComboBox(conditionNumberValues);
		conditionNumberCombo.setSelectedItem(algorithm.getCondition_number_value());
		JLabel instruction2 = new JLabel("个，即被认定为“核心企业”,则摘取以它为核心的担保圈：");
		p1_0.add(instruction1);
		p1_0.add(conditionNumberCombo);
		p1_0.add(instruction2);
		p1.add(p1_0);
		// =====================================================
		JPanel p1_4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		loanBalanceFloorCheck = new JCheckBox("某企业贷款余额超过");
		loanBalanceFloorCheck.setSelected(algorithm.isLoan_balance_floor_selected());
		p1_4.add(loanBalanceFloorCheck);
		loanBalanceFloorSpinner = new NumberSpinner(algorithm.getLoan_balance_floor_value(), 0, Integer.MAX_VALUE, 1);
		p1_4.add(loanBalanceFloorSpinner);
		loanBalanceFloorUnitCombo = new UnitComboBox();
		loanBalanceFloorUnitCombo.setSelectedItem(algorithm.getLoan_balance_floor_unit());
		p1_4.add(loanBalanceFloorUnitCombo);
		p1.add(p1_4);
		// =====================================================
		JPanel p1_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		guarantorFloorCheck = new JCheckBox("某企业被");
		guarantorFloorCheck.setSelected(algorithm.isGuarantor_floor_selected());
		p1_1.add(guarantorFloorCheck);
		Integer[] guarantorFloorValues = new Integer[11];
		for (int i = 0; i < guarantorFloorValues.length; i++)
			guarantorFloorValues[i] = new Integer(i);
		guarantorFloorCombo = new JComboBox(guarantorFloorValues);
		guarantorFloorCombo.setSelectedItem(algorithm.getGuarantor_floor_value());
		p1_1.add(guarantorFloorCombo);
		p1_1.add(new JLabel("家（含）以上企业担保 并且 被担保贷款总余额超过"));
//		p1.add(p1_1);

		// =====================================================
//		JPanel p1_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		guaranteedLoanBalanceFloorCheck = new JCheckBox(" ");
//		guaranteedLoanBalanceFloorCheck.setSelected(algorithm.isGuaranteed_loan_balance_floor_selected());
//		p1_1.add(guaranteedLoanBalanceFloorCheck);
		guaranteedLoanBalanceFloorSpinner = new NumberSpinner(algorithm.getGuaranteed_loan_balance_floor_value(), 0,
				Integer.MAX_VALUE, 1);
		p1_1.add(guaranteedLoanBalanceFloorSpinner);
		guaranteedLoanBalanceFloorUnitCombo = new UnitComboBox();
		guaranteedLoanBalanceFloorUnitCombo.setSelectedItem(algorithm.getGuaranteed_loan_balance_floor_unit());
		p1_1.add(guaranteedLoanBalanceFloorUnitCombo);
//		guaranteedLoanBalanceFloorCheck.setEnabled(false);
//		guaranteedLoanBalanceFloorSpinner.setEnabled(false);
//		guaranteedLoanBalanceFloorUnitCombo.setEnabled(false);
		p1.add(p1_1);
		// =====================================================
		

		// =====================================================
		JPanel p1_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		outGuarantorFloorCheck = new JCheckBox("某企业为");
		outGuarantorFloorCheck.setSelected(algorithm.isOut_guarantor_floor_selected());
		p1_5.add(outGuarantorFloorCheck);
		Integer[] outGuarantorFloorValues = new Integer[11];
		for (int i = 0; i < outGuarantorFloorValues.length; i++)
			outGuarantorFloorValues[i] = new Integer(i);
		outGuarantorFloorCombo = new JComboBox(outGuarantorFloorValues);
		outGuarantorFloorCombo.setSelectedItem(algorithm.getOut_guarantor_floor_value());
		p1_5.add(outGuarantorFloorCombo);
		p1_5.add(new JLabel("家（含）以上企业提供担保 并且 对外担保贷款总余额超过"));
		outGuaranteedLoanBalanceFloorSpinner = new NumberSpinner(algorithm.getOut_guaranteed_loan_balance_floor_value(), 0,
				Integer.MAX_VALUE, 1);
		p1_5.add(outGuaranteedLoanBalanceFloorSpinner);
		outGuaranteedLoanBalanceFloorUnitCombo = new UnitComboBox();
		outGuaranteedLoanBalanceFloorUnitCombo.setSelectedItem(algorithm.getOut_guaranteed_loan_balance_floor_unit());
		p1_5.add(outGuaranteedLoanBalanceFloorUnitCombo);
		p1.add(p1_5);
		// =====================================================
		
		JPanel p1_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mutuallyGuarantorFloorCheck = new JCheckBox("某企业与");
		mutuallyGuarantorFloorCheck.setSelected(algorithm.isMutually_guaranteed_floor_selected());
		p1_2.add(mutuallyGuarantorFloorCheck);
		Integer[] mutuallyGuarantorFloorValues = new Integer[11];
		for (int i = 0; i < mutuallyGuarantorFloorValues.length; i++)
			mutuallyGuarantorFloorValues[i] = new Integer(i);
		mutuallyGuarantorFloorCombo = new JComboBox(mutuallyGuarantorFloorValues);
		mutuallyGuarantorFloorCombo.setSelectedItem(algorithm.getMutually_guaranteed_floor_value());
		p1_2.add(mutuallyGuarantorFloorCombo);
		p1_2.add(new JLabel("家（含）以上企业存在互保关系"));
		p1.add(p1_2);
		
		this.add(p1);
	}

	private void addPanel2() {
		JPanel p2 = new JPanel(new GridLayout(6, 1));
		p2.setBorder(BorderFactory.createTitledBorder("拆分规则-担保圈摘取时的规模控制"));
		// =====================================================
		JPanel p2_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2_1.add(new JLabel("1手圈摘取规则："));
		ButtonGroup group1 = new ButtonGroup();
		radio1_1 = new JRadioButton("全取");
		radio1_1.setSelected(algorithm.isOne_hand_vertex_all());
		p2_1.add(radio1_1);
		group1.add(radio1_1);
		radio1_2 = new JRadioButton("只取为核心企业提供担保的企业");
		radio1_2.setSelected(algorithm.isOne_hand_vertex_in_only());
		p2_1.add(radio1_2);
		group1.add(radio1_2);
		radio1_3 = new JRadioButton("全不取");
		radio1_3.setSelected(algorithm.isOne_hand_vertex_none());
		p2_1.add(radio1_3);
		group1.add(radio1_3);
		p2.add(p2_1);
		// =====================================================
		JPanel p2_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2_2.add(new JLabel("2手圈摘取规则："));
		ButtonGroup group2 = new ButtonGroup();
		radio2_1 = new JRadioButton("全取");
		radio2_1.setSelected(algorithm.isTwo_hand_vertex_all());
		p2_2.add(radio2_1);
		group2.add(radio2_1);
		radio2_2 = new JRadioButton("只取为1手企业提供担保的企业 ");
		radio2_2.setSelected(algorithm.isTwo_hand_vertex_in_only());
		p2_2.add(radio2_2);
		group2.add(radio2_2);
		radio2_3 = new JRadioButton("全不取");
		radio2_3.setSelected(algorithm.isTwo_hand_vertex_none());
		p2_2.add(radio2_3);
		group2.add(radio2_3);
		p2.add(p2_2);
		// =====================================================
		JPanel p2_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2_3.add(new JLabel("3手圈摘取规则："));
		ButtonGroup group3 = new ButtonGroup();
		radio3_1 = new JRadioButton("全取");
		radio3_1.setSelected(algorithm.isThree_hand_vertex_all());
		p2_3.add(radio3_1);
		group3.add(radio3_1);
		radio3_2 = new JRadioButton("只取为2手企业提供担保的企业 ");
		radio3_2.setSelected(algorithm.isThree_hand_vertex_in_only());
		p2_3.add(radio3_2);
		group3.add(radio3_2);
		radio3_3 = new JRadioButton("全不取");
		radio3_3.setSelected(algorithm.isThree_hand_vertex_none());
		p2_3.add(radio3_3);
		group3.add(radio3_3);
		p2.add(p2_3);
		// =====================================================
		JPanel p2_4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		unpickCorecorpSonCheck = new JCheckBox("如果第N(0<N≤3)手企业也被认定为“核心企业”，则不取它的后手");
		unpickCorecorpSonCheck.setSelected(algorithm.isUnpick_corecorp_son());
		p2_4.add(unpickCorecorpSonCheck);
		p2.add(p2_4);
		// =====================================================
		JPanel p2_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pickMutuallyGuaranteedCorp = new JCheckBox("如果第N(0<N≤3)手企业与第N-1手企业存在互保关系，则必取之");
		pickMutuallyGuaranteedCorp.setSelected(algorithm.isPick_mutually_guaranteed_corp());
		p2_5.add(pickMutuallyGuaranteedCorp);
		p2.add(p2_5);
		// =====================================================
		JPanel p2_6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pickCorecorpLoop = new JCheckBox("如果核心企业位于一个环中(环路径长度≤4)，则取该环上的所有企业");
		pickCorecorpLoop.setSelected(algorithm.isPick_corecorp_loop());
		p2_6.add(pickCorecorpLoop);
		p2.add(p2_6);

		this.add(p2);
	}

	public boolean isGuarantorFloorSelected() {
		return guarantorFloorCheck.isSelected();
	}
	public boolean isOutGuarantorFloorSelected() {
		return outGuarantorFloorCheck.isSelected();
	}

	public boolean isMutuallyGuarantorFloorSelected() {
		return mutuallyGuarantorFloorCheck.isSelected();
	}

//	public boolean isGuaranteedLoanBalanceFloorSelected() {
//		return guaranteedLoanBalanceFloorCheck.isSelected();
//	}

	public boolean isLoanBalanceFloorSelected() {
		return loanBalanceFloorCheck.isSelected();
	}

	public boolean isUnpickCorecorpSonSelected() {
		return unpickCorecorpSonCheck.isSelected();
	}

	public boolean isPickMutuallyGuaranteedCorpSelected() {
		return pickMutuallyGuaranteedCorp.isSelected();
	}

	public boolean isPickCorecorpLoopSelected() {
		return pickCorecorpLoop.isSelected();
	}

	public int getGuarantorFloor() {
		return Integer.parseInt(String.valueOf(guarantorFloorCombo.getSelectedItem()));
	}
	public int getOutGuarantorFloor() {
		return Integer.parseInt(String.valueOf(outGuarantorFloorCombo.getSelectedItem()));
	}

	public int getMutuallyGuarantorFloor() {
		return Integer.parseInt(String.valueOf(mutuallyGuarantorFloorCombo.getSelectedItem()));
	}

	public int getGuaranteedLoanBalanceFloor() {
		return Integer.parseInt(String.valueOf(guaranteedLoanBalanceFloorSpinner.getValue()));
	}

	public int getLoanBalanceFloor() {
		return Integer.parseInt(String.valueOf(loanBalanceFloorSpinner.getValue()));
	}

	public Unit getGuaranteedLoanBalanceFloorUnit() {
		return (Unit) guaranteedLoanBalanceFloorUnitCombo.getSelectedItem();
	}

	public Unit getLoanBalanceFloorUnit() {
		return (Unit) loanBalanceFloorUnitCombo.getSelectedItem();
	}

	public boolean isOne_hand_vertex_all_selected() {
		return radio1_1.isSelected();
	}

	public boolean isOne_hand_vertex_in_only_selected() {
		return radio1_2.isSelected();
	}

	public boolean isOne_hand_vertex_none_selected() {
		return radio1_3.isSelected();
	}

	public boolean isTwo_hand_vertex_all_selected() {
		return radio2_1.isSelected();
	}

	public boolean isTwo_hand_vertex_in_only_selected() {
		return radio2_2.isSelected();
	}

	public boolean isTwo_hand_vertex_none_selected() {
		return radio2_3.isSelected();
	}

	public boolean isThree_hand_vertex_all_selected() {
		return radio3_1.isSelected();
	}

	public boolean isThree_hand_vertex_in_only_selected() {
		return radio3_2.isSelected();
	}

	public boolean isThree_hand_vertex_none_selected() {
		return radio3_3.isSelected();
	}

	public PickAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(PickAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public int getCondition_number_value() {

		return Integer.parseInt(String.valueOf(conditionNumberCombo.getSelectedItem()));
	}

//	public boolean isGuaranteed_loan_balance_floor_selected() {
//
//		return guaranteedLoanBalanceFloorCheck.isSelected();
//	}

	public Unit getGuaranteed_loan_balance_floor_unit() {

		return (Unit) guaranteedLoanBalanceFloorUnitCombo.getSelectedItem();
	}

	public int getGuaranteed_loan_balance_floor_value() {

		return Integer.parseInt(String.valueOf(guaranteedLoanBalanceFloorSpinner.getValue()));
	}

}
