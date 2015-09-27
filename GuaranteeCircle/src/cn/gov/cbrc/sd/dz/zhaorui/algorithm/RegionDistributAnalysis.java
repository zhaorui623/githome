package cn.gov.cbrc.sd.dz.zhaorui.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Region;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;

/**
 * 从地域分布维度分析
 * 
 * @author zr
 *
 */
public class RegionDistributAnalysis {

	private Map<String, Region> regionsMap;

	public RegionDistributAnalysis() throws Exception {
		super();
		this.regionsMap = new HashMap<String, Region>();
		Document doc = Config.getDoc();
		NodeList regionElements = doc.getElementsByTagName(Config.REGION_TAG);
		for (int i = 0; i < regionElements.getLength(); i++) {
			Element regionElement = (Element) regionElements.item(i);
			String name = regionElement.getAttribute("name");
			String codeStr = regionElement.getAttribute("code");
			String[] codes = codeStr.split(";");
			Region region = new Region(name, codes);
			for (String code : codes)
				this.regionsMap.put(code, region);
		}
	}

	public Region getRegion(String code) {
		Region region;
		if (code == null || "null".equals(code))
			return new Region("未知地区");
		region = this.regionsMap.get(code);
		if (region == null)
			region = new Region("省外地区", code);
		return region;
	}

	public void analysisRegion(Graphic graphic) {
		Set<Corporation> vSet = graphic.vertexSet();
		Map<Region, Integer> countMap = new HashMap<Region, Integer>();
		Map<Region, Double> loanBalanceMap = new HashMap<Region, Double>();
		// 取graphic中的每一个节点，看它的地区编码，然后记录各地区节点个数和贷款余额
		for (Corporation v : vSet) {
			String regionCode = v.getStringValue(Corporation.REGION_CODE_COL);
			if ("null".equals(regionCode) || regionCode == null)
				continue;
			double loanBalance = v.getDoubleValue(Corporation.LOAN_BALANCE_COL);
			Region vRegion = this.getRegion(regionCode);
			if (countMap.containsKey(vRegion) == false) {
				countMap.put(vRegion, 1);
				loanBalanceMap.put(vRegion, loanBalance);
			} else {
				countMap.put(vRegion, countMap.get(vRegion) + 1);
				loanBalanceMap.put(vRegion, loanBalanceMap.get(vRegion) + loanBalance);
			}
		}
		// 取节点个数最多的地区，认作graphic的地区，节点数相同的，取贷款余额较大的地区。
		int maxCount = -1;
		double maxLoanBalance = -1;
		Region region = null;
		for (Region r : countMap.keySet()) {
			int count = countMap.get(r);
			double loanBalance = loanBalanceMap.get(r);
			if (count > maxCount) {
				maxCount = count;
				maxLoanBalance = loanBalance;
				region = r;
			} else if (count == maxCount) {
				if (loanBalance > maxLoanBalance) {
					maxCount = count;
					maxLoanBalance = loanBalance;
					region = r;
				}
			}
		}
		graphic.setRegion(region);
	}

}
