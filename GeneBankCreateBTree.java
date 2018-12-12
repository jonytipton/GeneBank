import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Creates BTree using the gene bank
 *
 */

public class GeneBankCreateBTree {
	private static final long A = 0b00L;
	private static final long C = 0b01L;
	private static final long G = 0b10L;
	private static final long T = 0b11L;

	private static final int MAXLENGTH = 31;
	private static final int DEBUG = 1;
	
	public static void main(String[] args) throws IOException
    {

        if (args.length < 4)
        {
        	brokenArguments();
        }

        // cache instantiation with default at false
        boolean useCache = false;

        try
        {
            int cache = Integer.parseInt(args[0]);
            if (cache == 1) useCache = true;
            if (cache < 0 | cache > 1)
            {
            	brokenArguments();
            }

        } catch (NumberFormatException e)
        {
        	brokenArguments();
        }

        // Degree for testing
        int BTreeDegree = 0;

        try
        {	//use of optimal degree
            int degree = Integer.parseInt(args[1]);
            if (degree < 0) brokenArguments();
            else if (degree == 0) BTreeDegree = OptimalDegree();
            else BTreeDegree = degree;

        } catch (NumberFormatException e)
        {
        	brokenArguments();
        }
        int sequenceLength = 0;

        // sequence length setup
        try
        {
            int length = Integer.parseInt(args[3]);
            if (length < 1 || length > MAXLENGTH) brokenArguments();
            else sequenceLength = length;
        } catch (NumberFormatException e)
        {
        	brokenArguments();
        }

        // cache and debug levels for testing
        int cacheSize = 0;
        int debugLevel = 0;

        if (args.length > 4)
        {
            if (useCache)
            {
                try
                {
                    int csize = Integer.parseInt(args[4]);
                    if (csize < 1) brokenArguments();
                    else cacheSize = csize;
                } catch (NumberFormatException e)
                {
                	brokenArguments();
                }
            }
            if (!useCache || args.length > 5)
            {
                try
                {
                    int debugType = Integer.parseInt(useCache ? args[5] :
                            args[4]);
                    if (debugType < 0 || debugType > DEBUG) brokenArguments();
                    else debugLevel = debugType;
                } catch (NumberFormatException e)
                {
                	brokenArguments();
                }
            }
        }

        System.out.println("Program Running ...");

        // Gene bank File.
        File dnaFile = new File(args[2]);

        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new FileReader(dnaFile));
        } catch (FileNotFoundException e)
        {
            System.err.println("File not found: " + dnaFile.getPath());
        }
        // args are set up, scanner to .gbk file is primed and ready for action
     	// btree set up stuff
        String BTreeFile = (dnaFile + ".btree.data." + sequenceLength + "." +
                BTreeDegree);
        BTree tree = new BTree(BTreeDegree, BTreeFile, useCache, cacheSize);

        String line;
        assert in != null;
        line = in.readLine().toLowerCase().trim();

        boolean inSequence = false;
        int curPosition = 0;
        int charPosition = 0;
        long sequence = 0L;

        while (line != null)
        {
            if (inSequence)
            {
                if (line.startsWith("//"))
                {
                    inSequence = false;
                    sequence = 0;
                    curPosition = 0;
                } else
                {
                    while (charPosition < line.length())
                    {
                        char c = line.charAt(charPosition++);
                        switch (c)
                        {
                            case 'a':
                                sequence = ((sequence << 2) | A);
                                if (curPosition < sequenceLength) curPosition++;
                                break;
                            case 't':
                                sequence = ((sequence << 2) | T);
                                if (curPosition < sequenceLength) curPosition++;
                                break;
                            case 'c':
                                sequence = ((sequence << 2) | C);
                                if (curPosition < sequenceLength) curPosition++;
                                break;
                            case 'g':
                                sequence = ((sequence << 2) | G);
                                if (curPosition < sequenceLength) curPosition++;
                                break;
                            case 'n':
                                curPosition = 0;
                                sequence = 0;
                                continue;
                            default: //none of the above
                                continue;
                        }
                        if (curPosition >= sequenceLength)
                        {
                            tree.insert(sequence & (~(0xffffffffffffffffL << (sequenceLength << 1))));
                        }
                    }
                }
            } else if (line.startsWith("ORIGIN"))
            {
                inSequence = true;
            }
            line = in.readLine();
            charPosition = 0;
        }

        if (debugLevel > 0)
        {
            File dumpFile = new File("dump");
            PrintWriter writer = new PrintWriter(dumpFile);
            tree.printToFile(tree.getRoot(), writer, sequenceLength);
            writer.close();
        }
        System.out.println("\nFINISHED");
        if (useCache) tree.clearCache();
        in.close();
    }

    /**
     * Calculates the optimal degree
     *
     * @return optimal degree for the tree
     */
    private static int OptimalDegree()
    {
        double optimalDegree;
        int pointerData = 4;
        int objectData = 12;
        int metadata = 5;
        optimalDegree = 4096;
        optimalDegree += objectData;
        optimalDegree -= pointerData;
        optimalDegree -= metadata;
        optimalDegree /= (2 * (objectData + pointerData));
        return (int) Math.floor(optimalDegree);
    }

    private static void brokenArguments()
    {
        System.err.println("Usage: java GeneBankCreateBTree <cache> <degree> " +
                "<gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.err.println("<cache>: 0/1 (no cache/cache)");
        System.err.println("<degree>: degree of the BTree (0 for default)");
        System.err.println("<gbk file>: GeneBank file");
        System.err.println("<sequence length>: 1-31");
        System.err.println("[<cache size>]: size of cache, optional");
        System.err.println("[<debug level>]: 0/1 (no/yes)");
        System.exit(1);
    }
}
