package hw5;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public final class ProfessorPathsTest {
	
	@Test
	public void testCreateNewGraph() {
		ProfessorPaths test = new ProfessorPaths();
		assertThrows(IllegalArgumentException.class, () -> {test.createNewGraph("data/fake_name.csv");});
		assertThrows(IllegalArgumentException.class, () -> {test.createNewGraph("data/wrong_format.csv");});
		test.createNewGraph("data/smallGraph.csv");
		// test node and edge count for small graph
		assertTrue(test.graph.numNodes()==3);
		assertTrue(test.graph.numEdges()==6);
		String [] sources = {"A", "A", "B"};
		String [] dests = {"B","C","C"};
		String [] labels = {"1","2","3"};
		// test correctness of edges
		for (int i=0;i<3;i++) {
			assertTrue(test.graph.containsEdge(sources[i], dests[i], labels[i]));
			assertTrue(test.graph.containsEdge(dests[i], sources[i], labels[i]));
		}
		// tests edges for complete graph
		test.createNewGraph("data/interconnected.csv");
		assertTrue(test.graph.numNodes()==5);
		String [] nodes = {"A","B","C","D","E"};
		for (int i=0;i<4;i++) {
			for (int j=i+1;j<5;j++) {
				assertTrue(test.graph.containsEdge(nodes[i],nodes[j],"1"));
				assertTrue(test.graph.containsEdge(nodes[j],nodes[i],"1"));
			}
		}
		// tests graph generation on very large graphs.
		
		// 5000 courses, 5000 professors, 25 professors
		// per course. 5*4*5000 = 100,000 edges
		test.createNewGraph("data/largeEdgeCount.csv");
		assertTrue(test.graph.numNodes()==5000);
		assertTrue(test.graph.numEdges()==450000);
		// 100,000 nodes, 100,000 courses, 0 edges
		test.createNewGraph("data/largeNodeCount.csv");
		assertTrue(test.graph.numNodes()==100000);
		assertTrue(test.graph.numEdges()==0);
	}
	
	@Test
	public void testFindPath() {
		ProfessorPaths test = new ProfessorPaths();
		// edge case tests
		test.createNewGraph("data/smallGraph.csv");
		assertThrows(IllegalArgumentException.class, () -> {test.findPath(null, "A");});
		assertThrows(IllegalArgumentException.class, () -> {test.findPath("A", null);});
		assertTrue(test.findPath("A", "D").equals(new String("unknown professor D\n")));
		assertTrue(test.findPath("D", "A").equals(new String("unknown professor D\n")));
		assertTrue(test.findPath("D", "D").equals(new String("unknown professor D\n")));
		assertTrue(test.findPath("E", "D").equals(new String("unknown professor E\nunknown professor D\n")));
		assertTrue(test.findPath("E", "D").equals(new String("unknown professor E\nunknown professor D\n")));
		// simple no path found
		test.createNewGraph("data/simpleNoPath.csv");
		assertTrue(test.findPath("A","B").equals(new String("path from A to B:\nno path found\n")));
		// source == dest test
		assertTrue(test.findPath("A","A").equals("path from A to A:\n"));
		// one possible path test
		test.createNewGraph("data/onePath.csv");
		String answer = new String("path from source to dest:"
				+ "\nsource to 1 via a\n1 to 2 via b\n2 to 3 via c\n3 to 4 via d\n4 to dest via e\n");
		assertTrue(test.findPath("source","dest").equals(answer));
		// one possible shortest path test
		test.createNewGraph("data/oneShortestPath.csv");
		answer = new String("path from source to dest:"
				+ "\nsource to 5 via f\n5 to dest via g\n");
		assertTrue(test.findPath("source","dest").equals(answer));
		// ensures children are sorted
		test.createNewGraph("data/ensureSortedNodes.csv");
		answer = new String("path from source to dest:"
				+ "\nsource to 1 via a\n1 to dest via b\n");
		assertTrue(test.findPath("source","dest").equals(answer));
		// ensures edges are sorted
		test.createNewGraph("data/ensureSortedEdges.csv");
		answer = new String("path from source to dest:"
				+ "\nsource to dest via a\n");
		assertTrue(test.findPath("source","dest").equals(answer));
		// large graph path found
		test.createNewGraph("data/largeEdgeCount.csv");
		test.findPath("3","3435");
		// large graph no path found
		test.createNewGraph("data/largeEdgeCountNoPath.csv");
		assertTrue(test.findPath("0","5000").equals(new String("path from 0 to 5000:\nno path found\n")));
	}
}