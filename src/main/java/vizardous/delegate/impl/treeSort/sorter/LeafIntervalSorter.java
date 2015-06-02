package vizardous.delegate.impl.treeSort.sorter;

import java.util.Comparator;

import org.springframework.asm.commons.Method;

import vizardous.delegate.impl.treeSort.getter.CellValueGetter;
import vizardous.model.impl.Cell;

/**
 * The Leaf IntervalSorter provides the functionality for sorting a tree by
 * considering only the leaf values.
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class LeafIntervalSorter implements Comparator<Cell> {

	/** The getter that extracts the cells' value */
	private CellValueGetter getter;
	
	/**
	 * Creates a new Leaf Interval Sorter.
	 * 
	 * @param getter
	 *            that extracts the value to sort from cell
	 */
	public LeafIntervalSorter(CellValueGetter getter) {
		this.getter = getter;
	}
	
	/**
	 * Creates a new Leaf Interval Sorter.
	 * 
	 * @param sortOption
	 *            specifies the getter that is used
	 */
	public LeafIntervalSorter(SortParam sortOption) {
		getter = sortOption.getGetter();
	}
	
	/**
	 * Interval that contains min and max value
	 * 
	 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
	 */
	public class Interval implements Comparable<Interval>
	{
		/** min value of the interval */
		private Comparable min;
		/** max value of the interval */
		private Comparable max;
		
		/**
		 * Creates a new interval with min and max value
		 * 
		 * @param min
		 *            value of the interval
		 * @param max
		 *            value of the interval
		 */
		public Interval(Comparable min, Comparable max)
		{
			// Just copy min and max
			this.min = min;
			this.max = max;
		}
		
		/**
		 * Joins the current Interval with a specified one. After the joining
		 * the interval contains the old one and the specified.
		 * 
		 * @param interval
		 *            to join
		 */
		public void join(Interval interval) {
			// if the other interval's minimum is smaller than the current minimum copy the new minimum
			if(interval.min.compareTo(min) < 0)
				min = interval.min;
			// if the other interval's maximum is bigger than the current max copy the new maximum
			if(interval.max.compareTo(max) > 0)
				max = interval.max;
		}		

		/**
		 * Compares this interval with another one.
		 * 
		 * @param i
		 *            The interval to compare with
		 */
		public int compareTo(Interval i) {
			// We have I1=this=[min, max] and I2=[i.min, i.max]
			// if max < i.min then I1 is smaller than I2
			if(max.compareTo(i.min) < 0)
				return -1;
			// if min > i.max then I1 is bigger than I2
			else if(min.compareTo(i.max) > 0)
				return 1;

			// Now handle the case if the two intervals are overlapping
			// [  I1(this)   [  ]   I2]
			if(i.min.compareTo(max) < 0 && i.max.compareTo(max) > 0)
				return -1;
			// [  I2   [  ]  I1(this)  ]
			else if(min.compareTo(i.max) < 0 && max.compareTo(i.max) > 0)
				return 1;

			// See that highest leaf is always at the border (right or left (if reverse))
			if(max.compareTo(i.max) > 0)
				return 1;
			else if(max.compareTo(i.max) < 0)
				return -1;
			
			// Intervals can not be distinguished
			else
				return 0;
		}
		
		public Comparable getMin() {
			return min;
		}
		
		public Comparable getMax() {
			return max;
		}
		
		public String toString()
		{
			return "[" + min + "," + max + "]";
		}
	}

	public int compare(Cell o1, Cell o2) {
		// get the intervals by recursive construction
		Interval a = recInt(o1);
		Interval b = recInt(o2);
				
		// compare the two intervals
		int iCompare = a.compareTo(b);

		// if they are not equal return the result
		if(iCompare != 0)
			return iCompare;
		
		// if they seem to be equal check it by the ids
		return new IdSorter().compare(o1, o2);
	}
	
	/**
	 * Calculates the interval of leaf cells recursive
	 * 
	 * @param cell
	 *            that is considered
	 * @return that interval that contains all the values of cell's or its
	 *         successors' leaves
	 */
	public Interval recInt(Cell cell)
	{
		// Only consider leaves for interval creating
		if(cell.isLeaf())
		{
			// create new interval for the leaf where max and min are equal
			return new Interval(getter.get(cell), getter.get(cell));
		}

		// New empty interval because this cell is not a leaf but one of its total children has to be.
		Interval i = null;
		
		// join all the children intervals (they are all not null because every child has at least one leaf as successor)
		for(Cell child : cell.getChildren())
		{
			if(i == null)
				i = recInt(child);
			else
				i.join(recInt(child));
		}
		
		// return the result
		return i;
	}

}
