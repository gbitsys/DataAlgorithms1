import java.io.*;
import java.lang.module.ModuleDescriptor.Builder;
import java.nio.*;
import java.util.ArrayList;
import java.util.*;



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

	public static int writePage(DataClass[] dcInstances, String fName, int sizeOfIns) {


		try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
			int lengthOf = dcInstances.length;
			int numOfBytes = lengthOf*sizeOfIns;
			int toWrite = (int) Math.ceil((double)numOfBytes/PAGE_SIZE); //how many pages we want
			ByteBuffer bb = ByteBuffer.allocate(PAGE_SIZE);
			int numOfInstances = (int) ((double)PAGE_SIZE/sizeOfIns); //for grouping instances
			file.setLength((toWrite+4));
			int i=0;
			int j=0;
			//System.out.println("DEBUG: "+numOfBytes + " " + numOfInstances);//DEBUG
			for(int p=0; p<toWrite; p++){
				bb.clear();
				DataClass[] insToWrite = new DataClass[numOfInstances];
				while (i<lengthOf && (numOfInstances-j)>0){
					insToWrite[j]=dcInstances[i];
					j++;
					i++;
				}
				
				//System.out.println();
				DataPage dWrite = new DataPage(insToWrite, sizeOfIns);
				bb.put(dWrite.convertToByte(),0,(numOfInstances*sizeOfIns)-4);
				bb.putInt(numOfInstances * sizeOfIns,j);
				j=0;
				/*try{
					bb.putInt(PAGE_SIZE-4,dWrite.getNumOfRecords());
				}catch(Exception e){

				}*/

				file.write(bb.array());
			}
			System.out.println("DEBUG write file bytes: "+file.length());
			int fileBytes = (int)file.length();
			file.seek(fileBytes-4);
			file.writeInt(lengthOf);
			file.close();
			return toWrite;
			
		} catch (IOException e) { //case something wrong happens with our file
			System.out.println("File errror!!!");
			e.printStackTrace();
			return -1;
		}
	}
	//reading whole page and returns every Page in an array
	public static DataPage[] readPages(String filename, int insSize) {
		try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
			int fileSize = (int) file.length(); //multiple of page size
			int maxInstances = (int) ((double)PAGE_SIZE/insSize); //max num of instaces
 			int numOfPages = fileSize/256; //num of pages written in the file
			int seekPos=0; //our cursor position for reading  purposes
			DataPage[] dpArr = new DataPage[fileSize/PAGE_SIZE];

			file.seek(fileSize-4);
			int recsToRead = file.readInt(); //recs we want to read given from file at 4 last bytes

			DataClass[] dcArr = new DataClass[recsToRead];
			file.seek(0);//we are going to read from the beggining
			int curPage = (maxInstances*insSize);
			byte[] buffer = new byte[insSize]; //in this buffer we read the page and then modification takes in
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			for (int i=0; i<fileSize/PAGE_SIZE; i++){
				file.seek((i*PAGE_SIZE)+curPage);
				int recsInPage=file.readInt();
				System.out.println(recsInPage);
				
				for (int j=0; j<recsInPage;j++){
					file.seek(seekPos);
					file.read(buffer);
					dcArr[j]=DataClass.convertToObj(buffer, insSize);
					System.out.println("DEBUG readPages: "+dcArr[j].toString()+" seekPos: "+seekPos);
					seekPos+=insSize;
				}
				seekPos=(i+1)*PAGE_SIZE;
			}
			/*System.out.println(file.readInt());
			file.seek(4);
			file.read(buffer);
			bb.get(buffer);
			String  man = new String(buffer, java.nio.charset.StandardCharsets.US_ASCII);
			System.out.println(man);
			*/
			return dpArr;
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