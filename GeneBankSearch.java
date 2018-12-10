
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * GeneBankSearch
 *
 * Searches the BTree file created in the
 * GeneBankCreateBTree class for DNA sequences
 * listed in the queryFile
 */
public class GeneBankSearch {

	private static boolean useCache = false;
	private static boolean debug = false;
	private static int cacheSize = 0;
	private static String btreeFilename;
	private static String queryFile;
	private static String debugFile;
	private static BTree bTree;
	private static PrintWriter writer;
	private static File dFile;
	private static File query;
	private static File btreeFile;

	public static void main(String [] args) {

		//Check for number of arguments
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
		btreeFilename = args[1];
		queryFile = args[2];
		
		//Cache Size (Optional)
		if (useCache && args.length > 3) {
			cacheSize = Integer.parseInt(args[3]);
		}

		/*Debug
		0 - The output of the queries should be printed on the standard output stream.
		1 - The output of the queries print to a file, example test3_query7_result.
		Only one level of debug required.*/
		if (args.length == 5) {
			if (args[4].equals("1")) {
				debug = true;
			}
			//catch if input is not 0 or 1
			else if (!args[4].equals("0")) {
				System.out.println("Debug Argument must be 0 (no) or 1 (yes)");
				printUsage();
				System.exit(1);
			}
		}
		String btreeSource = "";
		if(debug){
			int x = 0;
			while(btreeFilename.charAt(x) != '.'){
				btreeSource += btreeFilename.charAt(x);
			}
			debugFile = btreeSource + "_" + queryFile;
			dFile = new File(debugFile);
			dFile.delete();
			try {
				dFile.createNewFile();
			} catch (IOException e1) {
				System.out.println("File woes upon you, thanks to: "+dFile);
			}
			try {
				writer = new PrintWriter(dFile);
			} catch (FileNotFoundException e) {
				System.out.println("File woes upon you, thanks to: "+dFile);
			}
		}
		
		try {
			File btreeFile = new File(btreeFilename);		// make the btreeFile from the btreeFilename
			bTree = new BTree(btreeFile);					// instantiate a BTree from btreeFile
			query = new File(queryFile);					
			Scanner scan = new Scanner(query);

			while (scan.hasNextLine()) {
				String search = scan.nextLine();
				long convertedSearch = convertToLong(search);
				TreeObject tObject = bTree.search(bTree.getRoot(), convertedSearch);

				if (tObject != null) {
					if(debug) {
						writer.println(search + ": " + tObject.getFrequency());
						
					}
					else {
						System.out.println(search + ": " + tObject.getFrequency());
					}
				}
				else{
					if(debug) {
						writer.println(search + ": 0");
					}
					else {
						System.out.println(search + ": 0");   // print unfound key
					}
				}
			}
			scan.close();
		} catch (Exception e) { //catch file not found
			System.out.println("File woes upon you, thanks to: "+ btreeFilename);
		}
	}

	/*
	 *  Prints proper usage for GeneBankSearch
	 */
	private static void printUsage() {
		System.out.println("Usage: java GeneBankSearch "
				+ "<0/1(no/with Cache)> <btree file> <query file> "
				+ "[<cache size>] [<0/1(no/yes debug>]");
	}
	
	/**
	 * Helper method, converts a DNA sequence to a base 4 number
	 * a = 0 c = 1 g = 2 t =3 (only letters it should be getting passed)
	 * ie:
	 * TAGCA = 3*4^4 + 0*4^3 + 2*4^2 + 1*4^1 + 0*4^0
	 * 
	 * @param sequence - a sequence of DNA bases(a,t,c,g)
	 * @return long value equivalent to a base-4 translation of a provided sequence
	 */
	public static long convertToLong(String sequence) {
		sequence = sequence.toLowerCase();
		long result = 0;

		for(int i = 0; i < sequence.length(); i++){
			long temp = 0;

			if (sequence.charAt(i) == 'a') temp = 0;
			if (sequence.charAt(i) == 'c') temp = 1;
			if (sequence.charAt(i) == 'g') temp = 2;
			if (sequence.charAt(i) == 't') temp = 3;
			result += temp * Math.pow(4,sequence.length() - (1 + i));
		}

		return result;
	}


}
