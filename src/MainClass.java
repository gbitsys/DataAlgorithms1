import javax.xml.crypto.Data;

public class MainClass {
	private static final int PAGE_SIZE=256;
	
	private static final int DC_SIZEA=59;
	private static final int DATA_SIZEA=55;
	private static final int KEY_SIZEA=4;
	
	private static final int DC_SIZEB=31;
	private static final int DATA_SIZEB=27;
	private static final int KEY_SIZE=4;
	public static void main(String[] args) {
		aRoad();
		bRoad();
	}

	// a method that generates our DataClasses and puts them into an array
	public static DataClass[] generateDataClasses(int numOfClasses, int size){
		DataClass[] dcArr = new DataClass[numOfClasses];
		int[] randomUniqKeys = new int[numOfClasses];
		randomUniqKeys = RandomGenerator.generateUniqInts(numOfClasses, 0, numOfClasses*2);

		for (int i=0; i<numOfClasses; i++){
			dcArr[i] = new DataClass(randomUniqKeys[i], RandomGenerator.generateGeek(size));
			//System.out.println(dcArr[i].toString());//printing out instances
		}

		return dcArr;
	}

	public static void aRoad(){
		int instancesNum [] = {50, 100, 200, 800};//, 1000, 2000, 5000, 10000, 50000, 100000, 200000}; 
		int diskAccesses [] = new int[instancesNum.length];
		double[] timeStart = new double[instancesNum.length];
		double[] timeEnd = new double[instancesNum.length];
		String fileName = "file1-a.bin";
		System.out.println("............SIZE A (A).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEA);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(instancesNum[i], 0, 2*instancesNum[i]) : RandomGenerator.generateInts(instancesNum[i], 0, 2*instancesNum[i]);
			FileHandling.writePage(dcArr, fileName, DC_SIZEA);//writing pages
			timeStart[i] = System.nanoTime();	
			diskAccesses[i]=(int)MultiCounter.getCount(1);		
			FileHandling.searchKeyFile(fileName, DC_SIZEA, keysSearch[i]);
			timeEnd[i]=System.nanoTime() - timeStart[i];
		}

		
		int instancesNum2 [] = {50, 100, 200, 800};//, 1000, 2000, 5000, 10000, 50000, 100000, 200000}; 
		int diskAccesses2 [] = new int[instancesNum.length];
		double[] timeStart2 = new double[instancesNum.length];
		double[] timeEnd2 = new double[instancesNum.length];
		String fileName2 = "file1-b.bin";
		System.out.println("............SIZE A (B).............");
		for (int i=0; i<instancesNum2.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEB);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(instancesNum[i], 0, 2*instancesNum[i]) : RandomGenerator.generateInts(instancesNum[i], 0, 2*instancesNum[i]);
			FileHandling.writePage(dcArr, fileName2, DC_SIZEB);//writing pages
			timeStart2[i] = System.nanoTime();	
			diskAccesses2[i]=(int)MultiCounter.getCount(1);		
			FileHandling.searchKeyFile(fileName, DC_SIZEB, keysSearch[i]);
			timeEnd2[i]=System.nanoTime() - timeStart2[i];
		}

	}

	public static void bRoad(){
	}
}
