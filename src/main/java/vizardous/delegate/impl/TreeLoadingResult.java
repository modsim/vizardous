package vizardous.delegate.impl;

import vizardous.delegate.impl.analysis.PhyloTreeAnalyser;
import vizardous.model.impl.Forest;

/**
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class TreeLoadingResult {
	
	public Forest forest;
	public PhyloTreeAnalyser phyloTreeAnalyser;
	
	/**
	 * @param forest
	 * @param phyloTreeAnalyser
	 */
	public TreeLoadingResult(Forest forest, PhyloTreeAnalyser phyloTreeAnalyser) {
		this.forest = forest;
		this.phyloTreeAnalyser = phyloTreeAnalyser;
	}
	
	
}
