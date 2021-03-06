public class TreeObject implements Comparable<TreeObject>{
	
	private int frequency;
	private long data;
	
	public TreeObject(long data, int frequency){
		this.data = data;
		this.frequency = frequency;
	}
	
	public TreeObject(long data){
		this.data = data;
		this.frequency = 1;
	}
	
	public void increaseFrequency(){
		this.frequency++;
	}
	   
	public int getFrequency(){
		return frequency;
	}
	
	public long getData(){
		return data;
	}
	
	public int compareTo(TreeObject newItem){
		return Long.compare(data, newItem.data);
	}
	
	public String toString(){
		String s = new String();
		s+= "Key: " + data + " frequency: " + frequency;
		return s;
	}
	
}
