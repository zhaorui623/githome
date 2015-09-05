package cn.gov.cbrc.sd.dz.zhaorui.component;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class NumberSpinner extends JSpinner{
	public NumberSpinner(int value,int min,int max,int step){
		super(new SpinnerNumberModel(value,min,max,step));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this, "0");
		this.setEditor(editor);
		JFormattedTextField textField = ((JSpinner.NumberEditor) this.getEditor())
				.getTextField();
		textField.setEditable(true);
		DefaultFormatterFactory factory = (DefaultFormatterFactory) textField.getFormatterFactory();
		NumberFormatter formatter = (NumberFormatter) factory.getDefaultFormatter();
		formatter.setAllowsInvalid(false);
	}
}
