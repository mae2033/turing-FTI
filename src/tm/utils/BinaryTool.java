package tm.utils;

// no se usa
public class BinaryTool {

	public static int toDecimal(String s) {
		return Integer.parseInt(s, 2);
	}

	public static boolean isValidBinary(String s) {
		return s.matches("[01]+");
	}

	public static String hexToBinary(String hex) {
		int decimal = Integer.parseInt(hex, 16);
		return Integer.toBinaryString(decimal);
	}

}
