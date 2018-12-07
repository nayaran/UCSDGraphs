package roadgraph;


import java.util.LinkedList;
import java.util.List;

import util.CacheManager;
import util.Cacheable;

public class MapRoute implements Cacheable{
	private MapNode source;
	private MapNode destination;
	
	private List<MapNode> route;
	
	public MapRoute(MapNode source, MapNode destination) {
		this.source = source;
		this.destination = destination;
		this.route = new LinkedList<MapNode>();
	}
	public MapNode getSource() {
		return source;
	}

	public void setSource(MapNode source) {
		this.source = source;
	}

	
	public MapNode getDestination() {
		return destination;
	}

	public void setDestination(MapNode destination) {
		this.destination = destination;
	}

	public List<MapNode> getRoute() {
		return route;
	}

	public void setRoute(List<MapNode> route) {
		this.route = route;
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
		if (getRoute().size() < 0) {
			stringRepresentation += "\tRoute not discovered yet!";
		}
		else {
			stringRepresentation += "\tRoute contains " + getRoute().size() + " nodes";
		}
		return stringRepresentation;
	}
}
