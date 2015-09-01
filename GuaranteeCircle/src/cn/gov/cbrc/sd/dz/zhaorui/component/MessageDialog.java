package cn.gov.cbrc.sd.dz.zhaorui.component;

import java.awt.Color;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageDialog extends JDialog {
	JTextArea area = new JTextArea();

	public MessageDialog(Frame parent, String title, String message) {
		this(parent, title, message, 700, 500);
	}

	public MessageDialog(Frame parent, String title, String message, int width,
			int height) {

		super(parent, true);
		this.setTitle(title);

		// area.setLineWrap(true);
		area.setText(message);
		area.setBackground(Color.white);
		area.setEditable(false);
		JScrollPane pane = new JScrollPane(area);
		pane.setAutoscrolls(true);
		if (parent != null)
			this.setLocationRelativeTo(parent);
		else
			this.setLocationByPlatform(true);
		this.add(pane);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(width, height);
	}

	public JTextArea getTextArea() {
		return area;
	}
}
