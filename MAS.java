import java.io.*;
import java.util.*;

public class MAS { 
	static Graph comp;
	static class vertexComp implements Comparator<Integer>{
 
	    @Override
	    public int compare(Integer v1, Integer v2) {
	        if(comp.outdegree(v2) - comp.indegree(v2) <= comp.outdegree(v1) - comp.indegree(v1)){
	            return 1;
	        } else {
	            return -1;
	        }
	    }
	}


	public static void main(String[] args) throws IOException {
		String inputFileName = args[0];
		BufferedReader f = new BufferedReader(new FileReader(inputFileName));
		int numNodes = Integer.parseInt(f.readLine());
		Graph g = new Graph(numNodes);
		Graph original = new Graph(numNodes);

		StringTokenizer line;
		for (int i = 0; i < numNodes; i++) {
		    line = new StringTokenizer(f.readLine());
		    for (int j = 0; j < numNodes; j++) {
		    	int e = Integer.parseInt(line.nextToken());
		    	if (e == 1) {
		    		original.addEdge(i, j);

		    		// Optimization: Remove two-way edges between nodes
		    		if (g.hasEdge(j, i)) {
		    			g.removeEdge(j, i);
		    		} else {
		    			g.addEdge(i, j);
		    		}
		    	}
		    }
		}
		comp = g;

		g.printMatrix();
		g.printAdjacency();

		int[] optimalOrder = new int[numNodes];
		int sourcePtr = 0;
		int sinkPtr = numNodes - 1;

		// Pop off sources and sinks to try to linearize
		// Sources
		ArrayList<Integer> sources = new ArrayList<Integer>();

		for (int v: g.getVertices()) {
			if (g.indegree(v) == 0) {
				sources.add(v);
			}
		}

		ArrayList<Integer> newSources;
		while (sources.size() != 0) {
			newSources = new ArrayList<Integer>();
			for (int v: sources) {
				for (int child: g.getChildren(v)) {
					if (g.indegree(child) == 1) {
						newSources.add(child);
					}
				}
				g.removeVertex(v);
				optimalOrder[sourcePtr] = v;
				sourcePtr++;
			}
			sources = newSources;
		}
		System.out.print("Sources: ");
		for (int i = 0; i < sourcePtr; i++) {
			System.out.print(optimalOrder[i] + " ");
		}
		System.out.println();

		// Sinks
		ArrayList<Integer> sinks = new ArrayList<Integer>();

		for (int v: g.getVertices()) {
			if (g.outdegree(v) == 0) {
				sinks.add(v);
			}
		}

		ArrayList<Integer> newSinks;
		while (sinks.size() != 0) {
			newSinks = new ArrayList<Integer>();
			for (int v: sinks) {
				for (int parent: g.getParents(v)) {
					if (g.outdegree(parent) == 1) {
						newSinks.add(parent);
					}
				}
				g.removeVertex(v);
				optimalOrder[sinkPtr] = v;
				sinkPtr--;
			}
			sinks = newSinks;
		}
		System.out.print("Sinks: ");
		for (int i = sinkPtr + 1; i < optimalOrder.length; i++) {
			System.out.print(optimalOrder[i] + " ");
		}
		System.out.println();

		// Preprocess
		// write getDisjoint(G) which returns List<Graph>
		List<Graph> disjointGraphs = getDisjoint(g);

		// for (Graph subgraph: disjointGraphs) {
		// 	// Process independently
		// }

		if (g.numVertices() < 11) {
			// brute force: try all orders of vertices
			// return early
		}

		int[] bestOrder = doLottaTimes(g, 100);


		// trying to sort by outdegree - indegree
		int[] sortOptimized = new int[g.numVertices()];

		Set<Integer> vert = g.getVertices();
		List<Integer> intermediary = new ArrayList<Integer>();
		
		for (int i : vert) {
			intermediary.add(i);
		}
		Collections.sort(intermediary, new vertexComp());

		int index = 0;
		for (int i : intermediary) {
			sortOptimized[index++] = i;
		}
		// end sorting trial

		for (int i = sourcePtr, j = 0; j < bestOrder.length; i++, j++) {
			optimalOrder[i] = bestOrder[j];
		}

		int[] vertexToIndex = new int[original.numVertices()];
		for (int i = 0; i < original.numVertices(); i++) {
			vertexToIndex[optimalOrder[i]] = i;
		}
		printArray(optimalOrder);

		System.out.println(computeForwardSize(original, vertexToIndex));
		System.out.println(original.numEdges());
	}

