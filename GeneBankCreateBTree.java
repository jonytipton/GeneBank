//GIT

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GeneBankCreateBTree {

	private static int degree;
	private static int cacheSize;
	public static boolean cacheInUse;
	private static File gbk;
	private static String gbkFilename;
	private static int sequenceLength;
	private static int debugLevel;
	private static BufferedReader reader;
	private static PrintWriter dump;
	private static BTree bTree;
//	private static final long CODE_A = 0L;
//	private static final long CODE_G = 2L;
//	private static final long CODE_C = 1L;
//	private static final long CODE_T = 3L;
	//these may need to be at play instead of the Longs above
	public static final long CODE_A = 0b00L;
	public static final long CODE_T = 0b11L;
	public static final long CODE_C = 0b01L;
	public static final long CODE_G = 0b10L;

	/**
	 * Main class that sets the arguments into variables
	 */
	public static void main(String [] args) throws IOException{
		if(args.length < 4) {
			brokenArguments();
		}
		cacheInUse = false;
		try {
			//Determine if cache is in use
			int cache = Integer.parseInt(args[0]);
			if (cache == 0) {
				cacheInUse = false;
			}
			if (cache == 1) {
				cacheInUse = true;
			}

			//Use of optimal degree or not
			int d = Integer.parseInt(args[1]);
			if(d == 0) {
				degree = optimalDegree();
			}else {
				degree = d;
			}

			//Set file and store GeneBank filename
			File gbk = new File(args[2]);
			gbkFilename = args[2];
			//Set sequence length
			sequenceLength = Integer.parseInt(args[3]);

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

			//Set up BufferedReader
			reader = null;
			try {
				reader = new BufferedReader(new FileReader(gbk));
			}catch(FileNotFoundException e) {
				System.err.println("File not found: " + gbk.getPath());
			}

		}catch(NumberFormatException e){
			brokenArguments();
		}
		String BTreeFile = (gbkFilename + ".btree.data." + sequenceLength + "." + degree);
		bTree = new BTree(degree, BTreeFile, cacheInUse, cacheSize);
		//TO DO: Initialize new cache
		String line = null;
		line = reader.readLine().toLowerCase().trim();
		boolean inSequence = false;
		int sequencePosition = 0;
		int charPosition = 0;
		long sequence = 0L;
			while(line != null) {
				if(inSequence) {
					if(line.startsWith("//")) {
						inSequence = false;
						sequence = 0;
						sequencePosition = 0;
					}else {
						while(charPosition < line.length()) {
							//Convert to long???
							char c = line.charAt(charPosition++);
							switch(c) {
								case 'a':
									sequence = (CODE_A);
									if(sequencePosition < sequenceLength) {
										sequencePosition++;
									}
									break;
								case 'g':
									sequence = (CODE_G);
									if(sequencePosition < sequenceLength) {
										sequencePosition++;
									}
									break;
								case 'c':
									sequence = (CODE_C);
									if(sequencePosition < sequenceLength) {
										sequencePosition++;
									}
									break;
								case 't':
									sequence = (CODE_T);
									if(sequencePosition < sequenceLength) {
										sequencePosition++;
									}
									break;
								case 'n':
									sequencePosition = 0;
									sequence = 0;
									continue;
								default:
									continue;
							}
							if(sequencePosition >= sequenceLength && !cacheInUse) {
								bTree.insert(sequence);
							}
						}
					}
					}else if(line.startsWith("ORIGIN")) {
						inSequence = true;
					}
				line = reader.readLine();
				charPosition = 0;
				}
		
			if(debugLevel > 0) {
				String dump;
				dump = gbk + ".btree.data.dump." + sequenceLength;
				File dumpFile = new File(dump);
				dumpFile.delete();
				dumpFile.createNewFile();
				PrintWriter writer = new PrintWriter(dumpFile);
				bTree.inOrderPrint(bTree.getRoot());
				writer.close();
			}else{
				//TO DO: Print error messages
			}
			System.out.println("done");
			
			if(cacheInUse) {
				bTree.flushCache();   
			}
			reader.close();
	}
		
	/**
	 * Optimal degree is found by using a formula given in 
	 * PowerPoint 15-B-Trees.pptx slide 14. The formula uses
	 * variables for a disk block size of 4096 bytes.
	 * Each node has a 200 bytes of meta-data to store.
	 * The size of each key is 120 bytes.
	 * Each pointer has size 8 bytes.
	 * Optimal degree (m) is found using this formula:
	 * "4096 = 200 + 8 + 120*(m-1) + 8 * (m)"
	 * m came out to be 30.
	 * @return optimal degree
	 */
	public static int optimalDegree() {
		int optimalDegree = 30;
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
}
