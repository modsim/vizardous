package vizardous.delegate.impl.jgraphx;

import vizardous.model.impl.Cell;
import vizardous.model.impl.Clade;
import vizardous.model.impl.Constants;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.view.mxGraph;

public class MyGraph extends mxGraph {

	/**
	 * Creates a new graph
	 */
	public MyGraph() {
		// create the graph with my own model implementation
		super(new MyModel());
	}
	
	/**
	 * Generate tooltip information for the given cell
	 * @return the tooltip
	 */
	@Override
	public String getToolTipForCell(Object arg0) {
		mxCell graphCell = (mxCell)arg0;
		
		if(graphCell.isVertex()) {
			Cell cell = (Cell)graphCell.getValue();
	        String tooltipInfo = "<html>\n<head>\n<title>Cell Informations</title>\n</head>\n<body>\n\n<h2>"
	                + cell.getId()
	                +"</p>\n<p>Cell Id: "+ cell.getId()
	                +"</p>\n<p>Cell Length: " + cell.getLength()
	                +"</p>\n<p>Cell Fluorescence yfp-type: "+ cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS) 
	                +"</p>\n<p>Cell Fluorescence crimson-type: "+ cell.getFluorescences().get(Constants.METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON) 
	                +"</p>\n<p>Cell Area: "+ cell.getArea() 
	                +"</p>\n<p>Cell Elapsed-Time: "+ cell.getMIFrameObject().getElapsedTime()
	                +"    ,<br>\netc...<br></p>"
//	                + String.format("pos: (%.2f,%.2f)", graphCell.getGeometry().getCenterX(), graphCell.getGeometry().getCenterY())
//	                + String.format("ypos: %.2f", graphCell.getGeometry().getY() + 50 - graphCell.getGeometry().getHeight()/2)
	                + String.format("<br />Diameter: %.2f", graphCell.getGeometry().getWidth())
	                + "\n\n</body>\n</html>";
	        return tooltipInfo;
		}
		else
		{
			return "Branch lenght: " + ((Clade)graphCell.getValue()).getBranchLength();
		}

		
		
/*		if(graphCell.isEdge())
			return "I'm an edge!";
		else
			return "I'm a vertex!";*/
	}
	
	/**
	 * Overrides the normal insertVertex function because a 'v' is added to the id.
	 * Throws IllegalArgumentException if the id is null.
	 */
	@Override
	public Object insertVertex(Object parent, String id, Object value, double x, double y, double width, double height, String style, boolean relative) throws IllegalArgumentException {
		if(id == null)
			throw new IllegalArgumentException("The id can't be null!");
		return super.insertVertex(parent, "v" + id, value, x, y, width, height, style, relative);
	};

	/**
	 * Overrides the normal insertEdge function because an 'e' is added to the id.
	 * Throws IllegalArgumentException if the id is null.
	 */
	@Override
	public Object insertEdge(Object parent, String id, Object value,
			Object source, Object target, String style) {
		if(id == null)
			throw new IllegalArgumentException("The id can't be null!");
		return super.insertEdge(parent, "e" + id, value, source, target, style);
	}

	/**
	 * There won't be displayed any labels in the graph.
	 * @return always "" (empty String)
	 */
	@Override
	public String getLabel(Object arg1) {
			return "";
	}

	/**
	 * Disable selection for edges (else edges can be moved)
	 */
	@Override
	public boolean isCellSelectable(Object cell) {
		if(((mxCell)cell).isEdge())
			return false;
		else
			return super.isCellSelectable(cell);
	}
}
