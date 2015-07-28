/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model;

/**
 * Provides XML constants for the phyloXML format and for the metaXML file.
 * 
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0.0
 */

public class Constants {
    
    /** The tag name for the xmlns:xsi attribut in phyloXML file. */
    public final static String JAXP_SCHEMA_LANGUAGE                                 = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    /** The tag name for the xmlns:xsi attribut in phyloXML file. */
    public final static String W3C_XML_SCHEMA                                       = "http://www.w3.org/2001/XMLSchema";
    
    public final static String JAXP_SCHEMA_SOURCE                                   = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
    // Added from Stefan
    // --------------------- beginning ------------------
    public final static String SAX_FEATURES_VALIDATION                              = "http://xml.org/sax/features/validation";
    
    public final static String APACHE_FEATURES_VALIDATION_SCHEMA                    = "http://apache.org/xml/features/validation/schema";
    
    public final static String APACHE_FEATURES_VALIDATION_SCHEMA_FULL               = "http://apache.org/xml/features/validation/schema-full-checking";
    
    public final static String APACHE_PROPERTIES_SCHEMA_EXTERNAL_LOCATION           = "http://apache.org/xml/properties/schema/external-schemaLocation";
    // --------------------- end ------------------
    
    /** The tag name for the xmlns attribut in metaXML file. */
    public final static String XML_NS                                               = "xmlns";
    
    /** The tag name for the xmlns:xsi attribut in metaXML file. */
    public final static String XML_NS_XSI                                           = "xmlns:xsi";
    
    /** The tag name for the xsi:schemaLocation attribut in metaXML file. */
    public final static String XSI_SHEMA_LOCATION                                   = "xsi:schemaLocation";
    
    /** The tag name for the projectName element in metaXML file. */
    public static final String METAINFORMATION_PROJECT_NAME                         = "projectName";
    
    /** The tag name for the experimentDuration element in metaXML file. */
    public static final String METAINFORMATION_EXPERIMENT_DURATION                  = "experimentDuration";
    
    /** The attribute name for the experimentDuration element unit in metaXML file. */
    public static final String METAINFORMATION_EXPERIMENT_DURATION_UNIT_ATTR        = "unit";
    
    /** The tag name for the frame element in metaXML file. */
    public static final String METAINFORMATION_FRAME                                = "frame";
    
    /** The attribute name for the frame element id in metaXML file. */
    public static final String METAINFORMATION_FRAME_ID_ATTR                        = "id";
    
    /** The attribute name for the frame element file in metaXML file. */
    public static final String METAINFORMATION_FRAME_FILE_ATTR                      = "file";
    
    /** The tag name for the elapsedTime element in metaXML file. */
    public static final String METAINFORMATION_FRAME_ELAPSEDTIME                    = "elapsedTime";
    
    /** The attribute name for the elapsedTime element unit in metaXML file. */
    public static final String METAINFORMATION_FRAME_ELAPSEDTIME_UNIT_ATTR          = "unit";
    
    /** The tag name for the cell element in metaXML file. */
    public static final String METAINFORMATION_CELL                                 = "cell";
    
    /** The attribute name for the cell element id in metaXML file. */
    public static final String METAINFORMATION_CELL_ID_ATTR                         = "id";
    
    /** The tag name for the length element in metaXML file. */
    public static final String METAINFORMATION_CELL_LENGTH                          = "length";
    
    /** The attribute name for the length element unit in metaXML file. */
    public static final String METAINFORMATION_CELL_LENGTH_UNIT_ATTR                = "unit";
    
    /** The tag name for the area element in metaXML file. */
    public static final String METAINFORMATION_CELL_AREA                            = "area";
    
   /** The attribute name for the area element unit in metaXML file. */
    public static final String METAINFORMATION_CELL_AREA_UNIT_ATTR                  = "unit";
    
    /** The tag name for the fluorescences element in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCES                   = "fluorescences";
    
    /** The tag name for the fluorescence element in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE                    = "fluorescence";
    
    /** The value of the channel element in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_TYPE_VENUS         = "yfp";
    
    /** The value of the channel element in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_TYPE_CRIMSON       = "crimson";
    
    /** The tag name for the unit attribut (in old version of the metaXML file) in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_UNIT_ATTR          = "unit";
    
    /** The attribute name for the fluorescence element unit in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_CHANNEL_ATTR       = "channel";
    
    /** The tag name for the mean element in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_MEAN               = "mean";
    
    /** The attribute name for the mean element unit in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_MEAN_UNIT_ATTR     = "unit";
    
    /** The tag name for the stddev element in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_STDDEV             = "stddev";
    
    /** The attribute name for the stddev element unit in metaXML file. */
    public static final String METAINFORMATION_CELL_FLUORESCENCE_STDDEV_UNIT_ATTR   = "unit";

    /** The tag name for the population element in metaML files. */
	public static final String METAINFORMATION_POPULATION = "population";

	/** The tag name for centers of mass in metaML files. */
	public static final String METAINFORMATION_CENTER = "center";

	/** The tag name for x coordinates in metaML files. */
	public static final String METAINFORMATION_X = "x";
	
	/** The tag name for y coordinates in metaML files. */
	public static final String METAINFORMATION_Y = "y";
	
	/** The tag name for phylogenies in phyloXML */
	public static final String PHYLOXML_PHYLOGENY = "phylogeny";
    
	/** The tag name for clades in phyloXML */
	public static final String PHYLOXML_CLADE = "clade";   
    
    /** The tag name for the name element in phyloXML file. */
    public static final String PHYLOXML_NAME = "name";
    
    /** The tag name for the branch_length element in phyloXML file. */
    public static final String PHYLOXML_BRANCHLENGTH = "branch_length";
    
    /** The tag name for the branch_length element in phyloXML file. */
    public static final String PHYLOXML_PROJECTNAME = "projectName";
}
