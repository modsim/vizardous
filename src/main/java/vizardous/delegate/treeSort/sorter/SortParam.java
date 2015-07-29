package vizardous.delegate.treeSort.sorter;

import vizardous.delegate.treeSort.getter.AreaGetter;
import vizardous.delegate.treeSort.getter.CellValueGetter;
import vizardous.delegate.treeSort.getter.DepthGetter;
import vizardous.delegate.treeSort.getter.FluorescenceGetter;
import vizardous.delegate.treeSort.getter.IdGetter;
import vizardous.delegate.treeSort.getter.LengthGetter;
import vizardous.delegate.treeSort.getter.NumChildrenGetter;
import vizardous.delegate.treeSort.getter.OffspringGetter;

/**
 * The SortBy enum gives options for different sorting parameters
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public enum SortParam {
	ID(new IdGetter(), "Cells are sorted by id."),
	AREA(new AreaGetter(), "Cells are sorted by their area."),
	DEPTH(new DepthGetter(), "Cells are sorted by their successors tree depth."),
	FLUORESCENCE_CRIMSON(new FluorescenceGetter("crimson"), "Cells are sorted by their crimson fluorescence value."),
	FLUORESCENCE_YFP(new FluorescenceGetter("yfp"), "Cells are sorted by their yfp fluorescence value."),
	LENGTH(new LengthGetter(), "Cells are sorted by their length."),
	NUM_CHILDREN(new NumChildrenGetter(), "Cells are sorted by their amount of successors."),
	OFFSPRING(new OffspringGetter(), "Cells are sorted by their amount of successor leaves.");
	
	/** The getter */
	private CellValueGetter getter;
	/** Tooltip that describes the getter's functionality */
	private String tooltip;
	
	/**
	 * Creates a new sort param with a getter and a tooltip
	 * 
	 * @param getter
	 *            that will be used for sorting
	 * @param tooltip
	 *            that describes sorting functionality
	 */
	private SortParam(CellValueGetter getter, String tooltip) {
		this.getter = getter;
		this.tooltip = tooltip;
	}
	
	/**
	 * @return the getter
	 */
	public CellValueGetter getGetter() {
		return getter;
	}
	
	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}
}
