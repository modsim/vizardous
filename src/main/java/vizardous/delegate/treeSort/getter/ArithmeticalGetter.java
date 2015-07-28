package vizardous.delegate.treeSort.getter;

import vizardous.model.Cell;

/**
 * The ArithmeticalGetter uses a specified getter to calculate the arithmetic
 * mean of all the children of a cell
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class ArithmeticalGetter implements CellValueGetter{

	/** The sub getter to extract a value. */
	private CellValueGetter getter;
	
	/**
	 * Constructs an ArithmeticalGetter based on the values that are provided by
	 * a {@link CellValueGetter}.
	 * 
	 * @param getter
	 */
	public ArithmeticalGetter(CellValueGetter getter) {
		this.getter = getter;
	}
	
	/** Stores sum and number of objects for the arithmetical mean */
	private class CalculationData {
		public double dSum;
		public int iNumber;
	}
	
	/**
	 * @return The arithmetical value of the cell and all its children.
	 * @param cell
	 *            {@link Cell} to consider
	 */
	public Double get(Cell cell) {
		
		// call the recurse function that calculates sum and number of elements
		CalculationData help = recursiveGet(cell);
		
		// return the arithmetical middle
		return help.dSum / (double)help.iNumber;
	}
	
	/**
	 * Recursive loops through the cells children.
	 * 
	 * @param cell
	 *            to consider
	 * @return A Calculation-class object with the sum and the number of
	 *         children.
	 */
	private CalculationData recursiveGet(Cell cell) {
		// Create new Calculation class object that holds the sum over all cells and their amount
		CalculationData calc = new CalculationData();
		calc.iNumber++;

		
		// Catch the case the getter does not return a number
		try
		{
			calc.dSum += ((Number)getter.get(cell)).doubleValue();
		}
		catch(ClassCastException exc)
		{
			throw new IllegalArgumentException("Getter must return a Number (Double, Integer, Long, etc.) to work with the ArithmeticalGetter");
		}
		
		// loop over all children and repeat algorithm (recursion)
		for(Cell child : cell.getChildren())
		{
			CalculationData localCalc = recursiveGet(child);
			calc.iNumber += localCalc.iNumber;
			calc.dSum += localCalc.dSum;
		}
		
		// return the filled structure
		return calc;
	}

}
