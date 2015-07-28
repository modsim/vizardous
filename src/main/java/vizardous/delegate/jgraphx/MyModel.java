package vizardous.delegate.jgraphx;

import org.apache.commons.lang3.NotImplementedException;

import Freeze.NotFoundException;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

public class MyModel extends mxGraphModel {
	
	/**
	 * Returns the vertex with the specified id or throws exceptions if the
	 * vertex has not been found or there is an edge with the specified id
	 * 
	 * @param id to lookup
	 * @return the associated vertex
	 */
	public mxCell getVertex(String id) throws NotFoundException, IllegalArgumentException {
		Object vertex = super.getCell('v' + id);
		
		if(vertex == null) {
			throw new NotFoundException("There is no vertex with the id \"" + id + "\" in the graph!");
		}

		mxCell cell = (mxCell)vertex;
		
		if(cell.isEdge()) {
			throw new IllegalArgumentException("The id \"" + id + "\" is associated with an edge to not a vertex!");
		}
		
		return cell;
	}
	
	/**
	 * Returns the edge with the specified id or throws exceptions if the
	 * edge has not been found or there is a vertex with the specified id
	 * 
	 * @param id to lookup
	 * @return the associated vertex
	 */
	public mxCell getEdge(String id) throws NotFoundException, IllegalArgumentException {
		Object edge = super.getCell('e' + id);
		
		if(edge == null) {
			throw new NotFoundException("There is no edge with the id \"" + id + "\" in the graph!");
		}

		mxCell cell = (mxCell)edge;
		
		if(cell.isVertex()) {
			throw new IllegalArgumentException("The id \"" + id + "\" is associated with an edge to not an edge!");
		}
		
		return cell;
	}
	
	/**
	 * Do definitely not use the get Cell method of the model directly. Use getVertex or getEdge instead.
	 */
	@Deprecated
	@Override
	public Object getCell(String id) throws NotImplementedException{
		return super.getCell(id);
	}
}
