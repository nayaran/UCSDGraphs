package roadgraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import geography.GeographicPoint;
/**
 * @author Anurag
 * 
 * A class which represents a node of the graph
 * of geographic locations
 * 
 */
public class MapNode implements Comparable<MapNode>{
	private GeographicPoint location;
	private List<MapEdge> streets;
	private HashMap<MapNode, Double> neighbourDistanceMap;
	private Double distanceFromStart;
	private Double estimatedDistanceToDestination;

	public MapNode(GeographicPoint location) {
		this.location = location;
		this.streets = new LinkedList<MapEdge>();
		this.neighbourDistanceMap = new HashMap<MapNode, Double>();
		this.distanceFromStart = Double.POSITIVE_INFINITY;
		this.estimatedDistanceToDestination = Double.POSITIVE_INFINITY;
	}

	@SuppressWarnings("unchecked")
	public Set<MapNode> getNeighbors() {
		return neighbourDistanceMap.keySet();
	}

	public HashMap<MapNode, Double> getNeighborDistanceMap() {
		return neighbourDistanceMap;
	}

	void addStreet(MapEdge street) {
		streets.add(street);
	}
	void addNeighbour(MapNode neighbour, Double distance) {
		neighbourDistanceMap.put(neighbour, distance);
	}
	
	public GeographicPoint getLocation() {
		return location;
	}
	public void setGeoPoint(GeographicPoint geoPoint) {
		this.location = geoPoint;
	}
	public List<MapEdge> getStreets() {
		return streets;
	}
	
	@Override
	public String toString() {

		String roundedDistanceFromStart = this.getDistanceFromStart() == Double.POSITIVE_INFINITY ? "INF" : String.valueOf(Math.round(this.getDistanceFromStart()*100.0)/100.0);
		String estimatedDistanceToDestination = this.getEstimatedDistanceToDestination() == Double.POSITIVE_INFINITY ? "INF" : String.valueOf(Math.round(this.getEstimatedDistanceToDestination()*100.0)/100.0);
		
		return "[(" + this.getLocation().getX() + ", " + this.location.getY() + "), "
				+ "distStart - " +  roundedDistanceFromStart 
				+ ", estDistEnd - " + estimatedDistanceToDestination + "]";
	}

	public Double getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(Double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}

	public Double getEstimatedDistanceToDestination() {
		return estimatedDistanceToDestination;
	}

	public void setEstimatedDistanceToDestination(Double estimatedDistanceToDestination) {
		this.estimatedDistanceToDestination = estimatedDistanceToDestination;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!MapNode.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final MapNode other = (MapNode) obj;
        if ((this.location == null)) {
            return false;
        }
        if (this.location.getX() != other.location.getX()
				|| this.location.getY() != other.location.getY()) {
        		return false;
        }
        	return true;
    }

	@Override
	public int compareTo(MapNode otherNode) {
		// A node should be considered smaller if it is nearer to the start
		if (this.getDistanceFromStart() > otherNode.getDistanceFromStart())
			return 1;
		else if (this.getDistanceFromStart() < otherNode.getDistanceFromStart())
			return -1;
		else return 0;
	}

}
