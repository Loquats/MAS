import java.io.*;
import java.util.StringTokenizer;

public class Instances { 
	public static void main(String[] args) throws IOException {
		// Instance 1
		int numNodes = 99;
		Randp r1 = new Randp(numNodes);
		int[] optimal1 = new int[numNodes];
		for (int i = 0; i < numNodes; i++) {
			optimal1[i] = r1.nextInt();
		}
		for (int i = 0; i < numNodes; i++) {
			System.out.print(optimal1[i] + " ");
		}
		System.out.println();

		Graph g1 = new Graph(numNodes);
		for (int i = 0; i < numNodes; i++) {
			for (int j = i + 1; j < numNodes; j++) {
				if (Math.random() < 0.9) {
					g1.addEdge(optimal1[i], optimal1[j]);
				} else {
					g1.addEdge(optimal1[j], optimal1[i]);
				}
			}
		}
		System.out.println(numNodes);
		g1.printMatrix();

		// Instance 2
		numNodes = 97;
		Randp r2 = new Randp(numNodes);
		int[] optimal2 = new int[numNodes];
		for (int i = 0; i < numNodes; i++) {
			optimal2[i] = r2.nextInt();
		}
		for (int i = 0; i < numNodes; i++) {
			System.out.print(optimal2[i] + " ");
		}
		System.out.println();

		Graph g2 = new Graph(numNodes);
		for (int i = 0; i < numNodes; i++) {
			for (int j = i + 1; j < numNodes; j++) {
				if (Math.random() < 0.91) {
					g2.addEdge(optimal2[i], optimal2[j]);
				} else {
					g2.addEdge(optimal2[j], optimal2[i]);
				}
				if (Math.random() < 0.18*j/numNodes) {
					g2.addEdge(optimal2[j], optimal2[i]);
				}
			}
		}
		System.out.println(numNodes);
		g2.printMatrix();

		// Instance 3
		numNodes = 100;
		Randp r3 = new Randp(numNodes);
		int[] optimal3 = new int[numNodes];
		for (int i = 0; i < numNodes; i++) {
			optimal3[i] = r3.nextInt();
		}
		for (int i = 0; i < numNodes; i++) {
			System.out.print(optimal3[i] + " ");
		}
		System.out.println();

		Graph g3 = new Graph(numNodes);
		for (int i = 0; i < numNodes; i++) {
			for (int j = i + 1; j < numNodes; j++) {
				if (Math.random() < 0.93-0.4*(j-i)/numNodes) {
					g3.addEdge(optimal3[i], optimal3[j]);
				} else {
					g3.addEdge(optimal3[j], optimal3[i]);
				}
			}
		}
		System.out.println(numNodes);
		g3.printMatrix();


	}
}