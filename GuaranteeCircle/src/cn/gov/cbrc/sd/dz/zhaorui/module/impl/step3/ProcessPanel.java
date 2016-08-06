package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.Step4Module;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TimeToolkit;

@SuppressWarnings("serial")
public class ProcessPanel extends JPanel {

	private GC GC = null;

	private Step3Module step3Module;

	private JPanel startPanel;
	private JButton start,startOnlyVIP;

	private JLabel statusIcons[], procedureLabels[], percentLabels[];

	private String[] labeltexts = { "第1步：识别独立连通子图", "第2步：对超大圈进行处理", "第3步：从重点客户维度分析", "第4步：高关联度担保圈合并",
			"第5步：过滤掉非圈非链、小圈小链", "第6步：从地区分布维度分析", "第7步：从风险分类维度分析", "第8步：生成每个担保圈的拓扑图","第9步:生成超大担保圈的拓扑图" };

	private String finishedLabel = "√", processingLabel = "→", percent100 = "(100%)";

	private JCheckBox skipGraphicGenerate;// 是否跳过“担保圈拓扑图生成”步骤
	private JCheckBox skipHugeGraphicGenerate;// 是否跳过“超大担保圈拓扑图生成”步骤
	private JCheckBox skipGraphicMerge;// 是否跳过“担保圈合并”步骤

	// private ImageIcon finishedIcon=ResourceManager.getIcon("ok.png", false);

	public ProcessPanel(Step3Module step3Module) {
		super(new BorderLayout(5, 5));
		this.GC = step3Module.getGC();
		this.step3Module = step3Module;
		this.setName(step3Module.getName());

		startPanel=new JPanel(new BorderLayout());
		start = new JButton("开始识别");
		startOnlyVIP = new JButton("只做重点客户担保圈识别");
		startPanel.add(start, BorderLayout.CENTER);
		startPanel.add(startOnlyVIP, BorderLayout.EAST);
		this.add(startPanel, BorderLayout.NORTH);

		addComponents();
		addListeners();
	}

	private void addComponents() {
		JPanel centerPanel = new JPanel(new GridLayout(labeltexts.length, 1));
		JPanel panels[] = new JPanel[labeltexts.length];
		statusIcons = new JLabel[labeltexts.length];
		procedureLabels = new JLabel[labeltexts.length];
		percentLabels = new JLabel[labeltexts.length];
		for (int i = 0; i < labeltexts.length; i++) {
			panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			statusIcons[i] = new JLabel("  ");
			statusIcons[i].setIcon(null);
			procedureLabels[i] = new JLabel(labeltexts[i]);
			percentLabels[i] = new JLabel("");
			panels[i].add(statusIcons[i]);
			panels[i].add(procedureLabels[i]);
			panels[i].add(percentLabels[i]);
			centerPanel.add(panels[i]);
			if (i == 3) {//允许跳过“第4步：高关联度担保圈合并"
				skipGraphicMerge = new JCheckBox("跳过本步骤");
				skipGraphicMerge.setSelected(true);
				panels[i].add(skipGraphicMerge);
				procedureLabels[i].setEnabled(!skipGraphicMerge.isSelected());
			}
			if (i == 7) {//允许跳过"第8步：生成每个担保圈的拓扑图" 
				skipGraphicGenerate = new JCheckBox("跳过本步骤");
				skipGraphicGenerate.setSelected(true);
				panels[i].add(skipGraphicGenerate);
				procedureLabels[i].setEnabled(!skipGraphicGenerate.isSelected());
			}
			if (i == 8) {//允许跳过"第9步:生成超大担保圈的拓扑图"
				skipHugeGraphicGenerate = new JCheckBox("跳过本步骤");
				skipHugeGraphicGenerate.setSelected(true);
				panels[i].add(skipHugeGraphicGenerate);
				procedureLabels[i].setEnabled(!skipHugeGraphicGenerate.isSelected());
			}
		}
		this.add(centerPanel, BorderLayout.CENTER);
	}

	private boolean isOnlyDoVIP=false;
	private boolean startOnlyVIPButtonClicked=false;
	
	public boolean isOnlyDoVIP() {
		return isOnlyDoVIP;
	}


	private void addListeners() {
		startOnlyVIP.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startOnlyVIPButtonClicked=true;
				start.doClick();
				startOnlyVIPButtonClicked=false;
			}
		});
		start.addActionListener(new ActionListener() {
			boolean shown = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Step4Module.shouldRefreshResult=true;
					isOnlyDoVIP=startOnlyVIPButtonClicked;
					String timeStamp=TimeToolkit.formater3.format(new Date());
					GC.setOutputDir(new File(System.getProperty("user.dir")+"\\"+timeStamp+"\\"));
					clearProcedueStatusMark();
					shown = false;
					final ProcedureThread thread = new ProcedureThread();
					thread.start(ProcessPanel.this);
					new Thread() {
						public void run() {
							while (thread.isAlive()) {
								Procedure p = thread.getCurrentProcedure();
								if (p != null) {
									int index = p.getIndex() - 1;
									if(index==-1)
										continue;
									for (int i = 0; i < index; i++) {
										statusIcons[i].setText(finishedLabel);
										percentLabels[i].setText(percent100);
									}
									statusIcons[index].setText(processingLabel);
									percentLabels[index].setText("(" + p.getPercent() + "%)");
								}
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
								}
								if (p != null && shown == false && p.getIndex() >= labeltexts.length-1) {
									shown = true;
									JOptionPane.showMessageDialog(null,
											"前序步骤全部结束，目前正在后台生成担保圈图像，该步骤耗时较长，将为您先切换到结果展示页面查看报表。", "提示",
											JOptionPane.INFORMATION_MESSAGE);
									Module.gotoStep(4);
								}
							}
							if (step3Module.isSucess()) {
								statusIcons[labeltexts.length - 1].setText(finishedLabel);
								percentLabels[labeltexts.length - 1].setText(percent100);
								JOptionPane.showMessageDialog(ProcessPanel.this, "识别过程结束！", "提示",
										JOptionPane.INFORMATION_MESSAGE);
								Module.gotoStep(4);
							}
						}
					}.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		skipGraphicGenerate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				procedureLabels[7].setEnabled(!skipGraphicGenerate.isSelected());
			}
		});
		skipGraphicMerge.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				procedureLabels[3].setEnabled(!skipGraphicMerge.isSelected());
			}
		});
		skipHugeGraphicGenerate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				procedureLabels[8].setEnabled(!skipHugeGraphicGenerate.isSelected());
			}
		});
	}

	public String[] getLabeltexts() {
		return labeltexts;
	}

	public void clearProcedueStatusMark() {
		step3Module.clearSucessMark();
		if (statusIcons != null)
			for (JLabel statusIcon : statusIcons)
				statusIcon.setText("  ");
		if (percentLabels != null)
			for (JLabel percentLabel : percentLabels)
				percentLabel.setText("");
		this.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}

	}

	public Step3Module getStep3Module() {

		return this.step3Module;
	}

	public boolean isSkipGraphicGenerate() {
		return skipGraphicGenerate.isSelected();
	}
	public boolean isSkipHugeGraphicGenerate() {
		return skipHugeGraphicGenerate.isSelected();
	}
	public boolean isSkipGraphicMerge() {
		return skipGraphicMerge.isSelected();
	}

	public int getProcedureCount() {
		
		return this.labeltexts.length;
	}

}
