import java.nio.ByteBuffer;

public class DataClass{
	
	private int key;//size 4 bytes
	private String dataStr;
	
	//constructor (size of data String)
	public DataClass(int size) {
		dataStr=randomGen(size);
	}
	
	public DataClass(int key, String dataString) {
		this.dataStr = dataString;
		this.key = key;
	}

	//a method that generates our 'random' ascii string for dataStr
	private String randomGen(int size) {
		return(RandomGenerator.generateASCII(size));
	}

	@Override
	public String toString(){
		return ("key :"+ this.key+" data: "+this.dataStr);
	}

	//method that takes a byte array and converts it into an Object of DataClass
	public static DataClass convertToObj(byte[] bArray, int size) {
		ByteBuffer bb = ByteBuffer.wrap(bArray);
		int key = bb.getInt(); //position moves 4 bytes to the front
		byte byteArray[] = new byte[size-4];
		byteArray = bb.get(bArray,4,size-4).array();
		String dataString = new String(byteArray, java.nio.charset.StandardCharsets.US_ASCII);
		DataClass dc = new DataClass(key, dataString);
		//System.out.println("DEBUG convToObj :"+dc.toString()+" size: "+size);
		return dc;
		}
	
	public static byte[] convertToByte(DataClass dc){
		ByteBuffer bb = ByteBuffer.allocate(dc.getDataString().length()+4);
		int someInt = dc.getKey();
		String someString = dc.getDataString();
		bb.putInt(someInt);
		bb.put(someString.getBytes(java.nio.charset.StandardCharsets.US_ASCII));
		return bb.array();
	}

	/*public static byte[] extracted(DataClass[] dcArr, int dataSize) {
		ByteBuffer bb = ByteBuffer.allocate(dcArr.length*dataSize);
        for (DataClass dc : dcArr){
            if (dc!=null){
                bb.putInt(dc.getKey());
                bb.put(dc.getDataString().getBytes(java.nio.charset.StandardCharsets.US_ASCII));
				//System.out.println("DEBUG DC "+dc.toString()+" mark "+bb.mark());
            }
        }
		
		System.out.println("DEBUG extracted:  ");
        return bb.array();
    }*/

	//getters and setters
	public int getKey() {
		return key;
	}

	public String getDataString() {
		return dataStr;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public void setDataString(String dataStr, int size){
		this.dataStr = this.randomGen(size);
	}
}
