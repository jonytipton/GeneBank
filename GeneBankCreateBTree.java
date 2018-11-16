import java.io.File;

public class GeneBankCreateBTree {
	
	private int degree;
	private int cacheSize;
	private File file;
	private int sequenceLength;
	private int debugLevel;
	
	public static void main(String [] args) {
		if (args.length == 6) {
			
		}else if (args.length == 5 && args[1].equals(1)) {
			
		}else if (args.length == 5 && args[1].equals(0)) {
			
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
