package cn.gov.cbrc.sd.dz.zhaorui.component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test {

		public static void main(String[] args) {
			final JFrame f = new JFrame();
			WizardPane w = new WizardPane("向导",
					"这是一个向导页面这是一个向导页面这是一个向导页面这是一个向导页面这是一个向导页面这是一个向导页面这是一个向导页面这是一个向导页面",
					new JPanel()){
						@Override
						protected void finish() {
							f.dispose();
						}

						@Override
						protected void recordAnswer(int pageIndex) {
							// TODO Auto-generated method stub
							
						}			
			};
			w.addPage("1", "这是第一个页面", new JLabel("这不是扯么，我是老大啊！"));
			w.addPage("2", "这是第二个页面", new JLabel("这不是扯么，我是老二啊！"));
			w.addPage(new JLabel("这不是扯么，我是老三啊！"));
			w.addPage(new JLabel("我是终结者！"));
			f.add(w);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setSize(500, 500);
			f.setVisible(true);
		}

}
