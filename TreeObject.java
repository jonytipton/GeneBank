/**
 * Represents the Tree objects
*/
public class TreeObject implements Comparable<TreeObject>{
	
	private long frequency;			// # of attempts at inserting the key data stored in a TreeObject
	private long data;				// key data stored in a TreeObject
	
	/**
	 * Constructs a TreeObject with specified data key and specified frequency
	 * @param data		-  	long key value
	 * @param frequency	-	number of attempts at inserting TreeObject's key value into the tree
	 */
	public TreeObject(long data, int frequency){
		this.data = data;
		this.frequency = frequency;
	}
	
	/**
	 * Contructs a fresh TreeObject, meaning the key data does not already appear in the tree
	 * @param data
	 */
	public TreeObject(long data){
		this.data = data;
		this.frequency = 1;			// new TreeObject, key data has yet to have been inserted in the tree
	}
	
	/**
	 * Increments a TreeObject's frequency count
	 */
	public void increaseFrequency(){
		this.frequency++;
	}
	   
	/**
	 * Gets a TreeObject's frequency count
	 * @return - long frequency associated with the TreeObject
	 */
	public long getFrequency(){
		return frequency;
	}
	
	/**
	 * Gets a TreeObject's key data
	 * @return	- long key associated with the TreeObject 
	 */
	public long getData(){
		return data;
	}
	
	/**
	 * Sets a TreeObject's key data
	 * @param data	- new long key value to store in the TreeNode's data
	 */
	public void setData(long data){
		this.data = data;
	}
	
	/**
	 * Overriden compareTo
	 * Uses TreeObjects' data to determine comparison relationship
	 */
	public int compareTo(TreeObject newItem){
		if(this.data < newItem.data){
			return -1;
		}else if(this.data > newItem.data){
			return 1;
		}else
			return 0;
	}
	
	/**
	 * Overriden toString method
	 */
	public String toString(){
		String s = "Key: " + data + " frequency: " + frequency;
		return s;
	}
	
}