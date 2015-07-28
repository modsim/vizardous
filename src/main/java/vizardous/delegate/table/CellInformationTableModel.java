package vizardous.delegate.table;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import vizardous.model.Cell;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellInformationTableModel extends DefaultTableModel {

	static final long serialVersionUID = 1L;

	private Set<String> cellIds = new HashSet<String>();
	
	public CellInformationTableModel() {
		super(new Double[][] {}, new String[] { "Cell Id", "Fluorescence",
				"Length", "Area" });
	}

	Class[] types = new Class[] { java.lang.Integer.class,
			java.lang.Double.class, java.lang.Double.class,
			java.lang.Double.class };
	boolean[] canEdit = new boolean[] { false, false, false, false };

	public Class getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return canEdit[columnIndex];
	}
	
	@Override
	public void addRow(Object[] rowData) {
		super.addRow(rowData);
		
		/* Store cellId separately */
		cellIds.add((String) rowData[0]);
	}
	
	@Override
	public void removeRow(int row) {
		// Remember cellId
		String cellId = (String) this.getValueAt(row, 0);
		
		// Remove data
		dataVector.remove(row);
		cellIds.remove(cellId);
		
		// Fire event
		fireTableChanged(new CellInformationTableDeleteEvent(this, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE, cellId));
	}
	
	public boolean contains(Cell cell) {
		String cellId = cell.getId();		
		return contains(cellId);
	}
	
	public boolean contains(String cellId) {
		return cellIds.contains(cellId);
	}
	
}
