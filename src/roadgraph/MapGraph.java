/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;

/**
 * @author UCSD MOOC development team and YOU
 * 
 *         A class which represents a graph of geographic locations Nodes in the
 *         graph are intersections between
 *
 */
public abstract class MapGraph {
	// TODO: Add your member variables here in WEEK 3
	protected Map<GeographicPoint, MapNode> mapNodes;
	private int numVertices;
	private int numEdges;

	/**
	 * Create a new empty MapGraph
	 */
	public MapGraph() {
		// TODO: Implement in this constructor in WEEK 3
		this.mapNodes = new HashMap<GeographicPoint, MapNode>();
		this.numVertices = 0;
		this.numEdges = 0;
	}

	/**
	 * Get the number of vertices (road intersections) in the graph
	 * 
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices() {
		// TODO: Implement this method in WEEK 3
		return numVertices;
	}

	/**
	 * Return the intersections, which are the vertices in this graph.
	 * 
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices() {
		// TODO: Implement this method in WEEK 3
		return mapNodes.keySet();
	}

	/**
	 * Get the number of road segments in the graph
	 * 
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges() {
		// TODO: Implement this method in WEEK 3
		return numEdges;
	}

	/**
	 * Add a node corresponding to an intersection at a Geographic Point If the
	 * location is already in the graph or null, this method does not change the
	 * graph.
	 * 
	 * @param location
	 *            The location of the intersection
	 * @return true if a node was added, false if it was not (the node was already
	 *         in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location) {
		// TODO: Implement this method in WEEK 3
		if (location == null || isLocationPresent(location))
			return false;
		mapNodes.put(location, new MapNode(location));
		numVertices++;
		return true;
	}

	/**
	 * Checks whether a location is already in the graph or not
	 * 
	 * @param location
	 *            The location of the intersection
	 * @return true if a node with the same coordinates already exists in the graph
	 */
	boolean isLocationPresent(GeographicPoint location) {
		return mapNodes.containsKey(location);
	}

	/**
	 * Adds a directed edge to the graph from pt1 to pt2. Precondition: Both
	 * GeographicPoints have already been added to the graph
	 * 
	 * @param from
	 *            The starting point of the edge
	 * @param to
	 *            The ending point of the edge
	 * @param roadName
	 *            The name of the road
	 * @param roadType
	 *            The type of the road
	 * @param length
	 *            The length of the road, in km
	 * @throws IllegalArgumentException
	 *             If the points have not already been added as nodes to the graph,
	 *             if any of the arguments is null, or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length)
			throws IllegalArgumentException {

		// TODO: Implement this method in WEEK 3

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

	/**
	 * Find the path from start to goal using breadth first search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @return The list of intersections that form the shortest (unweighted) path
	 *         from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		Consumer<GeographicPoint> temp = (x) -> {
		};
		return bfs(start, goal, temp);
	}

	/**
	 * Find the path from start to goal using breadth first search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @param nodeSearched
	 *            A hook for visualization. See assignment instructions for how to
	 *            use it.
	 * @return The list of intersections that form the shortest (unweighted) path
	 *         from start to goal (including both start and goal).
	 */
	protected abstract List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched);

	/**
	 * Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @return The list of intersections that form the shortest path from start to
	 *         goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
		Consumer<GeographicPoint> temp = (x) -> {
		};
		return dijkstra(start, goal, temp);
	}

	/**
	 * Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @param nodeSearched
	 *            A hook for visualization. See assignment instructions for how to
	 *            use it.
	 * @return The list of intersections that form the shortest path from start to
	 *         goal (including both start and goal).
	 */
	protected abstract List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched);

	/**
	 * Find the path from start to goal using A-Star search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @return The list of intersections that form the shortest path from start to
	 *         goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		Consumer<GeographicPoint> temp = (x) -> {
		};
		return aStarSearch(start, goal, temp);
	}

	/**
	 * Find the path from start to goal using A-Star search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @param nodeSearched
	 *            A hook for visualization. See assignment instructions for how to
	 *            use it.
	 * @return The list of intersections that form the shortest path from start to
	 *         goal (including both start and goal).
	 */
	protected abstract List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched);

	/**
	 * Helper method for printing the graph structure
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
			for (MapEdge street : mapNode.getStreets()) {
				s += "\n\t\t" + street;
			}
		}
		System.out.println(s);
	}

	public void resetGraph() {
		// Resets the info in each mapNode related to the search
		for (MapNode mapNode : mapNodes.values()) {
			mapNode.resetMapNode();
		}
	}
}
