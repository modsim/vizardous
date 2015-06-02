package vizardous.util;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Converter class that provides functionality for converting data structures
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class Converter {

	/**
	 * Converts a double list to a double array
	 * @param list to convert
	 * @return an array which contains the same content as the list
	 */
	public static double[] listToArray(List<Double> list) {
		double[] result = new double[list.size()];
		int i = 0;
		for(Double d : list) {
			result[i] = d;
			i++;
		}
			
		return result;
	}
}
