package cn.gov.cbrc.sd.dz.zhaorui.component;
/**
 * 日志面板
 */

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GCLogger;

public class InfoPane extends JScrollPane{
	
	private final Logger logger = GCLogger.getInstance(this.getClass());
	
	private JTextArea textarea;
	
	private static InfoPane instance=null;
	
	public static InfoPane getInstance(){
		if(instance==null)
			instance=new InfoPane();
		return instance;
	}
	
	private InfoPane(){
		super();
		textarea=new JTextArea(5,0);
		textarea.setEditable(false);
		textarea.setLineWrap(true);
		textarea.setBackground(Color.white);
		this.setViewportView(textarea);
		this.setAutoscrolls(true);
	}
	
	public void info(String info){
		textarea.append(java.util.Calendar.getInstance().getTime().toLocaleString()+" INFO:"+info+"\n");
		logger.info(info);
		textarea.setCaretPosition(textarea.getDocument().getLength());
	}

	public void error(String error) {
		textarea.append(java.util.Calendar.getInstance().getTime().toLocaleString()+" ERROR:"+error+"\n");
		logger.info(error);

		textarea.setCaretPosition(textarea.getDocument().getLength());
	}

	public void showMessage(String content) {		
		textarea.setText(content);
		textarea.setCaretPosition(0);
	}

	public void clear() {
		textarea.setText("");
	}

}
