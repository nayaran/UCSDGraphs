package roadgraph;

import java.util.Comparator;

public class DistanceToDestinationComparator implements Comparator<MapNode> {

	@Override
	public int compare(MapNode o1, MapNode o2) {
		// A node should be considered smaller if it is nearer to the destination
		if ((o1.getEstimatedDistanceToDestination() + o1.getDistanceFromStart()) 
				> (o2.getEstimatedDistanceToDestination() + o2.getDistanceFromStart()))
			return 1;
		else if ((o1.getEstimatedDistanceToDestination() + o1.getDistanceFromStart()) 
				< (o2.getEstimatedDistanceToDestination() + o2.getDistanceFromStart()))
			return -1;
		else return 0;
	}

}
