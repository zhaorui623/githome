package cn.gov.cbrc.sd.dz.zhaorui.component;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.net.*;

public class browser extends JFrame {
	JLabel jlabel;
	JTextField jtf;
	JEditorPane edpl;

	public browser() throws IOException {
		super("browser");
		Container con = getContentPane();
		jlabel = new JLabel("请输入要访问的网址:");
		jtf = new JTextField("");
		jtf.addActionListener(new MyEnter());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.add(jlabel);
		panel.add(jtf);
		con.add(panel, BorderLayout.NORTH);
		edpl = new JEditorPane();
		edpl.setEditable(false);
		edpl.addHyperlinkListener(new MyHyperListener());
		con.add(new JScrollPane(edpl), BorderLayout.CENTER);
		setSize(600, 800);
		setVisible(true);
		File file=new File("C:\\Documents and Settings\\zr\\桌面\\Java正则表达式教程.htm");
		edpl.setPage(file.toURI().toString());
	}

	void getPage(String site) {
		try {
			edpl.setPage(site);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "连接错误", "错误提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	class MyEnter implements ActionListener { // 处理输入网站时的回车操作
		public void actionPerformed(ActionEvent e) {
			String string = jtf.getText();
			getPage(string);
		}
	}

	class MyHyperListener implements HyperlinkListener {// 处理网页中的超链接
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				String string = e.getURL().toString();// 获取超链接指向的地址
				getPage(string);// 显示超链接指向的网页内容
			}
		}
	}

	public static void main(String arge[]) throws IOException {
		browser mybrowser = new browser();
		mybrowser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}