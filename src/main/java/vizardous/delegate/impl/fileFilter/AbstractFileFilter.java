/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl.fileFilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * An abstract {@link FileFilter} that filters according to the file extension.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public abstract class AbstractFileFilter extends FileFilter {

	/** TODO Documentation */
	String[] extensions;
	
	/** TODO Documentation */
	String description;
	
	public AbstractFileFilter() {
		this (new String[] {""}, null);
	}
	
	public AbstractFileFilter(String ext) {
		this (new String[] {ext}, null);
	}
	
	public AbstractFileFilter(String[] exts, String descr) {
		// Clone and lowercase the extensions (why, because it makes checking easier)
		extensions = new String[exts.length];
		for ( int i = exts.length - 1; i >=0; i-- ) {
			extensions[i] = exts[i].toLowerCase();
		}
		// Make sure we have a valid (if simplistic) description.
		description = (descr == null ? exts[0] + " files" : descr);
	}
	
	public boolean accept(File f) {
		// We always allow directories, regardless of their extensions.
		if (f.isDirectory()) { return true; }
		
		// It's a regular file, so check the extension
		String name = f.getName().toLowerCase();
		for ( int i  = extensions.length - 1; i >= 0; i-- ) {
			if ( extensions[i].equals(".*") ) { return true; }
			if ( name.endsWith(extensions[i]) ) { return true; }
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

}

