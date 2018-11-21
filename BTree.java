import java.util.LinkedList;

public class BTree
{
	
	private int degree;
	private BTreeNode root;
	
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
	
	private class BTreeNode
	{
		private LinkedList<Long> key;
		private int numKeys;
		private boolean root;
		private boolean leaf;
		private boolean isFull;
		private LinkedList<BTreeNode> children;
		private int degree;
		private int parent;
		private int level;
		
		public BTreeNode()
		{	
			children = new LinkedList<BTreeNode>();
			key = new LinkedList<Long>();
			leaf = true;
			isFull = false;
			parent = -1;
			level = 0;
		}

		public long getKey(long findKey)
		{
			return;
		}
		
		public int getLevel()
		{
			return level;
		}
		
		public void setLevel(int newLevel)
		{
			this.level = newLevel;
		}
		
		public void addKey(long newKey)
		{
			key.add(newKey);
		}
		
		public int getNumKeys()
		{
			return numKeys;
		}
		
		public boolean isRoot()
		{
			return root;
		}
		
		public boolean isLeaf()
		{
			return leaf;
		}
		
		public boolean isFull()
		{
			return isFull();
		}
		
		public void setIsLeaf(boolean isLeaf)
		{
			this.leaf = isLeaf;
		}
		
		public LinkedList<Long> getKey()
		{
			return key;
		}
		
		public LinkedList<BTreeNode> getChildren()
		{
			return children;
		}
		
		public void addChild(BTreeNode child)
		{
			children.add(child);
		}
		
		public BTreeNode getChild(int index)
		{
			return children.get(index);
		}
		
		public BTreeNode removeChild(int index)
		{
			return children.remove(index);
		}
	}
}
