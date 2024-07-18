package hw7;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import hw4.GraphDLM;
import java.util.HashMap;

public class CampusParserTest {
	
	@Test
	void testReadData() throws IOException {
		// init data structures for function
		MapNode testClear = new MapNode("clear","clear",1.0,1.0);
		GraphDLM<MapNode,Double> graph = new GraphDLM<MapNode,Double>();
		graph.addNode(testClear);
		HashMap<String,MapNode> buildingNames = new HashMap<String,MapNode>();
		buildingNames.put("clear", testClear);
		HashMap<String,MapNode> buildingIDs = new HashMap<String,MapNode>();
		buildingIDs.put("clear",testClear);
		// null argument tests
		assertThrows(IllegalArgumentException.class, () -> 
			{CampusParser.readData(null,"data/HW7SimpleEdges.csv",graph,buildingNames,buildingIDs);});
		assertThrows(IllegalArgumentException.class, () -> 
			{CampusParser.readData("data/HW7SimpleNodes.csv",null,graph,buildingNames,buildingIDs);});
		assertThrows(IllegalArgumentException.class, () -> 
			{CampusParser.readData("data/HW7SimpleNodes.csv","data/HW7SimpleEdges.csv",null,buildingNames,buildingIDs);});
		assertThrows(IllegalArgumentException.class, () -> 
			{CampusParser.readData("data/HW7SimpleNodes.csv","data/HW7SimpleEdges.csv",graph,null,buildingIDs);});
		assertThrows(IllegalArgumentException.class, () -> 
			{CampusParser.readData("data/HW7SimpleNodes.csv","data/HW7SimpleEdges.csv",graph,buildingNames,null);});
		// invalid file name tests
		assertThrows(IOException.class, () -> 
			{CampusParser.readData("FAKENAME.csv","data/HW7SimpleEdges.csv",graph,buildingNames,buildingIDs);});
		assertThrows(IOException.class, () -> 
			{CampusParser.readData("data/HW7SimpleNodes.csv","FAKENAME.csv",graph,buildingNames,buildingIDs);});
		// invalid file format tests
		assertThrows(IOException.class, () -> 
			{CampusParser.readData("data/HW7WrongNodeFormat.csv","data/HW7SimpleEdges.csv",graph,buildingNames,buildingIDs);});
		assertThrows(IOException.class, () -> 
			{CampusParser.readData("data/HW7SimpleNodes.csv","data/HW7WrongEdgeFormat.csv",graph,buildingNames,buildingIDs);});
		// load simple data
		CampusParser.readData("data/HW7SimpleNodes.csv","data/HW7SimpleEdges.csv",graph,buildingNames,buildingIDs);
		// ensure data structures are cleared
		assertFalse(graph.containsNode(testClear));
		assertFalse(buildingNames.containsKey("clear"));
		assertFalse(buildingNames.containsKey("clear"));
		// graph correctness testing
		MapNode a = new MapNode("a","1",1.0,1.0);
		MapNode intersection = new MapNode(null,"2",1.0,2.0);
		MapNode c = new MapNode("c","3",2.0,2.0);
		MapNode d = new MapNode("d","4",2.0,1.0);
		assertTrue(graph.numNodes()==4);
		assertTrue(graph.containsNode(a));
		assertTrue(graph.containsNode(intersection));
		assertTrue(graph.containsNode(c));
		assertTrue(graph.containsNode(d));
		assertTrue(graph.containsEdge(a, intersection, 1.0));
		assertTrue(graph.containsEdge(intersection, a, 1.0));
		assertTrue(graph.containsEdge(intersection, c, 1.0));
		assertTrue(graph.containsEdge(c, intersection, 1.0));
		assertTrue(graph.containsEdge(c, d, 1.0));
		assertTrue(graph.containsEdge(d, c, 1.0));
		assertTrue(graph.containsEdge(d, a, 1.0));
		assertTrue(graph.containsEdge(a, d, 1.0));
		// buildingNames correctness testing
		assertTrue(buildingNames.keySet().size()==3);
		assertTrue(buildingNames.containsKey("a"));
		assertTrue(buildingNames.containsKey("c"));
		assertTrue(buildingNames.containsKey("d"));
		// buildingIDs correctness testing
		assertTrue(buildingNames.keySet().size()==3);
		assertTrue(buildingIDs.containsKey("1"));
		assertTrue(buildingIDs.containsKey("3"));
		assertTrue(buildingIDs.containsKey("4"));
	}
	
}