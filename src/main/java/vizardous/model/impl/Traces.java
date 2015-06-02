package vizardous.model.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import vizardous.model.impl.Trace;

/**
 * Class representing a traces made up from multiple trace 
 * 
 * @author Christopher Probst <c.probst@fz-juelich.de>
 * @version 1.0.0
 */

public class Traces {
	private List<Trace> traces;
	
	public Traces(List<Trace> traces) {
		this.traces = traces;
	}
	
	public List<Trace> getTraces() {
		return this.traces;
	}
	
	public Trace getLastTrace() {
		return this.traces.get(0);
	}
	
	public void generateTraces(Forest forest, List<Cell> cellsInPhylogeny) {
		int t = 0;

	}

}
