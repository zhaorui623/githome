package cn.gov.cbrc.sd.dz.zhaorui.component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.GraphicListShowTable;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;

public class TableFilterPanel extends JPanel {
	
	private JTable table;
	
	private JTextField filterTextField;
	
	private int[] rowsToFilt;
	public TableFilterPanel(JTable table,int... rowsToFilt){
		this("查找：", table, rowsToFilt);
	}

	public TableFilterPanel(String filterLabelText,JTable table,int... rowsToFilt){
		super();
		this.table=table;
		this.rowsToFilt=rowsToFilt;
		this.add(new JLabel(filterLabelText));
		filterTextField=new JTextField(20);
		this.add(filterTextField);
		addListeners();
	}

	private void addListeners() {
		filterTextField.getDocument().addDocumentListener(new DocumentListener() {			
			@Override
			public void removeUpdate(DocumentEvent e) {
				TableToolkit.filt(table, filterTextField.getText(), rowsToFilt);
			}			
			@Override
			public void insertUpdate(DocumentEvent e) {
				TableToolkit.filt(table, filterTextField.getText(), rowsToFilt);
			}			
			@Override
			public void changedUpdate(DocumentEvent e) {
				TableToolkit.filt(table, filterTextField.getText(), rowsToFilt);
			}
		});
	}

	public void setTableToFilt(JTable table) {
		this.table=table;		
	}
}
