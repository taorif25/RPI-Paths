package hw5;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import hw4.GraphDLM;

public class ProfessorPaths {
	
	public GraphDLM<String,String> graph;
	
	/**
	 * @param: filename     The path to a "CSV" file that contains the
	 *                      "professor","course" pairs
	 * @modifies: graph
	 * @effects: adds all professors as nodes to the graph. Adds edges back and forth
	 * 			 between professors who have taught the same course. These edges are labeled
	 * 			 with that course's title.
	 * @throws:  IllegalArgumentException if filename can not be read or file is not a CSV
	 * 									  file following the proper format
	 * @returns: None
	 */
	public void createNewGraph(String filename) {
		// initializes data structures
		Map<String, ArrayList<String>> profsTeaching = new HashMap<String, ArrayList<String>>();
		Set<String> profs = new HashSet<String>();
		// attempts file reading
		try {
			ProfessorParser.readData(filename, profsTeaching, profs);
			// initializes graph
			graph = new GraphDLM<String,String>();
			Iterator<String> itr = profs.iterator();
			// adds all nodes to graph
			while(itr.hasNext()) {
				graph.addNode(itr.next());
			}
			// adds all edges to graph
			for (Entry<String, ArrayList<String>>  entry : profsTeaching.entrySet()) {
				ArrayList<String> teaching = entry.getValue();
				for (int i=0; i < teaching.size(); i++) {
					for (int j=i+1; j < teaching.size(); j++) {
						graph.addEdge(teaching.get(i), teaching.get(j), entry.getKey());
						graph.addEdge(teaching.get(j), teaching.get(i), entry.getKey());
					}
				}
			}
		} catch(IOException e) {
			throw new IllegalArgumentException("Invalid File");
		}
	}
	
	/**
	 * @param: node1     The source node to begin the path search from
	 * @param: node2	 The destination node for the path search
	 * @modifies: none
	 * @effects: none
	 * @throws:  IllegalArgumentException if node1==null or node2==null
	 * 
	 * @returns: "unknown professor "+node1+"\n"  if neither node1 nor node2
	 * 											  are in graph and the contents
	 * 											  of node1 and node2 are the same.
	 * 
	 * 			 "unknown professor "+node1+"\n"  else if neither node1 nor node2 are
	 * 			 "unknown professor "+node2+"\n"  present in graph.
	 * 
	 * 			 "unknown professor "+node1+"\n" else if node1 is not present in graph
	 * 
	 * 			 "unknown professor "+node2+"\n" else if node2 is not present in graph
	 * 
	 * 			 "path from " + node1 + " to "   else if node1.equals(node2)
	 * 			 + node2 + ":\n"
	 * 
	 * 			 "path from " + node1 + " to "   else if no path exists between node1
	 * 			 + node2 + ":\nno path found\n"  and node2 in the graph
	 * 
	 * 			 "path from " + node1 + " to "         else,
	 * 			 + node2 + ":\n"+
	 * 			 "node1 to subdest(1) via label(1)\n   where the shortest path from node1
	 * 			 ...								   to node2 has length n and each line
	 * 			 subdest(n) to node2 via label(n)\n" represents a step in the 
	 * 												   lexicographically lowest
	 * 												   shortest path.
	 */
	public String findPath(String node1, String node2) {
		if (node1==null || node2==null) {
			throw new IllegalArgumentException("Arguments must be non null");
		}
		// checks for unknown professors
		String answer = "";
		boolean unknown = false;
		if (!graph.containsNode(node1)) {
			answer = "unknown professor " + node1 + "\n";
			unknown = true;
		}
		if (!graph.containsNode(node2) && !node1.equals(node2)) {
			answer = answer + "unknown professor " + node2 + "\n";
			unknown = true;
		}
		if (unknown) return answer;
		// initializes answer string
		answer = "path from " + node1 + " to " + node2 + ":\n";
		// case if source is same as dest
		if (node1.equals(node2)) return answer;
		ArrayList<String> queue = new ArrayList<String>();
		queue.add(node1);
		//keys of first level represent found nodes, key value pairs in the LinkedHashMap represent node, edge  label 
		HashMap<String, LinkedHashMap<String, String>> paths = new HashMap<String, LinkedHashMap<String,String>>();
		// add empty path for source
		paths.put(node1, new LinkedHashMap<String,String>());
		while (!queue.isEmpty()) {
			String curr = queue.removeFirst();
			// alphabetically sorts children
			TreeMap<String, PriorityQueue<String> > sorted = 
					new TreeMap<String, PriorityQueue<String> >(graph.getChildren(curr));
			for (Entry<String, PriorityQueue<String>>  entry : sorted.entrySet()) {
				// iterates through not found children
				if (!paths.containsKey(entry.getKey())) {
					// copies path of parent and adds new node and edge that are traversed
					LinkedHashMap<String, String> newPath = new LinkedHashMap<String, String>(paths.get(curr));
		        	newPath.put(entry.getKey(),entry.getValue().element());
		        	// ends function if node2 is found
		        	if (entry.getKey().equals(node2)) {
		        		// generates output string based on the path found
		        		Iterator<Map.Entry<String, String>> itr = newPath.entrySet().iterator();
		        		String prevNode = node1;
		        		Map.Entry<String,String> currEntry = null;
		        		while (itr.hasNext()) {
		        			if (currEntry!=null) prevNode = currEntry.getKey();
		        			currEntry = itr.next();
		        			answer = answer+prevNode+" to "+currEntry.getKey()+" via "+currEntry.getValue()+"\n";
		        		}
		        		return answer;
		        	}
		        	// adds not found children to paths map and the queue
		        	paths.put(entry.getKey(), newPath);
		        	queue.add(entry.getKey());
				}
	        }
		}
		// case if no path found
		answer = answer + "no path found\n";
		return answer;
	}	

/*
	public static void main(String[] arg) {
		ProfessorPaths A = new ProfessorPaths();
		String file = arg[0];
		A.createNewGraph(file);
		System.out.println("Graph constructed with "+A.graph.numNodes()+" nodes and "+A.graph.numEdges()/2+" edges.\n");
		System.out.println(A.findPath("Mohammed J. Zaki", "Wilfredo Colon"));
		//System.out.println(A.findPath("David Eric Goldschmidt", "Hugh Johnson"));
		//System.out.println(A.findPath("Donald Knuth", "Malik Magdon-Ismail"));
		//System.out.println(A.findPath("Donald Knuth", "Brian Kernighan"));
		//System.out.println(A.findPath("Barbara Cutler", "Barbara Cutler"));
		//System.out.println(A.findPath("Donald Knuth", "Donald Knuth"));
		System.out.println(A.findPath("David Eric Goldschmidt", "Corey Christopher Woodcock"));
		System.out.println(A.findPath("Mattheos Koffas", "Christopher James Fisher-Lochhead"));
		System.out.println(A.findPath("Jennifer Cardinal", "Steven A. Tysoe"));
		System.out.println(A.findPath("Pankaj Karande", "Christopher James Fisher-Lochhead"));
	}*/

}