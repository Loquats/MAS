import java.util.Set;
import java.util.HashSet;

public class Graph {
	private Set<Integer>[] children;
	private Set<Integer>[] parents;
	private int size;
	private int numEdges = 0;

	public Graph(int size) {
		children = new HashSet[size];
		for (int i = 0; i < size; i++) {
			children[i] = new HashSet<Integer>();
		}

		parents = new HashSet[size];
		for (int i = 0; i < size; i++) {
			parents[i] = new HashSet<Integer>();
		}
		this.size = size;
	}

	/**
	* Create edge from i to j
	*/
	public void addEdge(int i, int j) {
		children[i].add(j);
		parents[j].add(i);
		numEdges++;
	}

	/**
	* Remove edge from i to j
	* DOES NOT REMOVE j to i
	*/
	public void removeEdge(int i, int j) {
		children[i].remove(j);
		parents[j].remove(i);
		numEdges--;
	}

	public boolean hasEdge(int i, int j) {
		return children[i].contains(j);
	}

	public int numVertices() {
		return size;
	}

	public int numEdges() {
		return numEdges;
	}

	public Set<Integer> getChildren(int node) {
		return children[node];
	}

	public void printMatrix() {
		for (int i = 0; i < size; i++) {
		    for (int j = 0; j < size; j++) {
		    	if (children[i].contains(j))
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
		    for (Integer j: children[i]) {
		    	System.out.print(j + " ");
		    }
		    System.out.println();
		}
	}
}