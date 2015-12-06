import java.util.*;

public class testMap {
	public static void main(String[] args) {
		int[] testVertices = new int[]{4, 2, 0, 6, 9, 18, 27, 17, 38};
		HashMap<Integer, Integer> forwards = map(testVertices);
		HashMap<Integer, Integer> backwards = inverseMap(testVertices);
		int[] mappedVertices = new int[testVertices.length];
		for (int i = 0; i < mappedVertices.length; i++) {
			mappedVertices[i] = i;
		}

		int[] test = getBackwards(mappedVertices, backwards);
		for (int i = 0; i < test.length; i++) {
			System.out.print(test[i] + " ");
		}

		System.out.println();

		mappedVertices = findNext(mappedVertices);
		test = getBackwards(mappedVertices, backwards);
		for (int i = 0; i < test.length; i++) {
			System.out.print(test[i] + " ");
		}

		System.out.println();

		for (int i = 0; i < 69; i++) {
			mappedVertices = findNext(mappedVertices);
		}

		test = getBackwards(mappedVertices, backwards);

		for (int i = 0; i < test.length; i++) {
			System.out.print(test[i] + " ");
		}

		System.out.println();

	}

	// forwards, maps actual vertices to 0...n
	public static HashMap<Integer, Integer> map(int[] map) {
		HashMap<Integer, Integer> m = new HashMap<Integer,Integer>();
		for (int i = 0; i < map.length; i ++) {
			m.put(map[i], i);
		}
		return m;
	}

	// backwards, maps 0...n back to actual values
	public static HashMap<Integer, Integer> inverseMap(int[] map) {
		HashMap<Integer, Integer> m = new HashMap<Integer,Integer>();
		for (int i = 0; i < map.length; i ++) {
			m.put(i, map[i]);
		}
		return m;
	}

	public static int[] getBackwards(int[] l, HashMap<Integer, Integer> key) {
		int[] ret = new int[l.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = key.get(l[i]);
		}
		return ret;
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
}

// what we want
// map vertices to 0...n
// map 0...n back to vertices

//PSEUDOREALCODE