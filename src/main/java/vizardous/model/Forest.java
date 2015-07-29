/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a Forest. 
 * This class parses the given files and stores them in the data model.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0.0
 */
public class Forest {

    private int                   complete          = 0;
    private ArrayList<MIFrame>    miFrameList       = new ArrayList<MIFrame>();
    private DecimalFormat         f                 = new DecimalFormat("#0.00"); 
    private PhyloXML              phyloXML          = null;
    private MetaXML                metaXML           = null;
    private List<Phylogeny>       phylogenies       = null;
    
    private HashMap<String, Cell> nameCellMap = new HashMap<String, Cell>();
    private HashMap<String, Clade> nameCladeMap = new HashMap<String, Clade>();
    
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(Forest.class);
    
    /** TODO */
    XPathFactory xpfac = XPathFactory.instance();
    
    private String projectName;
    
   /**
    * Default Forest constructor.
    * 
    */
    public Forest() {
        phylogenies = new LinkedList<Phylogeny>();
    }
    
    /**
     * Reads the content of the two provided files and creates an instance of
     * the data model.
     * 
     * Use this constructor when working with the command line.
     * 
     * @param phyloXML PhyloXML file.
     * @param metaML MetaML file.
     */
    public Forest(File phyloXMLFile, File metaMLFile) throws PhyloXMLException, MetaXMLException {
        phylogenies = new LinkedList<Phylogeny>();
    	
        try {
			this.phyloXML = readPhyloXML(phyloXMLFile);
		} catch (JDOMException e) {
			logger.error(String.format("%s is either not a PhyloXML file or is invalid", phyloXMLFile.getName()), e);
			throw new PhyloXMLException(String.format("%s is either not a PhyloXML file or is invalid", phyloXMLFile.getName()));
		}
        
    	try {
			this.metaXML  = readMetaXML(metaMLFile);
		} catch (JDOMException e) {
			logger.error(String.format("%s is either not a MetaXML file or is invalid", metaMLFile.getName()), e);
			throw new MetaXMLException(String.format("%s is either not a MetaXML file or is invalid", metaMLFile.getName()));
		}
    }
 
    /**
     * Gets Phyloxml wich contains an arbritary number of Phylogeny. 
     * 
     * @return Returns a phyloXML object.
     */
    public PhyloXML getPhyloxml() {
        return phyloXML;
    }

    /**
     * Gets MetaML wich contains an arbritary number of MetaML-frames. 
     * 
     * @return Returns a metaML object.
     */
    public MetaXML getMetaxml() {
        return metaXML;
    }

    /**
     * Parses the MetaML file and fill the structure of MetaML.
     * 
     * @param f MetaML file.
     * @return Returns an MetaML object.
     * @throws MetaXMLException 
     */
    private MetaXML readMetaXML(File f) throws JDOMException, MetaXMLException {

            String projectNameValue = null;
            double experimentDurationValue = -1.0;
            String experimentDurationUnitTyp = "N/A";;
            int frameIdValue = -1;
            String miFramePathValue = null;
            String cellIdValue = null;
            MIFrame miFrameObject = null;
            Cell cell = null;
            Population population = null;

            try {            	
            	// Set the implementation of SAXParserFactory to the one bundled wit the JRE
            	System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
            	
            	/* A round of non-validating parsing to get the file name of the referenced XSD */
            	SAXBuilder sb = new SAXBuilder(XMLReaders.NONVALIDATING);
            	Document doc = sb.build(f);
            	
            	Element unvalidatedRoot = doc.getRootElement();
            	String namespaceURI = unvalidatedRoot.getNamespace().getURI();
            	String newNamespaceURI = "http://13cflux.net/static/schemas/metaXML/2";
            	
            	/* Extract the filename of the schema against which to validate */
            	Namespace xsiNamespace = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            	String schemaLocationString = unvalidatedRoot.getAttribute("schemaLocation", xsiNamespace).getValue();
            	
            	String[] schemaLocations = schemaLocationString.split(" ");
            	String schemaFileName = schemaLocations[schemaLocations.length-1];
            	
            	/* If the file has the correct namespace, validate the file before parsing*/
            	if (namespaceURI.equals(newNamespaceURI)) {
            		// Read bundled XML schema
                	URL xsdUrl = getClass().getResource("/schemas/"+schemaFileName);
                	XMLReaderJDOMFactory factory = new XMLReaderXSDFactory(xsdUrl);
                	 
                	// This builder validates against the provided XSD
                	sb = new SAXBuilder(factory);
                	doc = sb.build(f);
            	}
            	            	
                    Element metaInformationElement = doc.getRootElement();
                    Namespace metaXMLNamespace = metaInformationElement.getNamespace();
                    
                    /*
                     * Parse <metaInformaion>
                     */
                    XPathFactory xpfac = XPathFactory.instance();
                    Namespace defaultNamespace = Namespace.getNamespace("metaxml", metaXMLNamespace.getURI());
                    XPathExpression<Element> projectNameExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_PROJECT_NAME, Filters.element(), null, defaultNamespace);

                    for (Element projectNameElement : projectNameExpression.evaluate(metaInformationElement)) {
                            projectNameValue = projectNameElement.getText();
                            
                            /* Check if the two files are from the same experiment */
                            if ((this.projectName != null) && !projectNameValue.equals(this.projectName)) {
                            	throw new MetaXMLException("Files are not from the same experiment. Check projectName!");
                            }
                    }

                    XPathExpression<Element> experimentDurationExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_EXPERIMENT_DURATION, Filters.element(), null, defaultNamespace);
                    for (Element experimentDurationElement : experimentDurationExpression.evaluate(metaInformationElement)) {
                            experimentDurationUnitTyp = experimentDurationElement.getAttributeValue(Constants.METAINFORMATION_EXPERIMENT_DURATION_UNIT_ATTR);
                            experimentDurationValue = Double.parseDouble(experimentDurationElement.getText());
                    }

