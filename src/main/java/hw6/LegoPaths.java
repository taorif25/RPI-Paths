package hw6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.LinkedList;

import hw4.GraphDLM;

public class LegoPaths {
	
	public GraphDLM<String,Double> graph;

	
	/**
	 * @param: filename   The path to a "CSV" file that contains the
	 *                    "part","set" pairs
	 * @modifies: graph
	 * @effects: adds all parts as nodes to the graph. Adds edges back and forth
	 * 			 between parts that have appeared in the same set. The label for each
	 * 			 edge is 1.0 / the number of sets the parts have in common.
	 * @throws:  IllegalArgumentException if filename can not be read or file is not a CSV
	 * 									  file following the proper format
	 * @returns: None
	 */
	public void createNewGraph(String filename) {
		// Inits temporary data structures
		HashMap<String, HashSet<String>> sets = new HashMap<String,HashSet<String>>();
		graph = new GraphDLM<String,Double>();
		HashMap<String,HashMap<String,Double>> edges = new HashMap<String,HashMap<String,Double>>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = null;
			// for each part, set, updates data in temp data structures
			while ((line = reader.readLine()) != null) {
				int i = line.indexOf("\",\"");
				if ((i == -1) || (line.charAt(0) != '\"') || (line.charAt(line.length() - 1) != '\"')) {
					throw new IllegalArgumentException("File " + filename + " not a CSV (\"PROFESSOR\",\"COURSE\") file.");
				}
				String part = line.substring(1, i);
				String set = line.substring(i + 3, line.length() - 1);
				// updates data structures
				sets.putIfAbsent(set, new HashSet<String>());
				edges.putIfAbsent(part, new HashMap<String,Double>());
				graph.addNode(part);
				HashSet<String> parts = sets.get(set);
				String currPart;
				HashMap<String,Double> nodeEdges = edges.get(part);
				Double currWeight;
				Iterator<String> itr = parts.iterator();
				// ensures duplicate lines don't skew result.
				if (!parts.contains(part)) {
					// decreases edge weight for parts in the same set as currPart.
					while (itr.hasNext()) {
						currPart = itr.next();
						if ((currWeight = nodeEdges.get(currPart)) != null) {
							Double nextWeight = 1.0 / ((int)(1.0/currWeight)+1);
							nodeEdges.put(currPart,nextWeight);
							edges.get(currPart).put(part,nextWeight);
						}
						else {
							nodeEdges.put(currPart,1.0);
							edges.get(currPart).put(part,1.0);
						}
					}
					parts.add(part);
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to read from file");
		}
		// moves final data into graph
		for (Entry<String, HashMap<String,Double>>  entry : edges.entrySet()) {
			for (Entry<String,Double>  subEntry : entry.getValue().entrySet()) {
				graph.addEdge(entry.getKey(), subEntry.getKey(), subEntry.getValue());
			}
		}
	}
	
	/**
	 * @param: G      the graph to search
	 * @param source  the node to start the search from
	 * @param dest    the node to end the search at
	 * @modifies: none
	 * @effects:  none
	 * @throws:  IllegalArgumentException if source or dest are not in graph
	 * @returns: An ArrayList<T> containing the shortest path from source to dest.
	 * 			 If no path exists, returns and empty ArrayList<T>.
	 */
	public static <T> LinkedList<QueueItem<T>> Dijkstra(GraphDLM<T,Double> G, T source, T dest) {
		if (!G.containsNode(source) || !G.containsNode(dest)) {
			throw new IllegalArgumentException("source and dest must be present in the graph");
		}
		// Initializes data structures for algo
		PriorityQueue<QueueItem<T>> adj = new PriorityQueue<QueueItem<T>>();
		HashMap<T,QueueItem<T>> adjData = new HashMap<T,QueueItem<T>>();
		HashMap<T,QueueItem<T>> known = new HashMap<T,QueueItem<T>>();
		// Init result
		LinkedList<QueueItem<T>> result = new LinkedList<QueueItem<T>>();
		// add source to adj region
		QueueItem<T> sourceItem = new QueueItem<T>(source,null,0.0,0.0);
		adj.add(sourceItem);
		adjData.put(source, sourceItem);
		// iterate until no more adjacent nodes
		while (!adj.isEmpty()) {
			QueueItem<T> min = adj.remove();
			adjData.remove(min.node);
			// case if destination is found
			if (min.node.equals(dest)) {
				QueueItem<T> curr = min;
				while (curr != null) {
					result.addFirst(curr);
					curr = known.get(curr.parent);
				}
				return result;
			}
			// adds min to known region
			known.put(min.node, min);
			HashMap<T,PriorityQueue<Double>> children = G.getChildren(min.node);
			// updates children of min in the queue
			for (Entry<T, PriorityQueue<Double>>  entry : children.entrySet()) {
				if (!known.containsKey(entry.getKey())) {
					QueueItem<T> curr;
					// checks if node is already adj
					if (adjData.containsKey(entry.getKey())) {
						curr = adjData.get(entry.getKey());
						if (curr.pathWeight > min.pathWeight + entry.getValue().peek()) {
							curr.edgeWeight = entry.getValue().peek();
							curr.pathWeight = min.pathWeight + curr.edgeWeight;
							curr.parent = min.node;
							adj.remove(curr);
							adj.add(curr);
						}
					}
					else {
						Double edgeWeight = entry.getValue().peek();
						curr = new QueueItem<T>(entry.getKey(),min.node,edgeWeight,edgeWeight+min.pathWeight);
						adjData.put(entry.getKey(),curr);
						adj.add(curr);
					}
				}
			}
		}
		// search fails, return empty list
		return result;
	}
	
	
	
	
	
	/**
	 * @param: source     The source node to begin the path search from
	 * @param: dest	 The destination node for the path search
	 * @modifies: none
	 * @effects: none
	 * @throws:  IllegalArgumentException if source==null or dest==null
	 * 
	 * @returns: "unknown part "+source+"\n"      if neither source nor dest
	 * 											  are in graph and the contents
	 * 											  of source and dest are the same.
	 * 
	 * 			 "unknown part "+source+"\n"      else if neither source nor dest are
	 * 			 "unknown part "+dest+"\n"        present in graph.
	 * 
	 * 			 "unknown part "+source+"\n"      else if source is not present in graph
	 * 
	 * 			 "unknown part "+dest+"\n"        else if dest is not present in graph
	 * 
	 * 			 "path from " + source + " to "   else if source.equals(dest)
	 * 			 + dest + ":\ntotal cost: 0.000"
	 * 
	 * 			 "path from " + source + " to "   else if no path exists between source
	 * 			 + dest + ":\nno path found\n"    and dest in the graph
	 * 
	 * 			 "path from " + source + " to "              else,
	 * 			 + dest + ":\n"+
	 * 			 "source to subdest(1) with weight w(1)\n    where the shortest path from source
	 * 			 ...								         to dest has length n, subdest(i) represents a node
	 * 			 subdest(n) to dest via with weight w(n)\n   in the path, w(i) is the step's weight rounded 3 decimals,
	 * 			 total cost: TOTAL_WEIGHT\n"                 and TOTAL_WEIGHT is the total weight of the
	 * 														 shortest path to 3 decimals.
	 */
	public String findPath(String source, String dest) {
		if (source==null || dest==null) {
			throw new IllegalArgumentException("Arguments must be non null");
		}
		// checks for unknown parts
		StringBuilder result = new StringBuilder();
		boolean unknown = false;
		if (!graph.containsNode(source)) {
			result = result.append("unknown part ").append(source).append("\n");
			unknown = true;
		}
		if (!graph.containsNode(dest) && !dest.equals(source)) {
			result = result.append("unknown part ").append(dest).append("\n");
			unknown = true;
		}
		if (unknown) return result.toString();
		result.append("path from ").append(source).append(" to ").append(dest).append(":\n");
		// source == dest case
		if (source.equals(dest)) {
			return result.append("total cost: 0.000\n").toString();
		}
		LinkedList<QueueItem<String>> path = Dijkstra(graph, source, dest);
		if  (path.size()==0) return result.append("no path found\n").toString();
		Iterator<QueueItem<String>> itr = path.iterator();
		QueueItem<String> curr = itr.next();
		while (itr.hasNext()) {
			curr = itr.next();
			result = result.append(curr.parent).append(" to ").append(curr.node).append(" with weight ");
			result = result.append(String.format("%.3f", curr.edgeWeight)).append("\n");
		}
		result = result.append("total cost: ").append(String.format("%.3f", curr.pathWeight)).append("\n");
		return result.toString();
	}
}