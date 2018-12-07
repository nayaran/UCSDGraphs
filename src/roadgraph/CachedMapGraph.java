package roadgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import util.CacheManager;

import geography.GeographicPoint;

public class CachedMapGraph extends MapGraph {

	private CacheManager cacheManager;
	
	public CachedMapGraph() {
		super();
		cacheManager = new CacheManager();
	}
	@Override
	protected List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// TODO Auto-generated method stub
		return null;
	}
	public void printCachedRoutes() {
		System.out.println(cacheManager);
	}

	@Override
	protected List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// Initialize
		MapNode startNode = mapNodes.get(start);
		MapNode endNode = mapNodes.get(goal);
		
		MapRoute route = new MapRoute(startNode, endNode);
		// Verify if we have the route cached
		route = (MapRoute) cacheManager.getFromCache(route.getIdentifier());
		if (route != null) {
			return getGeoPointsFromMapNodePath(route.getRoute());
		}
		// Since it is cache miss, find path using breadth first search
		route = new MapRoute(startNode, endNode);

		List<MapNode> mapNodePath;
		mapNodePath = findPathUsingDijkstra(route, nodeSearched);
		
		if(mapNodePath.isEmpty()) {
			//System.out.println("\nSorry, Goal not found!!");
			return new LinkedList<GeographicPoint>();
		}
		route.setRoute(mapNodePath);
		cacheManager.putInCache(route, 0);
		return getGeoPointsFromMapNodePath(mapNodePath);
	}
	private List<GeographicPoint> getGeoPointsFromMapNodePath(List<MapNode> mapNodePath){
		List<GeographicPoint> geoPointPath = new LinkedList<GeographicPoint>();
		for (MapNode node: mapNodePath) {
			geoPointPath.add(node.getLocation());
		}
		return geoPointPath;
	}
	private List<MapNode> findPathUsingDijkstra(MapRoute route, Consumer<GeographicPoint> nodeSearched) {
		
		// Initialize
		MapNode startNode = route.getSource();
		MapNode endNode = route.getDestination();
		
		HashSet<MapNode> visited = new HashSet<MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>(new DistanceFromStartComparator());
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		int visitedCount = 0;

		//System.out.println("Start - " + startNode);
		//System.out.println("Goal - " + endNode);
		
		boolean found = false;
		
		startNode.setDistanceFromStart(0.0);
		toExplore.add(startNode);
	
		// Perform search using dijkstra
		while(!toExplore.isEmpty()) {
			//System.out.println("\ntoExplore - " + Arrays.toString(toExplore.toArray()));
			//System.out.println("visited - " + Arrays.toString(visited.toArray()));			
			MapNode currentNode = toExplore.remove();
			//System.out.println("Dijkstra visiting - " + currentNode);	
			visitedCount++;

			if (!visited.contains(currentNode)) {
				//System.out.println(currentNode + " has not been visited");
				visited.add(currentNode);
				if(currentNode.equals(endNode)) {
					found = true;
					System.out.println("\nDijkstra");
					System.out.println("\tNodes visited - " + visitedCount);
					break;
				}
				HashMap<MapNode, Double> neighborDistanceMap = currentNode.getNeighborDistanceMap();
				//System.out.println("Neighbors - " + neighborDistanceMap.toString());
				for (MapNode neighbor: neighborDistanceMap.keySet()) {
					//System.out.println("At neighbor - " + neighbor);
					if(!visited.contains(neighbor)) {
						// Calculate distance from start for the neighbor

						Double newDistance = currentNode.getDistanceFromStart() + neighborDistanceMap.get(neighbor);
						Double oldDistance = neighbor.getDistanceFromStart();
						//System.out.println("newDistance - " + newDistance);
						//System.out.println("oldDistance - " + oldDistance);
						if (newDistance < oldDistance) {
							//System.out.println("Updating new distance!");
							neighbor.setDistanceFromStart(newDistance);
							parentMap.put(neighbor, currentNode);
							toExplore.add(neighbor);	
						}
					}
					else {
						//System.out.println("Neighbor already visited!");
					}
				}
			}
		}
		if (!found) {
			return new LinkedList<MapNode>();			
		}

		// Construct & return path
		return constructPathFromParentMap(startNode, endNode, parentMap);
	}
	
	private List<MapNode> constructPathFromParentMap(MapNode startNode, MapNode endNode, Map<MapNode, MapNode> parentMap){
		LinkedList<MapNode> path = new LinkedList<MapNode>();
		MapNode currentNode = endNode;
		path.add(currentNode);
		while(currentNode != startNode) {
			currentNode = parentMap.get(currentNode);
			path.addFirst(currentNode);
		}
		return path;
	}
	
	@Override
	protected List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
