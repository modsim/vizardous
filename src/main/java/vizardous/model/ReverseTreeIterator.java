package vizardous.model;

import java.util.Iterator;
import java.util.Stack;

/**
 * An {@link Iterator} that can iterate all predecessors of a provided
 * {@link Cell}. The first cell that is returned by this iterator is the parent
 * of the cell with which the iterator was initialized.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class ReverseTreeIterator implements Iterator<Cell> {

	private Stack<Cell> stack;
	
	public ReverseTreeIterator(Cell root) {
		this.stack = new Stack<Cell>();
		
		if (!root.isRoot()) {
			stack.push(root.getParentCell());
		}	
	}
	
	@Override
	public boolean hasNext() {
		return (!stack.isEmpty());
	}

	@Override
	public Cell next() {
		Cell c = stack.pop();
		
		if (!c.isRoot()) {
			stack.push(c.getParentCell());
		}
		
		return c;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not available.");
	}

}
