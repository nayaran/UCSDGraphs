package roadgraph;

import java.util.Comparator;

public class DistanceFromStartComparator implements Comparator<MapNode> {

	@Override
	public int compare(MapNode o1, MapNode o2) {
		// A node should be considered smaller if it is nearer to Start
		if (o1.getDistanceFromStart() > o2.getDistanceFromStart())
			return 1;
		else if (o1.getDistanceFromStart() < o2.getDistanceFromStart())
			return -1;
		else return 0;
	}

}
