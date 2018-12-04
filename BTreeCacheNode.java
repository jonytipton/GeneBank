import java.util.Iterator;
import java.util.LinkedList;

/**
 * Creates BTreeCache node and maintains it through methods.
 */

public class BTreeCacheNode extends BTree{
	private BTreeNode data;
	private int offset;
	public BTreeCache BTC;
	
	public class BTreeCache implements Iterable<BTreeNode>{
		
		private final int MAX_SIZE;
		private int numHits, numMisses;
		
		private LinkedList<BTreeNode> list;
		
		public BTreeCache(int MAX_SIZE){
			this.MAX_SIZE = MAX_SIZE;
			list = new LinkedList<BTreeNode>();
		}
		
		public BTreeNode add(BTreeNode o, int offset){
			BTreeNode rnode = null;
			if(isFull()){
				rnode = list.removeLast();
			}
			list.addFirst(o);
			return rnode;
		}
		
		public void clearCache(){
			list.clear();
		}
		
		public BTreeNode readNode(int offset){
			for(BTreeNode n: list){
				if(n.getOffset() == offset){
					list.remove(n);
					list.addFirst(n);
					increaseHits();
					return n;
				}
			}
			increaseMiss();
			return null;
		}
		
		public int getNumReferences(){
			return this.numHits + this.numMisses;
		}
		
		private void increaseHits(){
			this.numHits++;
		}
		
		private void increaseMiss(){
			this.numMisses++;
		}
		
		public int getHits(){
			return numHits;
		}
		
		public int getNumMiss(){
			return numMisses;
		}
		
		public double getHitRatio(){
			double ratio = ((double) getHits()) / getNumReferences();
			return ratio;
		}
		
		public int getSize(){
			return list.size();
		}
		
		public boolean isFull(){
			return getSize() == this.MAX_SIZE;
		}
		
		public Iterator<BTreeNode> iterator() {
			return list.iterator();
		}
	}
	
	public BTreeCacheNode(BTreeNode data, int offset, int max){
		this.data = data;
		this.offset = offset;
		this.BTC = new BTreeCache(max);
	}
	
	public BTreeNode getData(){
		return data;
	}
	
	public int getOffset(){
		return offset;
	}

}
