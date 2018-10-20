/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 3
	private Map<GeographicPoint, MapNode> mapNodes;
	private int numVertices;
	private int numEdges;

	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 3
		this.mapNodes = new HashMap<GeographicPoint, MapNode>();
		this.numVertices = 0;
		this.numEdges = 0;
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 3
		return numVertices;
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 3
		return mapNodes.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 3
		return numEdges;
	}
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 3
		if (location == null || isLocationPresent(location))
			return false;
		mapNodes.put(location, new MapNode(location));
		numVertices++;		
		return true;
	}

	/** Checks whether a location is already in the graph or not
	 * @param location  The location of the intersection
	 * @return true if a node with the same coordinates already exists
	 * in the graph
	 */
	boolean isLocationPresent(GeographicPoint location) {
		return mapNodes.containsKey(location);
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//TODO: Implement this method in WEEK 3
		
		// Throw exception in case of invalid arguments
		if (!isLocationPresent(from) || from == null)
			throw new IllegalArgumentException("from Location should be present in the graph");
		if (!isLocationPresent(to) || to == null)
			throw new IllegalArgumentException("to Location should be present in the graph");
		if (roadName == null || roadType == null || length == 0.0)
			throw new IllegalArgumentException("roadName, roadType & length - all three params should be non-null");
		
		MapNode fromMapNode = mapNodes.get(from);
		MapNode toMapNode = mapNodes.get(to);
		MapEdge mapEdge = new MapEdge(from, to, roadName, roadType, length);
		
		fromMapNode.addStreet(mapEdge);
		fromMapNode.addNeighbour(toMapNode, length);
		
		numEdges++;
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		
		// Initialize
		MapNode startNode = mapNodes.get(start);
		MapNode endNode = mapNodes.get(goal);
		List<GeographicPoint> path;
		
		// Find path using breadth first search
		path = findPathUsingBFS(startNode, endNode, nodeSearched);
		
		if(path.isEmpty()) {
			//System.out.println("\nSorry, Goal not found!!");
			return null;
		}
		return path;
	}
	
	public List<GeographicPoint> findPathUsingBFS(MapNode startNode, MapNode endNode, Consumer<GeographicPoint> nodeSearched) {
		Set<MapNode> visited = new HashSet<MapNode>();
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		Map<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
	
		//System.out.println("Start - " + startNode);
		//System.out.println("Goal - " + endNode);
		//System.out.println();
		
		// do bfs
		boolean found = false;
		toExplore.add(startNode);
		
		while(!toExplore.isEmpty()) {
			//System.out.println("\ntoExplore - " + toExplore);
			MapNode currentNode = toExplore.remove();
			// Hook for visualization.  See writeup.
			nodeSearched.accept(currentNode.getLocation());
			//System.out.println("currentNode - " + currentNode);
			if (currentNode.equals(endNode)) {
				found = true;
				break;
			}
			//System.out.println("found - " + found);
			Set <MapNode> neighbors = currentNode.getNeighbors();
			//System.out.println("neighbors - " + neighbors);
			//System.out.println("Visited - " + visited);
			for (MapNode neighbour: neighbors) {
				if (!visited.contains(neighbour)) {
					parentMap.put(neighbour, currentNode);
					visited.add(neighbour);
					toExplore.add(neighbour);
				}
			}
			//System.out.println("toExplore - " + toExplore);
		}
		
		if (!found)
			return new LinkedList<GeographicPoint>();
		
		return constructPathFromParentMap(startNode, endNode, parentMap);
	}
	
	List<GeographicPoint> constructPathFromParentMap(MapNode startNode, MapNode endNode, Map<MapNode, MapNode> parentMap){
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		MapNode currentNode = endNode;
		path.add(currentNode.getLocation());
		while(currentNode != startNode) {
			currentNode = parentMap.get(currentNode);
			path.addFirst(currentNode.getLocation());
		}
		return path;
	}
	
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 4

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		// Initialize
		MapNode startNode = mapNodes.get(start);
		MapNode endNode = mapNodes.get(goal);
		List<GeographicPoint> path;
		
		// Find path using breadth first search
		path = findPathUsingDijkstra(startNode, endNode, nodeSearched);
		
		if(path.isEmpty()) {
			//System.out.println("\nSorry, Goal not found!!");
			return new LinkedList<GeographicPoint>();
		}
		return path;
	}

	private List<GeographicPoint> findPathUsingDijkstra(MapNode startNode, MapNode endNode,
			Consumer<GeographicPoint> nodeSearched) {
		
		// Initialize
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
			System.out.println("Dijkstra visiting - " + currentNode);	
			visitedCount++;

			if (!visited.contains(currentNode)) {
				//System.out.println(currentNode + " has not been visited");
				visited.add(currentNode);
				if(currentNode.equals(endNode)) {
					found = true;
					System.out.println("\nDijkstra - Total nodes visited - "+ visitedCount);
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
			return new LinkedList<GeographicPoint>();			
		}

		// Construct & return path
		return constructPathFromParentMap(startNode, endNode, parentMap);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 4
		
		// TODO: Implement this method in WEEK 4

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		// Initialize
		MapNode startNode = mapNodes.get(start);
		MapNode endNode = mapNodes.get(goal);
		List<GeographicPoint> path;
		
		// Find path using breadth first search
		path = findPathUsingAStar(startNode, endNode, nodeSearched);
		
		if(path.isEmpty()) {
			//System.out.println("\nSorry, Goal not found!!");
			return new LinkedList<GeographicPoint>();
		}
		return path;
	}

	private List<GeographicPoint> findPathUsingAStar(MapNode startNode, MapNode endNode,
			Consumer<GeographicPoint> nodeSearched) {
		// Initialize
		HashSet<MapNode> visited = new HashSet<MapNode>();
		PriorityQueue<MapNode> toExplore = new PriorityQueue<MapNode>(new DistanceToDestinationComparator());
		HashMap<MapNode, MapNode> parentMap = new HashMap<MapNode, MapNode>();
		int visitedCount = 0;
		
		//System.out.println("Start - " + startNode);
		//System.out.println("Goal - " + endNode);
		
		boolean found = false;
		
		startNode.setDistanceFromStart(0.0);
		startNode.setEstimatedDistanceToDestination(0.0);
		toExplore.add(startNode);
	
		// Perform search using A Star
		while(!toExplore.isEmpty()) {
			//System.out.println("\ntoExplore - " + toExplore);
			//System.out.println("visited - " + visited);			
			MapNode currentNode = toExplore.remove();
			System.out.println("AStar visiting - " + currentNode);
			visitedCount++;
			
			if (!visited.contains(currentNode)) {
				//System.out.println(currentNode + " has not been visited");
				visited.add(currentNode);
				if(currentNode.equals(endNode)) {
					found = true;
					System.out.println("\nAStar - Total nodes visited - "+ visitedCount);
					break;
				}
				HashMap<MapNode, Double> neighborDistanceMap = currentNode.getNeighborDistanceMap();
				//System.out.println("Neighbors - " + neighborDistanceMap);
				for (MapNode neighbor: neighborDistanceMap.keySet()) {
					//System.out.println("neighbor - " + neighbor);
					if(!visited.contains(neighbor)) {
						// Calculate distance from start for the neighbor

						neighbor.setEstimatedDistanceToDestination(neighbor.getLocation().distance(endNode.getLocation()));
						
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
				}
			}
		}
		if (!found) {
			return new LinkedList<GeographicPoint>();			
		}

		// Construct & return path
		return constructPathFromParentMap(startNode, endNode, parentMap);
	}

	/** Helper method for printing the graph structure
	 */
	public void printGraph() {
		String s = "\nAdjacency list representation\n";
		s += "Size : " + getNumVertices() + " x" + getNumEdges() + " nodes";
		for (MapNode mapNode : mapNodes.values()) {
			s += "\n\nNode: " + mapNode.getLocation();
			s += "\n\tNeighbors:";
		
			for (MapNode neighbour : mapNode.getNeighbors()) {
				s += "\n\t\t" + neighbour.getLocation();
			}
			s += "\n\tOut Roads:";
			for (MapEdge street: mapNode.getStreets()) {
				s += "\n\t\t" + street;
			}
		}
		System.out.println(s);
	}	
	
	static void simpleTest() {
		GeographicPoint testStart;
		GeographicPoint testEnd;
		List<GeographicPoint> testroute;
		List<GeographicPoint> testroute2;
		
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		System.out.println("Map - ");
		simpleTestMap.printGraph();
		
		testStart = new GeographicPoint(1.0, 1.0);
		testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		testroute = simpleTestMap.dijkstra(testStart,testEnd);
		System.out.println("Dijkstra result - " + testroute);;
		
		System.out.println("----------------------------------------------------");
		simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		System.out.println("AStar result- " + testroute2);		
	}
	
	static void utcTest() {
		GeographicPoint testStart;
		GeographicPoint testEnd;
		List<GeographicPoint> testroute;
		List<GeographicPoint> testroute2;
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("\n\nTest 2 using utc: Dijkstra should be 13 and AStar should be 5");
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		testMap.printGraph();
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("Dijkstra result - " + testroute);;
		
		System.out.println("----------------------------------------------------");
		testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println("AStar result- " + testroute2);


	}
	static void utcTestComplexDijkstra() {
		GeographicPoint testStart;
		GeographicPoint testEnd;
		List<GeographicPoint> testroute;
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37");
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);	
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("Dijkstra result - " + testroute);;
		
	}
	static void utcTestComplexAStar() {
		GeographicPoint testStart;
		GeographicPoint testEnd;
		List<GeographicPoint> testroute2;
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: AStar should be 10");
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);	
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println("AStar result- " + testroute2);
		
	}
	static void utcTestQuiz() {
		GeographicPoint testStart;
		GeographicPoint testEnd;
		List<GeographicPoint> testroute;
		List<GeographicPoint> testroute2;
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.8648772, -117.2254046);
		testEnd = new GeographicPoint(32.8660691, -117.217393);
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		//testMap.printGraph();
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("Dijkstra result - " + testroute);;
		
		System.out.println("----------------------------------------------------");
		testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println("AStar result- " + testroute2);
		
	}
	static void testDistanceFromStartComparison() {
		MapNode node1 = new MapNode(new GeographicPoint(1.2, -1.2));
		node1.setDistanceFromStart(3.0);
		System.out.println(node1);
		
		MapNode node2 = new MapNode(new GeographicPoint(1.4, -1.4));
		node2.setDistanceFromStart(2.1);
		System.out.println(node2);

		int result = node1.compareTo(node2);
		System.out.println(result);
		if (result < 0)
			System.out.println("Success - node1 is farther to start than node2");
		else
			System.out.println("Failure");

	}
	static void testPriorityQueueBasedOnDistanceFromStart() {
		MapNode node1 = new MapNode(new GeographicPoint(32.8674388, -117.2190213));
		node1.setDistanceFromStart(1.12);
		System.out.println(node1);
		
		MapNode node2 = new MapNode(new GeographicPoint(32.8697828, -117.2244506));
		node2.setDistanceFromStart(1.134);
		System.out.println(node2);

		MapNode node3 = new MapNode(new GeographicPoint(32.8697828, -117.2244506));
		node3.setDistanceFromStart(1.135);
		System.out.println(node3);
		
		Queue<MapNode> priorityQueue = new PriorityQueue<MapNode>(new DistanceFromStartComparator());
		Queue<MapNode> simpleQueue = new LinkedList<MapNode>();
		
		priorityQueue.add(node3);
		simpleQueue.add(node3);
		priorityQueue.add(node2);
		simpleQueue.add(node2);
		priorityQueue.add(node1);
		simpleQueue.add(node1);
		
		System.out.println("Simple queue - ");
		while(!simpleQueue.isEmpty())
			System.out.println(simpleQueue.remove());

		System.out.println("Priority queue - ");
		while(!priorityQueue.isEmpty())
			System.out.println(priorityQueue.remove());

	}
	public static void main(String[] args)
	{
		//testDistanceFromStartComparison();
		//testPriorityQueueBasedOnDistanceFromStart();
		
		//GraphLoader.createIntersectionsFile("data/maps/utc.map",
        //        "data/intersections/utc.intersections");
		

		simpleTest();
		//utcTest();
		//utcTestComplexDijkstra();
		//utcTestComplexAStar();
		//utcTestQuiz();
	}
	
}
