package cn.gov.cbrc.sd.dz.zhaorui.model;

import org.w3c.dom.Document;

import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;

public class GCClassify {
	private int loan_balance_floor;
	private int loan_balance_ceiling;

	public GCClassify() throws Exception {
		Document doc = Config.getDoc();
		loan_balance_floor = Integer
				.parseInt(XMLToolkit.getElementById(doc, "29").getAttribute("loan-balance-floor"));
		loan_balance_ceiling = Integer
				.parseInt(XMLToolkit.getElementById(doc, "29").getAttribute("loan-balance-ceiling"));
	}

	public int getLoan_balance_floor() {
		return loan_balance_floor;
	}

	public void setLoan_balance_floor(int loan_balance_floor) {
		this.loan_balance_floor = loan_balance_floor;
	}

	public int getLoan_balance_ceiling() {
		return loan_balance_ceiling;
	}

	public void setLoan_balance_ceiling(int loan_balance_ceiling) {
		this.loan_balance_ceiling = loan_balance_ceiling;
	}

	public boolean updateConfigCache() throws Exception {
		Document doc=Config.getDoc();
		XMLToolkit.getElementById(doc, "29").setAttribute("loan-balance-floor",String.valueOf(loan_balance_floor));
		XMLToolkit.getElementById(doc, "29").setAttribute("loan-balance-ceiling",String.valueOf(loan_balance_ceiling));
		return true;
	}
	
}
