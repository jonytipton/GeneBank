import java.io.File;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class GeneBankCreateBTree {
	private static int t;					// degree of the btree to create
	private static int cacheSize;			// size of cache, if using
	public static boolean cacheInUse;		// indicates if using a cache to build the tree
	private static File gbk;				// do we need this static??
	private static String gbkFilename;		// string to capture .gbk filename used to build tree
	private static int k;					// subsequence length used to build the tree
	private static int debugLevel;			// indicates level of debug specified
	//private static BufferedReader reader;	// ****** is this necessary?
	//private static PrintWriter dump;		// ****** is this necessary?
	private static BTree bTree;				// does it make sense to make this a private static
	
	public static void main(String [] args) throws IOException{
		if(args.length < 4) {
			brokenArguments();
		}
		try {
			//Determine if cache is in use
			boolean cacheInUse = false;
			int cache = Integer.parseInt(args[0]);
			if (cache == 1) {
				cacheInUse = true;
			}
			else {
				cacheSize = 0;
			}

			//Use of optimal degree or not
			t = Integer.parseInt(args[1]);
			if(t == 0) {
				t = optimalDegree();
			}

			//Set file and store GeneBank filename
			gbk = new File(args[2]);
			gbkFilename = args[2];
			//Set sequence length
			k = Integer.parseInt(args[3]);

			if(args.length > 4) {
				if(cacheInUse) {
					try {
						cacheSize = Integer.parseInt(args[4]);
						if(cacheSize < 1) {
							brokenArguments();
						}

					}catch(NumberFormatException e){
						brokenArguments();
					}
				}
				if(!cacheInUse || args.length > 5) {
					try {
						debugLevel = Integer.parseInt(cacheInUse ? args[5] : args[4]);
						if(debugLevel < 0 || debugLevel > 1) {
							brokenArguments();
						}
					}catch(NumberFormatException e){
						brokenArguments();
					}
				}
			}
		}catch(NumberFormatException e){
			brokenArguments();
		}
		// args are set up, scanner to .gbk file is primed and ready for action
		// btree set up stuff
		String BTreeFile = (gbkFilename + ".btree.data." + k + "." + t);
		bTree = new BTree(t, BTreeFile, cacheInUse, cacheSize);
		
		// get to reading the .gbk file into the btree
		Scanner scan = new Scanner(gbk);
		String dummy = null;
		
		while(scan.hasNextLine()) {
			//proceed to the word ORIGIN
			while(dummy == null && scan.hasNextLine()) {
				dummy = scan.findInLine("ORIGIN");   // move the scanner to just past ORIGIN
				scan.nextLine();                    // advance scanner to the next line, dummy is either null or ORIGIN
			}
			
			// if the block above worked, dummy = "ORIGIN" and the scanner will be at the starting
			// of the line after the occurrence of ORIGIN, right where we want to be
			// time to construct a gbkSequence!
			
			//System.out.println(dummy);
			String gbkSequence = "";				// overall sequence we're extracting
			String seq = "";						// sequence pieces used to build gbkSequence
			
			while(scan.hasNext() && seq.compareTo("//") != 0){		// proceed until a "//" occurs
				seq = scan.next();
			    for(int i = 0; i < seq.length(); i++){
			     if(seq.charAt(i) == 'a' || seq.charAt(i) == 't' || seq.charAt(i) == 'c' || seq.charAt(i) == 'g')
			    	  gbkSequence += seq.charAt(i);		// append desired characters to gbkSequenc
			      else if(seq.charAt(i) == 'n') {
			    	  if(gbkSequence.compareTo("") != 0) {	// first instance of a 'n' in a sequence being read
			    		  //System.out.println(gbkSequence);
			    		  processSequence(k,gbkSequence,bTree);	// process sequence as it stands
			    		  gbkSequence = "";					// reset sequence to pick up once out of the 'n's
			    	  }
			      }
			    }
			}
			dummy = null;
			//System.out.println(gbkSequence);
			processSequence(k,gbkSequence,bTree);					// process remaining sequence
			// should return to the top of the loop and start looking for another instance of ORIGIN
		}
		scan.close();  // done reading the file, bTree should be populated with the goodies
		if(debugLevel > 0) {		// handle debug level 1
			String dump;
			dump = gbk + ".btree.data.dump." + k;
			File dumpFile = new File(dump);
			dumpFile.delete();
			dumpFile.createNewFile();
			PrintWriter writer = new PrintWriter(dumpFile);
			bTree.inOrderPrint(bTree.getRoot());
			writer.close();
		}
	}
	
	/**
	 * Optimal degree is found by using a formula given in
	 * PowerPoint 15-B-Trees.pptx slide 14. The formula uses
	 * variables for a disk block size of 4096 bytes.
	 * Each node has a ? bytes of meta-data to store.
	 * 	4 bytes - int numKeys
	 * 	4 bytes - int offset
	 * 	1 byte 	- boolean isLeaf
	 * 
	 * The size of each treeObject is 16 bytes(key - 8 byte + freq - 8 byte)   	2m-1 treeObjects per node
	 * Each pointer has size 4 bytes. 2m - child pointers + 1 - parent pointer	2m+1 pointers per node
	 * Optimal degree (m) is found using this formula:
	 * "4096 = 8 + 16*(2m-1) + 4* (2m+1)"
	 * m came out to be 85.
	 * @return optimal degree
	 */
	public static int optimalDegree() {
		int optimalDegree = 85;
		return optimalDegree;
	}
	/**
	 * Prints error message if arguments are not in correct format.
	 * Prints Usage help
	 */
	public static void brokenArguments() {
		System.err.println("Usage: java GeneBankCreate BTree <without/with cache> <degree>"
				+ " <gbk file> <sequence length> [<cache size>] [<debug level>]");
		System.err.println("<without/with cache>: 0 for no cache, 1 for cache.");
		System.err.println("<degree>: degree of the BTree. Use 0 if you want default.");
		System.err.println("<gbk file>: file to be parsed for sequence data.");
		System.err.println("<sequence length>: length of a sequence (1-31).");
		System.err.println("[<cache size>]: Optional. If cache is enabled, then size of cache.");
		System.err.println("[<debug level>]: 0 for regular output, 1 for file dump.");
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
	
	/**
	 * Helper method, processes a given string into subsequences of a specified length,
	 * converts subsequences to a long type key value and inserts the key into a specified 
	 * Btree object 
	 * @param k				- 	length of subsequences to generate
	 * @param gbkSequence	- 	sequence of DNA bases to process
	 * @param btree			-	Btree object to add subsequences to
	 */
	public static void processSequence(int k, String gbkSequence, BTree btree) {
		for(int i = 0; i <= gbkSequence.length() - k; i++) {
			String dummy = "";
			for(int j=i;j<i+k;j++) {
				dummy += gbkSequence.charAt(j);
			}
			long key = convertToLong(dummy);
			btree.insert(key);
			//System.out.println(dummy +" : "+key);
			
		}
	}


}
