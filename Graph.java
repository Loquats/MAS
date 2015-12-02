import java.util.Set;
import java.util.HashSet;

public class Graph {
	private Set<Integer>[] adjacent;
	private int[] pre;
	private int[] post;
	private int size;

	public Graph(int size) {
		adjacent = new HashSet[size];
		for (int i = 0; i < size; i++) {
			adjacent[i] = new HashSet<Integer>();
		}

		pre = new int[size];
		post = new int[size];
		this.size = size;
	}

	/**
	* Create and edge from i to j
	*/
	public void edge(int i, int j) {
		adjacent[i].add(j);
	}

	public boolean hasEdge(int i, int j) {
		return adjacent[i].contains(j);
	}

	public int size() {
		return size;
	}
	
	public void printMatrix() {
		for (int i = 0; i < size; i++) {
		    for (int j = 0; j < size; j++) {
		    	if (adjacent[i].contains(j))
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
		    for (Integer j: adjacent[i]) {
		    	System.out.print(j + " ");
		    }
		    System.out.println();
		}
	}
}