import java.io.*;
import java.util.*;

public class MAS { 
	public static void main(String[] args) throws IOException {
		String inputFileName = args[0];
		BufferedReader f = new BufferedReader(new FileReader(inputFileName));
		int numNodes = Integer.parseInt(f.readLine());
		Graph g = new Graph(numNodes);

		StringTokenizer line;
		for (int i = 0; i < numNodes; i++) {
		    line = new StringTokenizer(f.readLine());
		    for (int j = 0; j < numNodes; j++) {
		    	int e = Integer.parseInt(line.nextToken());
		    	if (e == 1) {
		    		// Optimization: Remove two-way edges between nodes
		    		if (g.hasEdge(j, i)) {
		    			g.removeEdge(j, i);
		    		} else {
		    			g.addEdge(i, j);
		    		}
		    	}
		    }
		}

		g.printMatrix();
		g.printAdjacency();

		// Pop off sources and sinks to try to linearize


		// Preprocess
		// write getDisjoint(G) which returns List<Graph>
		List<Graph> disjointGraphs = getDisjoint(g);

		for (Graph subgraph: disjointGraphs) {
			// Process independently
		}

		Randp r = new Randp(numNodes);
		int[] indexToVertex = new int[numNodes];
		int[] vertexToIndex = new int[numNodes];
		int v;
		for (int i = 0; i < numNodes; i++) {
			v = r.nextInt();
			indexToVertex[i] = v;
			vertexToIndex[v] = i;
		}

		for (int i = 0; i < numNodes; i++) {
			System.out.print(indexToVertex[i] + " ");
		}
		System.out.println();

		int forwardSize = computeForwardSize(g, indexToVertex, vertexToIndex);
		int backwardSize = g.numEdges() - forwardSize;
		System.out.println(forwardSize + " " + backwardSize);
	}

	// TODO: optimize this
	public static int computeForwardSize(Graph g, int[] indexToVertex, int[] vertexToIndex) {
		int size = 0;
		for (int i = 0; i < g.numVertices(); i++) {
			for (Integer j: g.getChildren(i)) {
				if (vertexToIndex[i] < vertexToIndex[j]) {
		    		size++;
		    	}
			}
		}
		return size;
	}

	public static List<Graph> getDisjoint(Graph g) {
		return null;
	}
}