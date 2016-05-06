package cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import cn.gov.cbrc.sd.dz.zhaorui.GC;
import cn.gov.cbrc.sd.dz.zhaorui.component.InfoPane;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step3.Step3Module;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel0.Panel0;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel1.Panel1;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel2.Panel2;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel3.Panel3;
import cn.gov.cbrc.sd.dz.zhaorui.module.impl.step4.impl.panel5.Panel5;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.GraphicToolkit;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.TableToolkit;

@SuppressWarnings("serial")
public class ResultPanel extends JPanel {

	private GC GC = null;
	private JTabbedPane tabbedPane;
	private Panel0 panel0;
	private Panel1 panel1;
	private Panel2 panel2;
	private Panel3 panel3;
	private Panel1 panel4;
	private Panel5 panel5;
	private JButton export;

	private List<Graphic> circles;

	private final String TOTALTABLE="总表";
	private final String TABLE1="表1-担保圈清单";
	private final String TABLE2="表2-涉圈企业清单";
	private final String TABLE3="表3-“区域分布”分析结果";
	private final String TABLE4="表4-“重点客户”分析结果";
	private final String TABLE5="表5-“风险分类”分析结果";
	
	public ResultPanel(Step4Module step4Module) {
		super();
		this.GC = step4Module.getGC();
		this.setName(step4Module.getName());
		
		this.setLayout(new BorderLayout());
		
		export=new JButton("导出全部结果");
		this.add(export, BorderLayout.NORTH);

		tabbedPane = new JTabbedPane(); // 创建选项卡面板对象
		// 创建面板
		panel0 = new Panel0();
		panel1 = new Panel1();
		panel2 = new Panel2();
		panel3 = new Panel3();
		panel4 = new Panel1();
		panel5 = new Panel5();
		// panel1.setBackground(Color.yellow);
		// panel2.setBackground(Color.blue);
		// panel3.setBackground(Color.green);
		// panel4.setBackground(Color.red);
		// 将标签面板加入到选项卡面板对象上
		tabbedPane.addTab(TOTALTABLE, null, panel0, "Total panel");
		tabbedPane.addTab(TABLE1, null, panel1, "First panel");
		tabbedPane.addTab(TABLE2, null, panel2, "Second panel");
		tabbedPane.addTab(TABLE3, null, panel3, "Thrid panel");
		tabbedPane.addTab(TABLE4, null, panel4, "Fourth panel");
		tabbedPane.addTab(TABLE5, null, panel5, "Fifth panel");

		this.add(tabbedPane, BorderLayout.CENTER);
		this.setBackground(Color.white);
		
		addListeners();
	}


	public void refreshResult(List<Graphic> circles) throws Exception {
		this.circles=circles;
		if (circles != null) {	
			InfoPane.getInstance().info("正在整理报表数据……");
			panel0.refreshResult(circles);
			
			panel1.refreshResult(circles);
			
			panel2.refreshResult(GraphicToolkit.getCorpsSet(circles));
			
			panel3.refreshResult(circles);
			 
			List<Graphic> vipCircles=new ArrayList<Graphic>();
			for(Graphic circle:circles)
				if(circle.isVIPGraphic())
					vipCircles.add(circle);
			panel4.refreshResult(vipCircles);
			
			panel5.refreshResult(circles);
			InfoPane.getInstance().info("报表数据整理完成");
		}
	}

	private void addListeners() {
		export.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean result=doExport();
					if(result==true)
						JOptionPane.showMessageDialog(null, "导出成功", "导出结果", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
	}


	protected boolean doExport() throws Exception {
		if(this.circles==null||this.circles.size()==0)
			return false;
		String dir=GC.getOutputDir().getAbsolutePath()+"\\报表\\";
		TableToolkit.exp2Excel(panel0.getTable(), new File(dir+TOTALTABLE+".xls"));
		TableToolkit.exp2Excel(panel1.getTable(), new File(dir+TABLE1+".xls"));
		TableToolkit.exp2Excel(panel2.getTable(), new File(dir+TABLE2+".xls"));
		TableToolkit.exp2Excel(panel3.getTable(), new File(dir+TABLE3+".xls"));
		TableToolkit.exp2Excel(panel4.getTable(), new File(dir+TABLE4+".xls"));
		TableToolkit.exp2Excel(panel5.getTable(), new File(dir+TABLE5+".xls"));
		return true;
	}


}
