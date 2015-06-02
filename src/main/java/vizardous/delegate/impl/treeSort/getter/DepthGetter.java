package vizardous.delegate.impl.treeSort.getter;

import vizardous.model.impl.Cell;

/**
 * This class gives the depth (in the tree) of a cell
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class DepthGetter implements CellValueGetter {

	public Integer get(Cell cell) {
		Integer iMaxDepthChildren = 0;
		
		for(Cell child : cell.getChildren())
		{
			iMaxDepthChildren = Math.max(iMaxDepthChildren, get(child));
		}
		
		return 1 + iMaxDepthChildren;
	}
}
