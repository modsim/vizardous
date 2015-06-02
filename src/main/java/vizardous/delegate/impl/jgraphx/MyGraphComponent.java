package vizardous.delegate.impl.jgraphx;

import java.awt.Point;
import java.awt.Rectangle;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

/**
 * Individual implementation of the mxGraphComponent that provides better zoom functionality
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class MyGraphComponent extends mxGraphComponent{

	/**
	 * Creates a new graph component
	 * @param graph to use
	 */
	public MyGraphComponent(mxGraph graph) {
		super(graph);
	}
	
	/**
	 * Provides better zoom functionality than mxGraphComponent.
	 * @param factor to zoom (max. 0.04)
	 */
	@Override
	public void zoom(double factor) {
		mxGraphView view = graph.getView();
		double newScale = (double) ((int) (view.getScale() * 100 * factor)) / 100;

		if(newScale <= 0.04)
			newScale = 0.05;
		
		factor = (newScale * 100)/(view.getScale() * 100);
		
		super.zoom(factor);
	}
	
	
	/**
	 * Extends the given implementation by the standard GraphComponent.
	 * Vertices are now in the foreground and selected if they were hit.
	 * @param x
	 * @param y
	 * @param hitSwimlaneContent
	 * @return Returns the cell at the given location.
	 */
	public Object getCellAt(int x, int y, boolean hitSwimlaneContent)
	{
		Object cell = getVertexAt(x, y, hitSwimlaneContent, null);
		if(cell != null)
			return cell;
		else
			return getCellAt(x, y, hitSwimlaneContent, null);
	}

	
	/**
	 * Returns the bottom-most vertex that intersects the given point (x, y) in
	 * the cell hierarchy starting at the given parent.
	 * 
	 * @param x
	 *            X-coordinate of the location to be checked.
	 * @param y
	 *            Y-coordinate of the location to be checked.
	 * @param parent
	 *            <mxCell> that should be used as the root of the recursion.
	 *            Default is <defaultParent>.
	 * @return Returns the child at the given location.
	 */
	public Object getVertexAt(int x, int y, boolean hitSwimlaneContent,
			Object parent)
	{
		if (parent == null)
		{
			parent = graph.getDefaultParent();
		}

		if (parent != null)
		{
			Point previousTranslate = canvas.getTranslate();
			double previousScale = canvas.getScale();

			try
			{
				canvas.setScale(graph.getView().getScale());
				canvas.setTranslate(0, 0);

				mxIGraphModel model = graph.getModel();
				mxGraphView view = graph.getView();

				Rectangle hit = new Rectangle(x, y, 1, 1);
				int childCount = model.getChildCount(parent);

				for (int i = childCount - 1; i >= 0; i--)
				{
					Object cell = model.getChildAt(parent, i);
					Object result = getCellAt(x, y, hitSwimlaneContent, cell);

					if (result != null && ((mxCell)result).isVertex())
					{
						return result;
					}
					else if (graph.isCellVisible(cell))
					{
						mxCellState state = view.getState(cell);

						if (state != null
								&& canvas.intersects(this, hit, state)
								&& (!graph.isSwimlane(cell)
										|| hitSwimlaneContent || (transparentSwimlaneContent && !canvas
										.hitSwimlaneContent(this, state, x, y))) && ((mxCell)cell).isVertex())
						{
							return cell;
						}
					}
				}
			}
			finally
			{
				canvas.setScale(previousScale);
				canvas.setTranslate(previousTranslate.x, previousTranslate.y);
			}
		}

		return null;
	}


}
