package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;

public class RegionGraphicShowPanel extends JPanel {

	private JScrollPane tablePane;
	private JButton exp2ExcelButton;
	
	RegionGraphicShowTable table;

	Panel2 panel2;

	public RegionGraphicShowPanel(Panel2 panel2) {
		super(new BorderLayout());

		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		exp2ExcelButton = new JButton("导出为Excel");
		northPanel.add(exp2ExcelButton);
		this.add(northPanel, BorderLayout.NORTH);
		
		tablePane=new JScrollPane();
		tablePane.setAutoscrolls(true);
		this.add(tablePane,BorderLayout.CENTER);
		
		this.panel2 = panel2;
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

	private void addTableListener() {
		if (table != null) {
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					int row = table.getSelectedRow();
					int col = table.getColumnCount();
					if (row >= 0) {
						row = table.convertRowIndexToModel(row);
						List<Graphic> circles = (List<Graphic>) table.getModel().getValueAt(row, col);
						if (panel2 != null) {
							Panel1 bottomPanel = panel2.getBottomPanel();
							if (bottomPanel != null)
								bottomPanel.refreshResult(circles);
						}
					}
				}
			});
		}
	}

	/**
	 * 根据传入的 “地区->担保圈列表”构造“结果展示表”
	 * 
	 * @param regionGraphicsMap
	 */
	public void refreshData(Map<Region, List<Graphic>> regionGraphicsMap) {
		RegionGraphicShowTableModel model = new RegionGraphicShowTableModel(regionGraphicsMap);
		table = new RegionGraphicShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		addTableListener();
		tablePane.setViewportView(table);
		tablePane.repaint();
	}

}
