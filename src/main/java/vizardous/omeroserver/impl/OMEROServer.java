/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.omeroserver.impl;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import omero.ServerError;
import omero.api.IMetadataPrx;
import omero.api.RawFileStorePrx;
import omero.api.ServiceFactoryPrx;
import omero.model.Annotation;
import omero.model.FileAnnotation;
import omero.sys.ParametersI;
import pojos.FileAnnotationData;
import vizardous.omeroserver.gui.Login;

/**
 * This class implements the connection and disconnection to OMERO server. 
 * It also implements the downloading of files from the OMERO server.
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0.0
 */

public class OMEROServer {

    private File downloadFolder;
	
	private String hostname;  
    private int port;
    private String userName;
    private String password;
    private omero.client client = null;
    
    /** The {@link Logger} for this class. */
    final Logger logger = LoggerFactory.getLogger(OMEROServer.class);
    
    /**
     * OMEROServer constructor.
     * 
     * @param jl Object of the class Login which use for tacking the login credentials.
     */
    public OMEROServer(Login jl) {
        hostname = jl.getHostname();
        port     = jl.getPort();
        userName = jl.getUsername();
        password = jl.getPassword();
    }
    
    /**
     * Create connection to OMERO server.
     * 
     * @throws ServerError root server exception
     * @throws CannotCreateSessionException
     * @throws PermissionDeniedException
     * @throws FileNotFoundException 
     */
    public File connectOMEROServer() throws ServerError, CannotCreateSessionException, PermissionDeniedException, FileNotFoundException {
        client = new omero.client(hostname, port);
        
        if(client != null) {
            try {
                ServiceFactoryPrx entry = client.createSession(userName, password);
                downloadFolder = readAttachments(entry);
            } catch (Ice.ConnectionRefusedException cre) {
                // Bad adress or password?            
                logger.error("Connection failure: " + cre.getMessage());
            } finally {
            	disconnectOMEROServer();
            }
        } else throw new NullPointerException("client object is null!");
        
        return downloadFolder;
    }
    
    /**
     * Used to disconnect from OMERO server
     * 
     */
    public void disconnectOMEROServer() {
        // Clear temp directory from files
//    	try {
//			FileUtils.deleteDirectory(downloadFolder);
//		} catch (IOException ioException) {
//			logger.error("Could not delete all temporary files", ioException);
//		}
        
    	client.closeSession();
    }
    
    /**
     * Read Attachments from OMERO server for given ServiceFactoryPrx.
     * 
     * @param entry Object of class ServiceFactoryPrx.
     * @throws ServerError root server exception.
     */  
    private File readAttachments(ServiceFactoryPrx entry) throws ServerError {
        File downloadFolder = null;
    	
    	// retrieve all annotations,
        List<Annotation> annotations = loadAllAnnotations(entry);
        try {
            downloadFolder = readAllAttachedFiles(entry, annotations, 262144000);
        }
        catch (FileNotFoundException ex) {
            logger.error(null, ex);
        } catch (IOException ex) {
            logger.error(null, ex);
        }
        
        return downloadFolder;
    }
    

    /**
     * Gets all annotations existing in all projects from OMERO server.
     * 
     * @param entry Object of class ServiceFactoryPrx.
     * @return Returns a list of all all annotations contain in all projects from OMERO server.
     * @throws ServerError root server exception
     */
    private List<Annotation> loadAllAnnotations(ServiceFactoryPrx entry) throws ServerError {
        long userId = entry.getAdminService().getEventContext().userId;
        List<String> nsToInclude = new ArrayList<String>();
        List<String> nsToExclude = new ArrayList<String>();
        ParametersI param = new ParametersI();
        param.exp(omero.rtypes.rlong(userId)); 
        IMetadataPrx proxy = entry.getMetadataService();
        // retrieve all annotations
        List<Annotation> annotations = proxy.loadSpecifiedAnnotations(FileAnnotation.class.getName(), nsToInclude, nsToExclude, param);
        return annotations;
    }

    /**
     * Gets all attached files in all projects from OMERO server.
     * 
     * @param entry Object of class ServiceFactoryPrx.
     * @param annotations Object of class ServiceFactoryPrx.
     * @param INC Integer value represents the length of the byte stream.
     * @throws ServerError root server exception.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private File readAllAttachedFiles(ServiceFactoryPrx entry, List<Annotation> annotations, int INC) throws ServerError, FileNotFoundException, IOException {
        Iterator<Annotation> j = annotations.iterator();
        Annotation annotation;
        FileAnnotationData fa;
        RawFileStorePrx store = null;
        
        String tempDirectory = System.getProperty("java.io.tmpdir");
        String downloadFolderName = "VizardousOmeroDownloads";
        
        downloadFolder = new File(tempDirectory + File.separator + downloadFolderName);
        
        if (!downloadFolder.exists()) {
        	downloadFolder.mkdirs(); // Create directory if it doesn't exist
        } else {
        	// TODO Buggy
        	downloadFolder.deleteOnExit(); // Delete folder when VM terminates
        }
        
        while (j.hasNext()) {
            annotation = j.next();
            if ( annotation instanceof FileAnnotation ) {
                fa = new FileAnnotationData((FileAnnotation) annotation);
                if (!fa.getFileFormat().equals(FileAnnotationData.XML)) {
                	continue;
                }
                
                // Set location for files
                File file = new File(downloadFolder, fa.getFileName());
                
                // Create file if it does not exist                
                if (!file.createNewFile()) {
                	continue; // Don't open streams if it already exists
                } else {
                	file.deleteOnExit(); // Delete the files when VM terminates
                }                
                
                FileOutputStream stream = new FileOutputStream(file);
                store   = entry.createRawFileStore();
                store.setFileId(fa.getFileID());
                int offset = 0;
                long size = fa.getFileSize();
                try {
                    for (offset = 0; (offset+INC) < size;) {
                        stream.write(store.read(offset, INC));
                        offset += INC;
                    }
                } finally {
                    stream.write(store.read(offset, (int) (size-offset)));
                    stream.close();
                }
            }
        }
        store.close();
        
        return downloadFolder;
    }
    
}
