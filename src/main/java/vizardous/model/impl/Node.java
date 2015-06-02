package vizardous.model.impl;

import java.util.List;

/**
 * Nodes refer to cells after the division event has taken place
 * 
 * 
 * @author Christopher Probst <c.probst@fz-juelich.de>
 *
 */

public class Node {
	/** Generation number of node */
	private int generation;
	
	/** Cell of node */
	private Cell cell;
	
	public Node(int generation, Cell cell) {
		
		this.generation = generation;
		
		this.cell = cell;
	}
	
	/** Check whether a parent node is root or not */
	public Boolean hasAcendants() {
		if(cell.isRoot()) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<Cell> getChildren() {
		return this.cell.getChildren();
	}
	
	/** Get cell length after division */
	public double getCellLengthAfterDivision() {
		return this.cell.getLength();
	}
	
	/** Get cell area after division */
	public double getCellAreaAfterDivision() {
		return this.cell.getArea();
	}
	
	/** Get the cell before division */
	public Cell getCellBeforeDivision() {
		return this.cell.getParentCell();
	}
	
	/** Get cell length before division */
	public double getCellLengthBeforeDivision() {
		return this.getCellBeforeDivision().getLength();
	}
	
	/** Get cell area before division */
	public double getCellAreaBeforeDivision() {
		return this.getCellBeforeDivision().getArea();
	}

	/** Get generation number of node */
	public int getGeneration() {
		return this.generation;
	}
	
	/** Get cell of node */
	public Cell getCell() {
		return this.cell;
	}
}
