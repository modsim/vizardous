package vizardous.model;
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package vizardous.model.impl;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.JDOMException;
//import org.jdom2.Namespace;
//import org.jdom2.filter.Filters;
//import org.jdom2.input.SAXBuilder;
//import org.jdom2.input.sax.XMLReaders;
//import org.jdom2.xpath.XPathExpression;
//import org.jdom2.xpath.XPathFactory;
//import org.w3c.dom.NodeList;
//
///**
// *
// * @author azzouzi
// */
//public class XMLParser {
//
//    private PhyloXML              phyloXML          = null;
//    private MetaXML               metaXML           = null;
//    
//    /**
//     * Reads the content of the two provided files and creates an instance of
//     * the data model.
//     * 
//     * Use this constructor when working with the command line.
//     * 
//     * @param phyloXML PhyloXML file.
//     * @param metaXML MetaXML file.
//     */
//    public XMLParser(File phyloXMLFile, File metaXMLFile) {
//        phylogenies = new LinkedList<Phylogeny>();
//    	this.phyloXML = readPhyloXML(phyloXMLFile);
//        this.metaXML  = readMetaXML(metaXMLFile);
//    }
//    
//    
//    /**
//     * Parses the MetaXML file and fill the structure of MetaXML.
//     * 
//     * @param f MetaXML file.
//     * @return Returns an MetaXML object.
//     */
//    private MetaXML readMetaXML(File f) {
//
//            String projectNameValue = null;
//            double experimentDurationValue = -1.0;
//            String experimentDurationUnitTyp = null;
//            int frameIdValue = -1;
//            String miFramePathValue = null;
//            double elapsedTimeValue = -1.0;
//            String elapsedTimeUnitTyp = null;
//            double cellLengthValue = -1;
//            String cellLengthUnitTyp = null;
//            double cellAreaValue = -1;
//            String cellAreaUnitTyp = null;
//            double cellFluorescenceValue = -1;
//            String cellFluorescenceUnitTyp = null;
//            String cellFluorescenceChannel = null;
//            String cellIdValue = null;
//            MIFrame miFrameObject = null;
//            Cell cell = null;
//            double cellX = -1d;
//            double cellY = -1d;
//            Population population = null;
//
//            try {
//                    SAXBuilder sax = new SAXBuilder(XMLReaders.NONVALIDATING);
//                    Document doc = sax.build(f);
//                    Element metaInformationElement = doc.getRootElement();
//
//                    /*
//                     * Parse <metaInformaion>
//                     */
//                    XPathFactory xpfac = XPathFactory.instance();
//                    Namespace defaultNamespace = Namespace.getNamespace("metaml", "http://metaXML.fz-juelich.de");
//                    XPathExpression<Element> projectNameExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_PROJECT_NAME, Filters.element(), null, defaultNamespace);
//
//                    for (Element projectNameElement : projectNameExpression.evaluate(metaInformationElement)) {
//                            projectNameValue = projectNameElement.getText();
//                    }
//
//                    XPathExpression<Element> experimentDurationExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_EXPERIMENT_DURATION, Filters.element(), null, defaultNamespace);
//                    for (Element experimentDurationElement : experimentDurationExpression.evaluate(metaInformationElement)) {
//                            experimentDurationUnitTyp = experimentDurationElement.getAttributeValue(Constants.METAINFORMATION_EXPERIMENT_DURATION_UNIT_ATTR);
//                            experimentDurationValue = Double.parseDouble(experimentDurationElement.getText());
//                    }
//
//                    /*
//                     * Parse <frame>s
//                     */
//                    XPathExpression<Element> frameExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_FRAME, Filters.element(), null, defaultNamespace);
//                    for (Element frameElement : frameExpression.evaluate(metaInformationElement)) {
//                            frameIdValue = Integer.parseInt(frameElement.getAttributeValue(Constants.METAINFORMATION_FRAME_ID_ATTR));
//                            miFramePathValue = frameElement.getAttributeValue(Constants.METAINFORMATION_FRAME_FILE_ATTR);
//
//                            XPathExpression<Element> elapsedTimeExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_FRAME_ELAPSEDTIME, Filters.element(), null, defaultNamespace);
//                            for (Element elapsedTimeElement : elapsedTimeExpression.evaluate(frameElement)) {
//                                    elapsedTimeValue = Double.parseDouble(elapsedTimeElement.getValue());
//                            }
//
//                            /*
//                             * TODO Parse <backgroundFluorescences>
//                             */
//
//                            /*
//                             * Parse <population>
//                             */
//                            Double populationX = -1d;
//                            Double populationY = -1d;
//
//                            XPathExpression<Element> populationExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_POPULATION, Filters.element(), null, defaultNamespace);
//                            for (Element populationElement : populationExpression.evaluate(frameElement)) {
//                                    // TODO Parse population id
//
//                                    // Parse population center
//                                    XPathExpression<Element> centerExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CENTER, Filters.element(), null, defaultNamespace);
//                                    for (Element centerElement : centerExpression.evaluate(populationElement)) {
//                                            populationX = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_X, defaultNamespace).getValue());
//                                            populationY = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_Y, defaultNamespace).getValue());
//                                    }
//
//                                    // TODO Parse population fluorescences
//
//                                    // TODO Parse population area
//
//                                    // TODO Parse population volume
//
//                                    population = new Population.Builder("", miFrameObject).x(populationX).y(populationY).build();
//                            }
//
//                            ArrayList<Cell> cellList = new ArrayList<Cell>();
//                            /*
//                             * Parse <cell>s
//                             */
//                            XPathExpression<Element> cellExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CELL, Filters.element(), null, defaultNamespace);
//                            for (Element cellElement : cellExpression.evaluate(frameElement)) {
//                                    cellIdValue = cellElement.getAttributeValue(Constants.METAINFORMATION_CELL_ID_ATTR);
//
//                                    // Read cell center
//                                    XPathExpression<Element> centerExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CENTER, Filters.element(), null, defaultNamespace);
//                                    for (Element centerElement : centerExpression.evaluate(cellElement)) {
//                                            cellX = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_X, defaultNamespace).getValue());
//                                            cellY = Double.parseDouble(centerElement.getChild(Constants.METAINFORMATION_Y, defaultNamespace).getValue());
//                                    }
//
//                                    // Read cell length
//                                    XPathExpression<Element> lengthExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CELL_LENGTH, Filters.element(), null, defaultNamespace);
//                                    for (Element lengthElement : lengthExpression.evaluate(cellElement)) {
//                                            cellLengthValue = Double.parseDouble(lengthElement.getValue());
//                                            cellLengthUnitTyp = lengthElement.getAttributeValue(Constants.METAINFORMATION_CELL_LENGTH_UNIT_ATTR);
//                                    }
//
//                                    // Read cell area
//                                    XPathExpression<Element> areaExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CELL_AREA, Filters.element(), null, defaultNamespace);
//                                    for (Element areaElement : areaExpression.evaluate(cellElement)) {
//                                            cellAreaValue = Double.parseDouble(areaElement.getValue());
//                                            cellAreaUnitTyp = areaElement.getAttributeValue(Constants.METAINFORMATION_CELL_AREA_UNIT_ATTR);
//                                    }
//
//                                    Map<String, Double> fluorescences = new HashMap<String, Double>();
//
//                                    // Read fluorescences
//                                    XPathExpression<Element> fluorescencesExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CELL_FLUORESCENCES, Filters.element(), null, defaultNamespace);
//                                    for (Element fluorescencesElement : fluorescencesExpression.evaluate(cellElement)) {
//                                            XPathExpression<Element> fluorescenceExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CELL_FLUORESCENCE, Filters.element(), null, defaultNamespace);
//                                            for (Element fluorescenceElement : fluorescenceExpression.evaluate(fluorescencesElement)) {
//                                                    cellFluorescenceChannel = fluorescenceElement.getAttributeValue(Constants.METAINFORMATION_CELL_FLUORESCENCE_CHANNEL_ATTR);
//
//                                                    // Read mean value
//                                                    XPathExpression<Element> meanExpression = xpfac.compile("metaml:"+Constants.METAINFORMATION_CELL_FLUORESCENCE_MEAN, Filters.element(), null, defaultNamespace);
//                                                    for (Element meanElement : meanExpression.evaluate(fluorescenceElement)) {
//                                                            cellFluorescenceUnitTyp = meanElement.getAttributeValue(Constants.METAINFORMATION_CELL_FLUORESCENCE_UNIT_ATTR);
//
//                                                            cellFluorescenceValue = Double.parseDouble(meanElement.getValue());
//                                                            fluorescences.put(cellFluorescenceChannel, cellFluorescenceValue);
//                                                    }
//
//                                                    // Read standard deviation
//                                                    // TODO Refactor parsing to take standard deviation into account
////							XPathExpression<Element> stddevExpression = xpfac.compile(Constants.METAINFORMATION_CELL_FLUORESCENCE_STDDEV, Filters.element());
////							for (Element stddevElement : stddevExpression.evaluate(fluorescenceElement)) {
////								cellFluorescenceUnitTyp = stddevElement.getAttributeValue(Constants.METAINFORMATION_CELL_FLUORESCENCE_UNIT_ATTR);
////
////								cellFluorescenceValue = Double.parseDouble(stddevElement.getValue());
////								fluorescences.put(cellFluorescenceChannel, cellFluorescenceValue);
////							}
//                                            }
//
//                                    }
//
//                                    Clade c = getCladeByCellId(cellIdValue);
//                                    cell = new Cell.Builder(cellIdValue, c, miFrameObject).
//                                                    x(cellX).y(cellY).
//                                                    length(cellLengthValue).lengthUnit(cellLengthUnitTyp).
//                                                    area(cellAreaValue).areaUnit(cellAreaUnitTyp).
//                                                    fluorescences(fluorescences).fluorescenceUnit(cellFluorescenceUnitTyp).build();
//
//                                    cellList.add(cell);
//                            }
//
//                            miFrameObject = new MIFrame(miFramePathValue, frameIdValue, elapsedTimeValue, elapsedTimeUnitTyp, cellList, population);
//                            cell.setMIFrameObject(miFrameObject);
//
//                            // TODO What happens if one population is found and it is missing in subsequent frames. In that case population will not be null
//                            if (population != null) {
//                                    population.setFrameObject(miFrameObject);
//                            }
//
//                            miFrameList.add(miFrameObject);
//                    }
//            } catch (JDOMException jdomException) {
//                    jdomException.printStackTrace();
//            } catch (IOException ioException) {
//                    ioException.printStackTrace();
//            } finally {
//                    orderMIFrameToCell(miFrameList);
//            }
//
//            return new MetaXML(projectNameValue, experimentDurationValue, miFrameList);
//    }
//    
//    /**
//     * Parses the PhyloXML file and fill the structure of MetaXML.
//     * 
//     * @param f PhyloXML file.
//     * @return Returns an PhyloXML object.
//     */
//    private PhyloXML readPhyloXML(File f) {
//    	try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			factory.setNamespaceAware(true);
//			org.w3c.dom.Document doc = factory.newDocumentBuilder().parse(f);
//			org.w3c.dom.Element rootElement = doc.getDocumentElement();
//			String xmlNS = rootElement.getAttribute(Constants.XML_NS);
//			String xmlnsXsi = rootElement.getAttribute(Constants.XML_NS_XSI);
//			String xsiSchemaLocation = rootElement.getAttribute(Constants.XSI_SHEMA_LOCATION);
//			
//			NodeList children = rootElement.getChildNodes();
//			for (int i = 0; i < children.getLength(); i++) {
//				org.w3c.dom.Node child = children.item(i);
//				
//				if (child.getNodeName().toLowerCase().equals(Constants.METAINFORMATION_PHYLOGENY)) {									
//                                    Phylogeny phylogenyObject2 = new Phylogeny(this);
//                                    NodeList phylogenyChildren = child.getChildNodes();
//					
//					for (int j = 0; j < phylogenyChildren.getLength(); j++) {
//						org.w3c.dom.Node phylogenyChild = phylogenyChildren.item(j);
//						
//						if (phylogenyChild.getNodeName().toLowerCase().equals(Constants.CLADE_TAG_IDENTIFIER)) {
//							// This way the first clade will not have a parent
//							readSubClades(phylogenyObject2 ,phylogenyChild, null);
//						}
//					}
//					
//					/*
//					 * This is a pretty dirty hack. We just hijack the
//					 * phylogenyObject2 field, knowing that readSubClades will
//					 * add the Clades to this Phylogeny. After one phylogeny has
//					 * been processed we add phylogenyObject2 to the list of
//					 * phylogenies and start over again.
//					 * 
//					 * THIS HACK WILL NOT BE THREADSAFE!!
//					 */
//					phylogenies.add(phylogenyObject2);
//					
////					phylogenyObject2 = new Phylogeny(this);
//				}
//			}
//        } catch (Exception ex) { }
//        
//        return new PhyloXML(phylogenies);
//    }
//  
//    /**
//     * Reads the sub-clades of a given clade in Phylogeny.
//     * 
//     * @param phyloObj Phylogeny.
//     * @param node Node.
//     * @param parentClade Parent clade.
//     */
//    private void readSubClades(Phylogeny phyloObj, org.w3c.dom.Node node, Clade parentClade) {
//        String cladeName = null;
//        Clade childClade = null;
//        
//        switch (node.getNodeType()) {
//        case org.w3c.dom.Node.COMMENT_NODE: 
//                                break;
//        case org.w3c.dom.Node.ELEMENT_NODE: 
//                                if(node.getNodeName().toLowerCase().equals(Constants.CLADE_TAG_IDENTIFIER))
//                                {
//                                    childClade = new Clade(phyloObj);
//                                    NodeList tmpList= node.getChildNodes();
//                                    for (int j = 0,  Len= tmpList.getLength(); j < Len; j++) 
//                                    {
//                                        org.w3c.dom.Node myNode = tmpList.item(j);
//                                        if(myNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
//                                        {
//                                            /* Clade -> Name: */
//                                            if(myNode.getNodeName().toLowerCase().equals(Constants.CLADE_NAME_IDENTIFIER))
//                                            {
//                                                cladeName = myNode.getChildNodes().item(0).getNodeValue();
//                                                childClade.setName(cladeName);
//                                                complete++;
//                                            }
//                                            /* Clade -> branch_length: */
//                                            if(myNode.getNodeName().toLowerCase().equals(Constants.CLADE_BRANCH_IDENTIFIER))
//                                            {
//                                                childClade.setBranchLength(Double.parseDouble(myNode.getChildNodes().item(0).getNodeValue()));
//                                                complete++;
//                                            }
//                                            
//                                            // Connect child with parent
//                                            childClade.setParentClade(parentClade);
//                                            if((parentClade != null) && (complete >= 2)) {
//                                            	parentClade.addSubClade(childClade);         
//                                            	complete = 0;
//                                            }
//                                        }
//                                    }
//                                    phyloObj.addClade(childClade);
//                                }
//                                break;
//        case org.w3c.dom.Node.TEXT_NODE: 
//                            String textContent = node.getTextContent().trim();
//                            break;
//        }
//            
//        if (node.hasChildNodes()) {
//            NodeList list = node.getChildNodes();
//            for (int i = 0, len = list.getLength(); i < len; i++) {
//                readSubClades(phyloObj, list.item(i), childClade);
//            }            
//        }
//    }    
//     
//    
//}
