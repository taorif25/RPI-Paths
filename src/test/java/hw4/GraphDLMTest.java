package hw4;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public final class GraphDLMTest {

	@Test
	public void testConstructorNoArgs() {
		new GraphDLM<String,String>();
	}
	
	@Test
	public void testConstructorOneArg() {
		// null argument
		assertThrows(IllegalArgumentException.class, () -> {new GraphDLM<String,String>(null);});
		ArrayList<String> nodes = new ArrayList<String>();
		new GraphDLM<String,String>(nodes); // empty ArrayList
		nodes.add(null);
		// null element in argument
		assertThrows(IllegalArgumentException.class, () -> {new GraphDLM<String,String>(nodes);});
		nodes.clear();
		nodes.add("");
		new GraphDLM<String,String>(nodes); // empty string
		nodes.add("racecar");
		nodes.add("FOOBAR");
		new GraphDLM<String,String>(nodes); // multiple elements
	}
	
	@Test
	public void testContainsNode() {
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.add("");
		nodes.add("car");
		nodes.add("ABCD");
		GraphDLM<String,String> test = new GraphDLM<String,String>(nodes);
		assertTrue(test.containsNode("")); // blank string in graph
		assertTrue(test.containsNode("car")); // normal  in graph
		assertTrue(test.containsNode("ABCD"));
		assertFalse(test.containsNode("aaa")); // not in graph
		assertFalse(test.containsNode("abcd")); // ensures casing is accounted for
	}
	
	@Test
	public void testConstructorRepresentationExposure() {
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.add("abc");
		GraphDLM<String,String> test = new GraphDLM<String,String>(nodes);
		nodes.set(0, "abcd");
		nodes.add("car");
		assertFalse(test.containsNode("abcd")); // editing an element
		assertFalse(test.containsNode("car")); // adding an element
	}
	
	@Test
	public void testAddNode() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		// adding null node
		assertThrows(IllegalArgumentException.class, () -> {test.addNode(null);});
		String val = "abc";
		assertTrue(test.addNode(val)); // test if valid node add returns correctly
		assertTrue(test.containsNode("abc")); // test if node is present
		val = "abcd";
		assertFalse(test.containsNode("abcd")); // check for rep exposure
		assertFalse(test.addNode("abc")); // identical node add check
		assertTrue(test.addNode("car")); // mutliple nodes added check
		assertTrue(test.containsNode("car"));
	}
	
	@Test
	public void testRemoveNode() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		// removing null node
		assertThrows(IllegalArgumentException.class, () -> {test.removeNode(null);});
		// removing node not present in graph
		assertThrows(IllegalArgumentException.class, () -> {test.removeNode("cat");});
		test.addNode("cat");
		test.addNode("rat");
		test.addEdge("rat", "cat", "1");
		test.removeNode("cat"); // removing a node present in the graph
		assertFalse(test.containsNode("cat"));
		assertFalse(test.containsEdge("rat", "cat", "1"));
	}
	
	@Test
	public void testAddAndContainsEdge() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		test.addNode("a");
		test.addNode("b");
		// tests null arguments for containsEdge
		assertThrows(IllegalArgumentException.class, () -> {test.containsEdge(null,"b","c");});
		assertThrows(IllegalArgumentException.class, () -> {test.containsEdge("a",null,"c");});
		assertThrows(IllegalArgumentException.class, () -> {test.containsEdge("a","b",null);});
		// tests valid nodes but invalid edge for containsEdge
		assertFalse(test.containsEdge("a","b","c"));
		// tests null arguments for addEdge
		assertThrows(IllegalArgumentException.class, () -> {test.addEdge(null,"b","c");});
		assertThrows(IllegalArgumentException.class, () -> {test.addEdge("a",null,"c");});
		assertThrows(IllegalArgumentException.class, () -> {test.addEdge("a","b",null);});
		// tests illegal args for addEdge
		assertThrows(IllegalArgumentException.class, () -> {test.addEdge("A","b","c");});
		assertThrows(IllegalArgumentException.class, () -> {test.addEdge("a","B","c");});
		// tests addEdge where data[source][dest] does not exist yet
		test.addEdge("a", "b", "c");
		// tests contains for a valid edge
		assertTrue(test.containsEdge("a", "b", "c"));
		// tests contains where data[source][dest] exists but invalid label
		assertFalse(test.containsEdge("a","b","C"));
		// tests data[source] exists but not data[source][dest]
		assertFalse(test.containsEdge("a","B","c"));
		// tests contains where data[source] does not exist
		assertFalse(test.containsEdge("A", "b", "c"));
		// tests addEdge where identical edge already exists
		test.addEdge("a", "b", "c");
		// tests addEdge where data[source][dest] exists
		test.addEdge("a", "b", "C");
		// tests containsEdge for multiple edges in data[source][dest]
		assertTrue(test.containsEdge("a", "b", "C"));
		// tests edges in reverse order
		test.addEdge("b", "a", "c");
		assertTrue(test.containsEdge("b", "a", "c"));
	}
	
	@Test
	public void testRemoveEdge() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		test.addNode("a");
		test.addNode("b");
		// test remove when data[source][dest] does not exist
		assertThrows(IllegalArgumentException.class, () -> {test.removeEdge("a","b","c");});
		test.addEdge("a", "b", "c");
		// test remove when label doesn't exist in data[source][dest]
		assertThrows(IllegalArgumentException.class, () -> {test.removeEdge("a","b","C");});
		// test removing a valid edge
		test.removeEdge("a", "b", "c");
		assertFalse(test.containsEdge("a", "b", "c"));
		// test null pointer exceptions
		assertThrows(IllegalArgumentException.class, () -> {test.removeEdge(null,"b","c");});
		assertThrows(IllegalArgumentException.class, () -> {test.removeEdge("a",null,"c");});
		assertThrows(IllegalArgumentException.class, () -> {test.removeEdge("a","b",null);});
		// test attempting to remove the same edge twice
		assertThrows(IllegalArgumentException.class, () -> {test.removeEdge("a","b","c");});
		// tests remove with multiple instances of the same edge
		test.addEdge("a", "b", "c");
		test.addEdge("a", "b", "c");
		test.removeEdge("a", "b", "c");
		assertTrue(test.containsEdge("a","b","c"));
		test.removeEdge("a", "b", "c");
		assertFalse(test.containsEdge("a", "b", "c"));
		// tests that b is removed as a child
		assertFalse(test.getChildren("a").containsKey("b"));
	}
	
	public void testIterator() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		test.addNode("b");
		test.addNode("a");
		test.addNode("c");
		test.addNode("B");
		Iterator<String> itr = test.iterator();
		ArrayList<String> nodes = new ArrayList<String>();
		while(itr.hasNext()) {
			nodes.add(itr.next());
		}
		// ensure nodes contains all nodes of the graph
		assertTrue(nodes.size()==4 && nodes.contains("b") && nodes.contains("a") 
				   && nodes.contains("c") && nodes.contains("B"));
	}
	
	@Test
	public void testGetNodes() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		test.addNode("b");
		test.addNode("a");
		test.addNode("c");
		test.addNode("B");
		ArrayList<String> nodes = test.getNodes();
		assertTrue(nodes.contains("b") && nodes.contains("a") && 
				   nodes.contains("c") && nodes.contains("B") && nodes.size()==4);
		test.removeNode("B");
		test.removeNode("a");
		assertTrue(nodes.contains("B") && nodes.contains("a"));
		nodes = test.getNodes();
		assertTrue(nodes.contains("b") && nodes.contains("c") && nodes.size()==2);
		nodes.set(0, "B");
		assertFalse(test.containsNode("B"));
	}
	
	@Test
	public void testGetChildren() {
		GraphDLM<String,String> test = new GraphDLM<String,String>();
		// test null argument
		assertThrows(IllegalArgumentException.class, () -> {test.getChildren(null);});
		assertThrows(IllegalArgumentException.class, () -> {test.getChildren("c");});
		test.addNode("a");
		test.addNode("b");
		test.addEdge("a", "b", "2");
		test.addEdge("a", "b", "1");
		test.addEdge("a", "a", "1");
		// tests valid argument
		HashMap<String, PriorityQueue<String> > children = test.getChildren("a");
		assertTrue(children.containsKey("a"));
		assertTrue(children.containsKey("b"));
		assertTrue(children.get("a").size()==1 && children.get("a").contains("1"));
		assertTrue(children.get("b").size()==2 && children.get("b").contains("1")
				   && children.get("b").contains("2"));
	}

}
