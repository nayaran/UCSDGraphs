package roadgraph;

import java.util.LinkedList;
import java.util.List;

import geography.GeographicPoint;
import util.CacheManager;
import util.Cacheable;

public class MapRoute implements Cacheable {
	private MapNode source;
	private MapNode destination;
	private List<MapNode> mapRoute;

	private boolean DETAILED_STR_REPRESENTATION_ON = false;

	public MapRoute(MapNode source, MapNode destination) {
		this.source = source;
		this.destination = destination;
		this.mapRoute = new LinkedList<MapNode>();
	}

	public MapRoute(MapNode source, MapNode destination, List<MapNode> mapRoute) {
		this.source = source;
		this.destination = destination;
		this.mapRoute = mapRoute;
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
		int id = 0;
		id += source.hashCode() + destination.hashCode();
		return id;
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
		if (DETAILED_STR_REPRESENTATION_ON) {
			String stringRepresentation = "From - " + getSource().toString() + ", " + "To - "
					+ getDestination().toString() + ", ";
			if (getMapRoute().size() < 3) {
				stringRepresentation += "No route info!";
			} else {
				stringRepresentation += "Route(" + getMapRoute().size() + " nodes) - " + getMapRoute();

			}
			return stringRepresentation;
		} else {
			String stringRepresentation = "" + getSource().toString() + " to " + getDestination().toString() + " : ";
			if (getMapRoute().size() < 3) {
				stringRepresentation += "No route info!";
			} else {
				stringRepresentation += "Route - " + getMapRoute();
			}
			return stringRepresentation;
			// return stringRepresentation.substring(0, stringRepresentation.length() - 2);
		}
	}

	public List<GeographicPoint> getGeoPointsRoute() {
		List<GeographicPoint> geoPointsRoute = new LinkedList<GeographicPoint>();
		for (MapNode node : getMapRoute()) {
			geoPointsRoute.add(node.getLocation());
		}
		return geoPointsRoute;
	}
}
