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
	private static int sequenceLength;
	private static int debugLevel;
	private static BufferedReader reader;
	private static PrintWriter dump;
	private BTree bTree;
	
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
				//TO DO: bTree.getOptimalDegree
			}else {
				degree = d;
			}
			
			//Set file
			File gbk = new File(args[2]);
			
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