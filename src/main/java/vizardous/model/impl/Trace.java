package vizardous.model.impl;

import java.util.List;

import vizardous.model.impl.Node;

/**
 * Class representing a trace made up from two nodes (two parent cells)
 * 
 * @author Christopher Probst <c.probst@fz-juelich.de>
 * @version 1.0.0
 */

public class Trace {

	/** Trace of two nodes (parent cells) */
	private List<Node> trace;
	
	public Trace(List<Node> trace) {
		
		this.trace = trace;
	}
	
	public List<Node> getTraces() {
		return this.trace;
	}
	
	public Node getParentNode() {
		return this.trace.get(0);
	}
	
	public Node getChildNode() {
		return this.trace.get(1);
	}
	
	public Boolean hasChild() {
		if(this.getChildNode() == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/** Check whether this trace contains the root node (cell) or not */
	public Boolean hasRoot() {
		if(this.getParentNode().getCell().isRoot()) {
			return true;
		} else {
			return false;
		}
	}
	
	/** Division time for respective division event */
	public double getDivisionTime() {
		MIFrame miframeLastCell = trace.get(1).getCell().getMIFrameObject();
		MIFrame miframeFirstCell = trace.get(0).getCell().getMIFrameObject();
		
		return miframeLastCell.getElapsedTime()-miframeFirstCell.getElapsedTime();
	}
	
	/** Generation number of division event */
	public int getGeneration() {
		return getParentNode().getGeneration();
	}
	
}
