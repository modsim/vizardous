package vizardous.delegate.treeSort.getter;

import vizardous.model.Cell;

/**
 * This class extracts the length (Double) of a specified cell.
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class LengthGetter implements CellValueGetter {

	public Double get(Cell cell) {
		return cell.getLength();
	}

}
