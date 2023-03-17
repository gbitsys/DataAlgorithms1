import java.nio.ByteBuffer;
import java.util.*;

public class DataPage{

    private static final int sizeBytes=256;
    private int numOfRecords; //4 bytes size
    private DataClass[] records; //= new DataClass[4]; // number of records depends on size of the records
    private int sizeOfIns;

    public DataPage(DataClass[] records, int sizeOfIns) {
        this.records = records;
        this.numOfRecords = records.length;
        this.sizeOfIns=sizeOfIns;
        if (sizeOfIns<numOfRecords){
            System.out.println("Overflow");
        }
    }

    public byte[] convertToByte(){
        ByteBuffer bb = java.nio.ByteBuffer.allocate(this.sizeBytes);
        byte[] bf = new byte[256];
        bb.put(bf);
        bb.rewind();
        int numOfInstances = 0;
        for (DataClass dc : records){
            if (dc!=null){
                bb.putInt(dc.getKey());
                bb.put(dc.getDataString().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
                numOfInstances++;
            }
        }
        bb.putInt(numOfInstances);
        return bb.array();
    }
    
    public DataClass[] getRecords(){
        return this.records;
    }

    public int getNumOfRecords(){
        return this.numOfRecords;
    }

}