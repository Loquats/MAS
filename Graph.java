public class Graph {
	private boolean[][] adjMatrix;

	public Graph(int size) {
		// Initialized to false
		adjMatrix = new boolean[size][size];
	}

	/**
	* Create and edge from i to j
	*/
	public void edge(int i, int j) {
		adjMatrix[i][j] = true;
	}

	public boolean hasEdge(int i, int j) {
		return adjMatrix[i][j];
	}

	public int size() {
		return adjMatrix.length;
	}

	public void printGraph() {
		for (int i = 0; i < adjMatrix.length; i++) {
		    for (int j = 0; j < adjMatrix[i].length; j++) {
		    	System.out.print(adjMatrix[i][j] + " ");
		    }
		    System.out.println();
		}
	}
}