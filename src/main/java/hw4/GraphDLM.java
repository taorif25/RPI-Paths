package hw4;

import java.util.Iterator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.ArrayList;

/*
 * graphDLM represents a directed labeled multi-graph.
 * GraphDLMs are mutable.
 * A GraphDLM consists of a set of nodes and a set of edges,
 * where each node is a unique N object and each edge connects two
 * nodes that are present in the graph. An edge can start and end
 * at the same node. Additionally, each edge is labeled with an
 * E object
 */

public class GraphDLM<N,E> {

    private HashMap<N, HashMap<N, PriorityQueue<E> > > data;

    /*
    * Representation Invariant:
    * 
    * data != null &&
    * 
    * For all keys k in data.keySet():
    * 
    * 		* k != null
    * 
    * 		* data[k] != null
    * 
    * 		* data[k].keySet() is a subset of data.KeySet()
    * 
    * 		* For all keys k2 in data[k].keySet():
    * 
    * 				* data[k][k2] != null
    * 
    * 				* data[k][k2] is not empty
    */

    /*
    * Abstraction Function:
    * 
    * Each key u in data.keySet() represents a node in the graph.
    * 
    * Each key v in data[u].keySet() represents a set of edges from node u to node v in the graph.
    * 
    * The elements in data[u][v] represent the labels associated with the set of edges
    * going from parent u to child v in descending weight order.
    */

    /**
     * @requires none
     * @param none
     * @modifies this.data
     * @effects initializes this.data as an empty HashMap<N, HashMap<N, PriorityQueue<E> > >
     * @returns a new GraphDLM object with data initialized as an empty HashMap.
     * @throws none
     */
    public GraphDLM() {
        data = new HashMap<N, HashMap<N, PriorityQueue<E> > >();
        //checkRep();
    }
    
    /**
     * @requires none
     * @param nodes an ArrayList of nodes to be added to the graph
     * @modifies this.data
     * @effects initializes this.data as a HashMap<N, HashMap<N, PriorityQueue<E> > >
     * 			with the distinct elements of nodes as keys and empty initialized values.
     * @returns a new GraphDLM object with data initialized as a graph containing distinct elements of nodes.
     * @throws IllegalArgumentException if nodes == null or any element of nodes == null
     */
    public GraphDLM(ArrayList<N> nodes) {
    	if (nodes == null) throw new IllegalArgumentException("Null Argument");
        data = new HashMap<N, HashMap<N, PriorityQueue<E> > >();
        for (int i=0; i < nodes.size(); i++) {
        	if (nodes.get(i)==null) throw new IllegalArgumentException("Null Argument");
        	data.put(nodes.get(i), new HashMap<N, PriorityQueue<E> >());
        }
        checkRep();
    }
    
