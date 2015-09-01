package cn.gov.cbrc.sd.dz.zhaorui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.poi.hssf.record.pivottable.PageItemRecord;

public abstract class WizardPane extends JPanel {

	private class Page{
		String title,description;
		Component comp;
		public Page(String title, String description, Component comp) {
			super();
			this.title = title;
			this.description = description;
			this.comp = comp;
		}
		
	}

	private JPanel topPanel, centerPanel, bottomPanel;

	private JLabel titleLabel;

	private JTextArea descriptionArea;

	private JButton pre, next;
	
	private List<Page> pages=new ArrayList<Page>();
	
	private int currentPage=0;
	
	//<页码->[被关注的组件1,被关注的组件2....]>
	private HashMap<Integer, ArrayList<Component>> valueableComps=new HashMap<Integer,ArrayList<Component>>();
	
	private Map<Integer,Object> valuesMap=new HashMap<Integer, Object>();

	public List<Component> getValueableComps(int pageIndex) {
		return valueableComps.get(pageIndex);
	}
	
	public void putValue(int pageIndex,Object o){
		valuesMap.put(pageIndex, o);
	}
	public Object getValue(int pageIndex){
		return valuesMap.get(pageIndex);
	}
	
	public  Map<Integer,Object>  getValuesMap(){
		return valuesMap;
	}

	public WizardPane(String title, String description, Component comp) {
		this.setLayout(new BorderLayout(5,5));		

		topPanel = new JPanel(new BorderLayout(5, 5));
		titleLabel = new JLabel(title);
		descriptionArea = new JTextArea(description);
		descriptionArea.setLineWrap(true);
		descriptionArea.setEditable(false);
		topPanel.add(titleLabel, "North");
		topPanel.add(descriptionArea);

		bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pre = new JButton("上一步");
		pre.setVisible(false);
		next = new JButton();
		bottomPanel.add(pre);
		bottomPanel.add(next);
		Dimension size=new Dimension(75,25);
		pre.setPreferredSize(size);
		next.setPreferredSize(size);

		JPanel realCenter=new JPanel(new BorderLayout());
		centerPanel=new JPanel(new BorderLayout());
		centerPanel.add(comp);
		realCenter.add(centerPanel,"North");
//		realCenter.setBorder(BorderFactory.createLineBorder(Color.gray));

		
		JPanel root=new JPanel(new BorderLayout(15,15));
		root.add(topPanel, "North");
		root.add(realCenter);
		root.add(bottomPanel,"South");	
		this.add(new JLabel("  "),"West");
		this.add(root);
		
		next.requestFocus();
		
		addListeners();

		addPage(title,description,comp);
	}
	
	public void addValueableComponent(int pageIndex,Component c){
		ArrayList<Component> l;
		if(this.valueableComps.containsKey(pageIndex)==false)
			l=new ArrayList<Component>();
		else
			l=valueableComps.get(pageIndex);
		l.add(c);
		valueableComps.put(pageIndex, l);
	}

	private void addListeners() {
		pre.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentPage==0)
					return;				
				else
					switchToPage(--currentPage);
			}			
		});
		next.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				recordAnswer(currentPage);
				if(currentPage==pages.size()-1)
					finish();
				else{
					switchToPage(++currentPage);
				}
			}			
		});
	}

	protected abstract void recordAnswer(int pageIndex) ;

	protected abstract void finish() ;

	protected void switchToPage(int index) {
		if(index==0)
			pre.setVisible(false);
		else
			pre.setVisible(true);
		if(index==pages.size()-1)
			next.setText("完成");
		else
			next.setText("下一步");
		Page page=pages.get(index);
		titleLabel.setText(page.title);
		descriptionArea.setText(page.description);
		centerPanel.removeAll();
		centerPanel.add(page.comp);
		this.updateUI();
	}

	public void addPage(String title,String description,Component comp){
		Page page=new Page(title,description,comp);
		pages.add(page);
		if(pages.size()==1)
			next.setText("完成");
		else
			next.setText("下一步");
	}
	public void addPage(Component comp){
		addPage(titleLabel.getText(),descriptionArea.getText(),comp);
	}

}
