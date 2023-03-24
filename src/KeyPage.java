import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

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
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
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
            int toWrite = (int) Math.ceil((double)kpArr.length*8/256);
            int numOfInstances = (int) ((double)256/8); //for grouping instances
            //indices
            int arrIndex = 0;
            int pageIndex = 0;
    
            ByteBuffer bb = ByteBuffer.allocate(256);
            while(pageIndex < toWrite && arrIndex < numOfInstances){
                if(bb.remaining()<8){ //checking if page is written
                    bb.clear();
                    file.write(bb.array());
                    MultiCounter.increaseCounter(2);
                    pageIndex++;
                }
                bb.putInt(kpArr[arrIndex].getKey());
                bb.putInt(kpArr[arrIndex].getNumOfPage());
                arrIndex++;
            }

            if (MultiCounter.getCount(2)<toWrite)
                file.write(bb.array());
            
                MultiCounter.resetCounter(2);
                MultiCounter.increaseCounter(2, toWrite);
                file.close();
            
        } catch (Exception e) {
            System.out.println("file error!!!");
        }

    }
}
