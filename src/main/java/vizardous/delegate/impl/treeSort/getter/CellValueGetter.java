package vizardous.delegate.impl.treeSort.getter;

import vizardous.model.impl.Cell;

/**
 * The interface for all getters that extract values from cells. This value must
 * implement the comparable interface for easy sorting.
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public interface CellValueGetter {
	
	/**
	 * Gets a comparable value of a specified cell
	 * @param cell to consider
	 * @return the specified value of the cell
	 */
	public Comparable get(Cell cell);

}
