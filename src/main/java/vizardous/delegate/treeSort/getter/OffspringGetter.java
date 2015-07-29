package vizardous.delegate.treeSort.getter;

import vizardous.model.Cell;

/**
 * This class extracts the number of successor leaves of a specified cell
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class OffspringGetter implements CellValueGetter {

	public Integer get(Cell cell) {
		return cell.getAttachedLeaves().size();
	}

}
