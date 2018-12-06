import java.io.File;
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
	private static String btreeFile;
	private static String queryFile;
	
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
		btreeFile = args[1];
		queryFile = args[2];
		
		//Cache Size (Optional)
		if (useCache && args.length > 3) {
			cacheSize = Integer.parseInt(args[3]);
		}
		
		/*Debug
		The output of the queries should be printed on the standard output stream. 
		A sample GeneBankSearch output is in test3_query7_result.
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
		
		String sequenceString = "";
		String degreeString = "";
		
		//find degree of the btreeFile
		for (int i = btreeFile.length() - 1; i >= 0; i--) {
			if (btreeFile.charAt(i) != '.') {
					degreeString += btreeFile.charAt(i);
			}
			else {
				break;
			}
		}
		
		degreeString = reverse(degreeString);
		
		//find sequence of btreeFile
		for (int i = btreeFile.length() - degreeString.length() - 2; i >= 0; i--) {
			if (btreeFile.charAt(i) != '.') {
				sequenceString += btreeFile.charAt(i);
			}
			else {
				break;
			}
		}
		
		sequenceString = reverse(sequenceString);
		
		//convert from string to int
		int degree = Integer.parseInt(degreeString); 
		int sequence = Integer.parseInt(sequenceString); 
		
		try {
			BTree bTree = new BTree(degree, btreeFile, useCache, cacheSize);
			Scanner scan = new Scanner(queryFile);
			
			while (scan.hasNext()) {
				String search = scan.nextLine();
				long convertedSearch = convertToLong(search);
				TreeObject tObject = bTree.search(bTree.getRoot(), convertedSearch);
				
				if (tObject != null) {
					System.out.println(convertToString(tObject.getData(), sequence) + ": " + tObject.getFrequency());
				}
			}
			scan.close();
		} catch (Exception e) { //catch file not found
			e.printStackTrace();
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
	
	/*
	 * Reverse the order of a string sequence
	 * 
	 * @param input, string being reversed for proper search
	 * @return reversed input string
	 */
	private static String reverse(String input) {
		if (input.length() == 1) {
			return input;
		}
		return "" + input.charAt(input.length() - 1)  + reverse(input.substring(0, input.length() -1));
	}
	

		/**
	     * Converts a string type into a long, character by character a, c, g, t it
	     * converts to binary representation.
	     *
	     * @param sequence substring of the dna sequence to convert into a long
	     * @return key which is a long type
	     */
		public static long convertToLong(String sequence) {
			long key = 0;
			sequence = sequence.toLowerCase();
			
			for (int i = 0; i < sequence.length(); i++) {
				if (sequence.charAt(i) == 'a') { //00
					if (i == 0) {
						key = 0;
					}
					else {
						key = key << 2; //shift left by 2, fill with 0
						key = key | 0;
					}
				}
				if (sequence.charAt(i) == 'c') { //01
					if (i == 0) {
						key = 1;
					}
					else {
						key = key << 2; //shift left by 2
						key = key | 1;
					}
				}
				if (sequence.charAt(i) == 'g') { //10
					if (i == 0) {
						key = 2;
					}
					else {
						key = key << 2; //shift left by 2
						key = key | 2;
					}
				}
				if (sequence.charAt(i) == 't') { //11
					if (i == 0) {
						key = 3;
					}
					else {
						key = key << 2; //shift left by 2
						key = key | 3;
					}
				}
			}
			return key;
		}
		
		/**
	     * Converts a long type into a String type
	     *
	     * @param sequence    the long type to be converted
	     * @param length the length of each DNA sequence between splits
	     */
		public static String convertToString(long sequence, int length) {
			String textSequence = "";
			long temp = 0;
			
			for (int i = 1; i <= length; i++) {
				//find starting point in bit sequence
				temp = (sequence & 3L << (length - i) * 2);
				//isolate sequence to two bits for comparison
				temp = temp >> (length - i) * 2;
					
				//compare 
				if (temp == 0L) { 
					textSequence += 'a';
				}
				else if (temp == 1L) {
					textSequence += 'c';
				}
				else if (temp == 2L) {
					textSequence += 'g';
				}
				else if (temp == 3L) {
					textSequence += 't';
				}
			}
			return textSequence;
		}
}
