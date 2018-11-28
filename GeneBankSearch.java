
public class GeneBankSearch {
	
	private static boolean useCache = false;
	private static boolean debug = false;
	private static int cacheSize = 0;
	
	public static void main(String [] args) {
		//Optional Cache usage. Arg[0] must be "0" or "1"
		if (args.length < 3 || args.length > 5) {
			printUsage();
			System.exit(1);
		}
		if (args[0].equals("1")) {
			useCache = true;
			//Using cache, Checks presence of cache size argument
			if (args.length < 4 || args.length > 5) {
				printUsage();
				System.out.println("Must input cache size if using cache implementation!");
				System.exit(1);
			}
		}
		//0-No Cache, print usage if invalid input
		else if (!args[0].equals("0")) {
			printUsage();
			System.exit(1);
		}
		
		//btree file stuff
		
		//query file stuff
		
		
		//Cache Size (Optional)
		if (useCache && args.length > 3) {
			cacheSize = Integer.parseInt(args[3]);
		}
		
		/*Debug
		The output of the queries should be printed on the standard output stream. 
		A sample GeneBankSearch output is in test3_query7_result.
		Only one level (0) of debug required. If user inputs anything as a 5th argument
		then debug will be set to true. */
		if (args.length == 5) {
			debug = true;
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage: java GeneBankSearch "
				+ "<0/1(no/with Cache)> <btree file> <query file> "
				+ "[<cache size>] [<debug level>]");
	}
}
