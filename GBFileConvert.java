import java.lang.Math;
/**
 * Converts Long and Strings
 */
public class GBFileConvert {
	private long key = 0;

	public long getKey() {
		return key;
	}

	//64 bit
	/** my take on the string to long conversion
	* converts letters in the string to numbers
	* a = 0 c = 1 g = 2 t =3 (only letters it should be getting passed)
	* then treats it as a base 4 number, ie:
	* TAGCA = 3*4^4 + 0*4^3 + 2*4^2 + 1*4^1 + 0*4^0
	**/
	public long convertToLong(String sequence) {
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

	//31 length sequence
	/**
     * Converts a long type into a String type
     *
     * @param sequence    the long type to be converted
     * @param length the length of each DNA sequence between splits
     */
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

