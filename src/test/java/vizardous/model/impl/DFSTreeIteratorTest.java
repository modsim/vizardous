/**
 * 
 */
package vizardous.model.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 0.1
 *
 */
public class DFSTreeIteratorTest {

	static DataModel model;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
            Map<File, File> mapFile = new HashMap<File, File>();
            mapFile.put(new File("/home/helfrich/TreeData/DFG/PHH1001.nd2-PHH1001.nd2(series10)_preprocessed_cropped_rois-1-65_final-1.xml"), 
                new File("/home/helfrich/TreeData/DFG/PHH1001.nd2-PHH1001.nd2(series10)_preprocessed_cropped_rois-1-65_final-1_meta.xml"));
            model = new DataModel(mapFile.entrySet());
            List<Forest> forests = model.getForestsList();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
//		Cell root = model.getPhyloxml().getPhylogenies().get(0).getCladeList().get(0).getCellObject();
//		
	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void test() {
////		Cell root = model.getPhyloxml().getPhylogenies().get(0).getCladeList().get(0).getCellObject();
////		
////		Iterator<Cell> iter = root.iterator();
////		
////		int counter = 0;
////		while (iter.hasNext() && counter < 3) {
////			Cell c = iter.next();
////			System.out.println(c.getId());
////			counter++;
////		}
//	}
//
//	@Test
//	public void test2() {
////		Cell root = model.getPhyloxml().getPhylogenies().get(0).getCladeList().get(0).getCellObject();
////		root.getChildren();
//	}
//	
}
