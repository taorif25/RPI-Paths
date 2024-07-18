package hw7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import hw4.GraphDLM;


// BuildingParser is used to read building data from files containing
// node information and edge information.

public class CampusParser {
	
	// BuildingParser is NOT an ADT
	// BuildingParser is part of the model
	
	
	/**
	 * @requires none
	 * @param: nodeFileName    the name of the file with Building data
	 * @param: edgeFileName    the name of the file with intersection data
	 * @param: result		   the GraphDLM to be populated with building / intersection data
	 * @param: buildings       the HashMap to map each building an ID to a building object
	 * @modifies: result, buildingNames, buildingIDs
	 * @effects:  buildingNames - keys contain all building names in the node file, associated values
	 * 						      hold a MapNode object with data linked to each name.
	 * 			  buildingIds   - keys contain all building IDs in the node file, associated values
	 * 						      hold a MapNode object with data linked to each ID.
	 * 			  graph         - GraphDLM with all intersections as nodes and edges back and forth between 
	 *                            intersections with paths between them. Edge weight is the distance between 
	 *                            the 2 intersections.
	 * @throws:  IOException  if file names can not be read from or do not follow
	 *                        the proper format.
	 *           IllegalArgumentException if any arguments are null
	 * @returns: none
	 */
	public static void readData(String nodeFileName, String edgeFileName, GraphDLM<MapNode,Double> graph, 
							    HashMap<String,MapNode> buildingNames, HashMap<String,MapNode> buildingIDs)
		throws IOException {
		if (nodeFileName==null||edgeFileName==null||graph==null||buildingNames==null||buildingIDs==null) {
			throw new IllegalArgumentException("Arguments must not be null.");
		}
		graph.clear();
		buildingNames.clear();
		buildingIDs.clear();
		HashMap<String,MapNode> intersections = new HashMap<String,MapNode>();
		// attempts to read from nodeFile
		try (BufferedReader reader = new BufferedReader(new FileReader(nodeFileName))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				int i1 = line.indexOf(",");
				int i2 = line.indexOf(",",i1+1);
				int i3 = line.indexOf(",",i2+1);
				if ( i1==-1 || i2==-1 || i3==-1 ) {
					throw new IOException("File " + nodeFileName + " not a CSV (name,id,x,y) file.");
				}
				String name = line.substring(0, i1);
				String id = line.substring(i1 + 1, i2);
				Double x = Double.valueOf(line.substring(i2+1,i3));
				Double y = Double.valueOf(line.substring(i3+1,line.length()));
				MapNode node;
				if (!name.equals("")) {
					node = new MapNode(name,id,x,y);
					buildingNames.put(name,node);
					buildingIDs.put(id, node);
				}
				else node = new MapNode(null,id,x,y);
				intersections.put(id,node);
				graph.addNode(node);
			}
		}
		// attempts to read from edgeFile
		try (BufferedReader reader = new BufferedReader(new FileReader(edgeFileName))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf(",");
				if (i == -1) {
					throw new IOException("File " + edgeFileName + " not a CSV (id1,id2) file.");
				}
				MapNode node1 = intersections.get(line.substring(0, i));
				MapNode node2 = intersections.get(line.substring(i + 1, line.length()));
				if (node1==null || node2==null) {
					throw new IOException("Invalid IDs in edge file");
				}
				Double xFactor = node1.getX() - node2.getX();
				xFactor = xFactor * xFactor;
				Double yFactor = node1.getY() - node2.getY();
				yFactor = yFactor * yFactor;
				Double weight = Math.sqrt(xFactor + yFactor);
				graph.addEdge(node1, node2, weight);
				graph.addEdge(node2, node1, weight);
			}
		}
	}
}