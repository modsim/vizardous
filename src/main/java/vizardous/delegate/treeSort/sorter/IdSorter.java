package vizardous.delegate.treeSort.sorter;

import java.util.Comparator;

import vizardous.model.Cell;

/**
 * Sample Sorter class that sorts Cells by their specific Id (compares strings
 * lexicographically).
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class IdSorter implements Comparator<Cell> {

	public int compare(Cell first, Cell second) {
		return first.getId().compareTo(second.getId());
	}

}
