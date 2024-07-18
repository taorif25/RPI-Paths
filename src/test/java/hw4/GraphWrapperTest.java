package hw4;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;


import org.junit.jupiter.api.Test;

public final class GraphWrapperTest {
	
	@Test
	public void testConstructor() {
		new GraphWrapper();
	}
	
	@Test
	public void testAddNode() {
		GraphWrapper test = new GraphWrapper();
		test.addNode("a");
	}
	
	@Test
	public void testAddEdge() {
		GraphWrapper test = new GraphWrapper();
		test.addNode("a");
		test.addEdge("a","a","a");
	}
	
	@Test
	public void testListNodes() {
		GraphWrapper test = new GraphWrapper();
		test.addNode("b");
		test.addNode("a");
		test.addNode("c");
		Iterator<String> itr = test.listNodes();
		String curr = null;
		String prev;
		while(itr.hasNext()) {
			prev = curr;
			curr = itr.next();
			assertTrue(prev==null || curr.compareTo(prev) > 0);
		}
	}
	
	@Test
	public void testListChildren() {
		GraphWrapper test = new GraphWrapper();
		test.addNode("a");
		test.addNode("b");
		test.addEdge("a","b","b");
		test.addEdge("a","b","b");
		test.addEdge("a","b","a");
		test.addEdge("a","a","b");
		Iterator<String> itr = test.listChildren("a");
		String curr = null;
		String prev;
		while(itr.hasNext()) {
			prev = curr;
			curr = itr.next();
			assertTrue(prev==null || curr.compareTo(prev) >= 0);
		}
	}
}