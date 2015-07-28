package vizardous.computations;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vizardous.delegate.MainView;
import vizardous.model.DataModel;
import vizardous.model.Forest;
import vizardous.model.MIFrame;
import vizardous.model.MetaXMLException;
import vizardous.model.PhyloXMLException;

/**
 * TODO
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * 
 * @version 0.1
 */
public class GrowthRateCalculator {

    /**
     * Private fields
     */
    private DataModel model;
    private double growthRate = -1d;
    private double factor = -1d;
	
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(GrowthRateCalculator.class);
    
        /**
	 * @return the factor
	 */
	public double getFactor() {
		return factor;
	}

		// Added from Charaf
        private Forest forest;

	/**
	 * TODO
	 * 
	 * @param model
	 */
	public GrowthRateCalculator(DataModel model) {
		this.model = model;
	}

        /**
	 * TODO
	 * 
	 * @param forest
	 */
	public GrowthRateCalculator(Forest forest) {
		this.forest = forest;
	}
        
	/**
	 * TODO
	 * 
	 * @param phyloXml
	 * @param metaXml
	 */
	public GrowthRateCalculator(File phyloXml, File metaXml) {
                try {
					this.forest = new Forest(phyloXml, metaXml);
				} catch (PhyloXMLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MetaXMLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public double process() {
		Collection<WeightedObservedPoint> points = new LinkedList<WeightedObservedPoint>();

		// TODO We might need to change the behavior due to the fact that cell that are not in a track might still be in the metaXml file
		for (MIFrame frame : forest.getMetaxml().getAllFrames()) {
			// For the growth rate to be correct we need elapsed time in hours
			double elapsedTime = frame.getElapsedTime() / 60;
			double cellNumber = (double) frame.getAllCellsInFrame().size();

			// The points are uniformly weighted
			points.add(new WeightedObservedPoint(1d, elapsedTime, Math.log(cellNumber)));
		}

		final AbstractCurveFitter fitter = PolynomialCurveFitter.create(1);
		double[] coeffs = fitter.fit(points);

		growthRate = coeffs[1];

		return growthRate;
	}

        /**
	 * TODO
	 * 
	 * @return
	 */
	public double processNew() {
		Collection<WeightedObservedPoint> points = new LinkedList<WeightedObservedPoint>();

		// TODO We might need to change the behavior due to the fact that cell that are not in a track might still be in the metaXml file
		for (MIFrame frame : forest.getMetaxml().getAllFrames()) {
			// For the growth rate to be correct we need elapsed time in hours
			double elapsedTime = frame.getElapsedTime() / 60;
			double cellNumber = (double) frame.getAllCellsInFrame().size();

			// The points are uniformly weighted
			points.add(new WeightedObservedPoint(elapsedTime/forest.getMetaxml().getExperimentDuration(), elapsedTime, Math.log(cellNumber)));
		}

		final AbstractCurveFitter fitter = PolynomialCurveFitter.create(1);
		double[] coeffs = fitter.fit(points);
		
		factor = coeffs[0];
		growthRate = coeffs[1];

		return growthRate;
	}
        
	public static void main(String[] args) {
//		GrowthRateCalculator growthRateCalculator = new GrowthRateCalculator(
//                        new File("/home/helfrich/TreeData/DFG/PHH1001.nd2-PHH1001.nd2(series10)_preprocessed_cropped_rois-1-65_final-1.xml"), 
//                        new File("/home/helfrich/TreeData/DFG/PHH1001.nd2-PHH1001.nd2(series10)_preprocessed_cropped_rois-1-65_final-1_meta.xml"));
//		double growthRate = growthRateCalculator.process();

//		System.out.println("growthRate: " + String.format("%.5f", growthRate));
                
                
                GrowthRateCalculator growthRateCalculatorNew = new GrowthRateCalculator(
                        new File("C:\\Users\\Azzouzi\\Desktop\\xmlTestFiles\\testDataMultiplePhylogenies\\PHH1001.nd2-PHH1001.nd2(series11)_preprocessed_cropped_rois-1_48_final-1.xml"), 
                        new File("C:\\Users\\Azzouzi\\Desktop\\xmlTestFiles\\testDataMultiplePhylogenies\\PHH1001.nd2-PHH1001.nd2(series11)_preprocessed_cropped_rois-1_48_final-1_meta - Kopie.xml"));
		double growthRateNew = growthRateCalculatorNew.processNew();

		LoggerFactory.getLogger(GrowthRateCalculator.class).debug("growthRateNew: " + String.format("%.5f", growthRateNew));
                
//                GrowthRateCalculator growthRateCalculatorNew2 = new GrowthRateCalculator(
//                        new File("C:\\Users\\Azzouzi\\Desktop\\xmlTestFiles\\CP_v6_test_new - Kopie - Kopie.xml"), 
//                        new File("C:\\Users\\Azzouzi\\Desktop\\xmlTestFiles\\metaInfo_idee_new_comp.xml"));
//		double growthRateNew2 = growthRateCalculatorNew2.processNew();
//
//		System.out.println("growthRateNew2: " + String.format("%.5f", growthRateNew2));
	}
}
