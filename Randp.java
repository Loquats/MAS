import java.util.Random;

public class Randp {
	private int[] ints;
	private Random rand;
	private int intsLeft;

	public Randp(int maxValue){
		rand = new Random();
		ints = new int[maxValue];
		intsLeft = maxValue;
		for(int i = 0; i < ints.length; i++){
			ints[i] = i;
		}
	}
	
	public int nextInt(){
		int retVal = 0;
		if(intsLeft > 0){
			int randIndex = rand.nextInt(intsLeft);
			retVal = ints[randIndex];
			swap(ints, randIndex, intsLeft - 1);
			intsLeft--;
		}
		return retVal;
	}
	
	public void swap(int[] arr, int a, int b){
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
}