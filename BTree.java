import java.io.File;  
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.LinkedList;

/**
 * class contains the BTreeNode structure
 */

public class BTree
{
	private int degree;
    private BTreeNode root;
    private int offset;
    private int nodeSize;
    private BTreeCacheNode.BTreeCache cache;
    private RandomAccessFile accessFile;
    private int insertionPoint;

    /**
     * Inner Class that contains constructor for BTreeNode
     */
    class BTreeNode
    {

        private int parent;
        private LinkedList<TreeObject> keys;
        private LinkedList<Integer> children;
        private int numKeys;
        private int offset;
        private boolean isLeaf;


        BTreeNode()
        {
            keys = new LinkedList<>();
            children = new LinkedList<>();
            numKeys = 0;
            parent = -1;
        }

        void setIsLeaf(boolean isLeaf)
        {
            this.isLeaf = isLeaf;
        }

        boolean isLeaf()
        {
            return isLeaf;
        }

        int getKeyCount()
        {
            return numKeys;
        }

        void setKeyCount(int keyCount)
        {
            this.numKeys = keyCount;
        }

        int getOffset()
        {
            return offset;
        }

        void setOffset(int offset)
        {
            this.offset = offset;
        }

        TreeObject getKey(int i)
        {
            return keys.get(i);
        }

        int getParent()
        {
            return parent;
        }

        void setParent(int Parent)
        {
            this.parent = Parent;
        }

        void addKey(TreeObject obj)
        {
            keys.add(obj);
        }

        void addKey(TreeObject obj, int i)
        {
            keys.add(i, obj);
        }

        TreeObject removeKey(int i)
        {
            return keys.remove(i);
        }

        int getChild(int i)
        {
            return children.get(i);
        }

        void addChild(int i)
        {
            children.add(i);
        }

        void addChild(Integer c, int i)
        {
            children.add(i, c);
        }

        int removeChild(int i)
        {
            return children.remove(i);
        }

        public String toString()
        {
            StringBuilder s = new StringBuilder();
            s.append("Keys: ");
            for (TreeObject aKeyList : keys)
            {
                s.append(aKeyList).append(" ");
            }
            s.append("\nchildList: ");
            for (Integer aChildList : children)
            {
                s.append(aChildList).append(" ");
            }
            return s.toString();
        }
    }
    /**
     * BTree creator
     * @params deg degree of the tree
     * @params FileName simple filename
     * @params usingCache boolean value whether using cache or not
     * @params cacheSize how large the cache implementation should be
     */
    public BTree(int deg, String FileName, boolean usingCache, int cacheSize)
    {
        nodeSize = (32 * deg - 3);
        offset = 12;
        insertionPoint = (offset + nodeSize);
        this.degree = deg;
        if (usingCache)
        {
            BTreeCacheNode cacheNode = new BTreeCacheNode(cacheSize);

            cache = cacheNode.bTreeCache;
        }

        BTreeNode nodeOne = new BTreeNode();

        root = nodeOne;
        root.setOffset(offset);
        nodeOne.setIsLeaf(true);
        nodeOne.setKeyCount(0);

        try
        {
            java.io.File file = new File(FileName);
            accessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException fnfe)
        {
            System.err.println("File not found check the name and try again.");
            System.exit(-1);
        }

        writeTreeMetadata();
    }

    /**
     * Instantiates a BTree
     *
     * @param fileName filename that should be used
     */
    public BTree(File fileName)
    {

        try
        {
            accessFile = new RandomAccessFile(fileName, "r");
        } catch (FileNotFoundException fnfe)
        {
            System.err.println("File not found check the name and try again.");
            System.exit(-1);
        }
        readTreeMetadata();
        root = readNode(offset);
    }

    public BTree()
    {
        super();
    }

    /**
     * Finds and returns root of the BTreeNode
     *
     * @return root of tree
     */
    BTreeNode getRoot()
    {
        return root;
    }

    /**
     * Inserts into the BTree
     *
     * @param key to be inserted
     */
    void insert(long key)
    {
        BTreeNode rootNode = root;
        int i = rootNode.getKeyCount();
        if (i == 2 * degree - 1)
        {
            TreeObject Tobj = new TreeObject(key);
            while (i > 0 && Tobj.compareTo(rootNode.getKey(i - 1)) < 0)
            {
                i--;
            }
            if (i > 0 && Tobj.compareTo(rootNode.getKey(i - 1)) == 0)
                rootNode.getKey(i - 1).increaseFrequency();
            else
            {

                BTreeNode temp = new BTreeNode();
                temp.setOffset(rootNode.getOffset());
                root = temp;
                rootNode.setOffset(insertionPoint);
                rootNode.setParent(temp.getOffset());
                temp.setIsLeaf(false);
                temp.addChild(rootNode.getOffset());
                splitChild(temp, 0, rootNode);
                notFullInsert(temp, key);
            }
        } else
            notFullInsert(rootNode, key);
    }

