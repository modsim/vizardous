package vizardous.delegate.table;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellInformationTableDeleteEvent extends TableModelEvent {

	private static final long serialVersionUID = 1L;

	private String cellId;
	
	public String getCellId() {
		return cellId;
	}
	
	public CellInformationTableDeleteEvent(TableModel source, int firstRow, int lastRow, int column, int type, String cellId) {
		super(source, firstRow, lastRow, column, type);
		
		this.cellId = cellId;
	}

}
