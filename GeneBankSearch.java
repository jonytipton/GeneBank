
public class GeneBankSearch {
	
	private static boolean useCache = false;
	private static boolean debug = false;
	private static int cacheSize = 0;
	
	public static void main(String [] args) {
	
		if (args.length < 3 || args.length > 5) {
			System.out.println("Incorrect Number of Arguments!");
			printUsage();
			System.exit(1);
		}
		//Optional Cache usage. Arg[0] must be "0" or "1"
		if (args[0].equals("1")) {
			useCache = true;
			//Using cache, Checks presence of cache size argument
			if (args.length < 4 || args.length > 5) {
				printUsage();
				System.out.println("Incorrect Number of Arguments!");
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
		Only one level of debug required.*/
		if (args.length == 5) {
			if (Integer.parseInt(args[4]).equals("1") {
				debug = true;
			}
			//catch if input is not 0 or 1
			else if (!Integer.parseInt(args[4]).equals("0")) {
				System.out.println("Debug Argument must be 0 (no) or 1 (yes)");
				printUsage();
				System.exit(1);
			}
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage: java GeneBankSearch "
						 + "<0/1(no/with Cache)> <btree file> <query file> "
						 + "[<cache size>] [<0/1(no/yes debug>]");
	}
}
