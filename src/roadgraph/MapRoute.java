package roadgraph;


import java.util.LinkedList;
import java.util.List;

import geography.GeographicPoint;
import util.CacheManager;
import util.Cacheable;

public class MapRoute implements Cacheable{
	private MapNode source;
	private MapNode destination;
	
	private List<MapNode> mapRoute;
	
	public MapRoute(MapNode source, MapNode destination) {
		this.source = source;
		this.destination = destination;
		this.mapRoute = new LinkedList<MapNode>();
	}
	public MapNode getSource() {
		return source;
	}

	public void setSource(MapNode source) {
		this.source = source;
	}
	
	public MapNode getDestination() {
		return this.destination;
	}

	public void setDestination(MapNode destination) {
		this.destination = destination;
	}

	public List<MapNode> getMapRoute() {
		return mapRoute;
	}

	public void setMapRoute(List<MapNode> route) {
		this.mapRoute = route;
	}
	
	public int hashCode() {
		return source.hashCode() + destination.hashCode();
	}
	@Override
	public boolean isExpired() {
		return false;
	}
	@Override
	public Object getIdentifier() {
		return this.hashCode();
	}
	@Override
	public Object getEnclosingObject() {
		return this;
	}
	public String toString() {
		String stringRepresentation = "\tFrom - " + getSource().toString() + "\n" +
				"\tTo - " + getDestination().toString() + "\n";
		if (getMapRoute().size() < 0) {
			stringRepresentation += "Route not discovered yet!";
		}
		else {
			stringRepresentation += "Route contains " + getMapRoute().size() + " nodes";
		}
		return stringRepresentation;
	}
	public List<GeographicPoint> getGeoPointsRoute(){
		List<GeographicPoint> geoPointsRoute = new LinkedList<GeographicPoint>();
		for (MapNode node: getMapRoute()) {
			geoPointsRoute.add(node.getLocation());
		}
		return geoPointsRoute;
	}
}
