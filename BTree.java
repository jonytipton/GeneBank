public class BTree
{
	
	


	//n[x] = # of keys currently stored
	//leaf[x] =  leaf node or nah
	//keys are stored in accending order
	//contains n[x]+1 children
	private class BTreeNode<E>
	{
		public E key[];
		public int numKeys;
		boolean root;
		boolean leaf;
		boolean isFull;
		public BTreeNode children[];
		
		public BTreeNode(E key, int degree)
		{
			leaf = true;
			children = new BTreeNode[2 * (degree) - 1];
		}
		public BTreeNode(E key)
		{
			leaf = true;	
		}

		public E getKey()
		{
			for(int i = 0; i < key.length; i++)
			{
				return key[i];
			}
		}

		public void setKey(E key)
		{
			this.key = key;
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
	}
}
