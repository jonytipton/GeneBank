/**
 * Converts Long and Strings
 */
public class GBFileConvert {
	private long key = 0;

    /**
     * Converts a string type into a long, character by character a, c, g, t it
     * converts to binary representation.
     *
     * @param dnaSubString substring of the dna sequence to convert into a long
     * @return key which is a long type
     */
    long convertStringToLong(String dnaSubString)
    {
        dnaSubString = dnaSubString.toLowerCase();

        for (int i = 0; i < dnaSubString.length(); i++)
        {
            if (dnaSubString.charAt(i) == 'a')
            { // 00
                if (i == 0)
                {
                    key = 0;
                } else
                {
                    key = key << 2;
                }
            }
            if (dnaSubString.charAt(i) == 'c')
            { // 01
                if (i == 0)
                {
                    key = 1;
                } else
                {
                    key = key << 2;
                    key = key | 1;
                }
            }
            if (dnaSubString.charAt(i) == 'g')
            { // 10
                if (i == 0)
                {
                    key = 2;
                } else
                {
                    key = key << 2;
                    key = key | 2;
                }
            }
            if (dnaSubString.charAt(i) == 't')
            { // 11
                if (i == 0)
                {
                    key = 3;
                } else
                {
                    key = key << 2;
                    key = key | 3;
                }
            }
        }
        return key;
    }

    /**
     * Converts a long type into a String type
     *
     * @param dnaSequence    the long type to be converted
     * @param sequenceLength the length of each DNA sequence between splits
     */
    String convertLongToString(long dnaSequence, int sequenceLength)
    {
        StringBuilder str = new StringBuilder();
        long temp;

        for (int i = 1; i <= sequenceLength; i++)
        {
            temp = (dnaSequence & (3L << ((sequenceLength - i) * 2)));
            temp = temp >> ((sequenceLength - i) * 2);

            if (temp == 0L)
            {
                str.append("a");
            } else if (temp == 1L)
            {
                str.append("c");
            } else if (temp == 2L)
            {
                str.append("g");
            } else if (temp == 3L)
            {
                str.append("t");
            }
        }
        return str.toString();
    }
}

