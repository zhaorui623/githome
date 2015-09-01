package cn.gov.cbrc.sd.dz.zhaorui.exception;

/**
 * @author zr
 * 
 * 用来展示错误信息的对话框 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GCLogger;

public class ErrorDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -886420718955534627L;

	private final Logger logger = GCLogger.getInstance(this.getClass());

	private JTextArea label = null;
	
	private JButton exit = null;	
	
	private JButton detail = null;
	
	private boolean isDetailShowed=true;
	
	private JTextArea textarea = null;
	
	private final JDialog dialog = this;

	private boolean shouldExitSystemWhenExit;
	/**
	 * 
	 * @param labelText 对话框上显示的文本文�?
	 * @param e 触发对话框弹出的异常
	 * @param shouldExitSystemWhenExit 当用户点击关闭按钮时是否要�?出整个应用程�?默认值为false
	 */
	public ErrorDialog(String labelText,Exception e,boolean shouldExitSystemWhenExit){
		
		doInit(labelText,e,shouldExitSystemWhenExit);
	}

	/**
	 * 
	 * @param labelText 对话框上显示的文本文�?
	 * @param e 触发对话框弹出的异常
	 */
	public ErrorDialog(String labelText,Exception e) {
		doInit(labelText,e,false);
	}
	
	private void doInit(String labelText, Exception e, boolean shouldExitSystemWhenExit) {
		logger.error(labelText+" - "+e.toString());
		this.shouldExitSystemWhenExit=shouldExitSystemWhenExit;
		setTitle("错误");
		setModal(true);
		setLayout(new BorderLayout());
		
		dialog.setSize(300,120);
		setLocationCenter();
		JPanel centerPane=new JPanel();
		centerPane.setLayout(new BorderLayout());
		final JPanel buttonPane=new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.TRAILING));		
		
		label = new JTextArea(labelText);
		label.getMargin().set(25, 10, 15, 15);
		label.setLineWrap(true);
		label.setEditable(false);
		exit = new JButton(shouldExitSystemWhenExit?"关闭":"取消");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("用户选择"+exit.getText());
				exit();
			}
		});
		detail = new JButton("显示细节");
		getRootPane().setDefaultButton(detail);
		detail.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {	
				if(isDetailShowed)			
				{
					dialog.setSize(dialog.getWidth(), (int) (dialog.getHeight()+textarea.getRows()*24));
					textarea.setVisible(true);
					detail.setText("隐藏细节");
				}else
				{dialog.setSize(dialog.getWidth(), (int) (dialog.getHeight()-textarea.getRows()*24));
					textarea.setVisible(false);
					detail.setText("显示细节");					
				}
				isDetailShowed=!isDetailShowed;
			}
		});
		textarea=new JTextArea(e.toString());
		textarea.setLineWrap(true);
		textarea.setRows(2);
		textarea.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2,Color.gray));
		textarea.setEditable(false);
		textarea.setBackground(Color.white);
		textarea.setVisible(false);
		centerPane.add(label);
		buttonPane.add(exit);
		buttonPane.add(detail);
		centerPane.add(buttonPane,"South");
		dialog.add(centerPane);
		dialog.add(textarea,BorderLayout.SOUTH);
		
		dialog.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		dialog.setVisible(true);
		exit.requestFocus();
		dialog.setAlwaysOnTop(true);
	}
	
	private void setLocationCenter() {
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();		
		setLocation((screenSize.width-getWidth())/2, (screenSize.height-getHeight())/2);
	}

	private void exit(){
		if(this.shouldExitSystemWhenExit)
			System.exit(0);
		else
			dialog.dispose();
	}

	public static void main(String[] args) {
		try{
			Integer.parseInt("a");
		}catch (NumberFormatException e) {
			new ErrorDialog("发生了异常",e,true);
		}
		
	}
}
