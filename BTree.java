import java.util.LinkedList;

public class BTree{

	private int degree;
	private BTreeNode root;
	private int BTreeOffset;
	private int nodeSize;
	private int insertionPoint;
	private File file;
	private RandomAccessFile RAF;
	
	private class BTreeNode
	{
		private int parent;
		private LinkedList<TreeObject> keys;
		private LinkedList<Integer> children;
		private int numKeys;
		private int offset;
		private boolean isLeaf;
		
		//not sure if we need this vars
		private boolean isFull;
		private int degree;
		private boolean root;
		//private int level; offset may be better to use based on the write up
		
		public BTreeNode()
		{	
			children = new LinkedList<Integer>();
			keys = new LinkedList<TreeObject>();
			numKeys = 0;
			parent = -1;
			//not sure if we need these three below
			leaf = true;
			isFull = false;
			level = 0;
		}
		
		public int getParent(){
			return parent;
		}
		
		public void setParent(int parent){
			this.parent = parent;
		}
		
		public long getKey(long findKey)
		{
			return;
		}
		
		public int getOffset()
		{
			return offset;
		}
		
		public void setOffset(int offset)
		{
			this.offset = offset;
		}
		
		public TreeObject getKey(int i){
			TreeObject obj = keys.get(i);
			return obj;
		}
		
		public void addKey(TreeObject o)
		{
			keys.add(o);
		}
		
		public void addKey(TreeObject o, int i)
		{
			keys.add(i, o);
		}
		
		public TreeObject removeKey(int i){
			return keys.remove(i);
		}
		
		public LinkedList<TreeObject> getKeys()
		{
			return keys;
		}
		
		public int getNumKeys()
		{
			return numKeys;
		}
		
		public void setNumKeys(int numKeys){
			this.numKeys = numKeys;
		}
		
		public boolean isRoot()
		{
			return root;
		}
		
		public boolean isLeaf()
		{
			return isLeaf;
		}
		
		public boolean isFull()
		{
			return isFull();
		}
		
		public void setIsLeaf(boolean isLeaf)
		{
			this.isLeaf = isLeaf;
		}
		
		public LinkedList<Integer> getChildren()
		{
			return children;
		}
		
		public void addChild(int index)
		{
			children.add(index);
		}
		
		public void addChild(Integer a, int index)
		{
			children.add(index, a);
		}
		
		public int getChild(int index)
		{
			return children.get(index).intValue();
		}
		
		public BTreeNode removeChild(int index)
		{
			return children.remove(index);
		}
		
		public String toString(){
			String s = new String();
			s += "Keys: ";
			for(int i = 0; i < keys.size(); i++){
				s+= (keys.get(i) + " ");
			}
			s += "\nchildren: ";
			for(int i = 0; i < children.size(); i++){
				s += (children.get(i) + " " );
			}
			return s;
		}
	}
	
	public BTree(int degree)
	{
		this.degree = degree;
		
		root = new BTreeNode();
	}
	
	public void insert(long k)
	{
		BTreeNode r = this.root;
		
		int i = r.numKeys;
		
		if(r.isFull)
		{
			
		}
		
	}
	
	public void insertFull()
	{
		
	}
	
	public void splitChild()
	{
		
	}
	
	public Long search(BTreeNode x, Long key)
	{
		return 
	}
	
	public BTreeNode getRoot()
	{
		return root;
	}
	
	
}
