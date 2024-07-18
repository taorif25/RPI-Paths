package hw6;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public final class LegoPathsTest {
	
	private LegoPaths test = new LegoPaths();
	
	// tests for an individual edge from a to be and from b to a
	private void testEdge(String a, String b, int weight) {
		Double frac = 1.0 / (double)weight;
		assertTrue(test.graph.containsEdge(a, b, frac));
		assertTrue(test.graph.containsEdge(b,a,frac));
	}
	
	@Test
	public void testCreateNewGraph() {
		// edge cases
		assertThrows(IllegalArgumentException.class, () -> {test.createNewGraph("data/fake_name.csv");});
		assertThrows(IllegalArgumentException.class, () -> {test.createNewGraph("data/HW6wrong_format.csv");});
		// correctness testing
		test.createNewGraph("data/HW6onePath.csv");
		assertTrue(test.graph.numNodes() == 7);
		assertTrue(test.graph.numEdges() == 14);
		testEdge("S", "A", 1);
		testEdge("D", "A", 1);
		testEdge("S", "B", 5);
		testEdge("C", "B", 5);
		testEdge("B", "E", 5);
		testEdge("E", "C", 10);
		testEdge("C", "F", 5);
		test.createNewGraph("data/HW6multiplePaths.csv");
		assertTrue(test.graph.numNodes() == 5);
		assertTrue(test.graph.numEdges() == 10);
		testEdge("S", "A", 2);
		testEdge("D", "A", 1);
		testEdge("S", "B", 1);
		testEdge("B", "C", 6);
		testEdge("D", "C", 6);
		test.createNewGraph("data/HW6interconnected.csv");
		assertTrue(test.graph.numNodes()==4);
		assertTrue(test.graph.numEdges()==12);
		testEdge("A","B",1);
		testEdge("A","C",1);
		testEdge("A","D",1);
		testEdge("B","C",1);
		testEdge("B","D",1);
		testEdge("C","D",1);
	}
	
	@Test
	public void testFindPath() {
		// no path found
		test.createNewGraph("data/HW6noPath.csv");
		assertTrue(test.findPath("S","D").equals("path from S to D:\nno path found\n"));
		// edge cases
		test.createNewGraph("data/HW6onePath.csv");
		assertThrows(IllegalArgumentException.class, () -> {test.findPath(null, "A");});
		assertThrows(IllegalArgumentException.class, () -> {test.findPath("A", null);});
		assertTrue(test.findPath("S", "a").equals(new String("unknown part a\n")));
		assertTrue(test.findPath("a", "D").equals(new String("unknown part a\n")));
		assertTrue(test.findPath("a", "a").equals(new String("unknown part a\n")));
		assertTrue(test.findPath("a", "b").equals(new String("unknown part a\nunknown part b\n")));
		assertTrue(test.findPath("b", "a").equals(new String("unknown part b\nunknown part a\n")));
		assertTrue(test.findPath("S", "S").equals(new String("path from S to S:\ntotal cost: 0.000\n")));
		// one simple path to dest
		String result = "path from S to D:\nS to A with weight 1.000\nA to D with weight 1.000\ntotal cost: 2.000\n";
		assertTrue(test.findPath("S", "D").equals(result));
		// multiple paths, one shortest path. checks correctness of greedy algo
		test.createNewGraph("data/HW6multiplePaths.csv");
		result = "path from S to D:\nS to B with weight 1.000\nB to C with weight 0.167\nC to D with weight 0.167\n";
		result = result + "total cost: 1.333\n";
		assertTrue(test.findPath("S", "D").equals(result));
		//test.createNewGraph("data/lego2024.csv");
		//test.findPath("31367 Green Duplo Egg Base", "98138pr0080 Pearl Gold Tile Round 1 x 1 with Blue, Yellow and Black Minecraft Print");
	}
}