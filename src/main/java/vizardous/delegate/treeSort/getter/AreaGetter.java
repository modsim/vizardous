package vizardous.delegate.treeSort.getter;

import vizardous.model.Cell;

/**
 * The AreaGetter gives back the area of a specified cell
 *  
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class AreaGetter implements CellValueGetter {

	public Double get(Cell cell) {
		return cell.getArea();
	}

}
