import java.util.Arrays;

public class MainClass {
	private static final int DC_SIZEA=59;
	private static final int DATA_SIZEA=55;
	
	private static final int DC_SIZEB=31;
	private static final int DATA_SIZEB=27;

	public static void main(String[] args) {
		aRoad();
		bRoad();
		cRoad();
	}

	// a method that generates our DataClasses and puts them into an array
	public static DataClass[] generateDataClasses(int numOfClasses, int size){
		DataClass[] dcArr = new DataClass[numOfClasses];
		int[] randomUniqKeys = new int[numOfClasses];
		randomUniqKeys = RandomGenerator.generateUniqInts(numOfClasses, 1, numOfClasses*2);

		for (int i=0; i<numOfClasses; i++){
			dcArr[i] = new DataClass(randomUniqKeys[i], RandomGenerator.generateGeek(size));
			//System.out.println(dcArr[i].toString());//printing out instances
		}

		return dcArr;
	}

	public static void aRoad(){
		int instancesNum [] = {50, 100, 200, 500, 800, 1000, 2000, 5000,10000, 50000, 100000, 200000};
		int diskAccesses [] = new int[instancesNum.length];
		double[] timeStart = new double[instancesNum.length];
		double[] timeEnd = new double[instancesNum.length];
		String fileName = "file1-a.bin";
		System.out.println("............SIZE A (A).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEA);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(1000, 1, 2*instancesNum[i]) : RandomGenerator.generateInts(1000, 1, 2*instancesNum[i]);
			FileHandling.writePage(dcArr, fileName, DC_SIZEA);//writing pages
			//MultiCounter.resetCounter(1);//reset counter for disk accesses
			for(int j = 0; j<keysSearch.length; j++){
				timeStart[i] += System.nanoTime();	
				FileHandling.searchKeyFile(fileName, DC_SIZEA, keysSearch[j]);
				diskAccesses[i]+=(int)MultiCounter.getCount(1);		
				timeEnd[i] += System.nanoTime();
			}
		}
		
		for (int i=0; i<instancesNum.length; i++){
			System.out.println("Instances num: "+instancesNum[i]+"\tSearch time: "+(timeEnd[i]-timeStart[i])/1000+"\tDisk accesses: "+diskAccesses[i]/(double)1000);
		}
 
		int instancesNum2 [] = {50, 100, 200, 500, 800, 1000, 2000, 5000,10000, 50000, 100000, 200000}; 
		int diskAccesses2 [] = new int[instancesNum2.length];
		double[] timeStart2 = new double[instancesNum2.length];
		double[] timeEnd2 = new double[instancesNum2.length];
		String fileName2 = "file1-b.bin";
		System.out.println("............SIZE B (A).............");
		for (int i=0; i<instancesNum2.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum2[i], DATA_SIZEB);
			int[] keysSearch = (instancesNum2[i]>1000) ? RandomGenerator.generateUniqInts(1000, 1, 2*instancesNum2[i]) : RandomGenerator.generateInts(1000, 1, 2*instancesNum2[i]);
			FileHandling.writePage(dcArr, fileName2, DC_SIZEB);//writing pages
			//MultiCounter.resetCounter(1);
			for(int j = 0; j<keysSearch.length; j++){
				timeStart2[i] += System.nanoTime();	
				FileHandling.searchKeyFile(fileName2, DC_SIZEB, keysSearch[j]);
				diskAccesses2[i]+=(int)MultiCounter.getCount(1);		
				timeEnd2[i] += System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum2.length; i++){
			System.out.println("Instances num: "+instancesNum2[i]+"\tSearch time: "+(timeEnd2[i]-timeStart2[i])/1000+"\tDisk accesses: "+diskAccesses2[i]/(double)1000);
		}

	}

