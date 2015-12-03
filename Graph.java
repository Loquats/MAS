
import java.util.*;

public class Graph {
	private HashMap<Integer, Set<Integer>> children = new HashMap<Integer, Set<Integer>>();
	private HashMap<Integer, Set<Integer>> parents = new HashMap<Integer, Set<Integer>>();
	private int size;
	private int numEdges = 0;

	public Graph(int size) {
		for (int i = 0; i < size; i++) {
			children.put(i, new HashSet<Integer>());
		}

		for (int i = 0; i < size; i++) {
			parents.put(i, new HashSet<Integer>());
		}
		this.size = size;
	}

	public Set<Integer> getVertices() {
		return children.keySet();
	}

	public void removeVertex(int vertex) {
		for (int parent: parents.get(vertex)) {
			children.get(parent).remove(vertex);
			numEdges--;
		}

		for (int child: children.get(vertex)) {
			parents.get(child).remove(vertex);
			numEdges--;
		}

		children.remove(vertex);
		parents.remove(vertex);
		size--;
	}

	/**
	* Create edge from i to j
	*/
	public void addEdge(int i, int j) {
		children.get(i).add(j);
		parents.get(j).add(i);
		numEdges++;
	}

	/**
	* Remove edge from i to j
	* DOES NOT REMOVE j to i
	*/
	public void removeEdge(int i, int j) {
		children.get(i).remove(j);
		parents.get(j).remove(i);
		numEdges--;
	}

	public boolean hasEdge(int i, int j) {
		return children.get(i).contains(j);
	}

	public int numVertices() {
		return size;
	}

	public int numEdges() {
		return numEdges;
	}

	public Set<Integer> getChildren(int vertex) {
		return children.get(vertex);
	}

	public Set<Integer> getParents(int vertex) {
		return parents.get(vertex);
	}

	public int indegree(int vertex) {
		return parents.get(vertex).size();
	}

	public int outdegree(int vertex) {
		return children.get(vertex).size();
	}

	public void printMatrix() {
		for (int i = 0; i < size; i++) {
		    for (int j = 0; j < size; j++) {
		    	if (children.get(i).contains(j))
		    		System.out.print("1 ");
		    	else
		    		System.out.print("0 ");
		    }
		    System.out.println();
		}
	}

	public void printAdjacency() {
		for (int i = 0; i < size; i++) {
			System.out.print("" + i + ": ");
		    for (Integer j: children.get(i)) {
		    	System.out.print(j + " ");
		    }
		    System.out.println();
		}
	}
}