    /**
     * Checks that the representation invariant holds.
     **/
    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
        if (data==null) throw new RuntimeException("data must be non null");
        for (Entry<N, HashMap<N, PriorityQueue<E>>>  entry : data.entrySet()) {
        	if (entry.getKey()==null) throw new RuntimeException("keys of data must be non null");
        	if (entry.getValue()==null) throw new RuntimeException("values of data must be non null");
        	for (Entry<N, PriorityQueue<E>> subEntry : entry.getValue().entrySet()) {
        		if (subEntry.getKey()==null) throw new RuntimeException("keys of data[k] must be non null");
        		if (!data.containsKey(subEntry.getKey())) throw new RuntimeException("data[k] must be a subset of data");
        		if (subEntry.getValue()==null) throw new RuntimeException("data[k][k2] must be non null");
        		if (subEntry.getValue().isEmpty()) throw new RuntimeException("Edge queues must be non empty");
        		Iterator<E> itr = subEntry.getValue().iterator();
        		while (itr.hasNext()) {
        			if (itr.next()==null) throw new RuntimeException("Queue elements must be non null");
        		}
        	}
        }
    }

    /**
     * @requires none
     * @param node the node to be added
     * @modifies self.data
     * @effects if data[node] does not exist, adds it as a new empty HashMap
     * @returns true if node is not present in data already, false otherwise
     * @throws IllegalArgumentException if (node == null)
     */
    public boolean addNode(N node) {
    	if (node==null) {
    		//checkRep();
    		throw new IllegalArgumentException("Null Argument");
    	}
        if (data.containsKey(node)) {
        	//checkRep();
        	return false;
        }
        data.put(node, new HashMap<N, PriorityQueue<E> >());
        //checkRep();
        return true;
    }

    /**
     * @requires none
     * @param node the node to be removed
     * @modifies self.data
     * @effects if data[node] exists, removes data[node] and for all other nodes i, removes data[i][node]
     * @returns none
     * @throws IllegalArgumentException if (node == null)
     * @throws IllegalArgumentException if node is not in graph
     */
    public void removeNode(N node) {
    	if (node==null) {
    		//checkRep();
    		throw new IllegalArgumentException("Null Argument");
    }
        if (!data.containsKey(node)) {
        	//checkRep();
        	throw new IllegalArgumentException("Node is not in graph");
        }
        data.remove(node);
        for (HashMap.Entry<N, HashMap<N, PriorityQueue<E> > > entry : data.entrySet()) {
        	entry.getValue().remove(node);
        }
        //checkRep();
    }

    /**
     * @requires none
     * @param node the node to be searched for in the graph
     * @modifies none
     * @effects none
     * @returns true if data[node] exists, else false
     * @throws IllegalArgumentException if (node==null)
     */
    public boolean containsNode(N node) {
    	if (node==null) throw new IllegalArgumentException("Null Argument");
        if (data.containsKey(node)) return true;
        else return false;
    }

    /**
     * @requires none
     * @param source the source of the edge to be added
     * @param dest the destination of the edge to be added
     * @param label the label of the edge to be added
     * @modifies self.data
     * @effects If data[source][dest] exists, adds value label to its PriorityQueue. 
     *          Else, creates new queue with value label at data[source][dest]
     * @returns none
     * @throws IllegalArgumentException if (source == null) || (dest == null) || (label == null)
     * @throws IllegalArgumentException if source or dest are not present in the graph
     */
    public void addEdge(N source, N dest, E label) {
    	if (source==null||dest==null||label==null) {
    		//checkRep();
    		throw new IllegalArgumentException("Null Argument");
    	}
        if (!(data.containsKey(source) && data.containsKey(dest))) {
        	//checkRep();
        	throw new IllegalArgumentException("Node is not present in graph");
        }
        HashMap<N, PriorityQueue<E> > edges = data.get(source);
        if (!edges.containsKey(dest)) edges.put(dest, new PriorityQueue<E>());
        edges.get(dest).add(label);
        //checkRep();
    }

    /**
     * @requires none
     * @param source the source of the edge to be removed
     * @param dest the destination of the edge to be removed
     * @param label the label of the edge to be removed
     * @modifies self.data
     * @effects if data[source][dest] exists and label is present in data[source][dest], 
     * 			removes one instance of label from data[source][dest]
     * @returns none
     * @throws IllegalArgumentException if (source == null) || (dest == null) || (label == null)
     * @throws IllegalArgumentException if data[source][dest] does not exist or label is not present in data[source][dest]
     */
    public void removeEdge(N source, N dest, E label) {
    	if (source==null||dest==null||label==null) {
    		//checkRep();
    		throw new IllegalArgumentException("Null Argument");
    	}
        if (!data.containsKey(source) || !data.get(source).containsKey(dest)) {
        	//checkRep();
        	throw new IllegalArgumentException("Invalid Edge");
        }
        if (!data.get(source).get(dest).remove(label)) {
        	//checkRep();
        	throw new IllegalArgumentException("Invalid Label");
        }
        if (data.get(source).get(dest).isEmpty()) data.get(source).remove(dest);
        //checkRep();
    }
    
    /**
     * @requires none
     * @param source the source of the edge to be found
     * @param dest the destination of the edge to be found
     * @modifies none
     * @effects none
     * @returns The minimum edge label between source and dest
     * @throws IllegalArgumentException if (source == null) || (dest == null)
     * @throws IllegalArgumentException if data[source][dest] does not exist
     */
    public E getMinEdge(N source, N dest) {
    	if (source==null||dest==null) {
    		//checkRep();
    		throw new IllegalArgumentException("Null Argument");
    	}
        if (!data.containsKey(source) || !data.get(source).containsKey(dest)) {
        	//checkRep();
        	throw new IllegalArgumentException("Invalid Edge");
        }
        return data.get(source).get(dest).element();
    }
    
    /**
     * @requires none
     * @param source the source of the edge to be removed
     * @param dest the destination of the edge to be removed
     * @modifies this.data
     * @effects removes the minimum label edge from source to dest
     * @returns none
     * @throws IllegalArgumentException if (source == null) || (dest == null)
     * @throws IllegalArgumentException if data[source][dest] does not exist
     */
    public void removeMinEdge(N source, N dest) {
    	if (source==null||dest==null) {
    		//checkRep();
    		throw new IllegalArgumentException("Null Argument");
    	}
        if (!data.containsKey(source) || !data.get(source).containsKey(dest)) {
        	//checkRep();
        	throw new IllegalArgumentException("No Edges Exist Between Given Nodes.");
        }
        data.get(source).get(dest).remove();
        if (data.get(source).get(dest).isEmpty()) data.get(source).remove(dest);
    }

    /**
     * @requires none
     * @param source the source of the edge to be found
     * @param dest the destination of the edge to be found
     * @param label the label of the edge to be found
     * @modifies none
     * @effects none
     * @returns true if data[source][dest] exists and label is present in data[source][dest], false otherwise
     * @throws IllegalArgumentException if (source == null) || (dest == null) || (label == null)
     */
    public boolean containsEdge(N source, N dest, E label) {
    	if (source==null||dest==null||label==null) throw new IllegalArgumentException("Null Argument");
    	if (!data.containsKey(source) || !data.get(source).containsKey(dest)) return false;
        if (!data.get(source).get(dest).contains(label)) return false;
        return true;
    }

    /**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns An iterator to the keySet() of self.data
     * @throws none
     */
    public Iterator<N> iterator() {
    	return data.keySet().iterator();
    }
    
    /**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns An array list containing all elements in the keySet() of data.
     * @throws none
     */
    public ArrayList<N> getNodes() {
    	return new ArrayList<N>(data.keySet());
    }

        /**
     * @requires none
     * @param parent The node whose children will be returned
     * @modifies none
     * @effects none
     * @returns a copy of data[parent] if parent is present in data
     * @throws IllegalArgumentException if (parent == null)
     * @throws IllegalArgumentException if data[parent] does not exist
     */
    public HashMap<N, PriorityQueue<E> > getChildren(N parent) {
    	if (parent==null) throw new IllegalArgumentException("Null Argument");
    	if (!data.containsKey(parent)) throw new IllegalArgumentException("Invalid Parent Node");
    	else return new HashMap<N, PriorityQueue<E> >(data.get(parent));
    }
    
    /**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns the number of nodes in the graph
     */
    public int numNodes() {
    	return data.keySet().size();
    }
    
    /**
     * @requires none
     * @param none
     * @modifies none
     * @effects none
     * @returns the number of edges in the graph
     */
    public int numEdges() {
    	int edges = 0;
    	for (Entry<N, HashMap<N, PriorityQueue<E>>> entry : data.entrySet()) {
    		for (Entry<N, PriorityQueue<E>> entry2 : entry.getValue().entrySet()) {
    			edges = edges + entry2.getValue().size();
    		}
    	}
    	return edges;
    }
    
    /**
     * @requires none
     * @param none
     * @modifies this.date
     * @effects re initializes data as an empty data structure
     * @returns none
     */
    public void clear() {
    	data = new HashMap<N,HashMap<N,PriorityQueue<E>>>();
    }
}