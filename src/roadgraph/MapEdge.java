package roadgraph;

import geography.GeographicPoint;
/**
 * @author Anurag
 * 
 * A class which represents an edge of the graph
 * of geographic locations
 * 
 */
public class MapEdge {

	private GeographicPoint source;
	private GeographicPoint destination;
	
	private String roadName;
	private String roadType;
	private double length;

	public MapEdge(GeographicPoint source, GeographicPoint destination, String roadName, String roadType,
			double length) {
		super();
		this.source = source;
		this.destination = destination;
		this.roadName = roadName;
		this.roadType = roadType;
		this.length = length;
	}

	public GeographicPoint getSource() {
		return source;
	}
	public void setSource(GeographicPoint source) {
		this.source = source;
	}
	public GeographicPoint getDestination() {
		return destination;
	}
	public void setDestination(GeographicPoint destination) {
		this.destination = destination;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public String getRoadType() {
		return roadType;
	}
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	
	public String toString() { 
	    return this.roadName + ", " + this.roadType + ", " + Math.round(this.length) + "km\n\t\t(" + this.source + ") To (" + this.destination + ")";
	} 

}
