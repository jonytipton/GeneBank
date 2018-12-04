import java.util.Random;

public class BTreeTest
{
    private static Random rand;

    public static void main(String[] args)
    {
        BTree testTree = new BTree(12, "test1", true, 10);
        
        rand = new Random();
        
        for (long i = 1; i < 1000; i++)//inserts data into tree twice to test dup count
        {
            testTree.insert(i);
            testTree.inOrderPrint(testTree.getRoot());
            System.out.println();
            
            testTree.insert(i);
            testTree.inOrderPrint(testTree.getRoot());
            System.out.println();
        }
        
        for (long i = 1; i < 1000; i++)//checks if data is in tree
        {
            TreeObject temp = testTree.search(testTree.getRoot(), i);
            
            if (temp == null || temp.compareTo(new TreeObject(i)) != 0)
            {
                System.err.println("Search failed.");
            }
        }
    }
}

