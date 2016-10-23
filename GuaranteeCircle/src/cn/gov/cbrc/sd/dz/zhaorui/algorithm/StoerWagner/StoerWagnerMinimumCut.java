package cn.gov.cbrc.sd.dz.zhaorui.algorithm.StoerWagner;
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.HashSet; 
import java.util.List; 
import java.util.Map; 
import java.util.PriorityQueue; 
import java.util.Set;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import cn.gov.cbrc.sd.dz.zhaorui.model.Corporation;
import cn.gov.cbrc.sd.dz.zhaorui.model.Graphic;

 
 
 
/**
 * Implements the <a href="http://dl.acm.org/citation.cfm?id=263872">Stoer and 
 * Wagner minimum cut algorithm</a>. Deterministically computes the minimum cut 
 * in O(|V||E| + |V|log|V|) time. This implementation uses Java's PriorityQueue 
 * and requires O(|V||E|log|E|) time. M. Stoer and F. Wagner, "A Simple Min-Cut 
 * Algorithm", Journal of the ACM, volume 44, number 4. pp 585-591, 1997. 
 * 
 * @author Robby McKilliam 
 */ 
public class StoerWagnerMinimumCut<V, E> 
{ 
    //~ Instance fields -------------------------------------------------------- 
 
    final WeightedGraph<Set<V>, DefaultWeightedEdge> workingGraph; 
 
    double bestcutweight = Double.POSITIVE_INFINITY; 
    Set<V> bestCut; 
 
    boolean firstRun = true; 
 
    //~ Constructors ----------------------------------------------------------- 
 
    /**
     * Will compute the minimum cut in graph. 
     * 
     * @param graph graph over which to run algorithm 
     */ 
//    public StoerWagnerMinimumCut(WeightedGraph<V, E> graph) 
    public StoerWagnerMinimumCut(WeightedGraph<V, E> graph) 
    { 
        //get a version of this graph where each vertex is wrapped with a list 
        workingGraph = 
            new SimpleWeightedGraph<Set<V>, DefaultWeightedEdge>( 
                DefaultWeightedEdge.class); 
        Map<V, Set<V>> vertexMap = new HashMap<V, Set<V>>(); 
        for (V v : graph.vertexSet()) { 
            Set<V> list = new HashSet<V>(); 
            list.add(v); 
            vertexMap.put(v, list); 
            workingGraph.addVertex(list); 
        } 
        for (E e : graph.edgeSet()) { 
            V s = graph.getEdgeSource(e); 
            Set<V> sNew = vertexMap.get(s); 
            V t = graph.getEdgeTarget(e); 
            Set<V> tNew = vertexMap.get(t); 
            DefaultWeightedEdge eNew = workingGraph.addEdge(sNew, tNew); 
            workingGraph.setEdgeWeight(eNew, graph.getEdgeWeight(e)); 
        } 
 
        //arbitrary vertex used to seed the algorithm. 
        Set<V> a = workingGraph.vertexSet().iterator().next();
//        double maxWeight=-1;
//        DefaultWeightedEdge maxWeightEdge = workingGraph.edgeSet().iterator().next();
//        for(DefaultWeightedEdge e: workingGraph.edgeSet()){
//        	double w=workingGraph.getEdgeWeight(e);
//        	if(w>maxWeight){
//        		maxWeight=w;
//        		maxWeightEdge=e;
//        	}
//        }
//        Set<V> a = workingGraph.getEdgeSource(maxWeightEdge);
        while (workingGraph.vertexSet().size() > 1) { 
            minimumCutPhase(a); 
        } 
    } 
 



