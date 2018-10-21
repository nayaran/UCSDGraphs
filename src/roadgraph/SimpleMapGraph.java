/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


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
 * @author UCSD MOOC development team and Anurag Narayan
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class SimpleMapGraph extends MapGraph{
	/** 
	 * Create a new empty MapGraph 
	 */
	public SimpleMapGraph()
	{
		super();
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
	
	protected List<GeographicPoint> findPathUsingBFS(MapNode startNode, MapNode endNode, Consumer<GeographicPoint> nodeSearched) {
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
	
	private List<GeographicPoint> constructPathFromParentMap(MapNode startNode, MapNode endNode, Map<MapNode, MapNode> parentMap){
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
	protected List<GeographicPoint> dijkstra(GeographicPoint start, 
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
	protected List<GeographicPoint> aStarSearch(GeographicPoint start, 
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
			//System.out.println("AStar visiting - " + currentNode);
			visitedCount++;
			
			if (!visited.contains(currentNode)) {
				//System.out.println(currentNode + " has not been visited");
				visited.add(currentNode);
				if(currentNode.equals(endNode)) {
					found = true;
					System.out.println("\nAStar");
					System.out.println("\tNodes visited - " + visitedCount);
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
}
