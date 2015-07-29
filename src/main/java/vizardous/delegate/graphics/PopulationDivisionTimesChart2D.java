package vizardous.delegate.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import vizardous.model.Cell;
import vizardous.model.Clade;
import vizardous.model.Forest;
import vizardous.model.MIFrame;
import vizardous.model.Phylogeny;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * TODO Documentation
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class PopulationDivisionTimesChart2D extends AbstractChart2D {

	private static final long serialVersionUID = 1L;

	/** The cells list. */
	private List<Cell> cells = null;

	private BoxAndWhiskerCategoryDataset dataset2;

	/**
	 * Constructs a {@link PopulationDivisionTimesChart2D} from a {@link Forest}
	 * .
	 * 
	 * @param forest
	 *            The underlying data model for a chart.
	 */
	public PopulationDivisionTimesChart2D(Forest forest) {
		super(forest);
		this.cells = new LinkedList<Cell>();
		this.colorPalette = new LinkedList<Color>();
		this.tabName = "Population Division Times";
		if (forest != null) {
			this.setLayout(new BorderLayout());
			// TODO Correct!
			this.dataset2 = createPopulationDivisionTimeDataset(forest);
			this.chart = createPopulationDivisionTimeChart(this.dataset2);
			final ChartPanel chartPanel = new ChartPanel(this.chart);
			chartPanel.setPreferredSize(new Dimension(800, 250));
			// chartPanel.setMouseZoomable(true, false);
			chartPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(214, 214, 214)));
			this.legendPanel = createLegendPanel(this.chart);

			this.add(BorderLayout.CENTER, chartPanel);
			this.add(BorderLayout.EAST, this.legendPanel);
			this.setBackground(Color.white);

			// Add this chart as ActionLister for the click in the PopupMenu
			JPopupMenu popupMenu = chartPanel.getPopupMenu();
//			JMenuItem dumpDataItem = new JMenuItem("Dump data to file");
//			popupMenu.add(dumpDataItem);
			dumpDataItem.addActionListener(this);
			popupMenu.add(this.saveSVGItem);
			this.saveSVGItem.addActionListener(this);
		} else {
			throw new IllegalArgumentException("Error: forest object is null.");
		}
	}

	/**
	 * TODO Documentation
	 * 
	 * @param forest
	 * @return
	 */
	private BoxAndWhiskerCategoryDataset createPopulationDivisionTimeDataset(Forest forest) {
		DefaultBoxAndWhiskerCategoryDataset ds = new DefaultBoxAndWhiskerCategoryDataset();
		
		int populationIndex = 1;

		List<Phylogeny> phyloList = forest.getPhyloxml().getPhylogenies();
		for (Phylogeny phylo : phyloList) {
			List<Clade> cladesList = phylo.getLeaves();
			
			// Map for storing the division times per generation
			Map<Integer, List<Double>> data = new TreeMap<Integer, List<Double>>();
			
			/*
			 * Create dataset from Cells in cells and their predecessors.
			 */
			for (Clade clade : cladesList) {
				/**
				 * Cells list for storing the cells that their division times
				 * values will be exported.
				 */
				Cell cell = clade.getCellObject();
				
				// Iterate in reverse order				
				// The case that the cell is not a leaf. The division time of
				// cell is equals to the elapsed time of the first ancestors,
				// which has more than one child minus the next ancestors, which
				// has more than one child

				Iterator<Cell> predecessorIterator = cell.reverseIterator();
				Map<Integer, Double> temp = new HashMap<Integer, Double>();

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

					// Reverse list to have them ordered by generation
					Collections.reverse(divisionTimes);
					
					// Create dataset and prepare for export
					for (int i = 0; i < divisionTimes.size(); i++) {
						Integer generation = i + 1; // Shift by one for a meaningful generation number
						double divisionTime = divisionTimes.get(i);

						List<Double> divisionTimesForGeneration = data.get(generation);						
						if (divisionTimesForGeneration == null) {
							divisionTimesForGeneration = new LinkedList<Double>();
							data.put(generation, divisionTimesForGeneration);
						}
						
						divisionTimesForGeneration.add(divisionTime);
					}
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
					Cell rootCell = phylo.getRootClade().getCellObject();
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
					// double celldivisionTime = cell.getMIFrameObject().getElapsedTime();
					// // Add information to the dataset
					// ds.add(divisionTimes, "population-" + populationIndex, "" + celldivisionTime);
					
					// Reverse list to have them ordered by generation
					Collections.reverse(divisionTimes);

					// Create dataset and prepare for export
					for (int i = 0; i < divisionTimes.size(); i++) {
						Integer generation = i + 1; // Shift by one for a meaningful generation number
						double divisionTime = divisionTimes.get(i);

						List<Double> divisionTimesForGeneration = data.get(generation);						
						if (divisionTimesForGeneration == null) {
							divisionTimesForGeneration = new LinkedList<Double>();
							data.put(generation, divisionTimesForGeneration);
						}
						
						divisionTimesForGeneration.add(divisionTime);
					}

				} else if (cell.isRoot()) {
					// Just for the root cell, sets with null division time and
					// null generation
					List<Double> temp2 = new LinkedList<Double>();
					double cellRootdivisionTime = 0.0;
					temp2.add(cellRootdivisionTime);
					// Doesn't add the division time of the root to the dataset
					// --> Alex
					// ds.add( temp2, "" + "population-" + populationIndex , ""
					// + new Double(0.0));
				}
			}
			
			for (Entry<Integer, List<Double>> entry : data.entrySet()) {
				int generation = entry.getKey();
				List<Double> divisionTimes = entry.getValue();
				
				ds.add(divisionTimes, "Population "+populationIndex, generation);
			}
			
			populationIndex++;
		}
		
		return ds;
	}

	/**
	 * TODO Documentation
	 * 
	 * @param dataset
	 * @return
	 */
	private JFreeChart createPopulationDivisionTimeChart(BoxAndWhiskerCategoryDataset dataset) {
		if (!(dataset instanceof BoxAndWhiskerCategoryDataset)) {
			return null;
		}

		final JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
				"Population Division Times", 
				//                "Time t [min]", 
				"Generation [-]", 
				"Division Time t [min]", 
				dataset, 
				true);
		
		styleChart(chart);
		//        chart.setBackgroundPaint(Color.white);
		//        LegendTitle legend = chart.getLegend();
		//        legend.setPosition(RectangleEdge.RIGHT);
		//        legend.setVisible(false);
		//
		//         // customise the range axis...
		//        final CategoryPlot plot = (CategoryPlot) chart.getPlot(); 
		//        CategoryAxis  domainAxis = plot.getDomainAxis();
		//        domainAxis.setCategoryLabelPositionOffset(0);
		//        domainAxis.setLowerMargin(0.0);
		//        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		//        domainAxis.setAxisLineVisible(true);
		//        
		//        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		//        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		//        rangeAxis.setAutoRangeIncludesZero(false);
		//        
		////        CategoryItemRenderer renderer = new BoxAndWhiskerRenderer(); 
		//                
		//        plot.setBackgroundPaint(Color.WHITE);   
		//        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);   
		//        plot.setDomainGridlinesVisible(true);   
		//        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);   

		return chart;
	}

	// @Override
	// public void exportToCSV(File f) {
	// /*
	// * Row = Cell ID XX
	// * Column = Timepoint
	// */
	// try {
	// CSVWriter csvWriter = new CSVWriter(new FileWriter(f), ',',
	// CSVWriter.NO_QUOTE_CHARACTER);
	//
	// List<String[]> forExport = new LinkedList<String[]>();
	// // Generate header
	// List<String> header = new ArrayList<String>();
	// header.add("Time");
	// for (Object rowKey : this.dataset.getRowKeys()) {
	// if (rowKey instanceof String) {
	// header.add((String) rowKey);
	// }
	// }
	// forExport.add((String[]) header.toArray(new String[header.size()]));
	// logger.debug("this.dataset.getColumnKeys()" +
	// this.dataset.getColumnKeys().size());
	// // for (Object columnKeyObj : this.dataset.getColumnKeys()) {
	// // String columnKey = null;
	// // if (columnKeyObj instanceof String && columnKeyObj != null) {
	// // columnKey = (String) columnKeyObj;
	// // System.out.println("columnKey" + columnKey.toString());
	// // } else {
	// // throw new IllegalArgumentException("columnKey is Null");// TODO Handle
	// this case
	// // }
	// //
	// // // Convert timeValues to String[]
	// // List<String> timeValues = new ArrayList<String>();
	// // int i = this.dataset.getColumnIndex(columnKey);
	// // System.out.println("i" + i);// TODO
	// //
	// // // Add information about timepoint
	// // timeValues.add((String) columnKey);
	// //
	// // for (Object rowKeyObj : this.dataset.getRowKeys()) {
	// // String rowKey = null;
	// // if (rowKeyObj instanceof String) {
	// // rowKey = (String) rowKeyObj;
	// // } else {
	// // // TODO Handle this case
	// // }
	// //
	// // int j = this.dataset.getRowIndex(rowKey);
	// // timeValues.add(Double.toString(this.dataset.getValue(j,
	// i).doubleValue()));
	// // }
	// //
	// // String[] valuesArray = (String[]) timeValues.toArray(new
	// String[timeValues.size()]);
	// // forExport.add(valuesArray);
	// // }
	// //---------------Charaf-------
	// for (Object columnKeyObj : this.dataset.getColumnKeys()) {
	// String columnKey = null;
	// if (columnKeyObj instanceof String && columnKeyObj != null) {
	// columnKey = (String) columnKeyObj;
	// logger.debug("columnKey" + columnKey.toString());
	// } else {
	// throw new IllegalArgumentException("columnKey is Null");// TODO Handle
	// this case
	// }
	//
	// // Convert timeValues to String[]
	// List<String> values = new ArrayList<String>();
	// int i = this.dataset.getColumnIndex(columnKey);
	// logger.debug("i" + i);// TODO
	//
	// // Add information about timepoint
	// values.add((String) columnKey);
	//
	// for (Object rowKeyObj : this.dataset.getRowKeys()) {
	// String rowKey = null;
	// if (rowKeyObj instanceof String) {
	// rowKey = (String) rowKeyObj;
	// }
	//
	// int j = this.dataset.getRowIndex(rowKey);
	// values.add(Double.toString(this.dataset.getValue(j, i).doubleValue()));
	// }
	//
	// String[] valuesArray = (String[]) values.toArray(new
	// String[values.size()]);
	// forExport.add(valuesArray);
	// }
	// csvWriter.writeAll(forExport);
	// csvWriter.close();
	// } catch (IOException e) {
	// // TODO Logging
	// e.printStackTrace();
	// }
	// }

	// @Override
	// public void exportToExcel(File f) {
	// List<Cell> selectedCells = cells;
	// int maxGeneration = 1;
	//
	// /*
	// * Assemble data
	// */
	// if( selectedCells != null ) {
	// Set<WritableCell> exportValues = new HashSet<WritableCell>();
	//
	// for(int i=0; i<selectedCells.size(); i++) {
	// Cell c = selectedCells.get(i);//forest.getCellByClade(cl.get(i));
	// exportValues.add(new Label(0, i+1, c.getId())); // i+1 because of the
	// headers
	//
	// // Map<Integer, Double> temp = cellGeneration.get(c.getId());
	//
	// // TODO Actually, before using the entries we would have to sort them
	// // for (Map.Entry<Integer, Double> e : temp.entrySet()) {
	// // exportValues.add(new jxl.write.Number(e.getKey(), i+1, e.getValue()));
	// //
	// // maxGeneration = e.getKey() > maxGeneration ? e.getKey() :
	// maxGeneration;
	// // }
	// }
	//
	// /*
	// * Export data
	// */
	// WorkbookSettings wbSettings = new WorkbookSettings();
	// wbSettings.setLocale(new Locale("en", "EN"));
	// WritableWorkbook workbook;
	// try {
	// workbook = Workbook.createWorkbook(f, wbSettings);
	// workbook.createSheet("Division times", 0);
	// WritableSheet excelSheet = workbook.getSheet(0);
	//
	// // Write headers
	// excelSheet.addCell(new Label(0, 0, "Cell Id"));
	// for (int i=1; i<=maxGeneration; i++) {
	// excelSheet.addCell(new Label(i, 0, "Generation "+Integer.toString(i)));
	// }
	//
	// // Write data
	// for (WritableCell c : exportValues) {
	// excelSheet.addCell(c);
	// }
	//
	// workbook.write();
	// workbook.close();
	// } catch (IOException e1) {
	// // TODO Logging
	// e1.printStackTrace();
	// } catch (WriteException e) {
	// // TODO Logging
	// e.printStackTrace();
	// }
	// }
	// }

	@Override
	public JFreeChart createJFreeChart(XYDataset dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportToCSV(File f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportToExcel(File f) {
		// TODO Auto-generated method stub

	}

}