	/**
     * Implements the MinimumCutPhase function of Stoer and Wagner 
     */ 
    protected void minimumCutPhase(Set<V> a) 
    { 
        //construct sorted queue with vertices connected to vertex a 
        PriorityQueue<VertexAndWeight> queue = 
            new PriorityQueue<VertexAndWeight>(); 
        Map<Set<V>, VertexAndWeight> dmap = 
            new HashMap<Set<V>, VertexAndWeight>(); 
        for (Set<V> v : workingGraph.vertexSet()) { 
            if (v != a) { 
            	DefaultWeightedEdge edge=workingGraph.getEdge(v, a);
                Double w = 
                    -(edge==null?0:workingGraph.getEdgeWeight(edge)); 
                VertexAndWeight vandw = new VertexAndWeight(v, w); 
                queue.add(vandw); 
                dmap.put(v, vandw); 
            } 
        } 
 
        //now iteratatively update the queue to get the required vertex ordering 
        List<Set<V>> list = 
            new ArrayList<Set<V>>(workingGraph.vertexSet().size()); 
        list.add(a); 
        while (!queue.isEmpty()) { 
            Set<V> v = queue.poll().vertex; 
            dmap.remove(v); 
            list.add(v); 
            for (DefaultWeightedEdge e : workingGraph.edgesOf(v)) { 
                Set<V> vc; 
                if (v != workingGraph.getEdgeSource(e)) { 
                    vc = workingGraph.getEdgeSource(e); 
                } else { 
                    vc = workingGraph.getEdgeTarget(e); 
                } 
                if (dmap.get(vc) != null) { 
                	DefaultWeightedEdge edge=workingGraph.getEdge(v, vc);
                    Double neww = 
                        -(/*edge==null?0:*/workingGraph.getEdgeWeight(edge)) 
                        + dmap.get(vc).weight; 
                    queue.remove(dmap.get(vc)); //this is O(logn) but could be 
                                                //O(1)? 
                    dmap.get(vc).weight = neww; 
                    queue.add(dmap.get(vc)); //this is O(logn) but could be 
                                             //O(1)? 
                } 
            } 
        } 
 
        //if this is the first run we compute the weight of last vertex in the 
        //list 
//        if (firstRun) { 
//            Set<V> v = list.get(list.size() - 1); 
//            double w = vertexWeight(v); 
//            if (w < bestcutweight) { 
//                bestcutweight = w; 
//                bestCut = v; 
//            } 
//            firstRun = false; 
//        } 
 
        //the last two elements in list are the vertices we want to merge. 
        Set<V> s = list.get(list.size() - 2); 
        Set<V> t = list.get(list.size() - 1); 
 
        double w=vertexWeight(t);
        if(w<bestcutweight){
        	bestcutweight=w;
        	bestCut=t;
        }

        System.out.println("list="+list);
        System.out.println("workingGraph="+workingGraph);
        System.out.println("cut="+t+"\tweight="+w+"\n==========\n");
        //merge these vertices and get the weight. 
        VertexAndWeight vw = mergeVertices(s, t); 
 
        //If this is the best cut so far store it. 
//        if (vw.weight < bestcutweight) { 
//            bestcutweight = vw.weight; 
//            bestCut = vw.vertex;
//        } 
        
    } 
 
    /**
     * Return the weight of the minimum cut 
     */ 
    public double minCutWeight() 
    { 
        return bestcutweight; 
    } 
 
    /**
     * Return a set of vertices on one side of the cut 
     */ 
    public Set<V> minCut() 
    { 
        return bestCut; 
    } 
 
    /**
     * Merges vertex t into vertex s, summing the weights as required. Returns 
     * the merged vertex and the sum of its weights 
     */ 
    protected VertexAndWeight mergeVertices(Set<V> s, Set<V> t) 
    { 
        //construct the new combinedvertex 
        Set<V> set = new HashSet<V>(); 
        for (V v : s) { 
            set.add(v); 
        } 
        for (V v : t) { 
            set.add(v); 
        } 
        workingGraph.addVertex(set); 
 
        //add edges and weights to the combined vertex 
        double wsum = 0.0; 
        for (Set<V> v : workingGraph.vertexSet()) { 
            if ((s != v) && (t != v)) { 
                DefaultWeightedEdge etv = workingGraph.getEdge(t, v); 
                DefaultWeightedEdge esv = workingGraph.getEdge(s, v); 
                double wtv = 0.0, wsv = 0.0; 
                if (etv != null) { 
                    wtv = workingGraph.getEdgeWeight(etv); 
                } 
                if (esv != null) { 
                    wsv = workingGraph.getEdgeWeight(esv); 
                } 
                double neww = wtv + wsv; 
                wsum += neww; 
                if (neww != 0.0) { 
                    workingGraph.setEdgeWeight( 
                        workingGraph.addEdge(set, v), 
                        neww); 
                } 
            } 
        } 
 
        //remove original vertices 
        workingGraph.removeVertex(t); 
        workingGraph.removeVertex(s); 
 
        return new VertexAndWeight(set, wsum); 
    } 
 
    /**
     * Compute the sum of the weights entering a vertex 
     */ 
    public double vertexWeight(Set<V> v) 
    { 
        double wsum = 0.0; 
        for (DefaultWeightedEdge e : workingGraph.edgesOf(v)) { 
            wsum += workingGraph.getEdgeWeight(e); 
        } 
        return wsum; 
    } 
 
    //~ Inner Classes ---------------------------------------------------------- 
 
    /**
     * Class for weighted vertices 
     */ 
    protected class VertexAndWeight 
        implements Comparable<VertexAndWeight> 
    { 
        public Set<V> vertex; 
        public Double weight; 
 
        public VertexAndWeight(Set<V> v, double w) 
        { 
            this.vertex = v; 
            this.weight = w; 
        } 
 
        @Override public int compareTo(VertexAndWeight that) 
        { 
            return Double.compare(weight, that.weight); 
        } 
 
        @Override public String toString() 
        { 
            return "(" + vertex + ", " + weight + ")"; 
        } 
    } 
    
