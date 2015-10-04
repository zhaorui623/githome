package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;

public class CorporationListShowPanel extends JPanel {

	private JScrollPane tablePane;
	private JButton exp2ExcelButton;

	static CorporationListShowTable table;

	/**
	 * 根据传入的 “企业列表”构造“担保圈内企业展示表”
	 * @param set
	 * @throws Exception 
	 */
	public CorporationListShowPanel(Set<Corporation> corporations) throws Exception {
		super(new BorderLayout());

		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		exp2ExcelButton = new JButton("导出为Excel");
		northPanel.add(exp2ExcelButton);
		this.add(northPanel, BorderLayout.NORTH);

		CorporationListShowTableModel model = new CorporationListShowTableModel(corporations);
		table = new CorporationListShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		tablePane = new JScrollPane();
		tablePane.setViewportView(table);
		tablePane.setAutoscrolls(true);
		tablePane.repaint();
		this.add(tablePane, BorderLayout.CENTER);

		addListeners();
	}

	private void addListeners() {
		exp2ExcelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table == null)
					return;
				if(table.getRowCount()==0)
					return;
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xls", "xls");
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(filter);
				int result = chooser.showSaveDialog(GC.getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (file.getName().endsWith(".xls") == false)
						file = new File(file.getPath() + ".xls");
					try {
						TableToolkit.exp2Excel(table, file);
						JOptionPane.showMessageDialog(GC.getParent(), "导出成功！", "导出结果", JOptionPane.INFORMATION_MESSAGE);
						InfoPane.getInstance().info("导出结果集成功，导出文件路径：" + file.getAbsolutePath());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(GC.getParent(), e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
						InfoPane.getInstance().info(e1.toString());
						if (e1.getCause() != null)
							InfoPane.getInstance().info(e1.getCause().toString());
						e1.printStackTrace();
					}
				}
			}

		});
	}
}