    /**
     * Inserts into the BTree ensuring the btree is not full
     *
     * @param key     to be inserted
     * @param nodeOne the BTreeNode to be inserted
     */
    private void notFullInsert(BTreeNode x, long key)
    {
        int i = x.getKeyCount();

        TreeObject treeObject = new TreeObject(key);

        if (x.isLeaf())
        {
            if (x.getKeyCount() != 0)
            {
                while (i > 0 && treeObject.compareTo(x.getKey(i - 1)) < 0)
                {
                    i--;
                }
            }
            if (i > 0 && treeObject.compareTo(x.getKey(i - 1)) == 0)
            {
                x.getKey(i - 1).increaseFrequency();
            } else
            {
                x.addKey(treeObject, i);
                x.setKeyCount(x.getKeyCount() + 1);
            }
            writeNode(x);
        } else
        {
            while (i > 0 && (treeObject.compareTo(x.getKey(i - 1)) < 0))
            {
                i--;
            }
            if (i > 0 && treeObject.compareTo(x.getKey(i - 1)) == 0)
            {
                x.getKey(i - 1).increaseFrequency();
                writeNode(x);
                return;
            }
            int offset = x.getChild(i);
            BTreeNode nodetwo = readNode(offset);
            if (nodetwo.getKeyCount() == 2 * degree - 1)
            {
                int j = nodetwo.getKeyCount();
                while (j > 0 && treeObject.compareTo(nodetwo.getKey(j - 1)) < 0)
                {
                    j--;
                }
                if (j > 0 && treeObject.compareTo(nodetwo.getKey(j - 1)) == 0)
                {
                    nodetwo.getKey(j - 1).increaseFrequency();
                    writeNode(nodetwo);
                    return;
                } else
                {
                    splitChild(x, i, nodetwo);
                    if (treeObject.compareTo(x.getKey(i)) > 0)
                    {
                        i++;
                    }
                }
            }
            offset = x.getChild(i);
            BTreeNode child = readNode(offset);
            notFullInsert(child, key);
        }
    }

    /**
     * Splits child node
     *
     * @param nodeOne the btree Node to be split
     * @param y       the node that will be split
     * @param i       the degree index
     */
    private void splitChild(BTreeNode x, int i, BTreeNode y)
    {
        BTreeNode z = new BTreeNode();
        z.setIsLeaf(y.isLeaf());
        z.setParent(y.getParent());

        for (int j = 0; j < degree - 1; j++)
        {
            z.addKey(y.removeKey(degree));
            z.setKeyCount(z.getKeyCount() + 1);
            y.setKeyCount(y.getKeyCount() - 1);

        }
        if (!y.isLeaf())
        {
            for (int j = 0; j < degree; j++)
            {
                z.addChild(y.removeChild(degree));
            }
        }
        x.addKey(y.removeKey(degree - 1), i);
        x.setKeyCount(x.getKeyCount() + 1);
        y.setKeyCount(y.getKeyCount() - 1);
        if (x == root && x.getKeyCount() == 1)
        {
            writeNode(y);
            insertionPoint += nodeSize;
            z.setOffset(insertionPoint);
            x.addChild(z.getOffset(), i + 1);
            writeNode(z);
            writeNode(x);
            insertionPoint += nodeSize;
        } else
        {
            writeNode(y);
            z.setOffset(insertionPoint);
            writeNode(z);
            x.addChild(z.getOffset(), i + 1);
            writeNode(x);
            insertionPoint += nodeSize;
        }
    }

    /**
     * Search the BTree
     *
     * @param key     to be inserted
     * @param nodeOne the node to be found
     */
    TreeObject search(BTreeNode x, long key)
    {
        int i = 0;
        TreeObject obj = new TreeObject(key);
        while (i < x.getKeyCount() && (obj.compareTo(x.getKey(i))
                > 0))
        {
            i++;

        }
        if (i < x.getKeyCount() && obj.compareTo(x.getKey(i)) == 0)

        {
            return x.getKey(i);

        }
        if (x.isLeaf())

        {
            return null;
        } else
        {
            int offset = x.getChild(i);

            BTreeNode nextNode = readNode(offset);

            return search(nextNode, key);
        }
    }

    /**
     * Prints to file, instantiating a printWriter
     *
     * @param node           the node to be printed
     * @param writer         the setup instantiation of PrintWriter
     * @param sequenceLength how long each DNA sequence should be
     */
    void printToFile(BTreeNode node, PrintWriter writer, int sequenceLength)
    {
        GBFileConvert parser = new GBFileConvert();

        if (node.isLeaf())
        {
            for (int i = 0; i < node.getKeyCount(); i++)
            {
                writer.print(parser.convertLongToString(node.getKey(i)
                        .getData(), sequenceLength) + ": ");
                writer.println(node.getKey(i).getFrequency());
            }
        }
        if (!node.isLeaf())
        {
            for (int i = 0; i < node.getKeyCount() + 1; ++i)
            {
                int offset = node.getChild(i);

                BTreeNode nodeTwo = readNode(offset);
                printToFile(nodeTwo, writer, sequenceLength);

                if (i < node.getKeyCount())
                {
                    writer.print(parser.convertLongToString(node.getKey(i)
                            .getData(), sequenceLength) + ": ");
                    writer.println(node.getKey(i).getFrequency());
                }
            }
        }
    }

