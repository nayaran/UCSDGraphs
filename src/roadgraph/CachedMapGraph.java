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

	private CacheManager cache;

	public CachedMapGraph() {
		super();
		cache = new CacheManager();
	}

	@Override
	protected List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// TODO Auto-generated method stub
		return null;
	}

	public void printCachedRoutes() {
		System.out.println(cache);
	}

	@Override
	protected List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// Initialize
		MapNode startNode = mapNodes.get(start);
		MapNode endNode = mapNodes.get(goal);
		List<MapNode> route;

		MapRoute mapRoute = new MapRoute(startNode, endNode);

		System.out.println("\nSearching using Dijkstra for " + mapRoute);

		// Find the route
		route = findPathUsingDijkstra(mapRoute, nodeSearched);

		if (route.isEmpty()) {
			return new LinkedList<GeographicPoint>();
		}
		mapRoute.setMapRoute(route);

		// Reset the MapNode data updated during the search
		resetGraph();

		return mapRoute.getGeoPointsRoute();
	}

	private List<MapNode> findPathUsingDijkstra(MapRoute mapRoute, Consumer<GeographicPoint> nodeSearched) {

		// Initialize
		MapNode startNode = mapRoute.getSource();
		MapNode endNode = mapRoute.getDestination();
		List<MapNode> route = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>(new DistanceFromStartComparator());
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		int visitedCount = 0;

		// System.out.println("Start - " + startNode);
		// System.out.println("Goal - " + endNode);
		boolean cacheHit = false;
		boolean found = false;

		startNode.setDistanceFromStart(0.0);
		toExplore.add(startNode);

		// Core Dijkstra algorithm
		while (!toExplore.isEmpty()) {

			// System.out.println("\ntoExplore - " + Arrays.toString(toExplore.toArray()));
			// System.out.println("visited - " + Arrays.toString(visited.toArray()));
			MapNode currentNode = toExplore.remove();
			// System.out.println("Dijkstra visiting - " + currentNode);
			visitedCount++;

			// Check if path from currentNode to Destination is available in the cache

			MapRoute mapRouteFromCache = (MapRoute) cache
					.get(new MapRoute(currentNode, endNode).getIdentifier());
			if (mapRouteFromCache != null) {
				// Since we found the route from currentNode to endNode in cache
				// We need to update parentMap accordingly
				parentMap = mergeParentMapWithCachedRoute(mapRouteFromCache, parentMap, currentNode);
				found = true;
				cacheHit = true;
				break;
			}

			// Continue rest of the algorithm since it was a cache miss

			if (!visited.contains(currentNode)) {
				// System.out.println(currentNode + " has not been visited");
				visited.add(currentNode);
				if (currentNode.equals(endNode)) {
					found = true;
					break;
				}

				HashMap<MapNode, Double> neighborDistanceMap = currentNode.getNeighborDistanceMap();
				// System.out.println("Neighbors - " + neighborDistanceMap.toString());
				for (MapNode neighbor : neighborDistanceMap.keySet()) {
					// System.out.println("At neighbor - " + neighbor);
					if (!visited.contains(neighbor)) {
						// Calculate distance from start for the neighbor

						Double newDistance = currentNode.getDistanceFromStart() + neighborDistanceMap.get(neighbor);
						Double oldDistance = neighbor.getDistanceFromStart();
						// System.out.println("newDistance - " + newDistance);
						// System.out.println("oldDistance - " + oldDistance);
						if (newDistance < oldDistance) {
							// System.out.println("Updating new distance!");
							neighbor.setDistanceFromStart(newDistance);
							parentMap.put(neighbor, currentNode);
							toExplore.add(neighbor);
						}
					} else {
						// System.out.println("Neighbor already visited!");
					}
				}
			}
		}

		if (found) {
			System.out.println("\nRESULT - Search using dijkstra success!");
			System.out.println("Cached hit - " + cacheHit);
			System.out.println("Nodes visited - " + visitedCount);

			// Construct the path from parentMap
			route = constructRouteFromParentMap(startNode, endNode, parentMap);

			// Update the route in cache
			updateCacheWithDiscoveredRoute(route);
		} else {
			System.out.println("\nRESULT - Sorry, no route not found!");
			System.out.println("Nodes visited - " + visitedCount);
		}
		return route;
	}

	private void updateCacheWithDiscoveredRoute(List<MapNode> route) {
		// Construct all possible routes from discovered path & update in the cache
		int sizeOfCacheBefore = cache.getSize();
		List<MapRoute> mapRoutes = constructMapRoutesFromRoute(route);
		for (MapRoute mapRoute : mapRoutes) {
			// if (cacheManager.getFromCache(mapRoute.getIdentifier()) != null) {
			cache.put(mapRoute, 0);
			// }
		}
		int updatesToCache = cache.getSize() - sizeOfCacheBefore;
		if (updatesToCache > 0 ) {
			System.out.println("Updated cache with - " + updatesToCache + " routes!");			
		}
	}

	public List<MapRoute> constructMapRoutesFromRoute(List<MapNode> route) {
		// Get all possible sub routes from given route
		List<LinkedList<MapNode>> subRoutes = getSubRoutesFromRoute(route);

		// Create a list of MapRoutes out of those routes
		List<MapRoute> mapRoutes = new LinkedList<MapRoute>();
		for (List<MapNode> subRoute : subRoutes) {
			// Construct a MapRoute object from subRoute
			// Also ignore the routes where which are immediate neighbors) (subRoute.size >
			// 2)
			if (!subRoute.isEmpty() && subRoute.size() > 2) {
				MapNode startNode = subRoute.get(0);
				MapNode endNode = subRoute.get(subRoute.size() - 1);
				MapRoute mapRoute = new MapRoute(startNode, endNode, subRoute);
				mapRoutes.add(mapRoute);
			}
		}
		// System.out.println("Original Route - " + route);
		// System.out.println("Constructed " + mapRoutes.size() + " mapRoutes");
		/*
		 * for (MapRoute mapRoute : mapRoutes) { System.out.println(mapRoute); }
		 */
		return mapRoutes;
	}

	private List<LinkedList<MapNode>> getSubRoutesFromRoute(List<MapNode> route) {
		List<LinkedList<MapNode>> subRoutes;
		subRoutes = subsequences((LinkedList<MapNode>) route);

		/*
		 * System.out.println("SubRoutes - "); for(LinkedList<MapNode> subRoute:
		 * subRoutes) System.out.println(subRoute + " , size - " + subRoute.size());
		 */
		return subRoutes;
	}

	public List<LinkedList<MapNode>> subsequences(LinkedList<MapNode> word) {
		if (word.isEmpty()) {
			LinkedList<LinkedList<MapNode>> emptyList = new LinkedList<LinkedList<MapNode>>();
			emptyList.add(new LinkedList<MapNode>());
			return emptyList;// base case
		} else {
			MapNode firstLetter = word.get(0);
			List<MapNode> restOfWord = new LinkedList<MapNode>(word.subList(1, word.size()));

			List<LinkedList<MapNode>> subsequencesOfRest = subsequences((LinkedList<MapNode>) restOfWord);

			List<LinkedList<MapNode>> result = new LinkedList<LinkedList<MapNode>>();
			;
			for (LinkedList<MapNode> subsequence : subsequencesOfRest) {
				// add to result the remaining combinations that don't include firstLetter
				result.add(subsequence);
				// add to result the remaining combinations including firstLetter
				LinkedList<MapNode> subsequenceContainingFirstLetter = (LinkedList<MapNode>) subsequence.clone();
				subsequenceContainingFirstLetter.addFirst(firstLetter);
				result.add(subsequenceContainingFirstLetter);
			}
			return result;
		}
	}

	public HashMap<MapNode, MapNode> mergeParentMapWithCachedRoute(MapRoute mapRoute,
			HashMap<MapNode, MapNode> parentMap, MapNode currentNode) {
		// For a mapRoute - ABCD, parentMap is a linked list in reverse direction - DCBA
		// We need to merge the existing parentMap and the parentMap constructed from
		// mapRoute
		// System.out.println("Merging parentMap with CachedRoute");
		HashMap<MapNode, MapNode> mergedParentMap = (HashMap<MapNode, MapNode>) parentMap.clone();
		HashMap<MapNode, MapNode> parentMapFromRoute = (HashMap<MapNode, MapNode>) constructParentMapFromRoute(
				mapRoute.getMapRoute());

		for (MapNode node : parentMapFromRoute.keySet()) {
			mergedParentMap.put(node, parentMapFromRoute.get(node));
		}
		// System.out.println("Parent map - " + parentMap);
		// System.out.println("Map Route - " + mapRoute);
		
		// System.out.println("Merged parent map - " + mergedParentMap);
		return mergedParentMap;
	}

	public List<MapNode> constructRouteFromParentMap(MapNode startNode, MapNode endNode,
			Map<MapNode, MapNode> parentMap) {
		// Reverses the nodes in parentMap to construct the path
		// System.out.println("Constructing route from parentMap");
		LinkedList<MapNode> route = new LinkedList<MapNode>();
		MapNode currentNode = endNode;
		route.add(currentNode);
		while (currentNode != startNode) {
			currentNode = parentMap.get(currentNode);
			route.addFirst(currentNode);
		}
		return route;
	}

	private Map<MapNode, MapNode> constructParentMapFromRoute(List<MapNode> route) {
		// System.out.println("Constructing parentMap from route");
		Map<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		for (int nodeIndex = 1; nodeIndex < route.size(); nodeIndex++) {
			parentMap.put(route.get(nodeIndex), route.get(nodeIndex - 1));
		}
		return parentMap;
	}

	@Override
	protected List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// TODO Auto-generated method stub
		return null;
	}
}
