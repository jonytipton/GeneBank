
public class GBFileConvert {
	private long key = 0;
	
	public long getKey() {
		return key;
	}
	
	//64 bit
	//Not sure if the key | "value" code returns expected result
	public long convertToLong(String sequence) {
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
	
	//31 length sequence
	public String convertToString(long sequence, int length) {
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