    /**
     * Writes a node pushed off the cache
     *
     * @param n the node toe be written
     */
    private void writeNode(BTreeNode n)
    {
        if (cache != null)
        {
            BTreeNode cacheNode = cache.add(n);

            // if a node was pushed off, write it
            if (cacheNode != null)
                writeNodeToFile(cacheNode);
        } else
        {
            writeNodeToFile(n);
        }
    }

    /**
     * Writes a node pushed off the cache
     *
     * @param n the node toe be written
     */
    private void writeNodeToFile(BTreeNode n)
    {
        int i;
        try
        {
            writeNodeMetadata(n, n.getOffset());
            accessFile.writeInt(n.getParent());

            for (i = 0; i < 2 * degree - 1; i++)
            {
                if (i < n.getKeyCount() + 1 && !n.isLeaf())
                {
                    accessFile.writeInt(n.getChild(i));
                } else if (i >= n.getKeyCount() + 1 || n.isLeaf())
                {
                    accessFile.writeInt(0);
                }
                if (i < n.getKeyCount())
                {
                    long data = n.getKey(i).getData();
                    accessFile.writeLong(data);
                    int frequency = n.getKey(i).getFrequency();
                    accessFile.writeInt(frequency);
                } else if (i >= n.getKeyCount() || n.isLeaf())
                {
                    accessFile.writeLong(0);
                }
            }
            if (i == n.getKeyCount() && !n.isLeaf())
            {
                accessFile.writeInt(n.getChild(i));
            }
        } catch (IOException ioe)
        {
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }
    }

    /**
     * Reads a cached node
     *
     * @param offset of the nodes
     */
    private BTreeNode readNode(int offset)
    {

        BTreeNode nodeTwo = null;

        // if node is cached, we can just read it from there
        if (cache != null) nodeTwo = cache.readNode(offset);
        if (nodeTwo != null) return nodeTwo;

        nodeTwo = new BTreeNode();
        TreeObject treeObj;
        nodeTwo.setOffset(offset);

        int k;
        try
        {
            accessFile.seek(offset);
            boolean isLeaf = accessFile.readBoolean();
            nodeTwo.setIsLeaf(isLeaf);
            int n = accessFile.readInt();
            nodeTwo.setKeyCount(n);
            int parent = accessFile.readInt();
            nodeTwo.setParent(parent);
            for (k = 0; k < 2 * degree - 1; k++)
            {
                if (k < nodeTwo.getKeyCount() + 1 && !nodeTwo.isLeaf())
                {
                    int child = accessFile.readInt();
                    nodeTwo.addChild(child);
                } else if (k >= nodeTwo.getKeyCount() + 1 || nodeTwo.isLeaf())
                {
                    accessFile.seek(accessFile.getFilePointer() + 4);
                }
                if (k < nodeTwo.getKeyCount())
                {
                    long value = accessFile.readLong();
                    int frequency = accessFile.readInt();
                    treeObj = new TreeObject(value, frequency);
                    nodeTwo.addKey(treeObj);
                }
            }
            if (k == nodeTwo.getKeyCount() && !nodeTwo.isLeaf())
            {
                int child = accessFile.readInt();
                nodeTwo.addChild(child);
            }
        } catch (IOException ioe)
        {
            System.err.println(ioe.getMessage());
            System.exit(-1);
        }

        return nodeTwo;
    }

    /**
     * Writes the metadata of the tree, including the degree ect.
     */
    private void writeTreeMetadata()
    {
        try
        {
            accessFile.seek(0);
            accessFile.writeInt(degree);
            accessFile.writeInt(32 * degree - 3);
            accessFile.writeInt(12);
        } catch (IOException ioe)
        {
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }
    }

    /**
     * Reads the meta data from file
     */
    private void readTreeMetadata()
    {
        try
        {
            accessFile.seek(0);
            degree = accessFile.readInt();
            nodeSize = accessFile.readInt();
            offset = accessFile.readInt();
        } catch (IOException ioe)
        {
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }
    }

    private void writeNodeMetadata(BTreeNode nodeOne, int offset)
    {
        try
        {
            accessFile.seek(offset);
            accessFile.writeBoolean(nodeOne.isLeaf());
            accessFile.writeInt(nodeOne.getKeyCount());
        } catch (IOException ioe)
        {
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }
    }

    /**
     * Simple cache clearing
     */
    void clearCache()
    {
        if (cache != null)
        {
            for (BTreeNode cnode : cache)
                writeNodeToFile(cnode);
        }
    }

 
}
