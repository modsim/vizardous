package vizardous.delegate.impl.treeSort.sorter;

import java.util.Comparator;

import vizardous.delegate.impl.treeSort.getter.ArithmeticalGetter;
import vizardous.delegate.impl.treeSort.getter.CellValueGetter;
import vizardous.model.impl.Cell;

/**
 * This class is a standard comparator for cells in a TreeMap and compares one specified value of the cell.
 * Therefore it extends the Comparable interface.
 *
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class ArithmeticalSingleValueSorter implements Comparator<Cell> {

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
	public ArithmeticalSingleValueSorter(CellValueGetter getter)
	{
		this.getter = getter;
	}
	
	/**
	 * Creates a new sorter with the specified sortOption
	 * 
	 * @param sortOption
	 *            that defines which getter will be used
	 */
	public ArithmeticalSingleValueSorter(SortParam sortOption)
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
	public ArithmeticalSingleValueSorter(CellValueGetter getter, boolean bReverse)
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
	public ArithmeticalSingleValueSorter(SortParam sortOption, boolean bReverse)
	{
		this(sortOption);
		this.bReverse = bReverse;
	}
		
	
	
	/**
	 * Compares to cells using a value served by the value getter
	 * 
	 * @param o1
	 *            first cell
	 * @param o1
	 *            second cell
	 * @return the comparison result
	 */
	public int compare(Cell o1, Cell o2) {
		// if the reverse mode is on, just negate the comparison result
		if(bReverse) {
			return -nestedCompare(o1, o2);
		}
		else {
			return nestedCompare(o1, o2);
		}
	}
	
	/**
	 * This is a private helper function for comparison of two cells that
	 * enables easier reverse sorting
	 * 
	 * @param o1
	 *            first cell
	 * @param o2
	 *            second cell
	 * @return the comparison result
	 */
	private int nestedCompare(Cell o1, Cell o2)
	{
		// extract the values out of the cells 
		Comparable firstValue = new ArithmeticalGetter(getter).get(o1);
		Comparable secondValue = new ArithmeticalGetter(getter).get(o2);
		
		//and compare them
		int iCompare = firstValue.compareTo(secondValue);
		
		// if the comparison says they are not equal save the comparsion in result
		if(iCompare != 0) {
			return iCompare;
		}
		// else check the ids to be sure whether they are really equal
		else {
			return  new IdSorter().compare(o1, o2);
		}
	}

}
