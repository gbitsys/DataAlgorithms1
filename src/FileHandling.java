import java.io.*;
import java.nio.*;



public class FileHandling {
	
	
	private static final int PAGE_SIZE=256;
	//private static final int INDEX_SIZE;
	
	public FileHandling() {
	}
	public static int writePage(DataClass[] dcInstances, String fName, int sizeOfIns) {


		try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
			int lengthOf = dcInstances.length;
			int numOfBytes = lengthOf*sizeOfIns;
			int numOfInstances = (int) ((double)PAGE_SIZE/sizeOfIns); //for grouping instances
			int toWrite = (int) Math.ceil((double)lengthOf/numOfInstances); //how many pages we want
			//System.out.println("DEBUG pages:"+toWrite);
			ByteBuffer bb = ByteBuffer.allocate(PAGE_SIZE);
			
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
				bb.put(dWrite.convertToByte(),0,(numOfInstances*sizeOfIns));
				bb.putInt(numOfInstances * sizeOfIns,j);
				j=0;
				
				file.write(bb.array());
			}
			System.out.println("DEBUG i:"+i);
			long fileBytes = file.length();
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

	public static int searchKeyFile(String filename, int insSize, int key){
		try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
			int fileSize = (int) file.length(); //multiple of page size
			int maxInstances = (int) ((double)PAGE_SIZE/insSize); //max num of instaces
 			int numOfPages = fileSize/PAGE_SIZE; //num of pages written in the file
			int seekPos=0; //our cursor position for reading  purposes
			byte[] buffer = new byte[insSize]; //in this buffer we read the page and then modification takes in

			file.seek(fileSize-4);
			int recsToRead = file.readInt(); //recs we want to read given from file at 4 last bytes

			DataClass[] dcArr = new DataClass[recsToRead];
			file.seek(0);//we are going to read from the beggining
			int curPage = (maxInstances*insSize);

			MultiCounter.resetCounter(1); //reset counter for "disk accesses"
			for (int i=0; i<numOfPages; i++){
				file.seek((i*PAGE_SIZE)+curPage);
				int recsInPage=file.readInt();
				MultiCounter.increaseCounter(1);//increase counter to measure "disk accesses"
				//System.out.println(recsInPage);
				for (int j=0; j<recsInPage;j++){
					file.seek(seekPos);
					file.read(buffer);
					dcArr[j]=DataClass.convertToObj(buffer, insSize);
					//System.out.println("DEBUG readPages: "+dcArr[j].toString()+" seekPos: "+seekPos);
					seekPos+=insSize;
					if(dcArr[j].getKey()==key)
						return i;//index of page
				}
				seekPos=(i+1)*PAGE_SIZE;
			}
			return -1;

			}catch (IOException e) { //case something wrong happens with our file
				System.out.println("File errror!!!");
				return -1;
			}	
		}

	//creates a page to write
	public byte[] createPage(DataClass[] dcArr, int sizeIns){
		return (new DataPage(dcArr, sizeIns)).convertToByte();
		
	}
	public static KeyPage[] writePageKp(DataClass[] dcInstances, String fName, int sizeOfIns) {


		try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
			int lengthOf = dcInstances.length;
			int numOfBytes = lengthOf*sizeOfIns;
			int numOfInstances = (int) ((double)PAGE_SIZE/sizeOfIns); //for grouping instances
			int toWrite = (int) Math.ceil((double)lengthOf/numOfInstances); //how many pages we want
			ByteBuffer bb = ByteBuffer.allocate(PAGE_SIZE);
			
			KeyPage[] kpArr = new KeyPage[dcInstances.length]; //for key page file
			file.setLength((toWrite+4));
			int i=0;
			int j=0;
			//System.out.println("DEBUG: "+numOfBytes + " " + numOfInstances);//DEBUG
			
			for(int p=0; p<toWrite; p++){
				bb.clear();
				DataClass[] insToWrite = new DataClass[numOfInstances];
				
				while (i<lengthOf && (numOfInstances-j)>0){
					insToWrite[j]=dcInstances[i];
					kpArr[i] = new KeyPage(dcInstances[i].getKey(), p);
					j++;
					i++;
				}
				
				//System.out.println();
				DataPage dWrite = new DataPage(insToWrite, sizeOfIns);
				bb.put(dWrite.convertToByte(),0,(numOfInstances*sizeOfIns)-4);
				bb.putInt(numOfInstances * sizeOfIns,j);
				j=0;

				file.write(bb.array());
			}
			//System.out.println("DEBUG write file bytes: "+file.length());
			long fileBytes = file.length();
			file.seek(fileBytes-4);
			file.writeInt(lengthOf);
			file.close();
			return kpArr;
			
		} catch (IOException e) { //case something wrong happens with our file
			System.out.println("File errror!!!");
			e.printStackTrace();
			return null;
		}
	}

	public static DataClass bRoadFind(String fName, int pageNum, int recSize,int searchKey){
		if (pageNum < 0){
			return null;
		}
		try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
			int numOfInstances = (int) ((double)PAGE_SIZE/recSize); //for grouping instances
			byte[] buffer = new byte[recSize];
			long filePos = PAGE_SIZE*pageNum;
			file.seek(filePos);
			MultiCounter.resetCounter(3);
			MultiCounter.increaseCounter(3);
			for (int i=0; i < numOfInstances; i++){
				file.read(buffer);
				DataClass dc = DataClass.convertToObj(buffer, recSize);
				int key = dc.getKey();
				if (key==searchKey){
					return dc;
				}
				filePos+=recSize;
			}
			return null;
		} catch (Exception e) {
			System.out.println("File error!!!");
			return null;
		}
	}
}