	// TODO: optimize this
	public static int computeForwardSize(Graph g, int[] vertexToIndex) {
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

	public static int[] doLottaTimes(Graph g, int iterations) {
		int bestOrderSize = -1;
		int[] bestOrder = null;
		for (int it = 0; it < iterations; it++) {
			Randp r = new Randp(g.numVertices());
			int[] indexToVertex = new int[g.numVertices()];
			int[] vertexToIndex = new int[g.numVertices()];
			int v;
			// Generate random ordering
			for (int j = 0; j < g.numVertices(); j++) {
				v = r.nextInt();
				indexToVertex[j] = v;
				vertexToIndex[v] = j;
			}
			// for (int i = 0; i < g.numVertices(); i++) {
			// 	System.out.print(indexToVertex[i] + " ");
			// }
			// System.out.println();
			int[] localBest = indexToVertex;
			int forwardSize = computeForwardSize(g, vertexToIndex);
			int backwardSize = g.numEdges() - forwardSize;
			if (backwardSize > forwardSize) {
				// Reverse list localBest
				for (int j = 0; j < localBest.length / 2; j++) {
				    int temp = localBest[j];
				    localBest[j] = localBest[localBest.length - j - 1];
				    localBest[localBest.length - j - 1] = temp;
				}
			}

			// do optimizations here
			boolean canImprove = true;
			while (canImprove) {
				canImprove = false;
				for (int i = 0; i < g.numVertices(); i++) {
					int maxIncrease = 0;
					int insertIndex = i;
					int currentIncrease = 0;
					for (int j = i - 1; j >= 0; j--) {
						if (g.hasEdge(localBest[i], localBest[j])) {
							currentIncrease++;
						}
						if (g.hasEdge(localBest[j], localBest[i])) {
							currentIncrease--;
						}
						if (currentIncrease > maxIncrease) {
							maxIncrease = currentIncrease;
							insertIndex = j;
						}
					}

					currentIncrease = 0;
					for (int j = i + 1; j < g.numVertices(); j++) {
						if (g.hasEdge(localBest[i], localBest[j])) {
							currentIncrease--;
						}
						if (g.hasEdge(localBest[j], localBest[i])) {
							currentIncrease++;
						}
						if (currentIncrease > maxIncrease) {
							maxIncrease = currentIncrease;
							insertIndex = j;
						}
					}
					// Insert if good
					if (maxIncrease <= 0) {
						continue;
					}
					// Insert
					int temp = localBest[i];
					if (insertIndex < i) {
						for (int k = i - 1; k >= insertIndex; k--) {
							localBest[k+1] = localBest[k];
						}
						localBest[insertIndex] = temp;
					} else {
						for (int k = i + 1; k <= insertIndex; k++) {
							localBest[k-1] = localBest[k];
						}
						localBest[insertIndex] = temp;
					}
					canImprove = true;
				}
			}

			// We've improved it as much as we can. 
			// Check if we should update bestOrder
			forwardSize = computeForwardSize(g, reverseMap(localBest));
			backwardSize = g.numEdges() - forwardSize;

			if (backwardSize > forwardSize) {
				System.out.println("You really fucked up.");
				return null;
			}

			if (forwardSize > bestOrderSize) {
				bestOrder = localBest;
				bestOrderSize = forwardSize;
			}
		}
		// for (int i = 0; i < g.numVertices(); i++) {
		// 	System.out.print(bestOrder[i] + " ");
		// }
		// System.out.println();
		// System.out.println(bestOrderSize);
		return bestOrder;
	}

	public static int[] reverseMap(int[] arr) {
		int[] reverseMapped = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			reverseMapped[arr[i]] = i;
		}
		return reverseMapped;
	}

	public static void printArray(int[] arr) {
		String s = "";
		for (int i = 0; i < arr.length; i++) {
			s += arr[i] + " ";
		}
		System.out.println(s);
	}

	public static List<Graph> getDisjoint(Graph g) {
		return null;
	}
}