    public static void main(String[] args) {
    	Graphic g=new Graphic();
    	Corporation corps[]=new Corporation[9];
    	for(int i=1;i<=8;i++){
    		corps[i]=Corporation.createDefaultCorp(""+i, ""+i);
    		g.addVertex(corps[i]);
    	}

    	g.addEdge(corps[1], corps[2],2);
    	g.addEdge(corps[2], corps[3],3);
    	g.addEdge(corps[3], corps[4],4);
    	g.addEdge(corps[5], corps[6],3);
    	g.addEdge(corps[6], corps[7],1);
    	g.addEdge(corps[7], corps[8],3);
    	g.addEdge(corps[1], corps[5],3);
    	g.addEdge(corps[2], corps[6],2);
    	g.addEdge(corps[3], corps[7],2);
    	g.addEdge(corps[4], corps[8],2);
    	g.addEdge(corps[4], corps[7],2);
    	g.addEdge(corps[5], corps[2],2);
    	
//    	SimpleWeightedGraph<String, DefaultWeightedEdge> graph=new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
//    	graph.addVertex("6");
//    	graph.addVertex("5");
//    	graph.addVertex("4");
//    	graph.addVertex("3");
//    	graph.addVertex("2");
//    	graph.addVertex("1");
//    	graph.addVertex("0");
//    	graph.addVertex("7");
//    	
//    	graph.addEdge("0", "1");
//    	graph.addEdge("0", "2");
//    	graph.addEdge("0", "3");
//    	graph.addEdge("0", "4");
//    	graph.addEdge("1", "2");
//    	graph.addEdge("1", "3");
//    	graph.addEdge("2", "3");
//    	graph.addEdge("3", "7");
//    	graph.addEdge("4", "5");
//    	graph.addEdge("4", "6");
//    	graph.addEdge("4", "7");
//    	graph.addEdge("5", "6");
//    	graph.addEdge("5", "7");
//    	graph.addEdge("6", "7");
//    	graph.addEdge("0", "7");
////    	graph.addEdge("1", "5");
//
//    	graph.setEdgeWeight(graph.getEdge("0", "1"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("0", "2"),3.0);
//    	graph.setEdgeWeight(graph.getEdge("0", "3"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("0", "4"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("1", "2"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("1", "3"),3.0);
//    	graph.setEdgeWeight(graph.getEdge("2", "3"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("3", "7"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("4", "5"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("4", "6"),4.0);
//    	graph.setEdgeWeight(graph.getEdge("4", "7"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("5", "6"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("5", "7"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("6", "7"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("0", "7"),2.0);
////    	graph.setEdgeWeight(graph.getEdge("1", "5"),1.0);
    	

//    	graph.addVertex("1");
//    	graph.addVertex("8");
//    	graph.addVertex("2");
//    	graph.addVertex("7");
//    	graph.addVertex("6");
//    	graph.addVertex("5");
//    	graph.addVertex("4");
//    	graph.addVertex("3");
//    	
//    	graph.addEdge("1", "2");
//    	graph.addEdge("2", "3");
//    	graph.addEdge("3", "4");
//    	graph.addEdge("5", "6");
//    	graph.addEdge("6", "7");
//    	graph.addEdge("7", "8");
//    	graph.addEdge("1", "5");
//    	graph.addEdge("2", "6");
//    	graph.addEdge("3", "7");
//    	graph.addEdge("4", "8");
//    	graph.addEdge("4", "7");
//    	graph.addEdge("5", "2");
//
//    	graph.setEdgeWeight(graph.getEdge("1", "2"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("2", "3"),3.0);
//    	graph.setEdgeWeight(graph.getEdge("3", "4"),4.0);
//    	graph.setEdgeWeight(graph.getEdge("5", "6"),3.0);
//    	graph.setEdgeWeight(graph.getEdge("6", "7"),1.0);
//    	graph.setEdgeWeight(graph.getEdge("7", "8"),3.0);
//    	graph.setEdgeWeight(graph.getEdge("1", "5"),3.0);
//    	graph.setEdgeWeight(graph.getEdge("3", "7"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("2", "6"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("4", "8"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("4", "7"),2.0);
//    	graph.setEdgeWeight(graph.getEdge("5", "2"),2.0);
    	
		StoerWagnerMinimumCut<Corporation, DefaultWeightedEdge> cut=new StoerWagnerMinimumCut<Corporation, DefaultWeightedEdge>(g.g());
		System.out.println(cut.minCut()+","+cut.minCutWeight()+"\n");
	}
} 
 