import java.io.*;
import java.util.*;

public class MAS { 
	static Graph comp;
	static class vertexComp implements Comparator<Integer>{
 
	    @Override
	    public int compare(Integer v1, Integer v2) {
	    	int compared = comp.outdegree(v2) - comp.indegree(v2) - (comp.outdegree(v1) - comp.indegree(v1));
	        if(compared < 0){
	            return 1;
	        } else if (compared>0){
	            return -1;
	        } else {
	        	return 0;
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
		System.out.println("Original: " + original.numEdges());
		System.out.println("Double-edge pruned: " + g.numEdges());
		int doublePrunedEdges = g.numEdges();


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

		// g.printAdjacency();

		// Preprocess
		// write getDisjoint(G) which returns List<Graph>
		List<Graph> disjointGraphs = getDisjoint(g);
		int[] optimalMiddle = new int[g.numVertices()];
		int i = sourcePtr;
		for (Graph subgraph: disjointGraphs) {
			int[] curr = evaluateGraph(subgraph);
			for (int j : curr) {
				optimalOrder[i++] = j;
			}
		}

		// int[] optimalMiddle = evaluateGraph(g);




		// for (int i = sourcePtr, j = 0; j < optimalMiddle.length; i++, j++) {
		// 	optimalOrder[i] = optimalMiddle[j];
		// }

		printArray(optimalOrder);

		int graphWeight = computeForwardSize(original, reverseMap(optimalOrder));
		System.out.println("Raw: " + graphWeight);
		System.out.println("Double-edge adjusted: " + (graphWeight - (original.numEdges() - doublePrunedEdges)/2));
	}

	public static int[] evaluateGraph(Graph g) {
		if (g.numVertices() < 10) {
			return bruteForce(g, g.numVertices());
		}

		int[] bestOrderRand = doLottaTimes(g, 500);

		//sorting by outdegree - indegree, removing each time
		Graph duplicate = g.clone();
		int[] sortByDegree = new int[g.numVertices()];
		for (int i = 0; i < g.numVertices(); i++) {
			int maxDegree = -1;
			int maxVert = -1;
			for (int v : duplicate.getVertices()) {
				int myDegree = duplicate.outdegree(v) - duplicate.indegree(v);
				if (myDegree > maxDegree) {
					maxDegree = myDegree;
					maxVert = v;
				}
			}
			sortByDegree[i] = maxVert;
			duplicate.removeVertex(maxVert);
		}

		while (optimizedByInsertion(g, sortByDegree)) {

		}


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

		while (optimizedByInsertion(g, sortOptimized)) {

		}

		int[] bestOrder = bestOrderRand;

		int sortOptVal = computeForwardSize(g, reverseMap(sortOptimized));
		int sortByDegVal = computeForwardSize(g, reverseMap(sortByDegree));
		int bestOrderVal = computeForwardSize(g, reverseMap(bestOrderRand));
		// System.out.println(sortOptVal + " : " + sortByDegVal + " : " + bestOrderVal);
		if (sortOptVal > sortByDegVal && sortOptVal > bestOrderVal) {
			bestOrder = sortOptimized;
		} else if (sortByDegVal > sortOptVal && sortByDegVal > bestOrderVal) {
			bestOrder = sortByDegree;
		}
		return bestOrder;
	}

	// TODO: optimize this
	public static int computeForwardSize(Graph g, HashMap<Integer, Integer> vertexToIndex) {
		int size = 0;
		for (int i: g.getVertices()) {
			for (Integer j: g.getChildren(i)) {
				if (vertexToIndex.get(i) < vertexToIndex.get(j)) {
		    		size++;
		    	}
			}
		}
		return size;
	}

	// public static boolean optimizeBySwapTrue(Graph g, int[] original) {
		// int originalSize, modSize, temp;
		// boolean optimized = false;
		// originalSize = computeForwardSize(g, reverseMap(original));
		// int[] best = original.clone();
		// for (int i = 0; i < original.length - 1; i++) {
		// 	for (int j = i+1; j<original.length; j++) {
		// 		// swap
		// 		temp = original[i];
		// 		original[i] = original[j];
		// 		original[j] = temp;
		// 		modSize = computeForwardSize(g, reverseMap(original));

		// 		if (modSize > originalSize) {
		// 			best = original.clone();
		// 			optimized = true;
		// 			System.out.println("swapped");
		// 		}
				
		// 		// always swap best
		// 		temp = original[i];
		// 		original[i] = original[j];
		// 		original[j] = temp;
		// 	}
		// }
		// original = best;
		// return optimized;
	// }

	public static boolean optimizeBySwapGreedy(Graph g, int[] original) {
		int originalSize, modSize, temp;
		originalSize = computeForwardSize(g, reverseMap(original));
		boolean optimized = false;
		for (int i = 0; i < original.length - 1; i++) {
			for (int j = i+1; j<original.length; j++) {
				// swap
				temp = original[i];
				original[i] = original[j];
				original[j] = temp;
				modSize = computeForwardSize(g, reverseMap(original));

				if (modSize < originalSize) {
					// swap back
					temp = original[i];
					original[i] = original[j];
					original[j] = temp;
				} else {
					originalSize = computeForwardSize(g, reverseMap(original));
					optimized = true;
				}
			}
		}
		return optimized;
	}


	public static boolean optimizedByInsertion(Graph g, int[] localBest) {
		boolean canImprove = false;
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
		return canImprove;
	}

	public static int[] findNext(int[] previous) {
		int k = findK(previous);
		int l = findL(previous, k);
		if (k == -1) return null;
		int temp = previous[k];
		previous[k] = previous[l];
		previous[l] = temp;
		reverse_sublist(previous, k + 1);
		return previous;
	}

	public static int findK(int[] list) {
		int k = -1;
		for (int i = 0; i < list.length - 1; i++) {
			if (list[i] < list[i+1]) {
				k = i;
			}
		}
		return k;
	}

	public static int findL(int[] list, int k) {
		int l = -1;
		for (int i = k; i < list.length; i++) {
			if (list[k] < list[i]) {
				l = i;
			}
		}
		return l;
	}

	public static int[] reverse_sublist(int[] list, int index) {
		for(int i = index; i < (list.length + index) / 2 ; i++)
		{
		    int temp = list[i];
		    list[i] = list[list.length - i - 1 + index];
		    list[list.length - i - 1 + index] = temp;
		}
		return list;
	}

	public static int[] bruteForce(Graph g, int nodes) {
		int max = 0;
		int[] start = new int[nodes];
		for (int i = 0; i < nodes; i++) {
			start[i] = i;
		}
		int[] maxList = start.clone();
		int score = computeForwardSize(g,reverseMap(start));
		while (start != null)  {
			score = computeForwardSize(g,reverseMap(start));
			if (score > max) {
				max = score;
				maxList = start.clone();
			}
			start = findNext(start);
		}
		return maxList;
	}

	public static int[] doLottaTimes(Graph g, int iterations) {
		int bestOrderSize = -1;
		int[] bestOrder = null;
		for (int it = 0; it < iterations; it++) {
			List<Integer> rand = new ArrayList<Integer>(g.getVertices());
			Collections.shuffle(rand);
			int[] indexToVertex = new int[g.numVertices()];
			int v;
			// Generate random ordering
			for (int j = 0; j < g.numVertices(); j++) {
				v = rand.get(j);
				indexToVertex[j] = v;
			}
			// for (int i = 0; i < g.numVertices(); i++) {
			// 	System.out.print(indexToVertex[i] + " ");
			// }
			// System.out.println();
			int[] localBest = indexToVertex;
			// System.out.println(it);
			int forwardSize = computeForwardSize(g, reverseMap(indexToVertex));
			int backwardSize = g.numEdges() - forwardSize;
			if (backwardSize > forwardSize) {
				// Reverse list localBest
				for (int j = 0; j < localBest.length / 2; j++) {
				    int temp = localBest[j];
				    localBest[j] = localBest[localBest.length - j - 1];
				    localBest[localBest.length - j - 1] = temp;
				}
			}

			while (optimizedByInsertion(g, localBest)) {

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

	public static HashMap<Integer, Integer> reverseMap(int[] arr) {
		HashMap<Integer, Integer> reverseMapped = new HashMap<Integer, Integer>();
		for (int i = 0; i < arr.length; i++) {
			reverseMapped.put(arr[i], i);
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
		LinkedList<HashSet<Integer>> setsOfDG = new LinkedList<HashSet<Integer>>();
		for (Integer vertex: g.getVertices()) {
			Set<Integer> endEdges = g.getChildren(vertex);
			LinkedList<HashSet<Integer>> commonDGs = new LinkedList<HashSet<Integer>>();
			
			// Check for source vertex in disjoint sets
			for (HashSet<Integer> disjointSet: setsOfDG) {
				if (disjointSet.contains(vertex)) {
					setsOfDG.remove(disjointSet);
					commonDGs.add(disjointSet);
				}
			}

			// Check for child vertex in disjoint setes
			for (Integer child: endEdges) {
				for (HashSet<Integer> disjointSet: setsOfDG) {
					if (disjointSet.contains(child)) {
						setsOfDG.remove(disjointSet);
						commonDGs.add(disjointSet);
					}
				}
			}
			
			// Create new disjoint set
			HashSet<Integer> set = new HashSet<Integer>();
			set.add(vertex);
			set.addAll(endEdges);
			for (HashSet<Integer> commonSet: commonDGs) {
				set.addAll(commonSet);
			}
			setsOfDG.add(set);
		}

		// for (HashSet<Integer> set: setsOfDG) {
		// 	System.out.println(set);
		// }

		// Create Graph List

		LinkedList<Graph> disjointGraphs = new LinkedList<Graph>();
		for (HashSet<Integer> set: setsOfDG) {
			Graph graph = new Graph(0);
			for (Integer vertex: set) {
				graph.addVertex(vertex);
			}
			for (Integer vertex: set) {
				for (Integer child: g.getChildren(vertex)) {
					graph.addEdge(vertex, child);
				}
			}
			disjointGraphs.add(graph);
		}

		// for (Graph graph : disjointGraphs) {
		// 	graph.printAdjacency();
		// }

		return disjointGraphs;
	}
}