package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel0;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.component.TableFilterPanel;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.GraphicDetailShowDialog;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;

public class TotalResultShowPanel extends JPanel {

	private JScrollPane tablePane;
	private JButton exp2ExcelButton;
	private TableFilterPanel circleNameFilterPanel,corpNameFilterPanel;
	private TotalResultShowTable table;

	public TotalResultShowPanel() {
		super(new BorderLayout());


		tablePane = new JScrollPane();
		this.add(tablePane, BorderLayout.CENTER);
		

		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		circleNameFilterPanel=new TableFilterPanel("查找担保圈名称：",table,0);
		corpNameFilterPanel=new TableFilterPanel("查找企业名称：",table,8);
		northPanel.add(circleNameFilterPanel);
		northPanel.add(corpNameFilterPanel);
		exp2ExcelButton = new JButton("导出为Excel");
		northPanel.add(exp2ExcelButton);
		this.add(northPanel, BorderLayout.NORTH);

		addListeners();
	}

	/**
	 * 根据传入的 “担保圈列表”构造“结果展示表”
	 * 
	 * @param regionGraphicsMap
	 */
	public void refreshData(List<Graphic> circles) {
		GraphicListShowTableModel model = new GraphicListShowTableModel(circles);
		table = new TotalResultShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
//		addTableListener();
		tablePane.setViewportView(table);
		tablePane.repaint();
		corpNameFilterPanel.setTableToFilt(table);
		circleNameFilterPanel.setTableToFilt(table);
	}

	private void addTableListener() {
		if (table != null) {
			table.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 1) {
						if (e.getSource() instanceof TotalResultShowTable) {
							TotalResultShowTable table = (TotalResultShowTable) e.getSource();
							int row = table.getSelectedRow();
							int col = table.getColumnCount();
							if (row >= 0) {
								row = table.convertRowIndexToModel(row);
								Graphic circle = (Graphic) table.getModel().getValueAt(row, col);
								JDialog dialog;
								try {
									dialog = new GraphicDetailShowDialog(circle);
									dialog.setVisible(true);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						}
					}
				}
			});
		}
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

	public JTable getTable() {

		return table;
	}
}
