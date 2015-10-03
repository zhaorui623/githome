package cn.gov.cbrc.sd.dz.zhaorui.toolkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;

public class GraphicToolkit {

	public static Set<Corporation> getVertexSet(List<Graphic> graphics) {
		Set<Corporation> set = null;
		if (graphics != null) {
			set = new HashSet<Corporation>();
			for (Graphic g : graphics) {
				set.addAll(g.vertexSet());
			}
		}
		return set;
	}

	public static double getLoanBalance(List<Graphic> graphics) {
		double loanBalance = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.LOAN_BALANCE_COL);
				loanBalance = loanBalance + value;
			}
		}
		return loanBalance;
	}

	public static double getGuanZhuLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.GUANZHU_LOAN_BALANCE_COL);
				result = result + value;
			}
		}
		return result;
	}

	public static double getBuLiangLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value = corp.getDoubleValue(Corporation.BULIANG_LOAN_BALANCE_COL);
				result = result + value;
			}
		}
		return result;
	}

	public static double getYuQi90YiNeiLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value1 = corp.getDoubleValue(Corporation.YUQI_30DAY_YINEI_LOAN_BALANCE_COL);
				double value2 = corp.getDoubleValue(Corporation.YUQI_31DAY_90DAY_LOAN_BALANCE_COL);
				result = result + value1 + value2;
			}
		}
		return result;
	}

	public static double getOffBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value1 = corp.getDoubleValue(Corporation.OFF_BALANCE_CD_COL);
				double value2 = corp.getDoubleValue(Corporation.OFF_BALANCE_XYZ_COL);
				double value3 = corp.getDoubleValue(Corporation.OFF_BALANCE_BH_COL);
				double value4 = corp.getDoubleValue(Corporation.OFF_BALANCE_WTDK_COL);
				double value5 = corp.getDoubleValue(Corporation.OFF_BALANCE_WTTZ_COL);
				double value6 = corp.getDoubleValue(Corporation.OFF_BALANCE_CN_COL);
				double value7 = corp.getDoubleValue(Corporation.OFF_BALANCE_XYFXRZYH_COL);
				double value8 = corp.getDoubleValue(Corporation.OFF_BALANCE_JRYSP_COL);
				result = result + value1 + value2 + value3 + value4 + value5 + value6 + value7 + value8;
			}
		}
		return result;
	}

	public static double getLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getLoanBalance(list);
	}

	public static double getGuanZhuLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getGuanZhuLoanBalance(list);
	}

	public static double getBuLiangLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getBuLiangLoanBalance(list);
	}

	public static double getYuQi90YiNeiLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getYuQi90YiNeiLoanBalance(list);
	}

	public static double getOffBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getOffBalance(list);
	}

	public static double getYuQi90YiShangLoanBalance(Graphic circle) {
		List<Graphic> list = new ArrayList<Graphic>();
		list.add(circle);
		return getYuQi90YiShangLoanBalance(list);
	}

	public static double getYuQi90YiShangLoanBalance(List<Graphic> graphics) {
		double result = 0;
		if (graphics != null) {
			Set<Corporation> corps = getVertexSet(graphics);
			for (Corporation corp : corps) {
				double value1 = corp.getDoubleValue(Corporation.YUQI_91DAY_180DAY_LOAN_BALANCE_COL);
				double value2 = corp.getDoubleValue(Corporation.YUQI_181DAY_365DAY_LOAN_BALANCE_COL);
				double value3 = corp.getDoubleValue(Corporation.YUQI_1YEAR_3YEAR_LOAN_BALANCE_COL);
				double value4 = corp.getDoubleValue(Corporation.YUQI_3YEAR_YISHANG_LOAN_BALANCE_COL);
				result = result + value1 + value2 + value3 + value4;
			}
		}
		return result;
	}

	/**
	 * 从graphics中找出“核心企业集”包含corps的graphic，移除之
	 * 
	 * @param corp
	 * @param graphics
	 */
	public static void removeGraphicWhosCoreCorpsContains(List<Corporation> corps, List<Graphic> graphics) {
		if (graphics != null && corps != null && corps.size() > 0) {
			Iterator<Graphic> iterator = graphics.iterator();
			while (iterator.hasNext()) {
				Graphic g = iterator.next();
				Set<Corporation> coreCorpsOfG = g.getCoreCorps();
				if (coreCorpsOfG != null && coreCorpsOfG.containsAll(corps)) {
					iterator.remove();
				}
			}
		}
	}
	public static void removeGraphicWhosCoreCorpis(Corporation corp, List<Graphic> graphics) {
		List<Corporation> corps=new ArrayList<Corporation>();
		corps.add(corp);
		removeGraphicWhosCoreCorpsContains(corps,graphics);
	}

}
