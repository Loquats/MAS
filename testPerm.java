public class testPerm {
	public static void main (String[] args) {

		int[] start = new int[3];
		int i;
		for (i = 0; i < 3; i++) {
			start[i] = i;
		}

		for (i = 0; i < start.length - 1; i++) {
			System.out.print(start[i]+ ", ");
		}
		System.out.println(start[2]);
		
		for (int j = 0; j < 20; j++) {
		start= findNext(start);
		for (i = 0; i < start.length - 1; i++) {
			System.out.print(start[i]+ ", ");
		}
		System.out.println(start[2]);
	}
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
