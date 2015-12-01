import java.io.*;
import java.util.StringTokenizer;

public class MAS { 
	public static void main(String[] args) throws IOException{
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
		    		g.edge(i, j);
		    	}
		    }
		}

		// g.printGraph();

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
		int backwardSize = computeBackwardSize(g, indexToVertex, vertexToIndex);
		System.out.println(forwardSize + " " + backwardSize);
	}

	// TODO: optimize this
	public static int computeForwardSize(Graph g, int[] indexToVertex, int[] vertexToIndex) {
		int size = 0;
		for (int i = 0; i < g.size(); i++) {
		    for (int j = 0; j < g.size(); j++) {
		    	if (g.hasEdge(i, j) && vertexToIndex[i] < vertexToIndex[j]) {
		    		size++;
		    	}
		    }
		}
		return size;
	}

	public static int computeBackwardSize(Graph g, int[] indexToVertex, int[] vertexToIndex) {
		int size = 0;
		for (int i = 0; i < g.size(); i++) {
		    for (int j = 0; j < g.size(); j++) {
		    	if (g.hasEdge(i, j) && vertexToIndex[i] > vertexToIndex[j]) {
		    		size++;
		    	}
		    }
		}
		return size;
	}
}