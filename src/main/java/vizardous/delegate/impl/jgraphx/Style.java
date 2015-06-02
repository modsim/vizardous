package vizardous.delegate.impl.jgraphx;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Style {

	/** Indicates whether to use the default style or not */
	public boolean bUseDefaultStyle = true;
	/** Map for key value pairs of the style */
	public Map<String, String> style = new HashMap<String, String>();
	/** predefined jgraphX style name */
	public String jgraphXStyle = "";
	
	/**
	 * Create new style by parsing jgraphX style data from String.
	 * @param strStyle to parse
	 */
	public Style(String strStyle) {
		if(strStyle == null || strStyle.equals(""))
			return;
		
		// check whether the default style shall be used
		if(strStyle.startsWith(";")) {
			bUseDefaultStyle = false;
			strStyle = strStyle.replaceFirst(";", "");
		}
		else {
			bUseDefaultStyle = true;
		}
		
		if(strStyle.equals(""))
			return;
		
		// get the style data
		String[] data = strStyle.split(";");
		
		// loop over data pairs
		for(String cur : data) {
			String[] specificData = cur.split("=");
			if(specificData.length == 1 && jgraphXStyle == "") {
				jgraphXStyle = specificData[0];
			}
			else if(specificData.length != 2) {
				throw new IllegalArgumentException("Failed parsing style pair: " + cur);
			}
			
			// store value pair
			style.put(specificData[0], specificData[1]);
		}
	}
	
	/**
	 * Stores the a color in a format readable for the jgraphX library
	 * @param key for the color
	 * @param color to store
	 */
	public void put(String key, Color color) {
		// convert the color to a hexadecimal representation that jgraphx understands
    	String strColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toUpperCase();
    	// and add the thing to the map
    	style.put(key, strColor);
	}
	
	public Style put(String key, String value) {
		style.put(key, value);
		return this;
	}
	
	/**
	 * Generates a jgraphX style string
	 */
	@Override
	public String toString() {
		String strResult = "";
		
		if(!bUseDefaultStyle) {
			strResult = ";";
		}
		
		if(!jgraphXStyle.equals("")) {
			strResult += jgraphXStyle + ';';
		}
		
		for(Map.Entry<String, String> data : style.entrySet()) {
			strResult += data.getKey() + "=" + data.getValue() + ";";
		}
		
		return strResult;
	}
}
