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
			System.out.println(dcArr[i].toString());//printing out instances
		}

		return dcArr;
	}

	public static void aRoad(){
		//Size A
		//50 instances dc size a
		System.out.println("..........SIZE A (A)..........");
		long start = System.nanoTime();
		DataClass[] dcArr = generateDataClasses(50, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		int [] keysSearch = new int[1000];
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*50);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}
		double end = System.nanoTime() - start;
		System.out.print("Time 50 A: " + end);
		System.out.println(".......Disk accesses: 50 A: "+MultiCounter.getCount(1));

		//100 instances dc size a
		long start2 = System.nanoTime();
		dcArr = generateDataClasses(100, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*100);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}
		double end2 =  System.nanoTime() - start2;
		System.out.print("Time 100 A: " + end2);
		System.out.println(".......Disk accesses: 100 A: "+MultiCounter.getCount(1));

		//200 instances dc size a
		long start3 = System.nanoTime();
		dcArr = generateDataClasses(200, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*200);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}
		double end3 =  System.nanoTime() - start3;
		System.out.print("Time 200 A: " + end3);
		System.out.println(".......Disk accesses 200 A: "+MultiCounter.getCount(1));	
 /*
		//500 instances dc size a
		long start4 = System.nanoTime();
		dcArr = generateDataClasses(500, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*500);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}
		double end4 =  Math.pow(System.nanoTime() - start4, -9);
		System.out.print("Time 200 A: " + end4);
		System.out.println(".......Disk accesses 500 A: "+MultiCounter.getCount(1));	
 		


		//800 instances dc size a
		long start5 = System.nanoTime();
		dcArr = generateDataClasses(800, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*800);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}	

		//1,000 instances dc size a
		long start6 = System.nanoTime();
		dcArr = generateDataClasses(1000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*1000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}

		//2,000 instances dc size a
		long start7 = System.nanoTime();
		dcArr = generateDataClasses(2000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateUniqInts(1000, 1, 2*2000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}

		//5,000 instances dc size a
		long start8 = System.nanoTime();
		dcArr = generateDataClasses(5000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateUniqInts(1000, 1, 2*5000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}
		
		//10,000 instances dc size a
		long start9 = System.nanoTime();
		dcArr = generateDataClasses(10000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateUniqInts(1000, 1, 2*10000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}

		//50,000 instances dc size a
		long start10 = System.nanoTime();
		dcArr = generateDataClasses(50000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateUniqInts(1000, 1, 2*50000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}

		//100,000 instances dc size a
		long start11 = System.nanoTime();
		dcArr = generateDataClasses(100000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateUniqInts(1000, 1, 2*100000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);

		}

		//200,000 instances dc size a
		long start12 = System.nanoTime();
		dcArr = generateDataClasses(200000, DATA_SIZEA);
		FileHandling.writePage(dcArr, "file1.bin", DC_SIZEA);
		keysSearch = RandomGenerator.generateUniqInts(1000, 1, 2*200000);
		for (int i=0; i<1000; i++){
			FileHandling.searchKeyFile("file1.bin", DC_SIZEA, keysSearch[i]);
		}
		*/

	}

	public static void bRoad(){
		//Size A
		//50 instances dc size a
		System.out.println("..........SIZE A (B)..........");
		long start = System.nanoTime();
		DataClass[] dcArr = generateDataClasses(50, DATA_SIZEA);
		KeyPage[] kpArr = FileHandling.writePageKp(dcArr, "file2_Pages.bin", DC_SIZEA);
		KeyPage.writeKpFile(kpArr, "file2_KeyPages.bin");
		int [] keysSearch = new int[1000];
		keysSearch = RandomGenerator.generateInts(1000, 1, 2*50);
		System.out.println();
		for (int i=0; i<50; i++){
			int page = KeyPage.searchKeyPage("file2_KeyPages.bin", keysSearch[i], 50);
			System.out.println("key: "+keysSearch[i]+" page found: "+page);
		}

		double end =  Math.pow(System.nanoTime() - start, -9);
		System.out.print("Time 50 A: " + end);
		System.out.println(".......Disk accesses: 50 A: "+MultiCounter.getCount(3));

	}
}
