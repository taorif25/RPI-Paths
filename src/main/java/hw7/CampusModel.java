package hw7;

import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;

import hw4.GraphDLM;
import hw6.*;


// BuildingPaths stores parsed building data in a GraphDLM.
// Allows access to Building data through getter methods
public class CampusModel {
	
	// BuildingPaths is NOT an ADT
	// BuildingPaths is part of the model
	
	// graph stores Building data
	private GraphDLM<MapNode,Double> graph;
	private HashMap<String,MapNode> buildingNames;
	private HashMap<String,MapNode> buildingIDs;
	
	
	
	/**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns a new BuildingPaths object with data read from 
     * 			"data/RPImapdataNodes.csv", "data/RPImapdataEdges.csv"
     * @throws IOException if the node or edge files cannot be read from
     */
	public CampusModel() throws IOException {
		graph = new GraphDLM<MapNode,Double>();
		buildingNames = new HashMap<String,MapNode>();
		buildingIDs = new HashMap<String,MapNode>();
		CampusParser.readData("data/RPI_map_data_Nodes.csv","data/RPI_map_data_Edges.csv",graph,buildingNames,buildingIDs);
	}
	
	/**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns an iterator of MapNodes traversing the buildings of RPI in alphabetical order.
     * @throws none
     */
	public Iterator<MapNode> getBuildings() {
		ArrayList<MapNode> buildings = new ArrayList<MapNode>(buildingNames.values());
		Collections.sort(buildings);
		return buildings.iterator();
	}
	
	/**
     * @requires none
     * @param nodeStr  The name or ID associated with the building being searched for.
     * @modifies none
     * @effects none
     * @returns The name of the building associated with nodeStr if such a building exists.
     * @throws IllegalArgumentException if no building exists with name or ID nodeStr
     */
	public String getName(String nodeStr) {
		MapNode building = buildingIDs.get(nodeStr);
		if (building==null) {
			if (buildingNames.containsKey(nodeStr)) {
				return nodeStr;
			}
			else throw new IllegalArgumentException("Not a valid building name or ID");
		}
		return building.getName();
	}
		
	/**
     * @requires none
     * @param sourceName  the name of the source Building
     * @param destName    the name of the destination Building
     * @modifies none
     * @effects none
     * @returns an Iterator traversing the shortest path from 
     * 			building associated with sourceName to building
     *          associated with destName.
     * 			returns a blank list if no such path exists.
     * @throws IllegalArgumentException if sourceName or destName are null
     *  	   or if Buildings do not exist with IDs or names sourceName or destName.
     */
	public Iterator<QueueItem<MapNode>> findPath(String sourceStr, String destStr) {
		if (sourceStr==null || destStr==null) throw new IllegalArgumentException("Arguments must be non null");
		// checks if strings are valid building names
		MapNode source = buildingNames.get(sourceStr);
		MapNode dest = buildingNames.get(destStr);
		if (source==null || dest==null) throw new IllegalArgumentException("Invalid building names");
		return LegoPaths.Dijkstra(graph, source, dest).iterator();
	}
}