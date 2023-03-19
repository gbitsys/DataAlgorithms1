import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
public class MainClass {
	private static final int PAGE_SIZE=256;
	
	private static final int DC_SIZEA=59;//!!!
	
	private static final int DATA_SIZEA=55;
	private static final int KEY_SIZEA=4;
	
	private static final int DC_SIZEB=31;
	private static final int DATA_SIZEB=27;
	private static final int KEY_SIZE=4;
	public static void main(String[] args) {
		DataClass[] dcArr = generateDataClasses(6, DATA_SIZEA);
		System.out.println("Write Page: "+FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA));
		FileHandling.readPages("file1.bin",DC_SIZEA);

	
		ByteBuffer bb = ByteBuffer.allocate(256);

		
		/*byte[] buffer = new byte[256];
		DataPage dp = DataPage.convertToPage(DataClass.extracted(generateDataClasses(3, DATA_SIZEB), DC_SIZEB), DC_SIZEB);
		//System.out.println(DataClass.extracted(generateDataClasses(3, DATA_SIZEB), DC_SIZEB));
		System.out.println(dp.getRecords()[1].toString());
		*/
	}


	// a method that generates our DataClasses and puts them into an array
	public static DataClass[] generateDataClasses(int numOfClasses, int size){
		DataClass[] dcArr = new DataClass[numOfClasses];
		int[] randomUniqKeys = new int[numOfClasses];
		randomUniqKeys = RandomGenerator.generateInts(numOfClasses, 0, numOfClasses*2);

		for (int i=0; i<numOfClasses; i++){
			dcArr[i] = new DataClass(randomUniqKeys[i], RandomGenerator.generateASCII(size));
		}


		//DEBUG
		System.out.println("Generated:");
		for (DataClass dc : dcArr){
			if (dc!=null)
				System.out.println("key :"+dc.getKey()+" data: "+dc.getDataString());
		}

		return dcArr;
	}
}
