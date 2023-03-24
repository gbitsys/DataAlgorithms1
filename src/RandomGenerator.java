
import java.util.*;
public class RandomGenerator {
	
	//bounds of ASCII characters we use
	private static int LowerASCII=48;
	private static int UpperASCII=90;
	private static int A_SIZE=55;
	private static StringBuilder strBuilder;
	private String dataStr;
	private static Random randomGenerator = new Random();
	private static String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	+ "0123456789"
	+ "abcdefghijklmnopqrstuvxyz";
	
	
	public RandomGenerator() {
	}
	
	public static String generateASCII(int size) {
		int[] num=new int[size];
		strBuilder = new StringBuilder();
		num = randomGenerator.ints(LowerASCII, UpperASCII).limit(size).toArray();
		for (int i=0; i<size; i++){
			strBuilder.append((char)num[i]);
		}
		return strBuilder.toString();
	}

	public static String generateGeek(int size){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
 
			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int)(AlphaNumericString.length()* Math.random());
		  
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		   }
		  
		   return sb.toString();
		  
	}

	// a method that generates an array of unique random integers
	public static int[] generateUniqInts(int numOfKeys, int minKey, int maxKey){
		Random rg = new java.util.Random();
		int[] randomInts = rg.ints(minKey,maxKey+1).distinct().limit(numOfKeys).toArray();

		return randomInts;
	}
	
	// a method that generates an array of random integers (not unique)
	public static int[] generateInts(int numOfKeys, int minKey, int maxKey){
		java.util.Random randomGenerator = new java.util.Random();
		int[] randomInts = randomGenerator.ints(minKey, maxKey+1).limit(numOfKeys).toArray();
		return randomInts;
	}
}
