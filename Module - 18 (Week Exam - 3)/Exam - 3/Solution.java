public class Solution {
	public static void main(String[] args) {

		switch (search) {
		case "dictionary":
			// read the lines in this case. Print true if the given word is in dictionary,
			// otherwise false.
			break;

		case "t9search":
			// read the lines in this case. Input here is numeric String format, examples 9842,
			// Where it should suggest the words in sorted order which are valid and are in dictionary.
			// If no words found, print "No match found."
			break;

		default:
			break;
		}
	}

	// Use this to get strings array from given file.
	public static String[] toLoadDictionary(String file) {
		In in = new In("/Files/" + file);
		return in.readAllStrings();
	}
}
