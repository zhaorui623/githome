package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SpinnerNumberModel;

import cn.gov.cbrc.sd.dz.zhaorui.component.NumberSpinner;
import cn.gov.cbrc.sd.dz.zhaorui.model.GCClassify;

public class GCClassifyConfigPanel extends JPanel {

	private GCClassify gcClassify;
	private NumberSpinner spinner1, spinner2, spinner3, spinner4;

	public GCClassifyConfigPanel(GCClassify gcClassify) {
		super(new BorderLayout());
		this.gcClassify=gcClassify;

		// 规模纬度配置
		JPanel scaleDimensionPanel = new JPanel(new GridLayout(4, 1));
		scaleDimensionPanel.setBorder(BorderFactory.createTitledBorder("规模维度"));
		scaleDimensionPanel.add(new JLabel("将担保圈按照规模从小到大分为A、B、C三类："));
		JPanel pa = new JPanel(new FlowLayout(FlowLayout.LEFT)), pb = new JPanel(new FlowLayout(FlowLayout.LEFT)),
				pc = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa.add(new JLabel("A类圈：圈内所有企业贷款余额之和位于(0,"));
		spinner1 = new NumberSpinner(gcClassify.getLoan_balance_floor(), 1, Integer.MAX_VALUE, 1);
		spinner1.setEnabled(false);
		pa.add(spinner1);
		pa.add(new JLabel("亿元]区间内"));
		pb.add(new JLabel("B类圈：圈内所有企业贷款余额之和位于("));
		spinner2 = new NumberSpinner(gcClassify.getLoan_balance_floor(), 1, Integer.MAX_VALUE, 1);
		pb.add(spinner2);
		pb.add(new JLabel("亿元,"));
		spinner3 = new NumberSpinner(gcClassify.getLoan_balance_ceiling(), 1, Integer.MAX_VALUE, 1);
		pb.add(spinner3);
		pb.add(new JLabel("亿元]区间内"));
		pc.add(new JLabel("C类圈：圈内所有企业贷款余额之和位于("));
		spinner4 = new NumberSpinner(gcClassify.getLoan_balance_ceiling(), 1, Integer.MAX_VALUE, 1);
		spinner4.setEnabled(false);
		pc.add(spinner4);
		pc.add(new JLabel("亿元,∞)区间内"));
		scaleDimensionPanel.add(pa);
		scaleDimensionPanel.add(pb);
		scaleDimensionPanel.add(pc);
		this.add(scaleDimensionPanel, BorderLayout.WEST);

		// 风险维度配置
		JPanel riskDimensionPanel = new JPanel(new GridLayout(4, 1));
		riskDimensionPanel.setBorder(BorderFactory.createTitledBorder("风险维度"));
		riskDimensionPanel.add(new JLabel("将担保圈按照风险从低到高分为1、2、3三类："));
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT)), p2 = new JPanel(new FlowLayout(FlowLayout.LEFT)),
				p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("1类圈：圈内所有企业贷款五级分类均为“正常”"));
		p2.add(new JLabel("2类圈：圈内有企业存在五级分类为“关注”的贷款"));
		p3.add(new JLabel("3类圈：圈内有企业存在不良贷款"));
		riskDimensionPanel.add(p1);
		riskDimensionPanel.add(p2);
		riskDimensionPanel.add(p3);
		this.add(riskDimensionPanel, BorderLayout.CENTER);

		// 矩阵展示
		JPanel matrixShowPanel = new JPanel(new GridLayout(3, 3));
		matrixShowPanel.setBorder(BorderFactory.createTitledBorder("分类矩阵"));
		matrixShowPanel.add(new JButton("C1"));
		matrixShowPanel.add(new JButton("C2"));
		matrixShowPanel.add(new JButton("C3"));
		matrixShowPanel.add(new JButton("B1"));
		matrixShowPanel.add(new JButton("B2"));
		matrixShowPanel.add(new JButton("B3"));
		matrixShowPanel.add(new JButton("A1"));
		matrixShowPanel.add(new JButton("A2"));
		matrixShowPanel.add(new JButton("A3"));
		this.add(matrixShowPanel, BorderLayout.EAST);

		addListeners();
	}

	private void addListeners() {
		spinner2.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				SpinnerNumberModel model2 = ((SpinnerNumberModel) e.getSource());
				int value2 = Integer.parseInt(String.valueOf(model2.getValue()));
				int value3 = Integer.parseInt(String.valueOf((spinner3.getValue())));
				if (value2 >= value3)
					spinner2.setValue(model2.getPreviousValue());
				else
					spinner1.setValue(value2);
			}
		});
		spinner3.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				SpinnerNumberModel model3 = ((SpinnerNumberModel) e.getSource());
				int value3 = Integer.parseInt(String.valueOf(model3.getValue()));
				int value2 = Integer.parseInt(String.valueOf((spinner2.getValue())));
				if (value3 <= value2)
					spinner3.setValue(model3.getPreviousValue());
				else
					spinner4.setValue(value3);
			}
		});
	}

	public int getLoan_balance_floor() {
		return Integer.parseInt(String.valueOf(spinner2.getValue()));
	}

	public int getLoan_balance_ceiling() {
		return Integer.parseInt(String.valueOf(spinner3.getValue()));
	}

	public GCClassify getGcClassify() {
		return gcClassify;
	}
}
