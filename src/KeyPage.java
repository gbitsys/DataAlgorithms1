public class KeyPage implements Comparable{
    private int key;
    private int numOfPage;//index in file of the page we want

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
    
}
