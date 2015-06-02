package vizardous.delegate.impl.treeSort.getter;

import vizardous.model.impl.Cell;

/**
 * This class extracts the value of the specified fluorescence channel of a
 * given cell
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class FluorescenceGetter implements CellValueGetter {

	/** The fluorescence channel */
	private String fluorescence;
	
	/**
	 * Constructs a FluorescenceGetter that extracts the value of the specified
	 * fluorescence channel.
	 * 
	 * @param fluorescence
	 *            Fluorescence channel from which values are obtained
	 */
	public FluorescenceGetter(String fluorescence)
	{
		this.fluorescence = fluorescence;
	}

	/**
	 * @param cell
	 *            The cell that we cope with
	 * @return The specified fluorescence of the cell
	 */
	public Double get(Cell cell) {
		return cell.getFluorescences().get(fluorescence);
	}

}
