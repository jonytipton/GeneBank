import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * class contains the BTreeNode structure
 */

public class BTree{

	private int degree;
	private BTreeNode root;
	private int BTreeOffset;
	private int nodeSize;
	private int insertionPoint;
	private File file;
	private RandomAccessFile RAF;
	private BTreeCacheNode.BTreeCache cache;
	private BTreeCacheNode BTCN;
	private Cache MattCache;
	
	/**
     * SubClass that contains constructor for BTreeNode
     */
	class BTreeNode
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
	
	/*
     * BTree constrcutor
     * @params deg degree of the tree
     * @params FileName simple filename
     * @params useCache boolean value whether using cache or not
     * @params cacheSize how large the cache implementation should be
     */
	public BTree(int deg, String FileName, boolean useCache, int cacheSize){
		nodeSize = (32* deg - 3);
		this.degree = deg;
		BTreeOffset = 12;
		insertionPoint = (BTreeOffset + nodeSize);
		
		if(useCache){
			BTCN = new BTreeCacheNode(root, 0 , cacheSize);
			cache = BTCN.BTC;
			//MattCache = new Cache(cachesize); 
			
		}
		
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

	 /**
     * Instantiates a BTree
     *
     * @param fileName filename that should be used
     */
	public BTree(File filename){
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
	
	public BTree() {super();}
	
	/**
     * Finds and returns root of the BTreeNode
     *
     * @return root of tree
     */
	public BTreeNode getRoot(){
		return root;
	}
	
	/**
     * Inserts into the BTree
     *
     * @param k to be inserted
     */ 
	 void insert(long k)
	{
		BTreeNode r = this.root;
		
		int i = r.getNumKeys();
		
		if(i == 2*degree-1){ 
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

	 /**
	  * Inserts into the BTree ensuring the btree is not full
	  *
	  * @param key     to be inserted
	  * @param x the BTreeNode to be inserted
	  */
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
	
	 /**
	  * Splits child node
	  *
	  * @param x the btree Node to be split
	  * @param y       the node that will be split
	  * @param i       the degree index
	  */
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
	
	/**
     * Search the BTree
     *
     * @param key     to be inserted
     * @param x		 the node to be found
     */
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
	
	/**
     * Writes the metadata of the tree, including the degree ect.
     */
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
	/**
     * Writes the Node to the metadata, using the offset
     * @param x		the Node being written to metadata
     * @param offset	the offset of the node
     */
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
	
	 /**
     * Reads the meta data from file
     */
	private void readTreeMetadata() {
		try{
			RAF.seek(0);;
			degree = RAF.readInt();
			nodeSize = RAF.readInt();
			BTreeOffset = RAF.readInt();
		}catch(IOException e){
			System.err.println("IO Exception occurred!");
			System.exit(-1);
		}
	}
	

    /** Reads a cached node
    *
    * @param offset of the nodes
    */
	private BTreeNode readNode(int offset) {
		BTreeNode y = null;
		//if node is cached we can read it from there
		if(cache != null){
			y = cache.readNode(offset);
		}if (y != null){
			return y;
		}
		y = new BTreeNode();
		TreeObject o = null;
		y.setOffset(offset);
		int j = 0;
		try{
			RAF.seek(offset);
			boolean isLeaf = RAF.readBoolean();
			y.setIsLeaf(isLeaf);
			int n = RAF.readInt();
			y.setNumKeys(n);
			int parent = RAF.readInt();
			y.setParent(parent);
			for(j = 0; j < 2 *degree-1; j++){
				if(j < y.getNumKeys() + 1 && !y.isLeaf()){
					int child = RAF.readInt();
					y.addChild(child);
				}else if(j >= y.getNumKeys() + 1 || y.isLeaf()){
					RAF.seek(RAF.getFilePointer() + 4);
				}if(j < y.getNumKeys()){
					long value = RAF.readLong();
					int frequency = RAF.readInt();
					o = new TreeObject(value, frequency);
					y.addKey(o);
				}
			}if(j == y.getNumKeys() && !y.isLeaf()){
				int child = RAF.readInt();
				y.addChild(child);
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		return y;
	}
	
	/**
     * Writes a node pushed off the cache
     *
     * @param n the node toe be written
     */
	private void writeNode(BTreeNode n, int offset) {
		if(cache != null){
			BTreeNode cnode = cache.add(n,offset);
			if(cnode != null) 
				writeNodeToFile(cnode,cnode.getOffset());
		}else{
			writeNodeToFile(n,offset);
		}
	}
	
	/**
     * cache clearing
     */
	public void flushCache(){
		if(cache != null){
			for(BTreeNode cnode : cache){
				writeNodeToFile(cnode, cnode.getOffset());
			}
		}
	}
	
	/**
     * Writes a node pushed off the cache
     *
     * @param n 	the node to be written
     * @param offest the offset of the node 
     */
	private void writeNodeToFile(BTreeNode n, int offset){
		int i = 0;
		try{
			writeNodeMetadata(n,n.getOffset());
			RAF.writeInt(n.getParent());
			for(i = 0; i< 2*degree-1; i++){
				if(i < n.getNumKeys() + 1 || n.isLeaf()){
					RAF.writeInt(0);
				}if(i < n.getNumKeys()){
					long data = n.getKey(i).getData();
					RAF.writeLong(data);
					int freq = n.getKey(i).getFrequency();
					RAF.writeInt(freq);
				}else if(i >= n.getNumKeys() || n.isLeaf()){
					RAF.writeLong(0);
				}
			}if(i == n.getNumKeys() && !n.isLeaf()){
				RAF.writeInt(n.getChild(i));
			}
		}catch(IOException e){
			System.err.println("IO Exception");
			System.exit(-1);
		}
	}
	 
	public void inOrderPrint(BTreeNode node){
		System.out.println(node);
		if(node.isLeaf() == true){
			for(int i = 0; i < node.getNumKeys(); i++){
				System.out.println(node.getKey(i));
			}
			return;
		}
		
		for(int i = 0; i < node.getNumKeys(); i++){
			int offset = node.getChild(i);
			BTreeNode y = readNode(offset);
			inOrderPrint(y);
			if(i < node.getNumKeys()){
				System.out.println(node.getKey(i));
			}
		}
	}
	
	/**
     * Prints to file, instantiating a printWriter
     *
     * @param node           the node to be printed
     * @param pwriter         the setup instantiation of PrintWriter
     * @param sequenceLength how long each DNA sequence should be
     */
	public void inOrderPrintToWriter(BTreeNode node, PrintWriter pwriter, int sequenceLength) throws IOException{
		GBFileConvert gbc = new GBFileConvert();
		for( int i = 0; i < node.getNumKeys(); i++){
			pwriter.println(node.getKey(i).getFrequency() + " ");
			pwriter.println(gbc.convertToString(node.getKey(i).getData(), sequenceLength));	
		}if(!node.isLeaf()){
			for(int i = 0; i < node.getNumKeys() + 1; ++i){
				int offset = node.getChild(i);
				BTreeNode n = readNode(offset);
				inOrderPrintToWriter(n,pwriter,sequenceLength);
				if(i < node.getNumKeys()){
					pwriter.print(node.getKey(i).getFrequency() + " ");
					pwriter.println(gbc.convertToString(node.getKey(i).getData(), sequenceLength));	
				}
			}
		}
	}
	
	public String convert(long key){
		String result = "";
		if(key == -1){
			return result;
		}
		String temp = "";
		String altTemp = "";
		altTemp = Long.toBinaryString(key);
		for(int i = insertionPoint * 2; i > 1; i -= 2){
			try{
				temp = altTemp.substring(i -1, i + 1);
				if(temp.equals("00")) result = result + "A";
				else if (temp.equals("11")) result = result + "T";
				else if (temp.equals("01")) result = result + "C";
				else if (temp.equals("10")) result = result + "G";
			}catch(StringIndexOutOfBoundsException e){
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
