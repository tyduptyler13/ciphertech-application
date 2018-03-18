import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class steg {

	//Modified and taken from http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	private static int getBMPLength(byte[] bytes, int pos){
		return ByteBuffer.wrap(bytes, pos, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	private static Path unknownPath(File f, int offset) throws IOException {
		File dir = new File(f.getName() + "_Output/");
		if (!dir.exists() || !dir.isDirectory()) dir.mkdir();
		File out = new File(dir.getPath() + "/" + Integer.toHexString(offset) + ".unknown");
		return out.toPath();
	}

	private static Path imagePath(File f, int offset) throws IOException {
		File dir = new File(f.getName() + "_Output/");
		if (!dir.exists() || !dir.isDirectory()) dir.mkdir();
		File out = new File(dir.getPath() + "/" + Integer.toHexString(offset) + ".bmp");
		return out.toPath();
	}

	private static void writeFile(Path p, byte[] bytes, int offset, int length) throws IOException {

		byte[] portion = Arrays.copyOfRange(bytes, offset, offset + length);

		String md5;
		try {
			MessageDigest dig = MessageDigest.getInstance("MD5");
			md5 = new String(bytesToHex(dig.digest(portion)));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Warning: No md5 algorithm in your java!");
			md5 = "NoAlgo";
		}


		System.out.println("Writting a file of length " + length + ", a hash of " + md5 + ", and an offset of " + offset + " to " + p.toString());
		Files.write(p, portion);
	}

	private static void findFiles(File f, byte[] bytes) throws IOException {
		int cursor = 0;

		for (int i = 0; i < bytes.length - 1; ++i){
			if (bytes[i] == 'B' && bytes[i+1] == 'M'){

				//Check if we have a bitmap file.
				int len = getBMPLength(bytes, i + 2); //Size is at BM offset + 2
				if (i + len > bytes.length) {
					System.out.println("Found possible file, but invalid length, skipping. @" + Integer.toHexString(i));
				} else {
					//We do have a bitmap file, do we need to write out the data before it?
					//Unknown file
					if (i > cursor) {
						writeFile(unknownPath(f, cursor), bytes, cursor, i - cursor);
					}

					//Now write out the bitmap file.
					writeFile(imagePath(f, cursor), bytes, i, len);
					cursor = i + len; //Need to move the cursor to the end of the bitmap file.
				}

			}
		}

		//Was the last bit of data part of an image?
		if (cursor != bytes.length) {
			writeFile(unknownPath(f, cursor), bytes, cursor, bytes.length - cursor);
		}

	}

	private static void parseFile(String file) {

		File f = new File(file);
		if (!f.exists()){
			System.out.println("File does not exist");
			return;
		}

		try {
			byte[] bytes = Files.readAllBytes(f.toPath());

			findFiles(f, bytes);

		} catch (IOException e) {
			System.err.println("Could not open/write the file! (IOException)");
			System.err.println(e.getMessage());
			System.exit(1);
		}

	}

	public static void main(String[] args) {

		if (args.length == 0) {

			Scanner s = new Scanner(System.in);

			String input = s.next();

			parseFile(input);

		} else if (args.length == 1) {
			if (args[0].equals("-h") || args[0].equals("--help")){
				System.out.println("Pass a file path to either the command line" +
				"or stdin of this program and it will output to a directory with a similar name.");
			} else {
				parseFile(args[0]);
			}

		}

	}

}
