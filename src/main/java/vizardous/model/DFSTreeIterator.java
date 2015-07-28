package vizardous.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * TODO
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0.0
 */
public class DFSTreeIterator implements Iterator<Cell> {

    private Stack<Cell> stack;
    private List<Cell> discovered;

    public DFSTreeIterator(Cell root) {
            this.stack = new Stack<Cell>();
            stack.push(root);
            discovered = new ArrayList<Cell>();
    }

    @Override
    public boolean hasNext() {
            return (!stack.isEmpty());
    }

    @Override
    public Cell next() {
            Cell c = stack.pop();

            if (!discovered.contains(c)) {
                    discovered.add(c);
                    for (Cell child : c.getChildren()) {
                            stack.push(child);
                    }
            }

            return c;
    }

    @Override
    public void remove() {
            throw new UnsupportedOperationException("Not available.");
    }

}
