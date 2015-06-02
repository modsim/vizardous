/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.model.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 * @version 1.0.0
 */
public class Population {

	private String id;	
	private MIFrame frameObject;
	
	private double x;
	private double y;
	private double z;
	
	private double area;
	private String areaUnit;
	
	private double volume;
	private String volumeUnit;
	
	Map<String, Double> fluorescences;
	private String fluorescenceUnit;
	
	/**
	 * TODO Documentation
	 * 
	 * @param builder
	 */
	public Population(Builder builder) {
		this.id = builder.id;
        this.frameObject = builder.frameObject;
    	
    	this.x = builder.x;
        this.y = builder.y;
        this.z = builder.z;
        
        this.area = builder.area;
        this.areaUnit = builder.areaUnit;
        
        this.volume = builder.volume;
        this.volumeUnit = builder.volumeUnit;
        
        this.fluorescences = builder.fluorescences;
        this.fluorescenceUnit = builder.fluorescenceUnit;
	}	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the frameObject
	 */
	public MIFrame getFrameObject() {
		return frameObject;
	}

	/**
	 * @param frameObject the frameObject to set
	 */
	public void setFrameObject(MIFrame frameObject) {
		this.frameObject = frameObject;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * @return the area
	 */
	public double getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(double area) {
		this.area = area;
	}

	/**
	 * @return the areaUnit
	 */
	public String getAreaUnit() {
		return areaUnit;
	}

	/**
	 * @param areaUnit the areaUnit to set
	 */
	public void setAreaUnit(String areaUnit) {
		this.areaUnit = areaUnit;
	}

	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * @return the volumeUnit
	 */
	public String getVolumeUnit() {
		return volumeUnit;
	}

	/**
	 * @param volumeUnit the volumeUnit to set
	 */
	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	/**
	 * @return the fluorescences
	 */
	public Map<String, Double> getFluorescences() {
		return fluorescences;
	}

	/**
	 * @param fluorescences the fluorescences to set
	 */
	public void setFluorescences(Map<String, Double> fluorescences) {
		this.fluorescences = fluorescences;
	}

	/**
	 * @return the fluorescenceUnit
	 */
	public String getFluorescenceUnit() {
		return fluorescenceUnit;
	}

	/**
	 * @param fluorescenceUnit the fluorescenceUnit to set
	 */
	public void setFluorescenceUnit(String fluorescenceUnit) {
		this.fluorescenceUnit = fluorescenceUnit;
	}

	/**
	 * TODO Documentation
	 * 
	 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
	 * @version 0.1
	 */
	public static class Builder {
		
		// Required parameters
		private final String id;		
		private final MIFrame frameObject;
		
		// Optional parameters
		private double x = -1;
		private double y = -1;
		private double z = -1;
		
		private double area = -1;
		private String areaUnit = null;
		
		private double volume = -1;
		private String volumeUnit = null;
		
		Map<String, Double> fluorescences = new HashMap<String, Double>();
		private String fluorescenceUnit = null;
		
		public Builder(String id, MIFrame frameObject) {
			this.id = id;	
			this.frameObject = frameObject;
		}
		
		public Builder x(double x) {
			this.x = x;
			return this;
		}
		
		public Builder y(double y) {
			this.y = y;
			return this;
		}
		
		public Builder z(double z) {
			this.z = z;
			return this;
		}
		
		public Builder area(double area) {
			this.area = area;
			return this;
		}
		
		public Builder areaUnit(String areaUnit) {
			this.areaUnit = areaUnit;
			return this;
		}
		
		public Builder volume(double volume) {
			this.volume = volume;
			return this;
		}
		
		public Builder volumeUnit(String volumeUnit) {
			this.volumeUnit = volumeUnit;
			return this;
		}
		
		public Builder fluorescences(Map<String, Double> fluorescences) {
			this.fluorescences = fluorescences;
			return this;
		}
		
		public Builder fluorescenceUnit(String fluorescenceUnit) {
			this.fluorescenceUnit = fluorescenceUnit;
			return this;
		}
		
		public Population build() {
			return new Population(this);
		}
		
	}

}
