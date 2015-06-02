package vizardous.delegate.impl.graphics;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vizardous.delegate.impl.table.CellInformationTableDeleteEvent;
import vizardous.model.impl.Cell;
import vizardous.model.impl.Forest;
import vizardous.model.impl.MIFrame;

/**
 * TODO Documentation
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class CellDivisionTimeChart2D extends TraceChart2D {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@link CellDivisionTimeChart2D} from a {@link Forest}.
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public CellDivisionTimeChart2D(Forest forest) {
		super(forest);
		
		this.tabName = "Division Times";
	}

	@Override
	public final JFreeChart createJFreeChart(XYDataset dataset) {
		String timeUnit = this.forest.getMetaxml().getAllFrames().get(0).getElapsedTimeUnit();

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart("Individual division times", // chart title
				"Generation [-]", // domain axis label
				String.format("Division time [%s]", timeUnit), // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
		);

		this.styleChart(chart);

		// Customize the domain axis
		final NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
		domainAxis.setTickUnit(new NumberTickUnit(1.0)); // Generations are
															// integers..

		return chart;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (this.dataset instanceof DefaultXYDataset) {
			DefaultXYDataset ds = (DefaultXYDataset) this.dataset;

			// Row removed
			if (e.getType() == TableModelEvent.DELETE) {
				// Note: CellInformationTableDeleteEvent provides the cellId
				if (e instanceof CellInformationTableDeleteEvent) {
					String cellId = ((CellInformationTableDeleteEvent) e).getCellId();
					String rowKey = "Cell ID " + cellId;
					try {
						ds.removeSeries(rowKey);
					} catch (UnknownKeyException unknownKeyEx) {
						this.logger.debug("Removal of row not possible (rowKey unknown)", unknownKeyEx);
					}
				}

			}

			// Row added
			if (e.getType() == TableModelEvent.INSERT) {
				TableModel tm = null;
				if (e.getSource() instanceof TableModel) {
					tm = (TableModel) e.getSource();
				}

				String cellId = (String) tm.getValueAt(e.getFirstRow(), 0);
				Cell cell = this.forest.getCellById(cellId);

				// Map for storing the division times per generation
				Map<Double, Double> temp = new TreeMap<Double, Double>();

				List<Cell> parentsCellList = this.forest.getPredecessors(cell.getId());

				// Iterate in reverse order
				Iterator<Cell> predecessorIterator = parentsCellList.iterator();

				// The case that the cell is a leaf. The division time of
				// cell/leaf is equals to elapsed time of leaf minus the elapsed
				// time of the first ancestors, which has more than one child
				if (cell.isLeaf()) {
					/* First and last cell of a generation */
					Cell firstCell = null;
					Cell lastCell = cell;

					/* Computed division times */
					List<Double> divisionTimes = new LinkedList<Double>();

					while (predecessorIterator.hasNext()) {
						Cell predecessor = predecessorIterator.next();
						// Check if predecessor has siblings (or is root node)
						if (predecessor.getParentCell() != null && predecessor.getParentCell().getChildren().size() <= 1) {
							continue;
						} else {
							firstCell = predecessor;
						}

						// Compute time between firstCell and lastCell
						MIFrame miframeLastCell = lastCell.getMIFrameObject();
						double elapsedTimeLastCell = 0.0d;
						if (miframeLastCell != null) {
							elapsedTimeLastCell = miframeLastCell.getElapsedTime();
						} else {
							throw new IllegalArgumentException("MIFrameObject is null!");
						}

						MIFrame miframeFirstCell = firstCell.getMIFrameObject();
						double elapsedTimeFirstCell = 0.0d;
						if (miframeFirstCell != null) {
							elapsedTimeFirstCell = miframeFirstCell.getElapsedTime();
						} else {
							throw new IllegalArgumentException("MIFrameObject is null!");
						}

						double divisionTime = elapsedTimeLastCell - elapsedTimeFirstCell;
						divisionTimes.add(divisionTime);

						firstCell = null;
						lastCell = predecessor;
					}

					// TODO Remove obsolete computations

					// Reverse list to have them ordered by generation
					Collections.reverse(divisionTimes);

					// Create dataset and prepare for export
					for (int i = 0; i < divisionTimes.size(); i++) {
						double generation = i + 1; // Shift by one for a
						// meaningful
						// generation number
						double divisionTime = divisionTimes.get(i);

						// Prepare for export
						temp.put(generation, divisionTime);
					}

					double[][] data = CellFluorescenceIndividualGenerationsChart2D.convertMapToArray(temp);
					ds.addSeries("Cell ID " + cell.getId(), data);
				}
				// The case that the cell is not a leaf. The division time of
				// cell is equals to the elapsed time of the first ancestors,
				// which has more than one child minus the next ancestors, which
				// has more than one child
				else if (!cell.isLeaf() && !cell.isRoot()) {
					/* Computed division times */
					List<Double> divisionTimes = new LinkedList<Double>();
					List<Cell> cellTempList = new LinkedList<Cell>();

					while (predecessorIterator.hasNext()) {
						Cell cellTemp = predecessorIterator.next();
						if (cellTemp.getParentCell() != null && cellTemp.getParentCell().getChildren().size() > 1) {
							cellTempList.add(cellTemp);
						}
					}
					Cell rootCell = parentsCellList.get(parentsCellList.size() - 1);
					cellTempList.add(rootCell);
					/* First and last cell of a generation */
					Cell firstCell = null;
					// Selected cell
					Cell lastCell = cellTempList.get(0);
					for (int i = 1; i < cellTempList.size(); i++) {
						firstCell = cellTempList.get(i);
						// Compute time between firstCell and lastCell
						MIFrame miframeLastCell = lastCell.getMIFrameObject();
						double elapsedTimeLastCell = 0.0d;
						if (miframeLastCell != null) {
							elapsedTimeLastCell = miframeLastCell.getElapsedTime();
						} else {
							throw new IllegalArgumentException("MIFrameObject is null!");
						}

						MIFrame miframeFirstCell = firstCell.getMIFrameObject();
						double elapsedTimeFirstCell = 0.0d;
						if (miframeFirstCell != null) {
							elapsedTimeFirstCell = miframeFirstCell.getElapsedTime();
						} else {
							throw new IllegalArgumentException("MIFrameObject is null!");
						}

						double divisionTime = elapsedTimeLastCell - elapsedTimeFirstCell;
						divisionTimes.add(divisionTime);
						lastCell = firstCell;
					}

					// TODO Remove obsolete computations

					// Reverse list to have them ordered by generation
					Collections.reverse(divisionTimes);

					// Create dataset and prepare for export
					for (int i = 0; i < divisionTimes.size(); i++) {
						double generation = i + 1; // Shift by one for a
						// meaningful
						// generation number
						double divisionTime = divisionTimes.get(i);

						// Prepare for export
						temp.put(generation, divisionTime);
					}

					double[][] data = CellFluorescenceIndividualGenerationsChart2D.convertMapToArray(temp);
					ds.addSeries("Cell ID " + cell.getId(), data);
				} else if (cell.isRoot()) {
					// Just for the root cell, sets with null division time and null generation
					// ds.addValue( (Number) new Double(0.0), "Cell ID " + cell.getId(), 0);
				}

			}

			// TODO Row changed (minor importance, will it ever happen?)
		}

		this.updateLegend();
	}

}
