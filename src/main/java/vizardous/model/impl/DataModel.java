/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents data model of Vizardous.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */

public class DataModel {
   
	/** TODO Documentation */
	private Set<Map.Entry<File, File>> files = null;
	
	/** TODO Documentation */
	private List<Forest> forests = null;
    
	/**
	 * Constructs a DataModel that contains forests from the provdided file
	 * pairs of PhyloXML and MetaXML.
	 * 
	 * @param msFiles
	 *            List of mapped files to parse.
	 */
	public DataModel(Set<Map.Entry<File, File>> msFiles) throws PhyloXMLException, MetaXMLException {
		this.files = msFiles;

		this.forests = new ArrayList<Forest>();
		for (Map.Entry<File, File> me : this.files) {
			File phyloFile = me.getKey();
			File metaFile = me.getValue();
			if ((phyloFile != null) && (metaFile != null)) {
				Forest f = new Forest(phyloFile, metaFile);
				forests.add(f);
			}
		}
	}
	
	/**
	 * Constructs an empty DataModel.
	 */
	public DataModel() {
		this.forests = new ArrayList<Forest>();
	}	
    
    /**
     * Adds a Forest to the DataSet.
     * 
     * @param forest The forest to add.
     */
    public void addForest(Forest forest) {
    	this.forests.add(forest);
    }

    /**
     * Removes the Forest from the list.
     * 
     * @param forest
     */
	public void removeForest(Forest forest) {
		int index = this.forests.indexOf(forest);
		
		this.forests.remove(index);
	}

	/**
     * Gets all the forest in Vizardous data model.
     * 
     * @return Returns a list of the forest existing in Vizardous data model
     */
    public List<Forest> getForestsList() {
        return this.forests;
    } 

}
