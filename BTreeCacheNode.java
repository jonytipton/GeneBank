import java.util.Iterator;
import java.util.LinkedList;
/**
* Creates BTreeCache node and maintains it through methods
*/
public class BTreeCacheNode extends BTree
{
    BTreeCache bTreeCache;

    public BTreeCacheNode(int max)
    {
        this.bTreeCache = new BTreeCache(max);
    }

    public class BTreeCache implements Iterable<BTreeNode>
    {

        private final int MAX_SIZE;
        private LinkedList<BTreeNode> list;

        BTreeNode add(BTreeNode addNode)
        {
            BTreeNode tempNode = null;
            if (isFull())
            {
                tempNode = list.removeLast();
            }

            list.addFirst(addNode);
            return tempNode;
        }

        BTreeNode readNode(int offset)
        {
            for (BTreeNode readNode : list)
            {
                if (readNode.getOffset() == offset)
                {
                    list.remove(readNode);
                    list.addFirst(readNode);
                    return readNode;
                }
            }
            return null;
        }

        BTreeCache(int MAX_SIZE)
        {
            this.MAX_SIZE = MAX_SIZE;

            list = new LinkedList<>();
        }

        int getSize()
        {
            return list.size();
        }

        boolean isFull()
        {
            return getSize() == MAX_SIZE;
        }

        @Override
        public Iterator<BTreeNode> iterator()
        {
            return list.iterator();
        }
    }
}
