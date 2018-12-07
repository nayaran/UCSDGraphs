package roadgraph;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import geography.GeographicPoint;
import util.GraphLoader;

public class CachedMapGraphTest {

	static void simpleTest() {
		GeographicPoint 	testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		MapGraph testMap = new CachedMapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", testMap);
		//simpleTestMap.printGraph();
				
		System.out.println("Test 1 using simpletest: Dijkstra should be 9");
		
		testDjikstraSearch(testStart, testEnd, testMap);	
	}

	static void utcTest() {
		GeographicPoint testStart = new GeographicPoint(32.869423, -117.220917);;
		GeographicPoint testEnd = new GeographicPoint(32.869255, -117.216927);
		CachedMapGraph testMap = new CachedMapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		//testMap.printGraph();
		
		// A very simple test using real data

		System.out.println("\n\nTest 2 using utc: Dijkstra should be 13");

		testDjikstraSearch(testStart, testEnd, testMap);		
	}

	static void utcTestComplexDijkstra() {
		// A very simple test using real data
		GeographicPoint testStart = new GeographicPoint(32.8674388, -117.2190213);
		GeographicPoint testEnd = new GeographicPoint(32.8697828, -117.2244506);
		CachedMapGraph testMap = new CachedMapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);	
		
		System.out.println("\nTest 3 using utc: Dijkstra should be 37");
		testDjikstraSearch(testStart, testEnd, testMap);		
	}

	static void utcTestQuiz() {
		// A very simple test using real data
		GeographicPoint testStart = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint testEnd = new GeographicPoint(32.8660691, -117.217393);
		CachedMapGraph testMap = new CachedMapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		//testMap.printGraph();

		System.out.println("\nTest 4 using utc: QUIZ");
		
		testDjikstraSearch(testStart, testEnd, testMap);
	}

	static void testDjikstraSearch(GeographicPoint start, GeographicPoint end, MapGraph map) {
		long startTime;
		long endTime;
		List<GeographicPoint> testroute;
		
		startTime = System.nanoTime();
		testroute = map.dijkstra(start,end);
		endTime = System.nanoTime();
		System.out.println("\tTime taken - " + (endTime-startTime)/1000 + "us");
		System.out.println("\tPath - " + testroute);
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

	static void testCachedRouteMembership () {
		GeographicPoint 	testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		CachedMapGraph testMap = new CachedMapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", testMap);
		//simpleTestMap.printGraph();
		long startTime = System.currentTimeMillis();
		testDjikstraSearch(testStart, testEnd, testMap);
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Time taken (without caching) - " + duration + "ms");
		System.out.println();
		startTime = System.currentTimeMillis();
		testDjikstraSearch(testStart, testEnd, testMap);
		duration = System.currentTimeMillis() - startTime;
		System.out.println("\nTime taken (with caching) - " + duration + "ms");
		System.out.println("\nCache details-");
		testMap.printCachedRoutes();
	}

	public static void main(String[] args)
	{
	
		//testDistanceFromStartComparison();
		//testPriorityQueueBasedOnDistanceFromStart();
		
		//GraphLoader.createIntersectionsFile("data/maps/utc.map",
        //        "data/intersections/utc.intersections");
		
		testCachedRouteMembership();
//		simpleTest();
//		utcTest();
//		utcTestComplexDijkstra();
//		utcTestQuiz();
	}
}
