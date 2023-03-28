import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class KeyPage implements Comparable{
    private int key;
    private int numOfPage;//index in file of the page we want
    /*total size is 8 bytes 
    we can write 32 instances in a Page
    */
    

    public KeyPage(int key, int numOfPage) {
        this.key = key;
        this.numOfPage = numOfPage;
    }

    @Override
    public int compareTo(Object arg0) {
        KeyPage kp = (KeyPage) arg0;
        if (this.getKey()>kp.getKey()){
            return 1;
        }
        if (this.getKey()<kp.getKey()){
            return -1;
        }
        else { //unreachable
            return 0;
        }
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
    public int getNumOfPage() {
        return numOfPage;
    }

    public static int binarySearch(String fName, int keySearch, int numOfPairs){
        try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
            int fileSize =(int)Math.ceil((double)numOfPairs/32)*256;//total bytes 
            //lowest page
            int low = 0;

            //highest page
            int high = (fileSize/256)-1;
            int key=-1;
            int numOfPage=-1;
            String index="0";
            byte[] buffer = new byte[8];
            int prevMid = -1;
            MultiCounter.resetCounter(5); //reset out counter
            while(low<=high){
                int mid = (int)Math.floor(low + (((double)high - low) / 2));
                file.seek(mid*256);
                MultiCounter.increaseCounter(5);
                int prevKey=-1;
                for (int i=0; i<32; i++){
                    file.read(buffer); //our instance
                    
                    KeyPage kp = KeyPage.convertBytesToKp(buffer);
                    key = kp.getKey();
                    numOfPage = kp.getNumOfPage();
                    if (key == keySearch && prevKey != key){
                        file.close();
                        return numOfPage;
                    }
                    if ((prevKey > keySearch && prevKey > key) || (key > keySearch && i==31)){
                        index = "lower"; //where to search
                        high = mid - 1;
                        break;
                    }
                    if ((prevKey < keySearch && prevKey > key) ||(key < keySearch && i==31)){
                        index = "higher"; //where to search
                        low = mid + 1;
                        break;
                    }
                    if ((i==31 && low==high) || mid==prevMid){
                        return -1;
                    }
                    prevKey=key;
                }
                prevMid = mid;
            }
            file.close();
            return -1;

            /*while (low <= high) {
                int mid = low  + ((high - low) / 2);
                if (sortedArray[mid] < key) {
                    low = mid + 1;
                } else if (sortedArray[mid] > key) {
                    high = mid - 1;
                } else if (sortedArray[mid] == key) {
                    index = mid;
                    break;
                }
            }
            return index;*/


        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("File error!!! binarySearch");
           // e.printStackTrace();
            return -1;
        }
    }

    public void setNumOfPage(int numOfPage) {
        this.numOfPage = numOfPage;
    }
    
    public static byte[] convertKpToBytes(KeyPage kp){
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putInt(kp.getKey());
        bb.putInt(kp.getNumOfPage());
        return bb.array();
    }

    public static KeyPage convertBytesToKp(byte[] kpBytes){
        ByteBuffer bb = ByteBuffer.wrap(kpBytes);
        int key = bb.getInt();
        int numOfPage = bb.getInt();
        return new KeyPage(key, numOfPage);
    }
    
    public static void writeKpFile(KeyPage[] kpArr, String fName){
        try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
            int numOfInstances = (int) ((double)256/8); //for grouping instances
            int toWrite = (int) Math.ceil((double)kpArr.length/32);

            //indices
            int arrIndex = 0;
            int pageIndex = 0;
            file.setLength(toWrite*256);
            ByteBuffer bb = ByteBuffer.allocate(256);
            while(pageIndex < toWrite && arrIndex < kpArr.length){
                if(bb.remaining()<8){ //checking if page is written
                    bb.clear();
                    file.write(bb.array());          
                    pageIndex++;
                }
                bb.putInt(kpArr[arrIndex].getKey());
                bb.putInt(kpArr[arrIndex].getNumOfPage());
                arrIndex++;
            }

            if (arrIndex==kpArr.length && bb.remaining()>0){
                bb.put(new byte[bb.remaining()]);
            }
                file.write(bb.array());
                file.close();
            
        } catch (Exception e) {
            System.out.println("file error!!!");
        }

    }


    /**
     * @param fName
     * @param keySearch
     * @param numOfPairs
     * @return -1: key not found, positive int: key position in page
     */
    public static int searchKeyPage(String fName, int keySearch, int numOfPairs){
        try (RandomAccessFile file = new RandomAccessFile(fName, "rws")) {
            byte[] buffer = new byte[8];
            int pairIndex = 0;
            int pairIndexTotal = 0;
            int fileSize =(int)Math.ceil((double)numOfPairs/32)*256;

            int key = -1;
            int numOfPage = -1;
            long seekPos = 0;
            MultiCounter.resetCounter(2);
            for (long i=0; i < fileSize; i+=256){
                file.seek(seekPos*i);
                MultiCounter.increaseCounter(2);
                while(pairIndexTotal<numOfPairs && pairIndex<32){
                    file.read(buffer);
                    KeyPage kp = KeyPage.convertBytesToKp(buffer);
                    key = kp.getKey();
                    numOfPage = kp.getNumOfPage();
                    pairIndex++;
                    pairIndexTotal++;
                    if (key == keySearch){
                        file.close();
                        return numOfPage;
                    }
                }
                pairIndex=0;
                seekPos=i+1;
            }
            return -1;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("File error!!!");
            return -1;
        }
    }
}
