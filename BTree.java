import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
		
		public BTreeNode()
		{	
			children = new LinkedList<Integer>();
			keys = new LinkedList<TreeObject>();
			numKeys = 0;
			parent = -1;
		}
		
		public int getParent(){
			return parent;
		}
		
		public void setParent(int parent){
			this.parent = parent;
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
		
		public boolean isLeaf()
		{
			return isLeaf;
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
		
		public int removeChild(int index)
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
	 
	public BTree(int deg, String FileName, boolean useCache, int cacheSize){
		nodeSize = (32* deg - 3);
		this.degree = deg;
		BTreeOffset = 12;
		insertionPoint = (BTreeOffset + nodeSize);
		
		if(useCache){
			//cache stuff!
		}
		
		//root = new BTreeNode();  You are probably correct in this code but to be safe
		BTreeNode n = new BTreeNode();
		root = n;
		root.setOffset(BTreeOffset);
		n.setIsLeaf(true);
		n.setNumKeys(0);
		
		try{
			file = new File(FileName);
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			RAF = new RandomAccessFile(file, "rw");
		}catch(FileNotFoundException e){
			System.err.println("Corrupt file");
			System.exit(-1);
		}
		writeTreeMetadata();
	}


	public BTree(int degree, File filename, boolean useCache, int cacheSize){
		try{
			RAF = new RandomAccessFile(filename, "r"); 
		}
		catch(FileNotFoundException e){
			System.err.println("Corrupt file");
			System.exit(-1);
		}
		readTreeMetadata();
		root = readNode(BTreeOffset);
	}
	

	public BTreeNode getRoot(){
		return root;
	}
	
	public void insert(long k)
	{
		BTreeNode r = this.root;
		
		int i = r.getNumKeys();
		
		if(i == 2*degree-1){  //if r.isfull
			TreeObject o = new TreeObject(k);
			while(i > 0 && o.compareTo(r.getKey(i-1)) < 0){
				i--;
			}if(i > 0 && o.compareTo(r.getKey(i-1)) == 0)
				r.getKey(i-1).increaseFrequency();
			else{
				BTreeNode n = new BTreeNode();
				n.setOffset(r.getOffset());
				root = n;
				r.setOffset(insertionPoint);
				r.setParent(n.getOffset());
				n.setIsLeaf(false);
				n.addChild(r.getOffset());
				splitChild(n,0,r);
				insertNotFull(n,k);
			}
		}else
			insertNotFull(r,k);
		
	}
	
	public void insertNotFull(BTreeNode x, long key){
		int i = x.getNumKeys();
		TreeObject o = new TreeObject(key);
		if(x.isLeaf()){
			if(x.getNumKeys() != 0){
				while(i > 0 && o.compareTo(x.getKey(i-1)) < 0){
					i--;
				}
			}if(i > 0 && o.compareTo(x.getKey(i-1)) == 0){
				x.getKey(i-1).increaseFrequency();
			}else{
				x.addKey(o, i);
				x.setNumKeys(x.getNumKeys()+1);
			}
			writeNode(x,x.getOffset());
		}else{
			while(i > 0 && (o.compareTo(x.getKey(i-1)) < 0)){
				i--;
			}if(i > 0 && o.compareTo(x.getKey(i-1)) == 0){
				x.getKey(i-1).increaseFrequency();
				writeNode(x,x.getOffset());
				return;
			}
			int offset = x.getChild(i);
			BTreeNode y = readNode(offset);
			if(y.getNumKeys() == 2 *degree -1){
				int j = y.getNumKeys();
				while(j > 0 && (o.compareTo(x.getKey(j-1)) < 0)){
					j--;
				}if(j > 0 && o.compareTo(x.getKey(j-1)) == 0){
					y.getKey(j-1).increaseFrequency();
					writeNode(y,y.getOffset());
					return;
				}else{
					splitChild(x,i,y);
					if(o.compareTo(x.getKey(i)) > 0){
						i++;
					}
				}
			}
			offset = x.getChild(i);
			BTreeNode child = readNode(offset);
			insertNotFull(child,key);
		}
	}
	

	public void splitChild(BTreeNode x, int i, BTreeNode y){
		BTreeNode z = new BTreeNode();
		z.setIsLeaf(y.isLeaf());
		z.setParent(y.getParent());
		for(int j = 0; j < degree - 1; j++){
			z.addKey(y.removeKey(degree));
			z.setNumKeys(z.getNumKeys()+1);
			y.setNumKeys(y.getNumKeys()-1);
		}if(!y.isLeaf()){
			for(int j = 0; j < degree; j++){
				z.addChild(y.removeChild(degree));
			}
		}
		x.addKey(y.removeKey(degree-1), i);
		x.setNumKeys(x.getNumKeys()+1);
		y.setNumKeys(y.getNumKeys()-1);
		if(x == root && x.getNumKeys() == 1){
			writeNode(y,insertionPoint);
			insertionPoint += nodeSize;
			z.setOffset(insertionPoint);
			x.addChild(z.getOffset(), i+1);
			writeNode(z, insertionPoint);
			writeNode(x, BTreeOffset);
			insertionPoint += nodeSize; 
		}else{
			writeNode(y,y.getOffset());
			z.setOffset(insertionPoint);
			writeNode(z,insertionPoint);
			x.addChild(z.getOffset(), i+1);
			writeNode(x,x.getOffset());
			insertionPoint += nodeSize;
		}
	}
	
	public TreeObject search(BTreeNode x, Long key)
	{
		int i = 0;
		TreeObject o = new TreeObject(key);
		while(i < x.getNumKeys() && (o.compareTo(x.getKey(i)) > 0)){
			i++;
		}if(i < x.getNumKeys() && o.compareTo(x.getKey(i)) == 0){
			return x.getKey(i);
		}if(x.isLeaf()){
			return null;
		}else{
			int offset = x.getChild(i);
			BTreeNode n = readNode(offset);
			return search(n, key);
		}
	}
	
	public void writeTreeMetadata(){
		try{
			RAF.seek(0);//sets the pointer to the beginning of the file
			RAF.writeInt(degree);
			RAF.writeInt(32*degree-3);
			RAF.writeInt(12);
		}catch(IOException e){
			System.err.println("IO Exception occurred!");
			System.exit(-1);
		}	
	}
	
	private void writeNodeMetadata(BTreeNode x, int offset) {
		try{
			RAF.seek(offset);
			RAF.writeBoolean(x.isLeaf());
			RAF.writeInt(x.getNumKeys());
		}catch(IOException e){
			System.err.println("IO Exception occurred!");
			System.exit(-1);
		}
	}
	
	private void readTreeMetadata() {
		// TODO Auto-generated method stub
		
	}
	
	private BTreeNode readNode(int bTreeOffset2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void writeNode(BTreeNode n, int offset) {
		
	}
	 
	//inOrderPrint
	//inOrderPrintToWriter
	

	
}
