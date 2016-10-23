package cn.gov.cbrc.sd.dz.zhaorui.algorithm.StoerWagner;

import org.jgrapht.EdgeFactory;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleGraph;

public class SimpleWeightedGraph<V, E> 
    extends SimpleGraph<V, E> 
    implements WeightedGraph<V, E> 
{ 
    //~ Static fields/initializers --------------------------------------------- 
 
    private static final long serialVersionUID = 3906088949100655922L; 
 
    //~ Constructors ----------------------------------------------------------- 
 
    /**
     * Creates a new simple weighted graph with the specified edge factory. 
     * 
     * @param ef the edge factory of the new graph. 
     */ 
    public SimpleWeightedGraph(EdgeFactory<V, E> ef) 
    { 
        super(ef); 
    } 
 
    /**
     * Creates a new simple weighted graph. 
     * 
     * @param edgeClass class on which to base factory for edges 
     */ 
    public SimpleWeightedGraph(Class<? extends E> edgeClass) 
    { 
        this(new ClassBasedEdgeFactory<V, E>(edgeClass)); 
    } 
} 
 
// End SimpleWeightedGraph.java

