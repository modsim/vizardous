package vizardous.delegate.impl.treeSort.sorter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections15.comparators.ComparableComparator;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * The DirectedSortedSparseGraph provides a class for directed and sorted graphs
 * (just vertices).
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 * 
 * @param <V>
 *            is the vertex class
 * @param <E>
 *            is the edge class
 */
public class DirectedSortedSparseGraph<V, E> extends DirectedSparseGraph<V, E>{

	/** The comparator that sorts vertices in TreeMap */
	protected Comparator<V> vertex_comparator;

	/**
	 * Creates a new graph that is sorted by the vertex comparator
	 * 
	 * @param vertex_comparator
	 *            sorts the TreeMap
	 */
	public DirectedSortedSparseGraph(Comparator<V> vertex_comparator)
	{
		super();
		
		// create a hash map wich will contain all vertices in the tree. It is not sorted!!!
		// The Pair construction contains at first the predecessors and second the successors.
		// For the sorted tree result just these will be sorted in a TreeMap
        vertices = new HashMap<V, Pair<Map<V,E>>>();
        
        this.vertex_comparator = vertex_comparator;
	}
		
	@Override
    public boolean addVertex(V vertex)
    {
        if(vertex == null) {
            throw new IllegalArgumentException("vertex may not be null");
        }
        if (!containsVertex(vertex)) {
        	// add the vertex to the hash map with a pair of predecessors and successors
            vertices.put(vertex, new Pair<Map<V,E>>(new TreeMap<V,E>(vertex_comparator), new TreeMap<V,E>(vertex_comparator)));
            return true;
        } else {
            return false;
        }
    }
	
}
