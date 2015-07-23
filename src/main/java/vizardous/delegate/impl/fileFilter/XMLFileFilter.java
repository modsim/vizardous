/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.fileFilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A {@link FileFilter} for XML files.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class XMLFileFilter extends AbstractFileFilter {

	private static final XMLFileFilter instance = new XMLFileFilter();

	public static XMLFileFilter getInstance() {
		return instance;
	}
	
	protected XMLFileFilter() {
                super(new String[] { ".xml" }, "eXtensible Markup Language (*.xml)");
    }
    
    @Override
    public boolean accept( final File f ) {
        final String file_name = f.getName().trim().toLowerCase();
        return file_name.endsWith( ".xml" ) || file_name.endsWith( "phylo.xml" )
                || file_name.endsWith( "meta.xml" ) || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "phylo-/MetaXML files (*.xml, *phylo.xml, *metaXML.xml)";
    }
} // XMLFilter