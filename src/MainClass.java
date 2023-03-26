public class MainClass {
	private static final int DC_SIZEA=59;
	private static final int DATA_SIZEA=55;
	
	private static final int DC_SIZEB=31;
	private static final int DATA_SIZEB=27;

	public static void main(String[] args) {
		aRoad();
		bRoad();
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
		int instancesNum [] = {50, 100, 200, 800};//, 1000, 2000, 5000, 10000, 50000, 100000, 200000}; 
		int diskAccesses [] = new int[instancesNum.length];
		double[] timeStart = new double[instancesNum.length];
		double[] timeEnd = new double[instancesNum.length];
		String fileName = "file1-a1.bin";
		System.out.println("............SIZE A (A).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEA);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(instancesNum[i], 1, 2*instancesNum[i]) : RandomGenerator.generateInts(instancesNum[i], 0, 2*instancesNum[i]);
			FileHandling.writePage(dcArr, fileName, DC_SIZEA);//writing pages
			for(int j = 0; j<keysSearch.length; j++){
				timeStart[i] += System.nanoTime();	
				FileHandling.searchKeyFile(fileName, DC_SIZEA, keysSearch[j]);
				diskAccesses[i]+=(int)MultiCounter.getCount(1);		
				timeEnd[i] += System.nanoTime();
			}
		}
		
		for (int i=0; i<instancesNum.length; i++){
			System.out.println("Instances num: "+instancesNum[i]+"\tSearch time: "+(timeEnd[i]-timeStart[i])+"\tDisk accesses: "+diskAccesses[i]);
		}

		int instancesNum2 [] = {50, 100, 200, 800};//, 1000, 2000, 5000, 10000, 50000, 100000, 200000}; 
		int diskAccesses2 [] = new int[instancesNum2.length];
		double[] timeStart2 = new double[instancesNum2.length];
		double[] timeEnd2 = new double[instancesNum2.length];
		String fileName2 = "file1-b1.bin";
		System.out.println("............SIZE B (A).............");
		for (int i=0; i<instancesNum2.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEB);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(instancesNum2[i], 1, 2*instancesNum2[i]) : RandomGenerator.generateInts(instancesNum2[i], 0, 2*instancesNum2[i]);
			FileHandling.writePage(dcArr, fileName2, DC_SIZEB);//writing pages
			for(int j = 0; j<keysSearch.length; j++){
				timeStart2[i] += System.nanoTime();	
				FileHandling.searchKeyFile(fileName2, DC_SIZEB, keysSearch[j]);
				diskAccesses2[i]+=(int)MultiCounter.getCount(1);		
				timeEnd2[i] += System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum.length; i++){
			System.out.println("Instances num: "+instancesNum2[i]+"\tSearch time: "+(timeEnd2[i]-timeStart2[i])+"\tDisk accesses: "+diskAccesses2[i]);
		}

	}

	public static void bRoad(){
		int instancesNum [] = {50, 100, 200, 800};//, 1000, 2000, 5000, 10000, 50000, 100000, 200000}; 
		int diskAccesses [] = new int[instancesNum.length];
		double[] timeStart = new double[instancesNum.length];
		double[] timeEnd = new double[instancesNum.length];
		String fileNamePages = "file1-a2.bin";
		String fileNameKeys = "file1-Keysb.bin";
		System.out.println("............SIZE A (B).............");
		for (int i=0; i<instancesNum.length; i++){
			DataClass[] dcArr = generateDataClasses(instancesNum[i], DATA_SIZEA);
			int[] keysSearch = (instancesNum[i]>1000) ? RandomGenerator.generateUniqInts(instancesNum[i], 1, 2*instancesNum[i]) : RandomGenerator.generateInts(instancesNum[i], 0, 2*instancesNum[i]);
			KeyPage[] kpArr = FileHandling.writePageKp(dcArr, fileNamePages, DC_SIZEA);//writing pages
			KeyPage.writeKpFile(kpArr, fileNameKeys);
			for(int j=0; j<keysSearch.length; j++){
				timeStart[i] += System.nanoTime();	
				int pageNum = KeyPage.searchKeyPage(fileNameKeys, keysSearch[j], dcArr.length);
				FileHandling.bRoadFind(fileNamePages, pageNum, DC_SIZEA, keysSearch[j]);
				diskAccesses[i]+=(int)MultiCounter.getCount(2)+1; //second counter is 1	
				timeEnd[i]+=System.nanoTime();
			}
		}

		for (int i=0; i<instancesNum.length; i++){
			System.out.println("Instances num: "+instancesNum[i]+"\tSearch time: "+(timeEnd[i]-timeStart[i])+"\tDisk accesses: "+diskAccesses[i]);
		}

	}
}
