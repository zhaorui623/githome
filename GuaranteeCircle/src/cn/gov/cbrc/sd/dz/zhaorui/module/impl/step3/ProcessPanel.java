package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;

@SuppressWarnings("serial")
public class ProcessPanel extends JPanel {

	private GC GC = null;

	private Step3Module step3Module;

	private JButton start;

	private JLabel statusIcons[], procedureLabels[], percentLabels[];

	private String[] labeltexts = { "第1步：识别独立连通子图", "第2步：对超大圈进行拆分", "第3步：过滤掉非圈非链、小圈小链", "第4步：生成每个担保圈的拓扑图",
			"第5步：从地区分布维度分析", "第6步：从重点客户维度分析", "第7步：从风险分类维度分析" };

	private String finishedLabel = "√", processingLabel = "→", percent100 = "(100%)";

	// private ImageIcon finishedIcon=ResourceManager.getIcon("ok.png", false);

	public ProcessPanel(Step3Module step3Module) {
		super(new BorderLayout(5, 5));
		this.GC = step3Module.getGC();
		this.step3Module = step3Module;
		this.setName(step3Module.getName());

		start = new JButton("开始识别");
		this.add(start, BorderLayout.NORTH);

		addComponents();
		addListeners();
	}

	private void addComponents() {
		JPanel centerPanel = new JPanel(new GridLayout(Step3Module.PROCEDURE_COUNT, 1));
		JPanel panels[] = new JPanel[Step3Module.PROCEDURE_COUNT];
		statusIcons = new JLabel[Step3Module.PROCEDURE_COUNT];
		procedureLabels = new JLabel[Step3Module.PROCEDURE_COUNT];
		percentLabels = new JLabel[Step3Module.PROCEDURE_COUNT];
		for (int i = 0; i < Step3Module.PROCEDURE_COUNT; i++) {

			panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			statusIcons[i] = new JLabel("  ");
			statusIcons[i].setIcon(null);
			procedureLabels[i] = new JLabel(labeltexts[i]);
			percentLabels[i] = new JLabel("");
			panels[i].add(statusIcons[i]);
			panels[i].add(procedureLabels[i]);
			panels[i].add(percentLabels[i]);
			centerPanel.add(panels[i]);
		}
		this.add(centerPanel, BorderLayout.CENTER);
	}

	private void addListeners() {
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final ProcedureThread thread = new ProcedureThread();
					thread.start(step3Module);
					new Thread() {
						public void run() {
							while (thread.isAlive()) {
								Procedure p = thread.getCurrentProcedure();
								if (p != null) {
									int index = p.getIndex() - 1;
									for (int i = 0; i < index; i++) {
										statusIcons[i].setText(finishedLabel);
										percentLabels[i].setText(percent100);
									}
									statusIcons[index].setText(processingLabel);
									percentLabels[index].setText("(" + p.getPercent() + "%)");
								}
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							statusIcons[Step3Module.PROCEDURE_COUNT - 1].setText(finishedLabel);
							percentLabels[Step3Module.PROCEDURE_COUNT - 1].setText(percent100);
							JOptionPane.showMessageDialog(ProcessPanel.this, "识别过程结束！", "提示",
									JOptionPane.INFORMATION_MESSAGE);
							Module.gotoStep(4);
						}
					}.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

}
