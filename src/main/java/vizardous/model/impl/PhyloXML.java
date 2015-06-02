/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents the content of a PhyloXML file. It supports multiple
 * Phylogeny objects but exposes an old API to ensure backwards compatibility.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0.0
 */
public class PhyloXML {

	/** Multiple phylogenies in one PhyloXML file. */
	private List<Phylogeny> phylogenies = new ArrayList<Phylogeny>();

	/**
	 * Default constructor.
	 */
	public PhyloXML() {}

	/**
	 * PhyloXML constructor.
         * 
	 * @param phylogenies Empty list of Phylogeny.
	 */
	public PhyloXML(List<Phylogeny> phylogenies) {
		this.phylogenies = phylogenies;
	}

	/**
	 * Constructor with variadic arguments to support backwards compatibility.
	 * 
	 * @param phylogenies One or more Phylogeny objects.
	 */
	public PhyloXML(Phylogeny... phylogenies) {
		this.phylogenies = new ArrayList<Phylogeny>(Arrays.asList(phylogenies));
	}

	/**
	 * Gets all Phylogenies existing in Phyloxml.
         * 
	 * @return Returns a list of all existing Phylogenies in this Phyloxml.
	 */
	public List<Phylogeny> getPhylogenies() {
		return this.phylogenies;
	}

	/**
	 * Keep the old version of the function for single phylogenies for
	 * compatibility reasons.
	 * 
	 * @return Returns the first Phylogeny in this Phyloxml/Forest.
	 */
//	@Deprecated
	public Phylogeny getPhylogenyObject() {
		return this.phylogenies.get(0);
	}
}
