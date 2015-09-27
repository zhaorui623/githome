package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.GraphicDetailShowDialog;

public class GraphicListShowPanel extends JScrollPane {

	static GraphicListShowTable table;

	public GraphicListShowPanel() {
		super();
	}

	/**
	 * 根据传入的 “担保圈列表”构造“结果展示表”
	 * 
	 * @param regionGraphicsMap
	 */
	public void refreshData(List<Graphic> circles) {
		GraphicListShowTableModel model = new GraphicListShowTableModel(circles);
		table = new GraphicListShowTable(model);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		addListeners();
		this.setViewportView(table);
		this.repaint();
	}

	private void addListeners() {
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
					if(e.getClickCount()>1){
						if(e.getSource() instanceof GraphicListShowTable){
							GraphicListShowTable table=(GraphicListShowTable) e.getSource();
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

}
