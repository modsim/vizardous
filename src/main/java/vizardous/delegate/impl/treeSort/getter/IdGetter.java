package vizardous.delegate.impl.treeSort.getter;

import vizardous.model.impl.Cell;

/**
 * This class gives the id of a specified cell
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class IdGetter implements CellValueGetter {

	public String get(Cell cell) {
		return cell.getId();
	}

}