	public static void bRoad(){
		int instancesNum [] = {50, 100, 200, 500, 800, 1000, 2000, 5000,10000, 50000, 100000, 200000};  
		int diskAccesses [] = new int[instancesNum.length];
		double[] timeStart = new double[instancesNum.length];
		double[] timeEnd = new double[instancesNum.length];
		String fileNamePages = "file2-a.bin";
		String fileNameKeys = "file2-Keysa.bin";
		System.out.println("............SIZE A (B).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEA);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(1000, 1, 2*instancesNum[i]) : RandomGenerator.generateInts(1000, 1, 2*instancesNum[i]);
			KeyPage[] kpArr = FileHandling.writePageKp(dcArr, fileNamePages, DC_SIZEA);//writing pages
			KeyPage.writeKpFile(kpArr, fileNameKeys);
			MultiCounter.resetCounter(2);//reset counter
			for(int j=0; j<keysSearch.length; j++){
				timeStart[i] += System.nanoTime();	
				int pageNum = KeyPage.searchKeyPage(fileNameKeys, keysSearch[j], dcArr.length);
				FileHandling.bRoadFind(fileNamePages, pageNum, DC_SIZEA, keysSearch[j]);
				diskAccesses[i]+=(int)MultiCounter.getCount(2)+1; 	
				timeEnd[i]+=System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum.length; i++){
			System.out.println("Instances num: "+instancesNum[i]+"\tSearch time: "+(timeEnd[i]-timeStart[i])/1000+"\tDisk accesses: "+diskAccesses[i]/(double)1000);
		}

		int instancesNum2 [] = {50, 100, 200, 500, 800, 1000, 2000, 5000,10000, 50000, 100000, 200000};
		int diskAccesses2 [] = new int[instancesNum.length];
		double[] timeStart2 = new double[instancesNum.length];
		double[] timeEnd2 = new double[instancesNum.length];
		fileNamePages = "file2-b.bin";
		fileNameKeys = "file2-Keysb.bin";
		System.out.println("............SIZE B (B).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum2[i], DATA_SIZEB);
			int[] keysSearch = (instancesNum2[i]>1000) ? RandomGenerator.generateUniqInts(1000, 1, 2*instancesNum2[i]) : RandomGenerator.generateInts(1000, 1, 2*instancesNum2[i]);
			KeyPage[] kpArr = FileHandling.writePageKp(dcArr, fileNamePages, DC_SIZEB);//writing pages
			KeyPage.writeKpFile(kpArr, fileNameKeys);
			MultiCounter.resetCounter(2);//reset counter
			for(int j=0; j<keysSearch.length; j++){
				timeStart2[i] += System.nanoTime();	
				int pageNum = KeyPage.searchKeyPage(fileNameKeys, keysSearch[j], dcArr.length);
				FileHandling.bRoadFind(fileNamePages, pageNum, DC_SIZEB, keysSearch[j]);
				diskAccesses2[i]+=(int)MultiCounter.getCount(2)+1; 
				timeEnd2[i]+=System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum2.length; i++){
			System.out.println("Instances num: "+instancesNum2[i]+"\tSearch time: "+(timeEnd2[i]-timeStart2[i])/1000+"\tDisk accesses: "+diskAccesses2[i]/(double)1000);
		}
	}

	public static void cRoad(){
		int instancesNum [] = {50, 100, 200, 500, 800, 1000, 2000, 5000,10000, 50000, 100000, 200000};
		int diskAccesses [] = new int[instancesNum.length];
		double[] timeStart = new double[instancesNum.length];
		double[] timeEnd = new double[instancesNum.length];
		String fileNamePages = "file3-a.bin";
		String fileNameKeys = "file3-Keysb.bin";
		System.out.println("............SIZE A (C).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEA);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(1000, 1, 2*instancesNum[i]) : RandomGenerator.generateInts(1000, 1, 2*instancesNum[i]);
			KeyPage[] kpArr = FileHandling.writePageKp(dcArr, fileNamePages, DC_SIZEA);//writing pages
			Arrays.sort(kpArr);
			KeyPage.writeKpFile(kpArr, fileNameKeys); //writing sorted array of key-pages
			MultiCounter.resetCounter(5);//reset counter
			for(int j=0; j<keysSearch.length; j++){
				timeStart[i] += System.nanoTime();	
				int pageNum = KeyPage.binarySearch(fileNameKeys, keysSearch[j], dcArr.length);
				diskAccesses[i]+=(int)MultiCounter.getCount(5); 	
				timeEnd[i]+=System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum.length; i++){
			System.out.println("Instances num: "+instancesNum[i]+"\tSearch time: "+(timeEnd[i]-timeStart[i])/1000+"\tDisk accesses: "+diskAccesses[i]/(double)1000);
		}

		int instancesNum2 [] = {50, 100, 200, 500, 800, 1000, 2000, 5000,10000, 50000, 100000, 200000}; 
		int diskAccesses2 [] = new int[instancesNum.length];
		double[] timeStart2 = new double[instancesNum.length];
		double[] timeEnd2 = new double[instancesNum.length];
		String fileNamePages2 = "file3-b.bin";
		String fileNameKeys2 = "file3-Keysb.bin";
		System.out.println("............SIZE B (C).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEB);
			int[] keysSearch = (instancesNum2[i]>1000) ? RandomGenerator.generateUniqInts(1000, 1, 2*instancesNum2[i]) : RandomGenerator.generateInts(1000, 1, 2*instancesNum2[i]);
			KeyPage[] kpArr = FileHandling.writePageKp(dcArr, fileNamePages2, DC_SIZEB);//writing pages
			Arrays.sort(kpArr);
			KeyPage.writeKpFile(kpArr, fileNameKeys2); //writing sorted array of key-pages
			MultiCounter.resetCounter(5);//reset counter
			for(int j=0; j<keysSearch.length; j++){
				timeStart2[i] += System.nanoTime();	
				int pageNum = KeyPage.binarySearch(fileNameKeys, keysSearch[j], dcArr.length);
				diskAccesses2[i]+=(int)MultiCounter.getCount(5); 	
				timeEnd2[i]+=System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum2.length; i++){
			System.out.println("Instances num: "+instancesNum2[i]+"\tSearch time: "+(timeEnd2[i]-timeStart2[i])/1000+"\tDisk accesses: "+diskAccesses2[i]/(double)1000);
		}
	}
}
