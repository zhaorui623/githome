package cn.gov.cbrc.sd.dz.zhaorui.algorithm.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.w3c.dom.Document;

import cn.gov.cbrc.sd.dz.zhaorui.algorithm.HugeCircleSplitAlgorithm;
import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;
import cn.gov.cbrc.sd.dz.zhaorui.model.Unit;
import cn.gov.cbrc.sd.dz.zhaorui.resource.Config;
import cn.gov.cbrc.sd.dz.zhaorui.resource.ResourceManager;
import cn.gov.cbrc.sd.dz.zhaorui.toolkit.XMLToolkit;

public class DefaultAlgorithm extends HugeCircleSplitAlgorithm {

	public DefaultAlgorithm(String algorithm_name, Boolean algorithm_enable, Boolean algorithm_selected) throws Exception {

		super(algorithm_name, algorithm_enable, algorithm_selected);

		
	}

	@Override
	protected List<Graphic> splitHugeCircleImpl(Graphic graphic) {
		return null;
		
	}

}
