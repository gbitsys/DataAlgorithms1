import java.io.*;
import java.nio.*;
import java.util.Scanner;


public class FileHandling {
	
	private String fName="file1"; 
	private static final int PAGE_SIZE=256;
	
	private static final int REC_SIZEA=59;
	private static final int DATA_SIZEA=55;
	private static final int KEY_SIZE=4;
	
	private static final int REC_SIZEB=31;
	//private static final int INDEX_SIZE;
	
	public FileHandling(String fileName) {
		this.fName = fileName;
	}

	public static void writePage(DataClass[] dcInstances, String fName, int sizeOfIns) {


		try (RandomAccessFile file = new RandomAccessFile(fName, "rw")) {
			int lengthOf = dcInstances.length;
			int numOfBytes = lengthOf*sizeOfIns;
			int toWrite = (int) Math.ceil((double)numOfBytes/PAGE_SIZE); //how many pages we want
			ByteBuffer bb = ByteBuffer.allocate(PAGE_SIZE);
			int numOfInstances = (int) ((double)PAGE_SIZE/sizeOfIns); //for grouping instances
			
			int i=0;
			int j=0;
			System.out.println("DEBUG: "+numOfBytes + " " + numOfInstances);//DEBUG
			for(int p=0; p<toWrite; p++){
				bb.clear();
				System.out.println("DEBUG 1 Loop");
				DataClass[] insToWrite = new DataClass[numOfInstances];
				while (i<lengthOf && (numOfInstances-j)>0){
					System.out.println("DEBUG 2 Loop");
					insToWrite[j]=dcInstances[i];
					j++;
					i++;
				}
				j=0;
				System.out.println();
				bb.put(new DataPage(insToWrite, sizeOfIns).convertToByte());
				System.out.println(bb.mark());
				file.write(bb.array());
			}
		} catch (IOException e) { //case something wrong happens with our file
			System.out.println("File errror!!!");
			e.printStackTrace();
		}
	}
	
	public static DataPage[] readPages(String filename, int sizeOfRec, int sizeOfIns) {
		try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
			byte[] buffer = new byte[PAGE_SIZE]; // read from disk
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			int recsToRead = (int) Math.floor((double)PAGE_SIZE/sizeOfRec); //max recs 
			int recs = 1;
			while (recs>0){
				file.seek(PAGE_SIZE-4); //moving the cursor to read how many recs we have in this DataPage
				recs=file.readInt();
				for (int i=0; i<recs; i++){
					file.seek(i*sizeOfIns);
					file.read(buffer,0,sizeOfIns);
					DataClass.convertToObj(buffer, sizeOfIns);
				}
			}
			
			return null;
		} catch (IOException e) { //case something wrong happens with our file
			System.out.println("File errror!!!");
			return null;
		}		
	} 

	//creates a page to write
	public byte[] createPage(DataClass[] dcArr, int sizeIns){
		return (new DataPage(dcArr, sizeIns)).convertToByte();
		
	}
}