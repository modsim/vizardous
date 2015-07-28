package vizardous.delegate.treeSort.getter;

import vizardous.model.Cell;

/**
 * This class gives the number of children of a specified cell
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class NumChildrenGetter implements CellValueGetter {

	public Integer get(Cell cell) {
		return getSuccesorCount(cell);
	}
	
	/**
	 * Calculates the number of successors of a specified cell
	 * 
	 * @param cell
	 *            that is considered
	 * @return the number of successors of the specified cell
	 */
	private Integer getSuccesorCount(Cell cell)
	{
		Integer iResult = 0;
		for(Cell child : cell.getChildren())
		{
			iResult++;
			iResult += getSuccesorCount(child);
		}
		
		return iResult;
	}

}
