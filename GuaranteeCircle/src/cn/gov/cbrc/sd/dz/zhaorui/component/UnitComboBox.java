package cn.gov.cbrc.sd.dz.zhaorui.component;

import javax.swing.JComboBox;

import cn.gov.cbrc.sd.dz.zhaorui.model.Unit;

public class UnitComboBox extends JComboBox {
	public UnitComboBox(){
		super(Unit.values());
	}

}
