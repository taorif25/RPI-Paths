package hw4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class GraphWrapper {
	
	
	private GraphDLM<String,String> graph;
	
    /**
     * @requires none
     * @param none
     * @modifies graph
     * @effects calls default constructor for graph
     * @returns none
     * @throws none
     */
	public GraphWrapper() {
		graph = new GraphDLM<String,String>();
	}
	
    /**
     * @requires none
     * @param nodeData the node to be added to graph
     * @modifies graph
     * @effects calls addNode(nodeData) for graph
     * @returns none
     * @throws IllegalArgumentException if (nodeData == null)
     */
	public void addNode(String nodeData) {
		graph.addNode(nodeData);
	}
	
	/**
     * @requires none
     * @param parentNode the source of the edge to be added
     * @param childNode the destination of the edge to be added
     * @param edgeLabel the label of the edge to be added
     * @modifies graph
     * @effects calls addEdge(parentNode,childNode,edgeLabel for graph
     * @returns none
     * @throws IllegalArgumentException if (parentNode == null) || (childNode == null) || (edgeLabel == null)
     * @throws IllegalArgumentException if parentNode or childNode are not present in the graph
     */
	public void addEdge(String parentNode, String childNode, String edgeLabel) {
		graph.addEdge(parentNode, childNode, edgeLabel);
	}
	
	/**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns an iterator to a sorted ArrayList of graph's nodes
     * @throws none
     */
	public Iterator<String> listNodes() {
		ArrayList<String> nodes = graph.getNodes();
		Collections.sort(nodes);
		return nodes.iterator();
	}
	
	/**
     * @requires none
     * @param parentNode the parent for the returned children nodes
     * @modifies none
     * @effects none
     * @returns returns an iterator to the children of parentNode
     *          formatted as childNode(edgeLabel) and sorted by childNode
     *          and then edgeLabel
     * @throws IllegalArgumentException if (parentNode == null)
     * @throws IllegalArgumentException if parentNode is not in graph
     */
	public Iterator<String> listChildren(String parentNode) {
		TreeMap<String, PriorityQueue<String> > sorted = new TreeMap<String, PriorityQueue<String> >(graph.getChildren(parentNode));
		ArrayList<String> formatted = new ArrayList<String>();
		for (Entry<String, PriorityQueue<String>>  entry : sorted.entrySet()) {
        	while(!entry.getValue().isEmpty()) {
        		formatted.add(entry.getKey() + "(" + entry.getValue().remove() + ")");
        	}
        }
		return formatted.iterator();
	}
}