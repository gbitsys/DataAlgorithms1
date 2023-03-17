import java.nio.ByteBuffer;

public class DataClass implements Comparable{
	
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
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String toString(){
		return ("key: "+ this.key+" data: "+this.dataStr);
	}

	//method that takes a byte array and converts it into an Object of DataClass
	public static DataClass convertToObj(byte[] bArray, int size) {
		byte[] buffer=new byte[size]; // read from disk
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		int key = bb.getInt(); //position moves 4 bytes to the front
		byte byteArray[] = new byte[size-4];
		bb.get(byteArray);
		String dataString = new String(byteArray, java.nio.charset.StandardCharsets.US_ASCII);
		DataClass dc = new DataClass(key, dataString);
		return dc;
		}


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
