import java.io.File;

public class GeneBankCreateBTree {
	
	private int degree;
	private int cacheSize;
	public boolean cacheInUse;
	private File file;
	private int sequenceLength;
	private int debugLevel;
	
	public static void main(String [] args) {
		if (args.length == 6) {
			if(args[0].equals(1)) {
				cacheInUse = true;
			}
			else {
				cacheInUse = false;
			}
			degree = args[1];
			file = new File(args[2]);
			
			
		}else if (args.length == 5 && args[0].equals(1)) {
			
		}else if (args.length == 5 && args[0].equals(0)) {
			
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