                    /*
                     * Parse <frame>s
                     */
                    XPathExpression<Element> frameExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_FRAME, Filters.element(), null, defaultNamespace);
                    for (Element frameElement : frameExpression.evaluate(metaInformationElement)) {
                    	
	                        double elapsedTimeValue = -1.0;
	                        String elapsedTimeUnitTyp = "N/A";
                            String cellFluorescenceUnitTyp = "N/A";

                    	
                            frameIdValue = Integer.parseInt(frameElement.getAttributeValue(Constants.METAINFORMATION_FRAME_ID_ATTR));
                            miFramePathValue = frameElement.getAttributeValue(Constants.METAINFORMATION_FRAME_FILE_ATTR);

                            XPathExpression<Element> elapsedTimeExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_FRAME_ELAPSEDTIME, Filters.element(), null, defaultNamespace);
                            for (Element elapsedTimeElement : elapsedTimeExpression.evaluate(frameElement)) {
                                    elapsedTimeValue = Double.parseDouble(elapsedTimeElement.getValue());
                                    elapsedTimeUnitTyp = elapsedTimeElement.getAttributeValue(Constants.METAINFORMATION_FRAME_ELAPSEDTIME_UNIT_ATTR);
                            }
                             
                            /*
                             * TODO Parse <backgroundFluorescences>
                             */

                            /*
                             * Parse <population>
                             */
                            Double populationX = -1d;
                            Double populationY = -1d;

                            XPathExpression<Element> populationExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_POPULATION, Filters.element(), null, defaultNamespace);
                            for (Element populationElement : populationExpression.evaluate(frameElement)) {
                                    // TODO Parse population id

                                    // Parse population center
                                    XPathExpression<Element> centerExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CENTER, Filters.element(), null, defaultNamespace);
                                    for (Element centerElement : centerExpression.evaluate(populationElement)) {
                                            populationX = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_X, defaultNamespace).getValue());
                                            populationY = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_Y, defaultNamespace).getValue());
                                    }

                                    // TODO Parse population fluorescences

                                    // TODO Parse population area

                                    // TODO Parse population volume

                                    population = new Population.Builder("", miFrameObject).x(populationX).y(populationY).build();
                            }

                            ArrayList<Cell> cellList = new ArrayList<Cell>();
                            /*
                             * Parse <cell>s
                             */
                            XPathExpression<Element> cellExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CELL, Filters.element(), null, defaultNamespace);
                            for (Element cellElement : cellExpression.evaluate(frameElement)) {
                            	
			                        double cellX = -1d;
			                        double cellY = -1d;
			                        double cellLengthValue = Cell.noLength;
			                        String cellLengthUnitTyp = "N/A";
			                        double cellAreaValue = Cell.noArea;
			                        String cellAreaUnitTyp = "N/A";

                            	
                                    cellIdValue = cellElement.getAttributeValue(Constants.METAINFORMATION_CELL_ID_ATTR);

                                    // Read cell center
                                    XPathExpression<Element> centerExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CENTER, Filters.element(), null, defaultNamespace);
                                    for (Element centerElement : centerExpression.evaluate(cellElement)) {
                                            cellX = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_X, defaultNamespace).getValue());
                                            cellY = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_Y, defaultNamespace).getValue());
                                    }

                                    // Read cell length
                                    XPathExpression<Element> lengthExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CELL_LENGTH, Filters.element(), null, defaultNamespace);
                                    for (Element lengthElement : lengthExpression.evaluate(cellElement)) {
                                            cellLengthValue = Double.parseDouble(lengthElement.getValue());
                                            cellLengthUnitTyp = lengthElement.getAttributeValue(Constants.METAINFORMATION_CELL_LENGTH_UNIT_ATTR);
                                    }

                                    // Read cell area
                                    XPathExpression<Element> areaExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CELL_AREA, Filters.element(), null, defaultNamespace);
                                    for (Element areaElement : areaExpression.evaluate(cellElement)) {
                                            cellAreaValue = Double.parseDouble(areaElement.getValue());
                                            cellAreaUnitTyp = areaElement.getAttributeValue(Constants.METAINFORMATION_CELL_AREA_UNIT_ATTR);
                                    }

                                    Map<String, Double> fluorescences = new HashMap<String, Double>();

                                    // Read fluorescences
                                    XPathExpression<Element> fluorescencesExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CELL_FLUORESCENCES, Filters.element(), null, defaultNamespace);
                                    for (Element fluorescencesElement : fluorescencesExpression.evaluate(cellElement)) {
                                            XPathExpression<Element> fluorescenceExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CELL_FLUORESCENCE, Filters.element(), null, defaultNamespace);
                                            for (Element fluorescenceElement : fluorescenceExpression.evaluate(fluorescencesElement)) {
                                            	
	                                                double cellFluorescenceValue = -1;
	                                                String cellFluorescenceChannel = null;
	                                                
                                                    cellFluorescenceChannel = fluorescenceElement.getAttributeValue(Constants.METAINFORMATION_CELL_FLUORESCENCE_CHANNEL_ATTR);

                                                    // Read mean value
                                                    XPathExpression<Element> meanExpression = xpfac.compile("metaxml:"+Constants.METAINFORMATION_CELL_FLUORESCENCE_MEAN, Filters.element(), null, defaultNamespace);
                                                    for (Element meanElement : meanExpression.evaluate(fluorescenceElement)) {
                                                            cellFluorescenceUnitTyp = meanElement.getAttributeValue(Constants.METAINFORMATION_CELL_FLUORESCENCE_UNIT_ATTR);

                                                            cellFluorescenceValue = Double.parseDouble(meanElement.getValue());
                                                            fluorescences.put(cellFluorescenceChannel, cellFluorescenceValue);
                                                    }

                                                    // Read standard deviation
                                                    // TODO Refactor parsing to take standard deviation into account
//							XPathExpression<Element> stddevExpression = xpfac.compile(Constants.METAINFORMATION_CELL_FLUORESCENCE_STDDEV, Filters.element());
//							for (Element stddevElement : stddevExpression.evaluate(fluorescenceElement)) {
//								cellFluorescenceUnitTyp = stddevElement.getAttributeValue(Constants.METAINFORMATION_CELL_FLUORESCENCE_UNIT_ATTR);
//
//								cellFluorescenceValue = Double.parseDouble(stddevElement.getValue());
//								fluorescences.put(cellFluorescenceChannel, cellFluorescenceValue);
//							}
                                            }

                                    }

                                    Clade c = getCladeByCellId(cellIdValue);
                                    cell = new Cell.Builder(cellIdValue, c, miFrameObject).
                                                    x(cellX).y(cellY).
                                                    length(cellLengthValue).lengthUnit(cellLengthUnitTyp).
                                                    area(cellAreaValue).areaUnit(cellAreaUnitTyp).
                                                    fluorescences(fluorescences).fluorescenceUnit(cellFluorescenceUnitTyp).build();
                                    
                                    this.nameCellMap.put(cellIdValue, cell);
                                    cellList.add(cell);
                            }

                            miFrameObject = new MIFrame(miFramePathValue, frameIdValue, elapsedTimeValue, elapsedTimeUnitTyp, cellList, population);
                            cell.setMIFrameObject(miFrameObject);

                            // TODO What happens if one population is found and it is missing in subsequent frames. In that case population will not be null
                            if (population != null) {
                                    population.setFrameObject(miFrameObject);
                            }

                            miFrameList.add(miFrameObject);
                    }
                    
                    orderMIFrameToCell(miFrameList);
            } catch (JDOMParseException jdomParseException) {
            	logger.error("Not a valid MetaXML file", jdomParseException);
            	
            	// TODO Explicitly cancel the generation of a model
            } catch (JDOMException jdomException) {
                    jdomException.printStackTrace();
            } catch (IOException ioException) {
            	logger.error("Could not read the MetaXML file", ioException);
            }

            return new MetaXML(projectNameValue, experimentDurationValue, experimentDurationUnitTyp, miFrameList);
    }
    
    /**
     * Parses the PhyloXML file and fill the structure of MetaML.
     * 
     * @param f PhyloXML file.
     * @return Returns an PhyloXML object.
     */
    private PhyloXML readPhyloXML(File f) throws JDOMException {
    	try {
            // TODO A round of non-validating parsing to get the file name of the referenced XSD
            
            // Set the implementation of SAXParserFactory to the one bundled wit the JRE
            System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
            
            // Read bundled XML schema            	
        	URL xsdUrl = getClass().getResource("/schemas/phyloxml-1.10.xsd");
        	XMLReaderJDOMFactory factory = new XMLReaderXSDFactory(xsdUrl);
             
            // This builder validates against the provided XSD
            SAXBuilder sb = new SAXBuilder(factory);
            Document doc = sb.build(f);
    		
			Element rootElement = doc.getRootElement();
			Namespace phyloXMLNamespace = rootElement.getNamespace();
			
			/*
             * Parse <phyloxml>
             */
			Namespace defaultNamespace = Namespace.getNamespace("phyloxml", phyloXMLNamespace.getURI());
			XPathExpression<Element> phylogenyExpression = xpfac.compile("phyloxml:"+Constants.PHYLOXML_PHYLOGENY, Filters.element(), null, defaultNamespace);
			
			for (Element phylogenyElement : phylogenyExpression.evaluate(rootElement)) {									
                Phylogeny phylogeny = new Phylogeny(this);
                
                readSubClades(defaultNamespace, phylogeny, phylogenyElement, null);
					
				/*
				 * This is a pretty dirty hack. We just hijack the
				 * phylogenyObject2 field, knowing that readSubClades will
				 * add the Clades to this Phylogeny. After one phylogeny has
				 * been processed we add phylogenyObject2 to the list of
				 * phylogenies and start over again.
				 * 
				 * THIS HACK WILL NOT BE THREADSAFE!!
				 */
				phylogenies.add(phylogeny);
			}
			
			/*
			 * Parse project name
			 */
			Namespace metaXMLNamespace = Namespace.getNamespace("metaxml", "http://metaXML.fz-juelich.de");
			XPathExpression<Element> projectNameExpression = xpfac.compile("metaxml:"+Constants.PHYLOXML_PROJECTNAME, Filters.element(), null, metaXMLNamespace);			
			for (Element projectNameElement : projectNameExpression.evaluate(rootElement)) {
				this.projectName = projectNameElement.getValue();
			}
        } catch (IOException ex) {
        	/* TODO Handle exceptions properly */
        	logger.error("Loading from file failed", ex);
        }
        
        return new PhyloXML(phylogenies);
    }
  
    /**
     * Recursively parses a clade and its children.
     * 
     * @param defaultNamespace The default namespace of the PhyloXML
     * @param phyloObj The {@link Phylogeny} to which the created clades are added
     * @param parentElement The parent in the XML file
     * @param parentClade The parent {@link Clade}
     */
    private void readSubClades(Namespace defaultNamespace, Phylogeny phyloObj, Element parentElement, Clade parentClade) { 
        XPathExpression<Element> cladeExpression = xpfac.compile("phyloxml:"+Constants.PHYLOXML_CLADE, Filters.element(), null, defaultNamespace);
        for (Element cladeElement : cladeExpression.evaluate(parentElement)) {	
            String cladeName = null;        
            Clade childClade = new Clade(phyloObj);
        	
        	/* Name */
            XPathExpression<Element> nameExpression = xpfac.compile("phyloxml:"+Constants.PHYLOXML_NAME, Filters.element(), null, defaultNamespace);
            for (Element nameElement : nameExpression.evaluate(cladeElement)) {
                cladeName = nameElement.getValue();
                childClade.setName(cladeName);
                complete++;
            }
            
            /* Branch length */
            XPathExpression<Element> branchLengthExpression = xpfac.compile("phyloxml:"+Constants.PHYLOXML_BRANCHLENGTH, Filters.element(), null, defaultNamespace);
            for (Element branchLengthElement : branchLengthExpression.evaluate(cladeElement)) {
                childClade.setBranchLength(Double.parseDouble(branchLengthElement.getValue()));;
                complete++;
            }
            
            /* Connect child with parent */
            childClade.setParentClade(parentClade);
            if((parentClade != null) && (complete >= 2)) {
            	parentClade.addSubClade(childClade);         
            	complete = 0;
            }
            
            phyloObj.addClade(childClade);
            
            nameCladeMap.put(childClade.getName(), childClade);
            
            /* Continue with children */
            readSubClades(defaultNamespace, phyloObj, cladeElement, childClade);        
        }
    }    
     
    /**
     * Gets all clades contained in the forest/phyloXML file.
     * 
     * @return Returns a list of all Clades contained in MetaML file.
     */
    public Collection<Clade> getAllCladesInPhyloXML() {
    	return this.nameCladeMap.values();
    }
    
    /**
     * Gets all Cells contained in the forest/metaML file.
     * 
     * @return Returns a list of all Cells in metaXML file.
     */
    public Collection<Cell> getAllCellsInMetaXML() {
    	return nameCellMap.values();
    }
    
    /**
     * Computes a list of the predecessors of a Cell/Clade (as defined by a
     * provided cellId).
     * 
     * @param cellId
     *            The ID of a Cell/Clade for which to compute the predecessors
     * @return A list that contains all predecessors up to the root node. The
     *         first element is the parent of the cell with ID cellId, the last
     *         one is the root.
     */
    public List<Cell> getPredecessors(String cellId) {   
        Cell inputCell = getCellById(cellId);

        return getPredecessors(inputCell);
    }

    /**
     * Computes a list of the predecessors of a provided Cell.
     * 
     * @param cell
     *            The Cell object for which to compute the predecessors
     * @return A list that contains all predecessors up to the root node. The
     *         first element is the parent of the cell with ID cellId, the last
     *         one is the root.
     */
    public List<Cell> getPredecessors(Cell inputCell) {
    	List<Cell> predecessors = new LinkedList<Cell>();
    	Iterator<Cell> iter = inputCell.reverseIterator();
    	
    	while(iter.hasNext()) {
    		predecessors.add(iter.next());
    	}
    	
    	return predecessors;
    }
    
    /**
     * Gets clade by given the name of the associated cell.
     * 
     * @param cellId Cell Id.
     * @return Returns the clade which associated to the given cell or null.
     */
    public Clade getCladeByCellId(String cellId) {
        return this.nameCladeMap.get(cellId);
    }
    
    /**
     * Gets all parents/ancestors of cell by given the Id of cell.
     * 
     * @param cellId Cell Id.
     * @return Returns a list of parents/ancestors cell of the given cell Id, or <code>null</code>
     * @throws <code>IllegalArgumentException</code> for none-Id.
     */
    public ArrayList<Cell> getCellParentsByCellId(String cellId) {
        ArrayList<Cell> cellList = new ArrayList<Cell>();
        ArrayList<Clade> cladesList = new ArrayList<Clade>();
        Clade cld = getCladeByCellId(cellId);
        Phylogeny phyloObjec = cld.getPhylogenyObj();
        if(cld != null) {
            cladesList.add(cld);
            phyloObjec.getCladeParents(cld, cladesList);
            for(int i=0; i<cladesList.size(); i++) {
                Cell cell = cladesList.get(i).getCellObject();
                cellList.add(cell);
            }
        } else
            throw new IllegalArgumentException("Clade is null");
        
        return cellList;
    }
    
    /**
     * Gets all children/descendants of cell by given the Id of cell.
     * 
     * @param cellId Cell Id.
     * @return Returns a list of children/descendants cell of the given cell Id, or <code>null</code>
     * @throws <code>IllegalArgumentException</code> for none-Id.
     */
    public ArrayList<Cell> getCellChildrenByCellId(String cellId) {
        ArrayList<Cell> cellList = new ArrayList<Cell>();
        ArrayList<Clade> cladesList = new ArrayList<Clade>();
        Clade cld = getCladeByCellId(cellId);
        Phylogeny phyloObjec = cld.getPhylogenyObj();
        if(cld != null) {
            cladesList.add(cld);
            phyloObjec.getAllChildrenOfClade(cld, cladesList);
            for(int i=0; i<cladesList.size(); i++) {
                Cell cell = cladesList.get(i).getCellObject();
                cellList.add(cell);
            }
        }else{
            throw new IllegalArgumentException("Clade is null");
        }
        
        return cellList;
    }
    
    /**
     * Computes the minimum cell length in a {@link Forest}.
     * 
     * @return minimum cell length
     */
    public double getMinimalValueLength() {      
        Collection<Cell> cellsInForest = getAllCellsInMetaXML();
        double minimalLength = Double.MAX_VALUE;
        
        for(Cell cell : cellsInForest) {
            double cellLength = cell.getLength();
        	
        	if (cellLength < minimalLength) {
            	minimalLength = cellLength;
            }
        }
        
        return minimalLength;
    }
    
    /**
     * Computes the maximum cell length in a {@link Forest}.
     * 
     * @return maximum cell length
     */
    public double getMaximalValueLength() {       
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();        
        double maximalLength = Double.MIN_VALUE;
        
        for(Cell cell : cellsInForest) {
            double cellLength = cell.getLength();
        	
        	if (cellLength > maximalLength) {
        		maximalLength = cellLength;
            }
        }
        
        return maximalLength;
    }
    
    /**
     * Computes the minimum cell area in a {@link Forest}.
     * 
     * @return minimum cell area
     */
    public double getMinimalValueArea() {
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();
        double minimalArea = Double.MAX_VALUE;
        
        for(Cell cell : cellsInForest) {
            double cellArea = cell.getArea();
        	
        	if (cellArea < minimalArea) {
        		minimalArea = cellArea;
            }
        }
        
        return minimalArea;
    }
    
    /**
     * Computes the maximum cell area in a {@link Forest}.
     * 
     * @return maximum cell area
     */
    public double getMaximalValueArea() {
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();
        double maximalArea = Double.MIN_VALUE;
        
        for(Cell cell : cellsInForest) {
            double cellArea = cell.getArea();
        	
        	if (cellArea > maximalArea) {
        		maximalArea = cellArea;
            }
        }
        
        return maximalArea;
    }
    
    /**
	 * Computes the minimal fluorescence intensity value of all {@link Cell}s in
	 * a {@link Forest}.
	 * 
	 * @param fluorescTyp fluorescence channel for which to compute the minimum
	 * @return minimal fluorescence intensity value
	 */
    public double getMinimalValueFluorescence(String fluorescTyp) {      
        Collection<Cell> cellsInForest = getAllCellsInMetaXML();
        double minimalFluorescence = Double.MAX_VALUE;
        
        for(Cell cell : cellsInForest) {
            double cellFluorescence = cell.getFluorescences().get(fluorescTyp) == null ? Double.MAX_VALUE : cell.getFluorescences().get(fluorescTyp);
        	
        	if (cellFluorescence < minimalFluorescence) {
        		minimalFluorescence = cellFluorescence;
            }
        }
        
        return minimalFluorescence;
    }
    
	/**
	 * Computes the maximum fluorescence intensity value of all {@link Cell}s in
	 * a {@link Forest}.
	 * 
	 * @param fluorescTyp fluorescence channel for which to compute the maximum
	 * @return maximum fluorescence intensity value
	 */
    public double getMaximalValueFluorescence(String fluorescTyp) {
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();
        double maximalFluorescence = Double.MIN_VALUE;
        
        for(Cell cell : cellsInForest) {
            double cellFluorescence = cell.getFluorescences().get(fluorescTyp) == null ? Double.MIN_VALUE : cell.getFluorescences().get(fluorescTyp);
        	
        	if (cellFluorescence > maximalFluorescence) {
        		maximalFluorescence = cellFluorescence;
            }
        }
        
        return maximalFluorescence;
    }
    
    /**
	 * Computes the minimum branch length of all {@link Clade}s in this
	 * {@link Forest}.
	 * 
	 * @return minimum branch length
	 */
    public double getMinimalValueCladeBranchLength() {
    	Collection<Clade> cladesInForest = getAllCladesInPhyloXML();
        double minimalBranchLength = Double.MAX_VALUE;
        
        for(Clade clade : cladesInForest) {
            double branchLength = clade.getBranchLength();
        	
        	if (branchLength < minimalBranchLength) {
        		minimalBranchLength = branchLength;
            }
        }
        
        return minimalBranchLength;
    }
    
	/**
	 * Computes the maximum branch length of all {@link Clade}s in this
	 * {@link Forest}.
	 * 
	 * @return maximum branch length
	 */
    public double getMaximalValueCladeBranchLength() {
    	Collection<Clade> cladesInForest = getAllCladesInPhyloXML();
        double maximalBranchLength = Double.MAX_VALUE;
        
        for(Clade clade : cladesInForest) {
            double branchLength = clade.getBranchLength();
        	
        	if (branchLength > maximalBranchLength) {
        		maximalBranchLength = branchLength;
            }
        }
        
        return maximalBranchLength;
    }
    
    /**
     * Normalizes the inputed value.
     * 
     * @param input to normalize value.
     * @param min minimum value.
     * @param max maximum value.
     * @return Returns normalized value.
     */
    public double normalize(double input, double min, double max) {
        return (input-min)/(max-min);
    }
    
    /**
     * Calculate the fluorescence average value of all children of the selected cell.
     * 
     * @param cellId selected cell.
     * @param fluorescenceTyp type of fluorescence [yfp or crimson].
     * @return Returns fluorescence average value.
     */     
    public double fluorescenceAverageValueOfDescendants(String cellId, String fluorescenceTyp) {
        double avr = 0, erg = 0;
        int len = 0;

        if( cellId != null ) {
            Cell selectedCell = getCellById(cellId);
            List<Cell> cellDescList = selectedCell.getChildren();
            len = cellDescList.size();
            for(int i=0; i<len; i++) { 
                if( cellDescList.get(i).getFluorescences().get(fluorescenceTyp) == null )
                    avr = avr + (-1.0);
                else
                    avr = (avr + cellDescList.get(i).getFluorescences().get(fluorescenceTyp));
            }
        }else 
            throw new IllegalArgumentException("Cell is null");
        
        
        String temp = f.format(avr/len);
        erg = Double.parseDouble(temp.replace(",", "."));
        logger.debug("Cells fluorescences average: " + erg);
        return erg;
    }
    
    /**
     * Calculate the length average value of all children of the selected cell.
     * 
     * @param cellId selected cell.
     * @return Returns length average value. 
     */     
    public double lengthAverageValueOfDescendants(String cellId) {
        double avr = 0, erg = 0;
        int len = 0;

        if( cellId != null ) {
            Cell selectedCell = getCellById(cellId);
            List<Cell> cellDescList = selectedCell.getChildren();
            len = cellDescList.size();
            for(int i=0; i<len; i++) 
                avr = (avr + cellDescList.get(i).getLength());
            
        }else 
            throw new IllegalArgumentException("Cell is null");
        
        String temp = f.format(avr/len);
        erg = Double.parseDouble(temp.replace(",", "."));
        logger.debug("Cells length average: " + erg);
        
        return erg;
    }
    
   /**
     * Calculate the area average value of all children of the selected cell. 
     * 
     * @param cellId selected cell.
     * @return Returns area average value.
     */     
    public double areaAverageValueOfDescendants(String cellId) {
        double avr = 0, erg = 0;
        int len = 0;

        if( cellId != null ) {
            Cell selectedCell = getCellById(cellId);
            List<Cell> cellDescList = selectedCell.getChildren();
            len = cellDescList.size();
            for(int i=0; i<len; i++) 
                avr = (avr + cellDescList.get(i).getArea());
            
        }else 
            throw new IllegalArgumentException("Cell is null");
        
        String temp = f.format(avr/len);
        erg = Double.parseDouble(temp.replace(",", "."));
        logger.debug("Cells area average: " + erg);
        
        return erg;
    }
        
    /**
     * Gets all cells that exist in the same level in a given Phylogeny.
     * 
     * @param level A given level
     * @return Returns a list of the cells that exit in the same level in Phylogeny.
     */
    public List<Cell> cellsInSameLevel(int level, Phylogeny phylo) {
        List<Clade> clades = phylo.getAllCladeInPhylogeny();
        List<Cell> cellsInPhylogeny = getCellsListFromCladesList(clades);
         List<Cell> tempList = new LinkedList<Cell>();
         List<Cell> cellList = new LinkedList<Cell>();
         ArrayList<MIFrame> miList = metaXML.getAllFrames();
         for(int i=0; i<miList.size(); i++) {
             if( miList.get(i).getFrameId() == level ) 
                 tempList = miList.get(i).getAllCellsInFrame();
         }
         for(Cell cell : tempList) {
            for(Cell cellObject : cellsInPhylogeny) {
                if(cell.equals(cellObject) ) 
                    cellList.add(cell);
            }
         }
         
         return cellList;
     }
    
    /**
     * Order cells to metaxml frame.
     * 
     * @param miFrameList A list of metaxml frame.
     */
    private void orderMIFrameToCell(ArrayList<MIFrame> miFrameList) {
        for(int i=0; i<miFrameList.size(); i++) {
            ArrayList<Cell> cl = miFrameList.get(i).getAllCellsInFrame();
            for(int j=0; j<cl.size(); j++) {
                cl.get(j).setMIFrameObject(miFrameList.get(i));
            }
        }
    }

    /**
     * Gets all clades with more as one child/descendant.
     * 
     * @return Returns a list of all clades that has more as one child/descendant
     */
    public List<Clade> getAllCladeWithMoreAsOneChild() {
        List<Clade> rtnList = new ArrayList<Clade>();
        List<Clade> cladesList = phyloXML.getPhylogenyObject().getAllCladeInPhylogeny();
         for(int i=0; i<cladesList.size(); i++) {
             if (cladesList.get(i).getSubCladeList().size() > 1) {
                 rtnList.add(cladesList.get(i));
             }
         }
        return rtnList;
    }
    
    /**
     * Gets all cells associate to the given list of clades.
     * 
     * @param cladeList A list clades.
     * @return Returns a list of cells that associate to the given list of clades.
     */
    public List<Cell> getCellsListFromCladesList(List<Clade> cladeList) {
         List<Cell> cellsList = new ArrayList<Cell>();
         for(int i=0; i<cladeList.size(); i++) {
             cellsList.add(cladeList.get(i).getCellObject());
         }
        return cellsList;
    }
    
    /**
     * Gets cell by given id of the cell.
     * 
     * @param name The name of the searched cell.
     * @return Returns founded cell or null.
     */
    public Cell getCellById(String id){
        return nameCellMap.get(id);
    }
    
    /**
     * Searches the forest for usable area data
     * @return true if the forest contains some area data else false
     */
    public boolean containsAreaData() {
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();
    	for(Cell cell : cellsInForest) {
    		if(cell.getArea() != Cell.noArea)
    			return true;
    	}
    	return false;
    }
    
    /**
     * Searches the forest for usable length data
     * @return true if the forest contains some length data else false
     */
    public boolean containsLengthData() {
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();
    	for(Cell cell : cellsInForest) {
    		if(cell.getLength() != Cell.noLength)
    			return true;
    	}
    	return false;
    }
        
    /**
     * Searches the forest for usable fluorescence data of the specified fluorescence type
     * @param fluorescTyp the type of fluorescence
     * @return true if the forest contains some fluorescence data of that channel else false
     */
    public boolean containsFluorescenceData(String fluorescTyp) {
    	Collection<Cell> cellsInForest = getAllCellsInMetaXML();
    	for(Cell cell : cellsInForest) {
    		Double val = cell.getFluorescences().get(fluorescTyp);
    		if(val != null && val != Cell.noFluorescence)
    			return true;
    	}
    	return false;
    }
}
