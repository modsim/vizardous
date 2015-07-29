package vizardous.delegate.dataFilter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vizardous.util.Converter;

/**
 * Filter class that provides filter functionality for data structures with comparable content
 * 
 * @author Johannes Seiffarth <j.seiffarth@fz-juelich.de>
 */
public class ComparableFilter {
	
	/**
	 * Filters a map. All values (not keys!) that equal kick will be removed
	 * @param map to filter
	 * @param kick Value to kick out
	 */
	public static <T extends Comparable<T>, K> void filter(Map<K,T> map, T kick) {
		
		Set<Map.Entry<K, T>> entrySet = map.entrySet();
		
		for(Iterator<Map.Entry<K,T>> it = entrySet.iterator(); it.hasNext();) {
			Map.Entry<K, T> entry = it.next();
			if(entry.getValue().equals(kick))
				it.remove();
		}
	}
	
	/**
	 * Filters a list. All values that equal kick will be removed
	 * @param list to filter
	 * @param kick Value to kick out
	 * @return a reference to list (no new list!)
	 */
	public static <T extends Comparable<T>> List<T> filter(List<T> list, T kick) {
		for(Iterator<T> it = list.iterator(); it.hasNext();) {
			T val = it.next();
			if(val.equals(kick))
				it.remove();
		}
		
		return list;
	}
	
	/**
	 * Filters a double array. All values that equal kick will be removed
	 * @param data array to filter
	 * @param kick Value to kick out
	 * @return a new filtered array
	 */
	public static double[] filter(double[] data, double kick) {
		LinkedList<Double> list = new LinkedList<Double>();
		for(double value : data) {
			if(value != kick)
				list.add(value);
		}
		
		return Converter.listToArray(list);
	}

}
