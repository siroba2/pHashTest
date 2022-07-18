import java.math.BigInteger;

public class MathAux {

    //Hamming distance
    public int hammingDist(String str1, String str2) {
        int i = 0, count = 0;
        while (i < str1.length()) {
            if (str1.charAt(i) != str2.charAt(i))
                count++;
            i++;
        }
        return count;
    }

    public static String binToHex(String bin) {
        long decimal = Long.parseLong(bin,2);
        return Long.toString(decimal,16);
    }

    public  static String longBinToHex (String bin){
        BigInteger b = new BigInteger(bin, 2);
        return b.toString(16);
    }



    private static void convertStringToHex(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
        }

        System.out.println(stringBuilder.toString());

    }
}
