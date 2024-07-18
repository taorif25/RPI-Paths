package hw6;

//QueueItem class allows node data to be easily stored in PriorityQueues.
//The class acts as a struct. member variables are public and exist solely to store data.
//This allows for easy modification of values.
public class QueueItem<T> implements Comparable<QueueItem<T>> {
	
	// stores the name of the node, the name of the node's parent,
	// the weight of the edge from parent to node, and
	// the total weight of the path from source to node.
	public Double edgeWeight;
	public Double pathWeight;
	public T node;
	public T parent;
	
	QueueItem(T n, T p, Double ew, Double pw) {
		node = n;
		parent = p;
		edgeWeight = ew;
		pathWeight = pw;
	}
	
	// allows for PriorityQueue to sort by path weight
	@Override
	public int compareTo(QueueItem<T> a) {
		return pathWeight.compareTo(a.pathWeight);
	}
	
	// allows for removal of nodes by name from PriorityQueue
	@Override
	public boolean equals(Object a) {
		if (!(a instanceof QueueItem)) return false;
		return node.equals(((QueueItem<?>)a).node);
	}
	// allows for hash container functionality
	@Override
	public int hashCode() {
		return node.hashCode();
	}
}