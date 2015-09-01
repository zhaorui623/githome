package cn.gov.cbrc.sd.dz.zhaorui.exception;

/**
 * @author zr
 * 
 * 带有“计时重试�?功能的错误展示对话框
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GCLogger;

public class TimerErrorDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1949621883151256330L;

	private final Logger logger = GCLogger.getInstance(this.getClass());

	private JTextArea label = null;

	private JLabel timerLabel = null;

	private JButton exit = null;
	
	private JButton retry = null;

	private JButton detail = null;

	private boolean isDetailShowed = true;

	private JTextArea textarea = null;

	private final JDialog dialog = this;

	private boolean shouldExitSystemWhenExit;

	private boolean isCancel=false;	

	private boolean isExit=false;

	public boolean isCancel() {
		return isCancel;
	}
	/**
	 * 
	 * @param labelText 对话框上显示的文本文�?
	 * @param e 触发对话框弹出的异常
	 * @param shouldExitSystemWhenExit 当用户点击关闭按钮时是否要�?出整个应用程�?默认值为false
	 * @param timeout 倒计时时间，单位:�?
	 */
	public TimerErrorDialog(String labelText, Exception e,
			boolean shouldExitSystemWhenExit, int timeout) {
		doInit(labelText, e, shouldExitSystemWhenExit, timeout);
	}
	/**
	 * 
	 * @param labelText 对话框上显示的文本文�?
	 * @param e 触发对话框弹出的异常
	 * @param timeout 倒计时时间，单位:�?
	 */
	public TimerErrorDialog(String labelText, Exception e, int timeout) {
		doInit(labelText, e, false, timeout);
	}

	public TimerErrorDialog(String labelText, Exception e) {
		doInit(labelText, e, false, 60);
	}
	public TimerErrorDialog(String labelText, Exception e,boolean shouldExitSystemWhenExit) {
		doInit(labelText, e, shouldExitSystemWhenExit, 60);
	}

	private void doInit(String labelText, Exception e,
			boolean shouldExitSystemWhenExit, int timeout) {
		e.printStackTrace();
		logger.error(labelText+" - "+e.toString());
		this.shouldExitSystemWhenExit = shouldExitSystemWhenExit;
		setTitle("错误");
		setModal(true);
		setLayout(new BorderLayout());

		dialog.setSize(300, 120);
		setLocationCenter();
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new BorderLayout());
		JPanel labelPane = new JPanel();
		labelPane.setLayout(new GridLayout(2, 1, 0, 0));
		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.TRAILING));

		label = new JTextArea(labelText);
		label.getMargin().set(15, 14, 0, 0);
		label.setLineWrap(true);
		label.setEditable(false);
		timerLabel=new JLabel();
		new Timer(timeout).start();
		logger.info("等待"+timeout+"秒后重试...");
		retry=new JButton("立即重试");
		retry.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("用户选择"+retry.getText());
				isExit=true;
				dialog.dispose();
			}
		});
		exit = new JButton(shouldExitSystemWhenExit?"关闭":"取消");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("用户选择"+exit.getText());
				isCancel=true;
				exit();
			}
		});
		detail = new JButton("显示细节");
		detail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isDetailShowed) {
					dialog.setSize(dialog.getWidth(),
							(int) (dialog.getHeight() + textarea.getRows()*24));
					textarea.setVisible(true);
					detail.setText("隐藏细节");
				} else {
					dialog.setSize(dialog.getWidth(),
							(int) (dialog.getHeight() - textarea.getRows()*24));
					textarea.setVisible(false);
					detail.setText("显示细节");
				}
				isDetailShowed = !isDetailShowed;
			}
		});
		textarea = new JTextArea(e.toString());
		textarea.setLineWrap(true);
		textarea.setRows(2);
		textarea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
				Color.gray));
		textarea.setEditable(false);
		textarea.setBackground(Color.white);
		textarea.setVisible(false);
		labelPane.add(label);
		labelPane.add(timerLabel);
		centerPane.add(labelPane);
		buttonPane.add(retry);
		buttonPane.add(exit);
		buttonPane.add(detail);
		centerPane.add(buttonPane, "South");
		dialog.add(centerPane);
		dialog.add(textarea, BorderLayout.SOUTH);

		dialog.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}

	private void setLocationCenter() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
	}

	private void exit() {
		isExit=true;
		if (this.shouldExitSystemWhenExit)
			System.exit(0);
		else
			dialog.dispose();
	}

	public static void main(String[] args) {
		try {
			Integer.parseInt("a");
		} catch (NumberFormatException e) {
			new TimerErrorDialog("数字格式化异常！", e, true,60);			
		}

	}

	/**
	 * 计时器，当�?计时结束或用户点击立即重试按钮时，此线程自然结束（对话框�?���?
	 * @author zr
	 *
	 */
	class Timer extends Thread {

		private int timeout;

		public Timer(int timeout) {
			this.timeout = timeout;
		}

		public void run() {
			for (int i = 0; i < timeout; i++) {
				try {
					timerLabel.setText("  将在"+(timeout-i)+"秒后重试...");
					sleep(1000);
					if(isExit)
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			dialog.dispose();
		}
	}
}
