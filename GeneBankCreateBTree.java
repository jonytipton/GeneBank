//GIT

import java.io.File;

public class GeneBankCreateBTree {
	private static String degree;
	private static String cacheSize;
	public static boolean cacheInUse;
	private static File file;
	private static String sequenceLength;
	private static String debugLevel;
	
	/**
	 * Main class that sets the arguments into variables
	 */
	public static void main(String [] args) {
		if (args.length == 6) {
			if(args[0].equals("1")) {
				cacheInUse = true;
			}
			else {
				cacheInUse = false;
			}
			degree = args[1];
			file = new File(args[2]);
			sequenceLength = args[3];
			cacheSize = args[4];
			debugLevel = args[5];
			
		}else if (args.length == 5 && args[0].equals("1")) {
			cacheInUse = true;
			degree = args[1];
			file = new File(args[2]);
			sequenceLength = args[3];
			cacheSize = args[4];
			degree = "0";
			
		}else if (args.length == 5 && args[0].equals("0")) {
			cacheInUse = false;
			degree = args[1];
			file = new File(args[2]);
			sequenceLength = args[3];
			degree = args[4];
			
		}else if (args.length < 5 || args.length > 6) {
			System.out.println("Usage: java GeneBankCreateBTree" +
		" <0/1 (no/with Cache)> <degree> <gbk file> <sequence length>" +
		" [<cache size>] [<debug level>].\n" +
		"Message: Debug Level can be 0 or 1. 0 for regular output, 1 for" +
		" creating a file named 'dump' that contains all DNA strings in the "
		+ "sequence length you want.");
		}
	}
}
