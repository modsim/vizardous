package vizardous.delegate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

import vizardous.delegate.table.CellsInformationTable;
import vizardous.model.Cell;
import vizardous.model.Constants;

public class CellPopupMenu extends JPopupMenu{

	private JMenuItem add;
	private CellsInformationTable cellInfoTable;
	List<Cell> currentList;
	
	CellPopupMenu(CellsInformationTable infoTable) {
		super();
		cellInfoTable = infoTable;
		add = new JMenuItem();
		
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Cell cell : CellPopupMenu.this.currentList) {
					Double fluorescence = (double) 0;
					
		            if ( cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) != null ) 
		                fluorescence = cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS);

	                ((DefaultTableModel) cellInfoTable.getModel()).
	                addRow(new Object[]{cell.getId(), fluorescence, cell.getLength(), cell.getArea()});
				}
			}
		});

		
		this.add(add);
	}
	
	public void show(List<Cell> list, Component invoker, int x, int y) {
		currentList = list;
		if(list.size() == 1) {
			add.setText("Add cell to Infotable");
			add.setToolTipText("Add current cell to the infotable.");
		}
		else {
			add.setText("Add cells to Infotable");
			add.setToolTipText("Add selected cells to the infotable.");
		}
				
		this.show(invoker, x, y);
	}
}
