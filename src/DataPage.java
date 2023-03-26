import java.nio.ByteBuffer;


public class DataPage{

    private static final int sizeBytes=256;
    private int bytesOfRecords; //4 bytes size
    private DataClass[] records; //= new DataClass[4]; // number of records depends on size of the records
    private int sizeOfIns;

    public DataPage(DataClass[] records, int sizeOfIns) {
        this.records = records;
        this.bytesOfRecords = records.length;
        this.sizeOfIns=sizeOfIns;
        if (sizeOfIns<bytesOfRecords){
            System.out.println("Overflow");
        }
    }

    public DataPage() {
    }

    @Override
    public String toString(){
        return ("numOfRecords :"+this.bytesOfRecords+" record 1: "+records[0].toString());
    }

    public byte[] convertToByte(){
        ByteBuffer bb = java.nio.ByteBuffer.allocate(this.sizeBytes);
        byte[] bf = new byte[sizeBytes];
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

    public static DataPage convertToPage(byte[] DataPage, int sizeOfRec){
        ByteBuffer bb = ByteBuffer.allocate(sizeOfRec);//byte buffer for records
        int numOfInstances = (int) ((double)sizeBytes/sizeOfRec); //for grouping instances
        DataClass[] dcArr = new DataClass[numOfInstances];
        DataPage dpReturn=new DataPage();
        int j=0;
        for(int i=0; i<sizeOfRec*numOfInstances; i+=sizeOfRec){
            bb.clear();
            System.out.println("DataPage DEBUG i: "+i+" length: "+DataPage.length+" mark "+bb.mark());
            bb.put(DataPage,0,sizeOfRec);
            //checking if null
            
            dcArr[j] = DataClass.convertToObj(bb.array(), sizeOfRec);
            //System.out.println(dcArr[j].toString());
            j++;
        }
        return new DataPage(dcArr, sizeOfRec);
        
    }
    
    public void setBytesOfRecords(int bytesOfRecords) {
        bytesOfRecords = bytesOfRecords;
    }

    public void setRecords(DataClass[] records) {
        this.records = records;
    }

    public void setSizeOfIns(int sizeOfIns) {
        this.sizeOfIns = sizeOfIns;
    }

    public DataClass[] getRecords(){
        return this.records;
    }

    public int getBytesOfRecords(){
        return this.bytesOfRecords;
    }

}