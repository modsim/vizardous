package vizardous.delegate.treeSort.sorter;

import java.util.Comparator;

import vizardous.model.Cell;

/**
 * This enumeration provides options for how values for sorting are computed.
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public enum SortType {
	BRANCH("At every brach the values of the child cells are compared. The \"bigger\" cell becomes the upper one!"),
	LEAF("Only the leaves of the tree are considered when sorting. This may give a perfect ascending sort but this is often prohibited by the tree structure. This sort guarantees that the maxiumum value is at the top but not that the lowest value is at the bottom!"),
	ARITHMETICAL_AVERAGE("At every branch the arithmetic value of the children and their successors are compared. The cell with the bigger average value becomes the upper one!");
	
	/** Tooltip that describes the getter's functionality */
	String tooltip;
	
	private SortType(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public static Comparator<Cell> getSorter(SortType sortType, SortParam param) {
		switch(sortType) {
		case BRANCH:
			return new SingleValueSorter(param.getGetter());
		case LEAF:
			return new LeafIntervalSorter(param.getGetter());
		case ARITHMETICAL_AVERAGE:
			return new ArithmeticalSingleValueSorter(param.getGetter());
		default:
			throw new IllegalArgumentException(sortType + " can not be handled");
		}
	}

	/**
	 * @return returns tooltip of SortType
	 */
	public String getTooltip()	{
		return tooltip;
	}

}
