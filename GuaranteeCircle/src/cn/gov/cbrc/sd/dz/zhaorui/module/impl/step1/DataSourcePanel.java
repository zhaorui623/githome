package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.exception.ErrorDialog;
import cn.gov.cbrc.sd.dz.zhaorui.module.Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step2.Step2Module;

@SuppressWarnings("serial")
public class DataSourcePanel extends JPanel {

	private GC GC = null;

	private Step1Module step1Module = null;

	private JSplitPane pane = null;

	private String instructionText = "将“EAST模型”目录下的两个模型文件（“担保关系表.EDA”和“客户信息表.EDA”）导入综合办公平台EAST系统，连接“客户风险”数据库，分别运行模型，将最终结果导出为Excel格式（最终结果集节点已标黄），得到“担保关系表.xls”和“客户信息表.xls”。然后在下方分别选取两个文件，点击“导入”按钮即完成数据导入。";
	
	private String successMessage= "导入成功！";
	private String failedMessage="导入失败，导入数据时发生异常！";

	private static InfoPane infopane;

	private JButton importButton, guaranteeInfoFileChooseButton, customerInfoFileChooseButton;

	private JTextField textField1, textField2;

	public DataSourcePanel(Step1Module step1Module) {
		pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.step1Module = step1Module;
		this.GC = step1Module.getGC();
		this.setName(step1Module.getName());
		this.setLayout(new BorderLayout(5, 5));

		// 上部主面板
		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		setTopComponent(pane, topPanel);

		// 下部日志面板
		infopane = InfoPane.getInstance();
		setBottomComponent(pane, infopane);

		this.add(pane);

		addListeners();
	}

	private void setBottomComponent(JSplitPane splitPane, Component comp) {
		splitPane.setBottomComponent(comp);
		// 设置分隔条位置
		splitPane.setResizeWeight(0.7);
	}

	private void setTopComponent(JSplitPane splitPane, Component comp) {

		Container c = (Container) comp;
		c.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel(new GridLayout(4, 1));
		JTextArea instruction = new JTextArea();
		instruction.setEditable(false);
		instruction.setLineWrap(true);
		instruction.setText(instructionText);
		northPanel.add(instruction);
		JPanel cp1 = new JPanel(new BorderLayout()), cp2 = new JPanel(new BorderLayout()),
				cp3 = new JPanel(new BorderLayout());
		cp1.add(new JLabel("担保关系表："),BorderLayout.WEST);
		textField1 = new JTextField(30);
		textField1.setEditable(false);
		cp1.add(textField1,BorderLayout.CENTER);
		guaranteeInfoFileChooseButton = new JButton("选择文件");
		cp1.add(guaranteeInfoFileChooseButton,BorderLayout.EAST);
		northPanel.add(cp1);
		cp2.add(new JLabel("客户信息表："),BorderLayout.WEST);
		textField2 = new JTextField(30);
		textField2.setEditable(false);
		cp2.add(textField2,BorderLayout.CENTER);
		customerInfoFileChooseButton = new JButton("选择文件");
		cp2.add(customerInfoFileChooseButton,BorderLayout.EAST);
		northPanel.add(cp2);
		importButton = new JButton("导入");
		cp3.add(importButton,BorderLayout.CENTER);
		northPanel.add(cp3);
		c.add(northPanel, BorderLayout.NORTH);		

		splitPane.setTopComponent(comp);

		// 设置分隔条位置
		splitPane.setResizeWeight(0.7);

	}

	private void addListeners() {
		guaranteeInfoFileChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				chooser.showDialog(new JLabel(), "选择");
				File file = chooser.getSelectedFile();
				if (file != null) {
					step1Module.setGuaranteeInfoFile(file);
					textField1.setText(file.getAbsolutePath());
					infopane.info("已选择“担保关系表”：" + file.getAbsolutePath());
				}
			}
		});
		customerInfoFileChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				chooser.showDialog(new JLabel(), "选择");
				File file = chooser.getSelectedFile();
				if (file != null) {
					step1Module.setCustomerInfoFile(file);
					textField2.setText(file.getAbsolutePath());
					infopane.info("已选择“客户信息表”：" + file.getAbsolutePath());
				}
			}
		});
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean isSuccess = step1Module.doImport();
					if (isSuccess == true) {
						JOptionPane.showMessageDialog(DataSourcePanel.this,successMessage, "提示",
								JOptionPane.INFORMATION_MESSAGE);
						infopane.info( successMessage);
						Module.gotoStep(2);
					}
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(DataSourcePanel.this, failedMessage, "错误",
							JOptionPane.ERROR_MESSAGE);
					infopane.error(failedMessage);
					infopane.error(exp.toString());
					exp.printStackTrace();
				}
			}
		});
	}

}
