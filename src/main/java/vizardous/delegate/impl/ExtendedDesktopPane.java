/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import java.awt.Rectangle;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;



/**
 *  This class extends the <code>JDesktopPane</code> with the functionality of tiling
 *  and cascading the internal frames.
 *  Parts of the source code of this component is taken from <code>http://www.javalobby.org/</code> 
 *  (<a href="http://www.javalobby.org/forums/thread.jspa?threadID=15690&tstart=0">cascading</a>, 
 *  <a href="http://www.javalobby.org/forums/thread.jspa?threadID=15696&tstart=0">tiling</a>). 
 */
public class ExtendedDesktopPane extends JDesktopPane {
    
	public void tileHorizontal(int layer) {
		JInternalFrame[] frames = getAllFramesInLayer(layer);
		if (frames.length == 0) {
			return;
		}

		tileHorizontal(frames, getBounds());
	}
	
	
	public void tileHorizontal() {
		JInternalFrame[] frames = getAllFrames();
		if (frames.length == 0) {
			return;
		}

		tileHorizontal(frames, getBounds());
	}
	
	
	public void tileVertical(int layer) {
		JInternalFrame[] frames = getAllFramesInLayer(layer);
		if (frames.length == 0) {
			return;
		}

		tileVertical(frames, getBounds());
	}
	
	
	public void tileVertical() {
		JInternalFrame[] frames = getAllFrames();
		if (frames.length == 0) {
			return;
		}

		tileVertical(frames, getBounds());
	}
	
	
	private void restoreFrames(JInternalFrame[] frames) {
		for (int i = 0; i < frames.length; i++) {
			try {
				frames[i].setMaximum(false);
			}
			catch (PropertyVetoException e) {
				e.printStackTrace();  //TODO Exception nach außen leiten?
			}
		}
	}
	
	
	private void tileHorizontal(JInternalFrame[] frames, Rectangle dBounds) {
		int cols = (int)Math.sqrt(frames.length);
		int rows = (int)(Math.ceil( ((double)frames.length) / cols));
		int lastRow = frames.length - cols * (rows - 1);
		int width, height;

		restoreFrames(frames);
		if (lastRow == 0) {
			rows--;
			height = dBounds.height / rows;
		}
		else {
			height = dBounds.height / rows;
			if ( lastRow < cols ) {
				rows--;
				width = dBounds.width / lastRow;
				for (int i = 0; i < lastRow; i++ ) {
					frames[cols * rows + i].setBounds(i * width, rows * height, width, height);
				}
			}
		}

		width = dBounds.width / cols;
		for (int j = 0; j < rows; j++ ) {
			for (int i = 0; i < cols; i++ ) {
				frames[i + j * cols].setBounds(i * width, j * height, width, height);
			}
		}
	}
	
	
	private void tileVertical(JInternalFrame[] frames, Rectangle dBounds) {
		int rows = (int)Math.sqrt(frames.length);
		int cols = (int)(Math.ceil( ((double)frames.length) / rows));
		int lastCol = frames.length - rows * (cols - 1);
		int height, width;
		
		restoreFrames(frames);
		if (lastCol == 0) {
			cols--;
			width = dBounds.width / cols;
		}
		else {
			width = dBounds.width / cols;
			if ( lastCol < rows ) {
				cols--;
				height = dBounds.height / lastCol;
				for (int i = 0; i < lastCol; i++ ) {
					frames[rows * cols + i].setBounds(cols * width, i * height, width, height);
				}
			}
		}

		height = dBounds.height / rows;
		for (int j = 0; j < cols; j++ ) {
			for (int i = 0; i < rows; i++ ) {
				frames[i + j * rows].setBounds(j * width, i * height, width, height);
			}
		}
	}
	
	
	public void cascade(int layer) {
		JInternalFrame[] frames = getAllFramesInLayer(layer);
		if (frames.length == 0) return;

		cascade(frames, getBounds(), 24);
	}
	
	
	public void cascade() {
		JInternalFrame[] frames = getAllFrames();
		if (frames.length == 0) return;

		cascade(frames, getBounds(), 24);
	}
	
	
	private void cascade(JInternalFrame[] frames, Rectangle dBounds, int separation) {
		int margin = frames.length*separation + separation;
		int width = dBounds.width - margin;
		int height = dBounds.height - margin;
		
		restoreFrames(frames);
		for (int i = 0; i < frames.length; i++) {
			frames[i].setBounds(separation + dBounds.x + i * separation,
					separation + dBounds.y + i * separation,
					width, height);
		}
	}
        
}