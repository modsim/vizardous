package vizardous.delegate.impl.treeSort.sorter;

import java.util.Comparator;

import vizardous.delegate.impl.treeSort.getter.CellValueGetter;
import vizardous.model.impl.Cell;

/**
 * This class is a standard comparator for cells in a TreeMap and compares one specified value of the cell.
 * Therefore it extends the Comparable interface.
 *
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class SingleValueSorter implements Comparator<Cell> {

	/** The getter that extracts a cell's value */
	private CellValueGetter getter;
	
	/** Enables reverse sorting */
	private boolean bReverse = false;
	
	/**
	 * Creates a new sorter with the specified getter
	 * 
	 * @param getter
	 *            that will be used for extracting values from cells
	 */
	public SingleValueSorter(CellValueGetter getter) {
		this.getter = getter;
	}
	
	/**
	 * Creates a new sorter with the specified sortOption
	 * 
	 * @param sortOption
	 *            that defines which getter will be used
	 */
	public SingleValueSorter(SortParam sortOption)
	{
		getter = sortOption.getGetter();
	}

	/**
	 * Creates a new sorter with the specified getter and reverse mode
	 * 
	 * @param getter
	 *            that will be used
	 * @param bReverse
	 *            reverse mode (false -> no reverse, true -> reverse)
	 */
	public SingleValueSorter(CellValueGetter getter, boolean bReverse)
	{
		this(getter);
		this.bReverse = bReverse;
	}
	
	/**
	 * Creates a new sorter with the specified sortOption and reverse mode
	 * 
	 * @param sortOption
	 *            that defines which getter will be used
	 * @param bReverse
	 *            reverse mode (false -> no reverse, true -> reverse)
	 */
	public SingleValueSorter(SortParam sortOption, boolean bReverse)
	{
		this(sortOption);
		this.bReverse = bReverse;
	}	
	
	
	/**
	 * Compares to cells using a value served by the value getter
	 * 
	 * @param c1
	 *            The first cell
	 * @param c2
	 *            The second cell
	 * @return the comparison result
	 */
	public int compare(Cell c1, Cell c2) {
		// if the reverse mode is on, just negate the comparison result
		if(bReverse)
			return -nestedCompare(c1, c2);
		else
			return nestedCompare(c1, c2);
	}
	
	/**
	 * This is a private helper function for comparison of two cells that
	 * enables easier reverse sorting
	 * 
	 * @param c1
	 *            first cell
	 * @param c2
	 *            second cell
	 * @return the comparison result
	 */
	private int nestedCompare(Cell c1, Cell c2)
	{
		// extract the values out of the cells
		Comparable firstCell = getter.get(c1);
		Comparable secondCell = getter.get(c2);
		
		// compare both cells
		int iCompare = firstCell.compareTo(secondCell);
		
		// if the comparison says they are not equal save the comparison in result
		if(iCompare != 0)
			return iCompare;
		// else check the Ids to be sure whether they are really equal
		else
			return new IdSorter().compare(c1, c2);
	}

}
