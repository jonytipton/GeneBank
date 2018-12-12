import java.io.File;
import java.util.Scanner;


/**
 * Searches the BTree
 *
 */
public class GeneBankSearch
{

    public static void main(String[] args)
    {

        //verify the argument length
        if (args.length < 3 || args.length > 5)
        {
            printUsage();
        }

        if (!(args[0].equals("0") || args[0].equals("1")))
        {
            printUsage();
        }

        String btreeFile = args[1];
        String searchFile = args[2];

        String sequence = "", deg = "";

        //Finds the degree
        for (int i = btreeFile.length() - 1; i >= 0; i--)
        {
            if (btreeFile.charAt(i) != '.')
                deg += btreeFile.charAt(i);
            else break;
        }
        deg = invertString(deg);

        //Finds the sequence length
        for (int i = btreeFile.length() - deg.length() - 2; i >= 0; i--)
        {
            if (btreeFile.charAt(i) != '.')
                sequence += btreeFile.charAt(i);
            else break;
        }
        sequence = invertString(sequence);

        try
        {
            GBFileConvert gbc = new GBFileConvert();
            BTree tree = new BTree(new File(btreeFile));
            Scanner scan = new Scanner(new File(searchFile));

            while (scan.hasNext())
            {
                String query = scan.nextLine();
                long q = gbc.convertStringToLong(query);
                TreeObject result = tree.search(tree.getRoot(), q);

                if (result != null)
                    System.out.println(gbc.convertLongToString(result.getData
                            (), Integer.parseInt(sequence)) + ": " + result
                            .getFrequency());
            }

            scan.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String invertString(String s)
    {
        if (s.length() == 1)
            return s;
        return "" + s.charAt(s.length() - 1) + invertString(s.substring(0, s
                .length() - 1));
    }

    private static void printUsage()
    {
        System.err.println("Usage: java GeneBankSearch <0/1(no/with Cache)> " +
                "<btree file> <query file> [<cache size>] [<debug level>]\n");
        System.exit(1);
    }
}
