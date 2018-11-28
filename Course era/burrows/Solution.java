import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class Solution {
	public static void main(String[] args) {
		String caseType = StdIn.readString();
		switch (caseType) {

		case "CircularSuffixArrayTest":
			System.out.println("Testing CircularSuffixArray...");
			while (!StdIn.isEmpty()) {
				String fileName = StdIn.readString();
				try {
					In in = new In(fileName);
					CircularSuffixArray csa = new CircularSuffixArray(in.readAll());
					System.out.println(csa.index(StdIn.readInt()));
					System.out.println(csa.length());
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
			break;

		case "MoveToFrontTest":
			String type = StdIn.readString();
			if (type.equals("encode")) {
				String fileName = StdIn.readString();
				In in = new In(fileName);
				String content = in.readAll();
				testEncode(content);
			} else {
				String fileName = StdIn.readString();
				In in = new In(fileName);
				List<Byte> contents = new ArrayList<Byte>();
				while(!in.isEmpty()) {
					contents.add(in.readByte());
				}
				Byte[] bytes = contents.toArray(new Byte[0]);
				byte[] bytesPrimitive = new byte[bytes.length];
				for (int i = 0; i < bytes.length; i++) {
					bytesPrimitive[i] = bytes[i].byteValue();
				}
				testDecode(bytesPrimitive);
			}
			break;
		case "BurrowsWheelerTest":
			break;
		default:
			try {
				CircularSuffixArray csa = new CircularSuffixArray(null);
				System.out.println(csa.index(StdIn.readInt()));
				System.out.println(csa.length());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			break;
		}
	}

	public static void testEncode(String DECODED_INPUT) {
		// backup standard in and out
		InputStream standardIn = System.in;
		PrintStream standardOut = System.out;
		byte[] encoded = null;
		try {
			// setup new input
			System.setIn(new ByteArrayInputStream(DECODED_INPUT.getBytes()));
			// create new output stream as byte array and assign to standard
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));

			MoveToFront.encode();
			encoded = baos.toByteArray();
		} finally {
			// return standard input and output
			System.setIn(standardIn);
			System.setOut(standardOut);
			System.out.println(Arrays.toString(encoded));
		}
	}

	public static void testDecode(byte[] ENCODED_INPUT) {
		// backup standard in and out
		InputStream standardIn = System.in;
		PrintStream standardOut = System.out;
		String decoded = null;
		try {
			// setup new input with encoded message
			System.setIn(new ByteArrayInputStream(ENCODED_INPUT));
			// create new output stream as byte array and assign to standard
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos));

			MoveToFront.decode();
			decoded = baos.toString();
			// check length and chars
		} finally {
			// return standard input and output
			System.setIn(standardIn);
			System.setOut(standardOut);
			System.out.println(decoded);
		}
	}
}

class CircularSuffixArrayTest {

}

class MoveToFrontTest {

}

class BurrowsWheelerTest {

}